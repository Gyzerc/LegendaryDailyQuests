package com.gyzer.legendaryrealms.Data.Quest.Checker;

import com.gyzer.legendaryrealms.Utils.ItemCheck;
import com.gyzer.legendaryrealms.Data.Quest.GoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.QuestObjective;
import org.bukkit.entity.Player;

public class GiveItemGoalChecker extends GoalChecker {


    @Override
    public int canPass(Player p,QuestObjective objective,double now, double amount) {
        ItemCheck check = new ItemCheck(objective.getValue());
        int has = check.hasAmountPlayerInventory(p);
        int need = (int)amount;
        int current = (int)now;
        if (has > 0) {
            int take =  Math.min(has,( need - current ));
            check.takePlayerInventory(p , take);
            return take;
        }
        return -1;
    }


   /* public boolean check(Player p, ObjectiveType type, Categorize categorize) {
        boolean hasTaken = false;
        PlayerData data = LegendaryDailyQuestsAPI.getPlayerData(p);
        String cat = categorize.getId();
        List<String> completed = data.getCompleteds(cat);
        for (String questId:data.getAccepts(cat)){
            Quest quest = LegendaryDailyQuests.getLegendaryDailyQuests().getQuestsManager().getQuest(questId);
            if (quest != null) {
                ProgressData progressData = data.getProgressData(cat);
                if (completed.contains(questId)) {
                    continue;
                }
                for (QuestObjective objectiveType : quest.getObjectives().stream().filter(questObjective -> questObjective.getType().equals(type)).toList()) {
                    String id = objectiveType.getId();
                    int current = (int) progressData.getProgress(questId, id);
                    int need = (int) objectiveType.getAmount();
                    if (current >= need) {
                        continue;
                    }
                    //检测玩家背包是否含有指定物品
                    ItemCheck check = new ItemCheck(objectiveType.getValue());
                    int has = check.hasAmountPlayerInventory(p);
                    if (has > 0) {
                        int take =  Math.min(has,( need - current ));
                        check.takePlayerInventory(p , take);
                        addProgress(p, data, cat, quest, id, take);
                        hasTaken = true;
                    }
                }
            }
        }
        return hasTaken;
    }*/
}
