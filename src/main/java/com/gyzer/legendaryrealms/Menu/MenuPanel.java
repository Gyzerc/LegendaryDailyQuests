package com.gyzer.legendaryrealms.Menu;

import com.gyzer.legendaryrealms.API.LegendaryDailyQuestsAPI;
import com.gyzer.legendaryrealms.Configurations.Language;
import com.gyzer.legendaryrealms.Data.Quest.Categorize;
import com.gyzer.legendaryrealms.Data.Quest.Checker.GiveItemGoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import com.gyzer.legendaryrealms.Data.Quest.Objective.QuestObjective;
import com.gyzer.legendaryrealms.Data.Quest.Progress.ProgressData;
import com.gyzer.legendaryrealms.Data.Quest.Quest;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Utils.ReplaceUtils;
import com.gyzer.legendaryrealms.Utils.RunUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MenuPanel implements InventoryHolder {
    private Inventory inv;
    private Player p;
    private Categorize categorize;
    private MenuLoader loader;
    private HashMap<Integer,String> stringStore;
    private int questAmount;
    final LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();
    final Language lang = legendaryDailyQuests.getConfigurationsManager().getLanguage();

    public MenuPanel(Player p, Categorize categorize) {
        this.p = p;
        this.categorize = categorize;
        this.loader = categorize.getLoader();
        this.stringStore = new HashMap<>();
        if (loader != null) {
            this.inv = Bukkit.createInventory(this,loader.getSize(),loader.getTitle());
            this.questAmount = 0;
            PlayerData data = LegendaryDailyQuestsAPI.getPlayerData(p);
            //检测是否本轮的任务还未刷新
            if (data.getAccepts(categorize.getId()).isEmpty()){
                if (categorize.getQuests().size() > 0){
                    LegendaryDailyQuestsAPI.randomPlayerQuests(p.getUniqueId(),categorize);
                }
            }
            loadMenu(LegendaryDailyQuestsAPI.getPlayerData(p));
        }
    }

    private void loadMenu(PlayerData data) {
        String id = categorize.getId();
        LinkedList<String> quests = data.getQuests().getOrDefault(id,new LinkedList<>());
        List<String> completeds = data.getCompleteds().getOrDefault(id,new ArrayList<>());
        List<String> accepts = data.getAccepts().getOrDefault(id,new ArrayList<>());

        String completedStr = getPlaceHolder("completed");
        String waitStr = getPlaceHolder("wait");
        String doing = getPlaceHolder("doing");
        String give_item = getPlaceHolder("give_item");

        DrawEssentailSpecial(inv, menuItem -> {
            switch (menuItem.getFuction()){
                case "refresh" :
                    ItemStack i = menuItem.getItem().clone();
                    i = new ReplaceUtils()
                            .addSinglePlaceHolder("refresh",""+data.getRefresh().getOrDefault(id,0))
                            .startReplace(i);
                    menuItem.setItem(i);
                    menuItem.setPut(true);
                    break;
                case "rewards" :
                    i = menuItem.getItem().clone();
                    i = new ReplaceUtils()
                            .addSinglePlaceHolder("completeds",""+completeds.size())
                            .addSinglePlaceHolder("quests",""+quests.size())
                            .startReplace(i);
                    menuItem.setItem(i);
                    menuItem.setPut(true);
                    break;
                case "quest" :
                    if (questAmount < categorize.getAmount() && questAmount < quests.size()) {
                        Quest quest = LegendaryDailyQuests.getLegendaryDailyQuests().getQuestsManager().getQuest(quests.get(questAmount));
                        if (quest != null) {
                            String questId = quest.getId();
                            ItemStack mi = menuItem.getItem();
                            String display = mi.getItemMeta().hasDisplayName() ? mi.getItemMeta().getDisplayName() : "%quest%";

                            i = new ItemStack(quest.getPreview_material(),quest.getPreview_amount());
                            ItemMeta meta = i.getItemMeta();
                            meta.setDisplayName(display);
                            List<String> lore = mi.getItemMeta().hasLore() ? mi.getItemMeta().getLore() : new ArrayList<>();
                            if (legendaryDailyQuests.version_high) {
                                meta.setCustomModelData(quest.getPreview_model());
                            }
                            meta.setLore(lore);
                            i.setItemMeta(meta);
                            String state = waitStr;
                            if (accepts.contains(questId)){
                                if (completeds.contains(questId)){
                                    state = completedStr;
                                }
                                else {
                                    if (hasGiveItemGoal(p, categorize, quest)) {
                                        state = give_item;
                                    } else {
                                        state = doing;
                                    }
                                }
                            }
                            i = new ReplaceUtils()
                                    .addListPlaceHolder("description",quest.getPreview_lore())
                                    .addListPlaceHolder("rewards",quest.getPreview_reward())
                                    .addSinglePlaceHolder("placeholder",state)
                                    .addSinglePlaceHolder("quest",quest.getDisplay())
                                    .addSinglePlaceHolder("progress",LegendaryDailyQuestsAPI.getProgressPercent(p,data,categorize.getId(),quest)+"")
                                    .startReplace(i);
                            menuItem.setItem(QuestProgressReplacement(i,quest,data));
                            menuItem.setPut(true);
                            menuItem.setValue(questId);
                            questAmount++;
                            return;
                        }
                    }
                    menuItem.setPut(false);
                    break;
            }
        });
    }

    private boolean hasGiveItemGoal(Player p, Categorize categorize, Quest quest) {
        PlayerData data = LegendaryDailyQuestsAPI.getPlayerData(p);
        String cat = categorize.getId();
        List<QuestObjective> objectives = quest.getObjectives().stream().filter(questObjective -> questObjective.getType().equals(ObjectiveType.GIVE_ITEM)).collect(Collectors.toList());
        if (objectives.isEmpty()) {
            return false;
        }
        for (QuestObjective objective : objectives) {
            String id = objective.getId();
            int need = (int) objective.getAmount();
            int has = (int) data.getProgressData(cat).getProgress(quest.getId(),id);
            if (has < need) {
                return true;
            }
        }
        return false;
    }

    public void onClick(InventoryClickEvent e) {
        if (e.getRawSlot() >= 0) {
            MenuItem menuItem = getMenuItem(e.getRawSlot());
            if (menuItem != null){
                PlayerData data = LegendaryDailyQuestsAPI.getPlayerData(p);
                switch (menuItem.getFuction()){
                    case "refresh" : {
                        int amount = data.getRefresh().getOrDefault(categorize.getId(), 0);
                        if (amount == 0) {
                            p.sendMessage(lang.PLUGIN + lang.refresh_notenough);
                            return;
                        }
                        if ( (!legendaryDailyQuests.getConfigurationsManager().getConfig().can_refresh_accepted && data.getAccepts(categorize.getId()).isEmpty()) || legendaryDailyQuests.getConfigurationsManager().getConfig().can_refresh_accepted ) {
                            p.sendMessage(lang.PLUGIN + lang.refresh);
                            data.takeRefresh(categorize.getId(), 1);
                            LegendaryDailyQuestsAPI.randomPlayerQuests(p.getUniqueId(), categorize);
                            MenuPanel panel = new MenuPanel(p, categorize);
                            Bukkit.getScheduler().runTask(legendaryDailyQuests, panel::open);
                            return;
                        }
                        p.sendMessage(lang.PLUGIN + lang.refresh_accepted);
                        break;
                    }
                    case "quest" : {
                        String questId = getString(e.getRawSlot());
                        if (questId != null) {
                            Quest quest = legendaryDailyQuests.getQuestsManager().getQuest(questId);
                            if (quest != null) {
                                if (LegendaryDailyQuestsAPI.hasAcceptQuest(data, categorize.getId(), questId)) {
                                    if (hasGiveItemGoal(p, categorize, quest)) {
                                        if (new GiveItemGoalChecker().checkGive_Item(p, ObjectiveType.GIVE_ITEM, categorize)) {
                                            MenuPanel panel = new MenuPanel(p, categorize);
                                            Bukkit.getScheduler().runTask(legendaryDailyQuests, panel::open);
                                        }
                                    }
                                } else {
                                    LegendaryDailyQuestsAPI.acceptQuest(p, data, categorize.getId(), quest);
                                    MenuPanel panel = new MenuPanel(p, categorize);
                                    Bukkit.getScheduler().runTask(legendaryDailyQuests, panel::open);
                                }
                            }
                        }
                        break;
                    }
                    case "rewards" : {
                        if (!data.hasClaim(categorize.getId())) {
                            if (LegendaryDailyQuestsAPI.hasCompletedAll(data, categorize)) {
                                data.claim(categorize.getId());
                                data.update(false);
                                p.sendMessage(lang.PLUGIN + lang.finalyReward_claim.replace("%categorize%", categorize.getDisplay()));
                                legendaryDailyQuests.getConfigurationsManager().getConfig().claim.ifPresent(sound -> p.playSound(p.getLocation(), sound, 1, 1));
                                RunUtils.run(p, categorize.getRewards());
                                return;
                            }
                            p.sendMessage(lang.PLUGIN + lang.finalyReward_cant.replace("%categorize%", categorize.getDisplay()));
                            return;
                        }
                        p.sendMessage(lang.PLUGIN + lang.finalyReward_already.replace("%categorize%", categorize.getDisplay()));
                        return;
                    }
                }
            }
        }
    }


    private ItemStack QuestProgressReplacement(ItemStack i, Quest quest, PlayerData data) {
        ProgressData progressData = data.getProgressData().getOrDefault(categorize.getId(),new ProgressData(new HashMap<>()));
        ReplaceUtils replaceUtils = new ReplaceUtils();
        for (QuestObjective objective : quest.getObjectives()){
            String goal =objective.getId();
            if (objective.getType().isUseInt()){
                int progress = (int) progressData.getProgress(quest.getId(),goal);
                replaceUtils.addSinglePlaceHolder("progress_" + goal, "" + progress);
            }
            else {
                double progress = progressData.getProgress(quest.getId(), goal);
                replaceUtils.addSinglePlaceHolder("progress_" + goal, "" + progress);
            }
        }
        return replaceUtils.startReplace(i);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }


    public MenuItem getMenuItem(int slot){
        return loader.getMenuItem(slot);
    }

    public String getPlaceHolder(String id){
        return loader.getPlaceHolder(id);
    }

    public List<String> getLayout(){
        return loader.getLayout();
    }

    public void DrawEssentailSpecial(Inventory inv, Consumer<MenuItem> consumer){
        LegendaryDailyQuests.getLegendaryDailyQuests().sync(new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<Integer,MenuItem> entry : loader.getItem().entrySet()){
                    MenuItem menuItem = entry.getValue();
                    MenuItem newMenu = new MenuItem(menuItem.getId(),menuItem.getItem().clone(),menuItem.getFuction());
                    consumer.accept(newMenu);
                    if (newMenu.isPut()) {
                        inv.setItem(entry.getKey(),newMenu.getItem());
                    }
                    if (newMenu.getValue() != null){
                        stringStore.put(entry.getKey(), newMenu.getValue());
                    }
                }
            }
        });
    }
    public String getString(int slot){
        return stringStore.get(slot);
    }

    public MenuLoader getLoader() {
        return loader;
    }

    public void open(){
        if (loader.getSound().isPresent()){
            p.playSound(p.getLocation(),loader.getSound().get(),1,1);
        }
        p.openInventory(inv);
    }


}
