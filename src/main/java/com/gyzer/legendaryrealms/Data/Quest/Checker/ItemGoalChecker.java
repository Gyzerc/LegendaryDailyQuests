package com.gyzer.legendaryrealms.Data.Quest.Checker;

import com.gyzer.legendaryrealms.Data.Quest.GoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.QuestObjective;
import com.gyzer.legendaryrealms.Utils.ItemCheck;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemGoalChecker extends GoalChecker {
    private final ItemStack input;

    public ItemGoalChecker(ItemStack input) {
        this.input = input;
    }

    @Override
    public int canPass(Player p, QuestObjective objective, double current, double amount) {
        ItemCheck check = new ItemCheck(objective.getValue());
        return check.Compare(input) ? 1 : -1;
    }
}
