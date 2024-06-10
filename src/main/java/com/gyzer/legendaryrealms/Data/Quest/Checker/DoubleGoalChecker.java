package com.gyzer.legendaryrealms.Data.Quest.Checker;

import com.gyzer.legendaryrealms.Data.Quest.GoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.QuestObjective;
import com.gyzer.legendaryrealms.Utils.StringUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DoubleGoalChecker extends GoalChecker {
    @Override
    public int canPass(Player p, QuestObjective objective, double current, double amount) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            double value = StringUtils.getDouble(PlaceholderAPI.setPlaceholders(p,objective.getValue()),0.0);
            if (value >= objective.getAmount()) {

                return 1;
            }
        }
        return -1;
    }
}
