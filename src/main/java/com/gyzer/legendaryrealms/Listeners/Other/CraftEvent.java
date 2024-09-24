package com.gyzer.legendaryrealms.Listeners.Other;

import com.gyzer.legendaryrealms.API.LegendaryDailyQuestsAPI;
import com.gyzer.legendaryrealms.Data.Quest.Checker.ItemGoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import com.gyzer.legendaryrealms.Data.Quest.Objective.QuestObjective;
import com.gyzer.legendaryrealms.Data.Quest.Quest;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Utils.ItemCheck;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CraftEvent implements Listener {
    @EventHandler
    public void onCraft(CraftItemEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player p = (Player) e.getWhoClicked();
            if (checkHasQuestNeed(p,e.getRecipe().getResult())) {
                ItemStack result = e.getRecipe().getResult();
                PlayerInventory inventory = p.getInventory();
                ItemStack[] contents = e.getInventory().getMatrix();
                int amount = result.getAmount();
                if (e.isShiftClick()) {
                    int space = 0;
                    for (ItemStack i : inventory.getStorageContents()) {
                        if (i == null || i.getType().equals(Material.AIR)) {
                            space += result.getMaxStackSize();
                            continue;
                        }
                        if (i.isSimilar(result)) {
                            space += result.getMaxStackSize() - i.getAmount();
                        }
                    }
                    int amountSpace = space / result.getAmount();
                    int max = Arrays.stream(contents).filter(itemStack -> itemStack != null && !itemStack.getType().equals(Material.AIR)).mapToInt(ItemStack::getAmount).min().orElse(Integer.MAX_VALUE);
                    amount = Math.min(max,amountSpace) * result.getAmount();
                }
                new ItemGoalChecker(result).check(p,ObjectiveType.CRAFT,amount);
            }
        }
    }

    private boolean checkHasQuestNeed(Player p ,ItemStack i) {
        PlayerData data = LegendaryDailyQuestsAPI.getPlayerData(p);
        for (Map.Entry<String, List<String>> accepts : data.getAccepts().entrySet()) {
            for (Quest quest : accepts.getValue().stream().map(s -> LegendaryDailyQuests.getLegendaryDailyQuests().getQuestsManager().getQuest(s)).collect(Collectors.toList())) {
                if (quest != null) {
                    List<QuestObjective> objectives = quest.getObjectives().stream().filter(objective -> objective.getType().equals(ObjectiveType.CRAFT)).collect(Collectors.toList());
                    if (objectives.size() > 0) {
                        for (QuestObjective objective : objectives) {
                            ItemCheck check = new ItemCheck(objective.getValue());
                            if (check.Compare(i)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
