package com.gyzer.legendaryrealms.Data.Quest;

import com.gyzer.legendaryrealms.Menu.MenuLoader;

import java.util.List;

public class Categorize {
    private final String id;
    private final String display;
    private final int cycle;
    private final int amount;
    private List<String> quests;
    private final MenuLoader loader;
    private final List<String> rewards;
    public Categorize(String id, String display, int cycle, int amount, List<String> quests,MenuLoader loader,List<String> rewards) {
        this.id = id;
        this.display = display;
        this.cycle = cycle;
        this.amount = amount;
        this.quests = quests;
        this.loader = loader;
        this.rewards = rewards;
    }

    public String getDisplay() {
        return display;
    }

    public List<String> getRewards() {
        return rewards;
    }

    public MenuLoader getLoader() {
        return loader;
    }

    public String getId() {
        return id;
    }

    public List<String> getQuests() {
        return quests;
    }

    public void setQuests(List<String> quests) {
        this.quests = quests;
    }

    public int getCycle() {
        return cycle;
    }

    public int getAmount() {
        return amount;
    }
}
