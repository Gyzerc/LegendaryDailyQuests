package com.gyzer.legendaryrealms.Data.Quest.Checker;

import com.gyzer.legendaryrealms.Data.Quest.GoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.QuestObjective;
import org.bukkit.entity.Player;

public class GoalNoCheck extends GoalChecker {
    @Override
    public int canPass(Player p, QuestObjective objective, double current, double amount) {
        return 1;
    }
}
