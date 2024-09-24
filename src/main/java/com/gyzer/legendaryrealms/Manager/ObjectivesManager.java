package com.gyzer.legendaryrealms.Manager;

import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class ObjectivesManager {
    private final LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();

    public ObjectivesManager() {

    }

    public ObjectiveType getType(String str){
        try {
            ObjectiveType type = ObjectiveType.valueOf(str.toUpperCase());
            if (type.getPlugin() != null) {
                if (Bukkit.getPluginManager().isPluginEnabled(type.getPlugin())) {
                    return  type;
                }
                legendaryDailyQuests.info("The goal "+str+" need plugin: "+type.getPlugin()+"!",Level.SEVERE);
                return null;
            }
            return type;
        } catch (IllegalArgumentException e){
            legendaryDailyQuests.info("Unable to find the goal type: "+str, Level.SEVERE);
            return null;
        }
    }
}
