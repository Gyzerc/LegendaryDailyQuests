package com.gyzer.legendaryrealms.Manager;

import com.gyzer.legendaryrealms.Configurations.Language;
import com.gyzer.legendaryrealms.Data.Quest.Condition.Condition;
import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import com.gyzer.legendaryrealms.Data.Quest.Objective.QuestObjective;
import com.gyzer.legendaryrealms.Data.Quest.Quest;
import com.gyzer.legendaryrealms.Data.Quest.QuestRarity;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Utils.ItemUtils;
import com.gyzer.legendaryrealms.Utils.MsgUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class QuestsManager {
    private final LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();
    private final Language language = legendaryDailyQuests.getConfigurationsManager().getLanguage();
    private HashMap<String, Quest> caches;
    public QuestsManager(){
        caches = new HashMap<>();
        legendaryDailyQuests.sync(()->loadAllQuests());
    }

    public Quest getQuest(String id){
        return caches.get(id);
    }
    private void loadAllQuests() {
        File file = new File(legendaryDailyQuests.getDataFolder()+"/Quests");
        if (!file.exists()){
            Arrays.asList("Butcher","Golden_Miner","Material_Shortage_1","Material_Shortage_2","Stonemason","Happy_Trade","Hunger","Running_Man","Adventurer","Adventurer_2","Adventurer_3","Adventurer_4","Hungriness",
                            "TriggerQuest","Milking","Farmer","Goodbye_World","FishMan","Breeder","Blacksmith","Clother","Warrior","Enchanter","Timer","Refiner")
                    .forEach(s -> legendaryDailyQuests.saveResource("Quests/"+s+".yml",false));
        }
        File[] files = file.listFiles();
        int a = 0;
        if (files != null){
            for (File QuestFile : files){
                String id = QuestFile.getName().replace(".yml","");
                YamlConfiguration yml = YamlConfiguration.loadConfiguration(QuestFile);


                String display = MsgUtils.msg(yml.getString("display","Quest"));
                Material preview_material = ItemUtils.getMaterial(yml.getString("material","STONE")).orElse(Material.STONE);
                int preview_amount = yml.getInt("amount",1);
                int preview_model = yml.getInt("model",0);
                List<String> preview_lore = MsgUtils.msg(yml.getStringList("description"));
                List<String> preview_rewards = MsgUtils.msg(yml.getStringList("reward_description"));
                List<String> rewards = yml.getStringList("reward.run");
                String rarityStr = yml.getString("rarity");
                QuestRarity rarity = rarityStr != null ? (legendaryDailyQuests.getQuestRaritiesManager().getRarity(rarityStr) != null ? legendaryDailyQuests.getQuestRaritiesManager().getRarity(rarityStr) : legendaryDailyQuests.getQuestRaritiesManager().getLowest()) : legendaryDailyQuests.getQuestRaritiesManager().getLowest();


                ConfigurationSection section = yml.getConfigurationSection("goals");
                if (section == null){
                    legendaryDailyQuests.info("There is no goal set for this task! Skipped loading the task: "+QuestFile.getName(), Level.SEVERE);
                    continue;
                }

                LinkedList<QuestObjective> objectives = new LinkedList<>();
                for (String goalId : section.getKeys(false)){
                    ObjectiveType type = legendaryDailyQuests.getObjectivesManager().getType(section.getString(goalId+".type","NULL"));
                    if (type != null){
                        String value = section.getString(goalId+".value",null);
                        if (value == null){
                            continue;
                        }
                        double amount = section.getDouble(goalId+".amount",1);
                        Condition[] conditions = legendaryDailyQuests.getConditionsManager().matchCondition(section.getStringList(goalId+".conditions"));
                        List<String> run = section.getStringList(goalId+".run");
                        objectives.add(new QuestObjective(goalId,type,value,amount,run,conditions));
                    }
                }
                if (objectives.size() == 0){
                    legendaryDailyQuests.info("The goal of this task was not set correctly, and loading the task has been skipped: "+QuestFile.getName(), Level.SEVERE);
                    continue;
                }
                caches.put(id,new Quest(id,display,preview_material,preview_model,preview_amount,preview_lore,preview_rewards,objectives,rewards,rarity));
                legendaryDailyQuests.sendConsoleMessage(language.PLUGIN+" &aSuccessfully load quest &f"+id+"");
                a++;
             }
        }
        legendaryDailyQuests.sendConsoleMessage(language.PLUGIN+" &eloaded &a"+a+" &equests.");
    }

    public List<String> getQuestsByRarity(QuestRarity rarity) {
        return caches.values().stream().filter(q -> q.getRarity().getId().equals(rarity.getId())).map(q -> q.getId()).collect(Collectors.toList());
    }
}
