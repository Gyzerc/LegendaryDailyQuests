package com.gyzer.legendaryrealms.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerCompletedQuestEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
   private final Player p;
   private final String cat;
   private final String questId;

    public PlayerCompletedQuestEvent(Player p, String cat, String questId) {
        this.p = p;
        this.cat = cat;
        this.questId = questId;
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
}
