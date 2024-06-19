package com.gyzer.legendaryrealms.Data.User;

import com.gyzer.legendaryrealms.API.LegendaryDailyQuestsAPI;
import com.gyzer.legendaryrealms.Data.Quest.Progress.Progress;
import com.gyzer.legendaryrealms.Data.Quest.Progress.ProgressData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Utils.StringUtils;
import org.bukkit.Bukkit;

import java.util.*;

public class PlayerData {
    private UUID uuid;
    private HashMap<String ,LinkedList<String>> quests;
    private HashMap<String ,List<String>> accepts;
    private HashMap<String ,List<String>> completeds;
    private HashMap<String, ProgressData> progressData;
    private HashMap<String, Boolean> claimFinallyRewards;
    private HashMap<String, Integer> refresh;

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.quests = new HashMap<>();
        this.accepts = new HashMap<>();
        this.completeds = new HashMap<>();
        this.claimFinallyRewards = new HashMap<>();
        this.refresh = new HashMap<>();
        this.progressData = new HashMap<>();
    }

    public PlayerData(UUID uuid, HashMap<String, LinkedList<String>> quests, HashMap<String, List<String>> accepts, HashMap<String, List<String>> completeds, HashMap<String, ProgressData> progressData, HashMap<String, Boolean> claimFinallyRewards,HashMap<String,Integer> refresh) {
        this.uuid = uuid;
        this.quests = quests;
        this.accepts = accepts;
        this.completeds = completeds;
        this.progressData = progressData;
        this.claimFinallyRewards = claimFinallyRewards;
        this.refresh = refresh;
    }

    public void setRefresh(HashMap<String, Integer> refresh) {
        this.refresh = refresh;
    }

    public UUID getUuid() {
        return uuid;
    }

    public HashMap<String, LinkedList<String>> getQuests() {
        return quests;
    }

    public HashMap<String, List<String>> getAccepts() {
        return accepts;
    }

    public HashMap<String, List<String>> getCompleteds() {
        return completeds;
    }

    public HashMap<String, ProgressData> getProgressData() {
        return progressData;
    }

    public ProgressData getProgressData(String cat) {
        return progressData.getOrDefault(cat,new ProgressData(new HashMap<>()));
    }

    public void takeRefresh(String cat,int amount) {
        int refresh = this.refresh.getOrDefault(cat,0);
        this.refresh.put(cat,Math.max(0,(refresh-amount)));
    }
    public void setRefresh(String cat,int amount){
        this.refresh.put(cat,amount);
    }
    public void addRefresh(String cat,int amount){
        int refresh = this.refresh.getOrDefault(cat,0);
        this.refresh.put(cat,(refresh+amount));
    }
    public boolean hasClaim(String cat){
        return claimFinallyRewards.getOrDefault(cat,false);
    }
    public void claim(String cat){
        claimFinallyRewards.put(cat,true);
    }
    public HashMap<String, Boolean> getClaimFinallyRewards() {
        return claimFinallyRewards;
    }

    public void setQuests(HashMap<String, LinkedList<String>> quests) {
        this.quests = quests;
    }

    public void setAccepts(HashMap<String, List<String>> accepts) {
        this.accepts = accepts;
    }

    public void setCompleteds(HashMap<String, List<String>> completeds) {
        this.completeds = completeds;
    }

    public void setProgressData(HashMap<String, ProgressData> progressData) {
        this.progressData = progressData;
    }

    public void setClaimFinallyRewards(HashMap<String, Boolean> claimFinallyRewards) {
        this.claimFinallyRewards = claimFinallyRewards;
    }

    public String progressToStr() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String,ProgressData> entry : progressData.entrySet()){
            builder.append("[").append(entry.getKey()).append("=").append(entry.getValue().toString()).append("]");
        }
        return builder.toString();
    }
    public void update(boolean removeCache){
        LegendaryDailyQuests.getLegendaryDailyQuests().getPlayerDataManager().update(this,removeCache);
    }

    public void checkQuests() {
        LegendaryDailyQuests.getLegendaryDailyQuests().getCategorizesManager().getCategorizes().forEach(categorize -> {
            if (quests.containsKey(categorize.getId()) || categorize.getQuests().size() == 0){
                return;
            }
            LegendaryDailyQuestsAPI.randomPlayerQuests(uuid,categorize,new ArrayList<>());
        });
    }

    public HashMap<String, Integer> getRefresh() {
        return refresh;
    }

    public List<String> getAccepts(String cat) {
        return accepts.getOrDefault(cat,new ArrayList<>());
    }
    public List<String> getCompleteds(String cat) {
        return completeds.getOrDefault(cat,new ArrayList<>());
    }
}
