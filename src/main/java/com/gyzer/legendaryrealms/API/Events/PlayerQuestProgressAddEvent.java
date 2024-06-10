package com.gyzer.legendaryrealms.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerQuestProgressAddEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    private Player p;
    private String cat;
    private String questId;
    private String goal;
    private double before;
    private double now;

    public PlayerQuestProgressAddEvent(Player p, String cat, String questId, String goal, double before, double now) {
        this.p = p;
        this.cat = cat;
        this.questId = questId;
        this.goal = goal;
        this.before = before;
        this.now = now;
    }

    public Player getPlayer() {
        return p;
    }

    public String getCat() {
        return cat;
    }

    public String getQuestId() {
        return questId;
    }

    public String getGoal() {
        return goal;
    }

    public double getBefore() {
        return before;
    }

    public double getNow() {
        return now;
    }
}
