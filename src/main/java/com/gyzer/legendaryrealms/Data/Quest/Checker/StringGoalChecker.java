package com.gyzer.legendaryrealms.Data.Quest.Checker;

import com.gyzer.legendaryrealms.Data.Quest.GoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.QuestObjective;
import com.gyzer.legendaryrealms.Utils.MsgUtils;
import org.bukkit.entity.Player;

public class StringGoalChecker extends GoalChecker {
    /*public void check(Player p, ObjectiveType type, String target, double amount) {
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
                    if (objectiveType.getValue().equals( target )) {
                        addProgress(p,data,cat,quest,id,amount);
                    }
                }
            }
        }
    } */
    private final String input;

    public StringGoalChecker(String input) {
        this.input = input;
    }

    @Override
    public int canPass(Player p, QuestObjective objective, double current, double amount) {
        return MsgUtils.msg(objective.getValue()).equals(input) ? 1 : -1;
    }
}
