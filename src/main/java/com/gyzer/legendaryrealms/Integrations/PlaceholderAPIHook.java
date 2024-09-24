package com.gyzer.legendaryrealms.Integrations;

import com.gyzer.legendaryrealms.Manager.IntegrationsManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderAPIHook {
    private final IntegrationsManager integrationsManager;
    private boolean enable = false;
    public PlaceholderAPIHook(IntegrationsManager integrationsManager) {
        this.integrationsManager = integrationsManager;
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            enable = true;
            integrationsManager.msg("PlaceholderAPI");
        }
    }

    public boolean isEnable() {
        return enable;
    }

    public String getHolder(Player p, String input) {
        return PlaceholderAPI.setPlaceholders(p,input);
    }
    public String getHolder(OfflinePlayer p,String input) {
        return PlaceholderAPI.setPlaceholders(p,input);
    }
}
