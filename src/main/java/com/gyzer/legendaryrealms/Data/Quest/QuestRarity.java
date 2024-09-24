package com.gyzer.legendaryrealms.Data.Quest;

public class QuestRarity {
    private final String id;
    private final String display;
    private final int weight;
    private final int chance;
    private final boolean broad;
    private final int max;

    public QuestRarity(String id, String display, int weight, int chance, boolean broad, int max) {
        this.id = id;
        this.display = display;
        this.weight = weight;
        this.chance = chance;
        this.broad = broad;
        this.max = max;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public int getWeight() {
        return weight;
    }

    public int getChance() {
        return chance;
    }

    public boolean isBroad() {
        return broad;
    }

    public int getMax() {
        return max;
    }
}
