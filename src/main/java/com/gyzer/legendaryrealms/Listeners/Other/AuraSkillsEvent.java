package com.gyzer.legendaryrealms.Listeners.Other;

import com.gyzer.legendaryrealms.Data.Quest.Checker.StringGoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import dev.aurelium.auraskills.api.event.skill.XpGainEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AuraSkillsEvent implements Listener {
    @EventHandler
    public void onGain(XpGainEvent e) {
        new StringGoalChecker(e.getSkill().name()).check(e.getPlayer(), ObjectiveType.AURASKILLS_EXP,e.getAmount());
    }
}
