package com.gyzer.legendaryrealms.Data.Quest;

import com.gyzer.legendaryrealms.Data.Quest.Objective.QuestObjective;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class Quest {
    private String id;
    private String display;
    private Material preview_material;
    private int preview_model;
    private int preview_amount;
    private List<String> preview_lore;
    private List<String> preview_reward;
    private LinkedList<QuestObjective> objectives;
    private List<String> rewards;
    private QuestRarity rarity;
    private int completeGoals;
    public Quest(String id, String display,Material preview_material, int preview_model, int preview_amount, List<String> preview_lore, List<String> preview_reward, LinkedList<QuestObjective> objectives, List<String> rewards,QuestRarity rarity,int completeGoals) {
        this.id = id;
        this.display = display;
        this.preview_material = preview_material;
        this.preview_model = preview_model;
        this.preview_amount = preview_amount;
        this.preview_lore = preview_lore;
        this.preview_reward = preview_reward;
        this.objectives = objectives;
        this.rewards = rewards;
        this.rarity = rarity;
        this.completeGoals = completeGoals;
    }

    public int getCompleteGoals() {
        return completeGoals;
    }

    public QuestRarity getRarity() {
        return rarity;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public Material getPreview_material() {
        return preview_material;
    }

    public int getPreview_model() {
        return preview_model;
    }

    public int getPreview_amount() {
        return preview_amount;
    }

    public List<String> getPreview_lore() {
        return preview_lore;
    }

    public List<String> getPreview_reward() {
        return preview_reward;
    }

    public LinkedList<QuestObjective> getObjectives() {
        return objectives;
    }

    public List<String> getRewards() {
        return rewards;
    }
}

