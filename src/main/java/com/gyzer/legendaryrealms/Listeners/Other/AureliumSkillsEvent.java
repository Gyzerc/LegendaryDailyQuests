package com.gyzer.legendaryrealms.Listeners.Other;

import com.archyx.aureliumskills.api.event.XpGainEvent;
import com.gyzer.legendaryrealms.Data.Quest.Checker.StringGoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AureliumSkillsEvent implements Listener {
    @EventHandler
    public void onGain(XpGainEvent e) {
        new StringGoalChecker(e.getSkill().name()).check(e.getPlayer(), ObjectiveType.AURELIUMSKILLS_EXP,e.getAmount());
    }
}
