package com.gyzer.legendaryrealms.Manager;

import com.gyzer.legendaryrealms.Integrations.PlaceholderAPIHook;
import com.gyzer.legendaryrealms.Integrations.VaultHook;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Listeners.Other.*;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class IntegrationsManager {
    private final VaultHook vaultHook;
    private final PlaceholderAPIHook placeholderAPIHook;
    public IntegrationsManager() {
        placeholderAPIHook = new PlaceholderAPIHook(this);
        vaultHook = new VaultHook(this);
        if (Bukkit.getPluginManager().isPluginEnabled("AureliumSkills")) {
            Bukkit.getPluginManager().registerEvents(new AureliumSkillsEvent(),LegendaryDailyQuests.getLegendaryDailyQuests());
            msg("AureliumSkills");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("mcMMO")) {
            Bukkit.getPluginManager().registerEvents(new mcMMOEvent(),LegendaryDailyQuests.getLegendaryDailyQuests());
            msg("mcMMO");
        }
    }

    public VaultHook getVaultHook() {
        return vaultHook;
    }

    public PlaceholderAPIHook getPlaceholderAPIHook() {
        return placeholderAPIHook;
    }

    public void msg(String plugin) {
        LegendaryDailyQuests.getLegendaryDailyQuests().info("Hooked plugin "+plugin, Level.INFO);
    }
}
