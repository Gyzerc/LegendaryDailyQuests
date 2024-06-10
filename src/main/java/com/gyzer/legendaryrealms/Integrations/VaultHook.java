package com.gyzer.legendaryrealms.Integrations;

import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Manager.IntegrationsManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    private Economy economy;
    private IntegrationsManager integrationsManager;
    public VaultHook(IntegrationsManager integrationsManager) {
        this.integrationsManager = integrationsManager;
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Economy> rsp = LegendaryDailyQuests.getLegendaryDailyQuests().getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                this.economy = rsp.getProvider();
                integrationsManager.msg("Vault");
            }
        }
    }
    public Economy getEconomy() {
        return economy;
    }
    public double look(Player p){
        return economy.getBalance(p);
    }
    public boolean has(Player p,double amount) {
        return look(p) >= amount;
    }
    public void take(Player p,double amount) {
        economy.withdrawPlayer(p,amount);
    }
    public void add(Player p,double amount){
        economy.depositPlayer(p,amount);
    }
}
