package com.gyzer.legendaryrealms.API;

import com.gyzer.legendaryrealms.API.Events.PlayerAcceptQuestEvent;
import com.gyzer.legendaryrealms.API.Events.PlayerCompletedAllQuestsEvent;
import com.gyzer.legendaryrealms.API.Events.PlayerCompletedQuestEvent;
import com.gyzer.legendaryrealms.API.Events.PlayerDailyQuestsRefreshEvent;
import com.gyzer.legendaryrealms.Configurations.Config;
import com.gyzer.legendaryrealms.Configurations.Language;
import com.gyzer.legendaryrealms.Data.Quest.Categorize;
import com.gyzer.legendaryrealms.Data.Quest.Objective.QuestObjective;
import com.gyzer.legendaryrealms.Data.Quest.Progress.ProgressData;
import com.gyzer.legendaryrealms.Data.Quest.Quest;
import com.gyzer.legendaryrealms.Data.Quest.QuestRarity;
import com.gyzer.legendaryrealms.Data.System.CompletedData;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Utils.RunUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class LegendaryDailyQuestsAPI {
     static final Language lang = LegendaryDailyQuests.getLegendaryDailyQuests().getConfigurationsManager().getLanguage();
    static final Config config = LegendaryDailyQuests.getLegendaryDailyQuests().getConfigurationsManager().getConfig();
    public static PlayerData getPlayerData(Player p){
        return LegendaryDailyQuests.getLegendaryDailyQuests().getPlayerDataManager().getPlayerData(p);
    }
    public static PlayerData getPlayerData(UUID uuid){
        return LegendaryDailyQuests.getLegendaryDailyQuests().getPlayerDataManager().getPlayerData(uuid);
    }
    public static void randomPlayerQuests(UUID uuid, Categorize categorize , List<String> specials,boolean reset){
        PlayerData data = getPlayerData(uuid);
        String id = categorize.getId();
        data.getQuests().remove(id);
        data.getAccepts().remove(id);
        data.getCompleteds().remove(id);
        data.getClaimFinallyRewards().remove(id);
        data.getProgressData().remove(id);

        LinkedList<String> select = new LinkedList<>(specials);
        int a = categorize.getAmount() - specials.size();
        int round = 0;

        while (a > 0 && round < config.MAX_ROUND) {
            for (QuestRarity rarity : LegendaryDailyQuests.getLegendaryDailyQuests().getQuestRaritiesManager().getSorts()) {
                int selectAmount = select.stream().filter(qid -> LegendaryDailyQuests.getLegendaryDailyQuests().getQuestsManager().getQuest(qid).getRarity().getId().equals(rarity.getId())).collect(Collectors.toList()).size();
                if (rarity.getMax() > 0 && selectAmount >= rarity.getMax()) {
                    continue;
                }
                int roll = (new Random()).nextInt(101);
                if (roll <= rarity.getChance()) {
                    List<String> quests = new ArrayList<>(categorize.getQuests().stream().filter(quest -> {
                                Quest q = LegendaryDailyQuests.getLegendaryDailyQuests().getQuestsManager().getQuest(quest);
                                if (q != null) {
                                    return q.getRarity().getId().equals(rarity.getId());
                                }
                                return false;
                            }
                    ).collect(Collectors.toList()));
                    if (!quests.isEmpty()) {
                        roll = (new Random()).nextInt(quests.size());
                        String questId = quests.remove(roll);
                        if (!select.contains(questId) && LegendaryDailyQuests.getLegendaryDailyQuests().getQuestsManager().getQuest(questId) != null) {
                            select.add(questId);
                            a--;

                        }
                    }
                }
            }
            round++;
        }
        int roll = categorize.getRefreshPoints(Bukkit.getPlayer(uuid));
        if (reset && roll >= 0) {
            data.getRefresh().put(id, categorize.getRefreshPoints(Bukkit.getPlayer(uuid)));
        }
        data.getQuests().put(id,select);
        data.update(false);
        //触发事件
        Bukkit.getScheduler().runTask(LegendaryDailyQuests.getLegendaryDailyQuests(),()->Bukkit.getPluginManager().callEvent(new PlayerDailyQuestsRefreshEvent(uuid, categorize.getId(), select)));
    }
    public static boolean checkCanAcceptQuestAndAccept(Player p, PlayerData data, String cat, Quest quest){
        LinkedList<String> quests = data.getQuests().getOrDefault(cat,new LinkedList<>());
        String questId = quest.getId();
        if (quests.contains(questId)) {
            List<String> accepts = new ArrayList<>(data.getAccepts().getOrDefault(cat,new ArrayList<>()));
            if (accepts.contains(questId)){
                return false;
            }
            accepts.add(questId);
            data.getAccepts().put(cat,accepts);
            data.update(false);

            p.sendMessage(lang.PLUGIN+lang.accept.replace("%quest%",quest.getDisplay()));
            config.accept.ifPresent(sound -> p.playSound(p.getLocation(),sound,1,1));
            return true;
        }
        return false;
    }

    public static void acceptQuest(Player p,PlayerData data,String cat,Quest quest) {
        String questId = quest.getId();
        List<String> accepts = new ArrayList<>(data.getAccepts().getOrDefault(cat,new ArrayList<>()));
        if (accepts.contains(questId)){
            return;
        }
        accepts.add(questId);
        data.getAccepts().put(cat,accepts);
        data.update(false);
        //触发事件
        Bukkit.getScheduler().runTask(LegendaryDailyQuests.getLegendaryDailyQuests(),()->Bukkit.getPluginManager().callEvent(new PlayerAcceptQuestEvent(p,cat,questId)));

        p.sendMessage(lang.PLUGIN+lang.accept.replace("%quest%",quest.getDisplay()));
        config.accept.ifPresent(sound -> p.playSound(p.getLocation(),sound,1,1));
        if (quest.getRarity().isBroad()) {
            Bukkit.broadcastMessage(lang.PLUGIN + lang.broad_when_accept.replace("%player%",p.getName()).replace("%rarity%",quest.getRarity().getDisplay()).replace("%quest%",quest.getDisplay()));
        }

    }

    public static boolean hasAcceptQuest(PlayerData data,String cat,String questId){
        LinkedList<String> quests = data.getQuests().getOrDefault(cat,new LinkedList<>());
        if (quests.contains(questId)) {
            if (data.getAccepts(cat).contains(questId)) {
                return true;
            }
        }
        return false;
    }
    public static void completedQuest(Player p, PlayerData data, String cat, Quest quest,boolean force) {
        String questId = quest.getId();
        List<String> completeds = new ArrayList<>(data.getCompleteds().getOrDefault(cat,new ArrayList<>()));
        if (!completeds.contains(questId)) {
            completeds.add(questId);
            data.getCompleteds().put(cat, completeds);
            if (force) {
                ProgressData progressData = data.getProgressData(cat);
                quest.getObjectives().forEach(o -> progressData.setProgress(questId,o.getId(),o.getAmount()));
                data.getProgressData().put(cat,progressData);
            }
            data.update(false);

            config.completed.ifPresent(sound -> p.playSound(p.getLocation(), sound, 1, 1));
            p.sendMessage(lang.PLUGIN + lang.completed.replace("%quest%", quest.getDisplay()));
            p.sendTitle(lang.completed_title.replace("%quest%", quest.getDisplay()), lang.completed_subtitle.replace("%quest%", quest.getDisplay()));
            RunUtils.run(p, quest.getRewards());
            Bukkit.getScheduler().runTask(LegendaryDailyQuests.getLegendaryDailyQuests(), () -> Bukkit.getPluginManager().callEvent(new PlayerCompletedQuestEvent(p, cat, questId)));


            if (hasCompletedAll(data, LegendaryDailyQuests.getLegendaryDailyQuests().getCategorizesManager().getCategorize(cat))) {
                p.sendMessage(lang.PLUGIN + lang.completed_all.replace("%categorize%", LegendaryDailyQuests.getLegendaryDailyQuests().getCategorizesManager().getCategorize(cat).getDisplay()));
                CompletedData completedData = getCompletedData();
                completedData.addUUID(cat, p.getUniqueId());
                completedData.update();

                Bukkit.getScheduler().runTask(LegendaryDailyQuests.getLegendaryDailyQuests(), () -> Bukkit.getPluginManager().callEvent(new PlayerCompletedAllQuestsEvent(p, cat)));
            }
        }
    }
    public static CompletedData getCompletedData(){
        return LegendaryDailyQuests.getLegendaryDailyQuests().getSystemDataManager().getCompletedData();
    }
    public static double getProgressPercent(Player p, PlayerData data, String cat, Quest quest) {
        int full = 100 / quest.getObjectives().size();
        double total = 0.0;
        for (QuestObjective objective : quest.getObjectives()) {
            double target = objective.getAmount();
            double progress = data.getProgressData().getOrDefault(cat,new ProgressData(new HashMap<>())).getProgress(quest.getId(), objective.getId());
            double percent = progress / target;
            double add = percent * full;
            total+=add;
        }
        String df = new DecimalFormat("#0.00").format(total);
        return Double.parseDouble(df);
    }

    public static boolean hasCompletedAll(PlayerData data, Categorize categorize) {
        LinkedList<String> quests = data.getQuests().getOrDefault(categorize.getId(),new LinkedList<>());
        if (quests.isEmpty()){
            return false;
        }
        List<String> completeds = data.getCompleteds().getOrDefault(categorize.getId(),new ArrayList<>());
        for (String quest : quests){
            if (!completeds.contains(quest)){
                return false;
            }
        }
        return true;
    }

}
