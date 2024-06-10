package com.gyzer.legendaryrealms.Listeners.Other;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import com.gyzer.legendaryrealms.Data.Quest.Checker.StringGoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class mcMMOEvent implements Listener {
    @EventHandler
    public void onExp(McMMOPlayerXpGainEvent e) {
        new StringGoalChecker(e.getSkill().name()).check(e.getPlayer(),ObjectiveType.MCMMO_EXP,e.getRawXpGained());
    }
}
