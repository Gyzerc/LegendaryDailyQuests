package com.gyzer.legendaryrealms.Data.Quest.Checker;

import com.gyzer.legendaryrealms.Data.Quest.GoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.QuestObjective;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EntityGoalChecker extends GoalChecker {
    private final Entity entity;

    public EntityGoalChecker(Entity entity) {
        this.entity = entity;
    }

    @Override
    public int canPass(Player p, QuestObjective objective, double current, double amount) {
        return objective.getValue().toUpperCase().equals(entity.getType().name()) ? 1 : -1;
    }
    /*public void check(Player p, ObjectiveType type, Entity entity, double amount) {
        PlayerData data = LegendaryDailyQuestsAPI.getPlayerData(p);
        for (Map.Entry<String, List<String>> entry:data.getAccepts().entrySet()){
            List<String> completed = data.getCompleteds().getOrDefault(entry.getKey(),new ArrayList<>());
            String cat = entry.getKey();
            List<String> accepts = entry.getValue();

            ProgressData progressData = data.getProgressData().getOrDefault(cat,new ProgressData(new HashMap<>()));
            for (Quest quest : accepts.stream().map(s -> LegendaryDailyQuests.getLegendaryDailyQuests().getQuestsManager().getQuest(s)).toList()) {
                String questId = quest.getId();
                if (completed.contains(questId)){
                    continue;
                }
                for (QuestObjective objectiveType:quest.getObjectives().stream().filter(questObjective -> questObjective.getType().equals(type)).toList()) {
                    String id = objectiveType.getId();
                    double current = progressData.getProgress(questId,id);
                    if (current >= objectiveType.getAmount()){
                        continue;
                    }
                    //检测是否是相同的生物类型
                    if (objectiveType.getValue().toUpperCase().equals(entity.getType().name())) {
                        addProgress(p,data,cat,quest,id,amount);
                    }
                }
            }
        }
    }*/

}
