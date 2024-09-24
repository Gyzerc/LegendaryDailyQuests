package com.gyzer.legendaryrealms.Data.Quest;

import com.gyzer.legendaryrealms.API.Events.PlayerQuestProgressAddEvent;
import com.gyzer.legendaryrealms.API.LegendaryDailyQuestsAPI;
import com.gyzer.legendaryrealms.Data.Quest.Condition.Condition;
import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import com.gyzer.legendaryrealms.Data.Quest.Objective.QuestObjective;
import com.gyzer.legendaryrealms.Data.Quest.Progress.ProgressData;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Utils.ItemCheck;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public abstract class GoalChecker{
    public void addProgress(Player p, ObjectiveType type, double amount) {
        PlayerData data = LegendaryDailyQuestsAPI.getPlayerData(p);
        for (Map.Entry<String, List<String>> entry:data.getAccepts().entrySet()){
            List<String> completed = data.getCompleteds().getOrDefault(entry.getKey(),new ArrayList<>());
            String cat = entry.getKey();
            List<String> accepts = entry.getValue();
            ProgressData progressData = data.getProgressData().getOrDefault(cat,new ProgressData(new HashMap<>()));
            for (Quest quest : accepts.stream().map(s -> LegendaryDailyQuests.getLegendaryDailyQuests().getQuestsManager().getQuest(s)).collect(Collectors.toList())) {
                String questId = quest.getId();
                if (completed.contains(questId)){
                    continue;
                }
                for (QuestObjective objectiveType:quest.getObjectives().stream().filter(questObjective -> questObjective.getType().equals(type)).collect(Collectors.toList())) {
                    String id = objectiveType.getId();
                    double current = progressData.getProgress(questId,id);
                    if (current >= objectiveType.getAmount()){
                        continue;
                    }
                    addProgress(p,data,cat,quest,id,amount);
                }
            }
        }
    }

    public void addProgress(Player p,PlayerData data,String cat,Quest quest,String goal,double amount){
        String questId = quest.getId();
        List<String> completeds = data.getCompleteds().getOrDefault(cat,new ArrayList<>());
        if (completeds.contains(questId)){
            return;
        }
        ProgressData progressData = data.getProgressData(cat);
        double current = progressData.getProgress(questId,goal);
        double target = quest.getObjectives().stream().filter(questObjective -> questObjective.getId().equals(goal)).findFirst().get().getAmount();
        double set = Double.parseDouble(LegendaryDailyQuests.getLegendaryDailyQuests().toSmallCount(Math.min(target,(current+amount))));
        progressData.setProgress(questId,goal,set);
        data.getProgressData().put(cat,progressData);
        data.update(false);
        //触发事件
        Bukkit.getScheduler().runTask(LegendaryDailyQuests.getLegendaryDailyQuests(),()->Bukkit.getPluginManager().callEvent(new PlayerQuestProgressAddEvent(p,cat,questId,goal,current,set)));




        boolean completed = true;
        int needAmount = quest.getCompleteGoals();
        int completed_amount = 0;

        for (QuestObjective objective : quest.getObjectives()){
            String id = objective.getId();
            if (objective.getAmount() > progressData.getProgress(questId,id)){
                completed = false;
                if (needAmount == -1) {
                    break;
                }
            } else {
                if (needAmount > 0) {
                    completed_amount+=1;
                    if (completed_amount >= needAmount) {
                        completed = true;
                        break;
                    }
                }
            }
        }
        if (completed) {
            LegendaryDailyQuestsAPI.completedQuest(p,data,cat,quest,false);
        }
    }



    public void check(Player p, ObjectiveType type, double amount) {
        if (type.equals(ObjectiveType.GIVE_ITEM)) {
            return;
        }
        PlayerData data = LegendaryDailyQuestsAPI.getPlayerData(p);
        for (Map.Entry<String, List<String>> entry:data.getAccepts().entrySet()){
            List<String> completed = data.getCompleteds().getOrDefault(entry.getKey(),new ArrayList<>());
            String cat = entry.getKey();
            List<String> accepts = entry.getValue();

            ProgressData progressData = data.getProgressData().getOrDefault(cat,new ProgressData(new HashMap<>()));
            for (Quest quest : accepts.stream().map(s -> LegendaryDailyQuests.getLegendaryDailyQuests().getQuestsManager().getQuest(s)).collect(Collectors.toList())) {
                String questId = quest.getId();
                if (completed.contains(questId)){
                    continue;
                }
                for (QuestObjective objectiveType:quest.getObjectives().stream().filter(questObjective -> questObjective.getType().equals(type)).collect(Collectors.toList())) {
                    String id = objectiveType.getId();
                    double current = progressData.getProgress(questId,id);
                    if (current >= objectiveType.getAmount()){
                        continue;
                    }
                    if (objectiveType.getValue().equals("ANY")) {
                        addProgress(p,data,cat,quest,id,amount);
                        continue;
                    }
                    if (canPass(p,objectiveType,current,objectiveType.getAmount()) > 0) {
                        if (canPassConditions(p,objectiveType.getConditions())) {
                            if (type.equals(ObjectiveType.PLACEHOLDERAPI)) {
                                amount = objectiveType.getAmount();
                            }
                            addProgress(p, data, cat, quest, id, amount);
                        }
                    }
                }
            }
        }
    }

    private boolean canPassConditions(Player p,Condition[] conditions) {
        for (Condition condition : conditions) {
            if (!condition.canPass(p)){
                return false;
            }
        }
        return true;
    }

    public boolean checkGive_Item(Player p, ObjectiveType type, Categorize categorize) {
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
                for (QuestObjective objectiveType : quest.getObjectives().stream().filter(questObjective -> questObjective.getType().equals(type)).collect(Collectors.toList())) {
                    String id = objectiveType.getId();
                    int current = (int) progressData.getProgress(questId, id);
                    int need = (int) objectiveType.getAmount();
                    if (current >= need) {
                        continue;
                    }
                    //检测玩家背包是否含有指定物品
                    int take =  canPass(p,objectiveType,current, objectiveType.getAmount());
                    if (take > 0) {
                        if ( (quest.getCompleteGoals() != -1 && (take + current) >= need) || quest.getCompleteGoals() == -1) {
                            new ItemCheck(objectiveType.getValue()).takePlayerInventory(p,take);
                            addProgress(p, data, cat, quest, id, take);
                            hasTaken = true;
                        }
                    }
                }
            }
        }
        return hasTaken;
    }

    public abstract int canPass(Player p,QuestObjective objective,double current,double amount);
}
