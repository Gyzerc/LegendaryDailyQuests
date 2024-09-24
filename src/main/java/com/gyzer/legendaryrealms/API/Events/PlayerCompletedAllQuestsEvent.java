package com.gyzer.legendaryrealms.API.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerCompletedAllQuestsEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
    private final Player p;
    private final String categorize;

    public PlayerCompletedAllQuestsEvent(Player p,String categorize) {
        this.p = p;
        this.categorize = categorize;
    }

    public Player getPlayer() {
        return p;
    }

    public String getCategorize() {
        return categorize;
    }
}
