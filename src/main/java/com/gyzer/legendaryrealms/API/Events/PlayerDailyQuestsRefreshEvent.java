package com.gyzer.legendaryrealms.API.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.UUID;

public class PlayerDailyQuestsRefreshEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    private UUID uuid;
    private String cat;
    private LinkedList<String> refreshQuests;

    public PlayerDailyQuestsRefreshEvent(UUID uuid, String cat, LinkedList<String> refreshQuests) {
        this.uuid = uuid;
        this.cat = cat;
        this.refreshQuests = refreshQuests;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getCategorize() {
        return cat;
    }

    public LinkedList<String> getRefreshQuests() {
        return refreshQuests;
    }
}
