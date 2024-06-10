package com.gyzer.legendaryrealms.API.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NewDayEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {return handlers;}
    private int date;

    public NewDayEvent(int date) {
        this.date = date;
    }

    public int getDate() {
        return date;
    }
}
