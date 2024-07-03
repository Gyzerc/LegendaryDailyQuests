package com.gyzer.legendaryrealms.Manager;

import com.gyzer.legendaryrealms.Data.Quest.QuestRarity;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Utils.MsgUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class QuestRaritiesManager {
    private Map<String, QuestRarity> caches;
    private LegendaryDailyQuests legendaryDailyQuests;
    private LinkedList<QuestRarity> sorts;

    public QuestRaritiesManager(LegendaryDailyQuests legendaryDailyQuests) {
        this.legendaryDailyQuests = legendaryDailyQuests;
        caches = new HashMap<>();
        loadRarities();
    }

    private void loadRarities() {
        File file = new File(legendaryDailyQuests.getDataFolder() , "Rarities.yml");
        if (!file.exists()) {
            legendaryDailyQuests.saveResource("Rarities.yml",false);
        }
        YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = yml.getConfigurationSection("");
        if (section != null) {
            for (String id : section.getKeys(false)) {
                String display = MsgUtils.msg(section.getString(id+".display"));
                int weight = section.getInt(id+".weight",caches.size());
                int max = section.getInt(id+".max",-1);
                int chance = section.getInt(id+".chance",100);
                boolean broad = section.getBoolean(id+".broad-when-accept",false);
                caches.put(id,new QuestRarity(id,display,weight,chance,broad,max));
            }
        }
        sorts = new LinkedList<>();
        for (Map.Entry<String, QuestRarity> entry : this.caches.entrySet()) {
            sorts.add(entry.getValue());
            Collections.sort(sorts, new Comparator<QuestRarity>() {
                public int compare(QuestRarity o1, QuestRarity o2) {
                    return o2.getWeight() - o1.getWeight();
                }
            });
        }

        legendaryDailyQuests.sendConsoleMessage(legendaryDailyQuests.getConfigurationsManager().getLanguage().PLUGIN+" &eloaded &a"+caches.size()+" &equest rarities.");
    }

    public QuestRarity getRarity(String id) {
        return caches.get(id);
    }
    public List<String> getRarites() {
        return caches.keySet().stream().collect(Collectors.toList());
    }

    public LinkedList<QuestRarity> getSorts() {
        return sorts;
    }

    public QuestRarity getLowest() {
        return sorts.get(0);
    }
}
