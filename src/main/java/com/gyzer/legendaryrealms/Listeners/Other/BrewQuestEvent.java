package com.gyzer.legendaryrealms.Listeners.Other;

import com.gyzer.legendaryrealms.Data.Quest.Checker.StringGoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.HashMap;

public class BrewQuestEvent implements Listener {
    private HashMap<Location, String[]> stackCache=new HashMap<>();
    private HashMap<Location, Player> ownerCache=new HashMap<>();

    @EventHandler
    public void onBrewing(BrewEvent e){
        Bukkit.broadcastMessage(e.getBlock().getLocation().toString());
        Player p =  ownerCache.remove(e.getBlock().getLocation());
        if (p != null){
            String[] types=stackCache.remove(e.getBlock().getLocation());
            BrewerInventory inv=e.getContents();
            for (int slot=0;slot <=2 ; slot++){
                String old=types[slot];
                ItemStack i=inv.getItem(slot);
                if (i!=null && (i.getType().equals(Material.POTION) || i.getType().equals(Material.SPLASH_POTION)) ){
                    PotionMeta id= (PotionMeta) i.getItemMeta();
                    String type=id.getBasePotionData().getType().name();
                    if (!type.equals(old)){
                        new StringGoalChecker(type).check(p, ObjectiveType.BREW,1);
                    }
                }
            }
        }
    }


    @EventHandler
    public void onBrewingCheck(InventoryClickEvent e){
        if (e.getInventory().getType()== InventoryType.BREWING){
            Player p= (Player) e.getWhoClicked();
            BrewerInventory inv= (BrewerInventory) e.getInventory();
            if (e.getRawSlot() >=0){
                String[] types=new String[3];
                for (int slot=0; slot<=2; slot++){
                    ItemStack i=inv.getItem(slot);
                    if (i!=null && (i.getType().equals(Material.POTION) || i.getType().equals(Material.SPLASH_POTION) || i.getType().equals(Material.LINGERING_POTION))){
                        PotionMeta meta= (PotionMeta) i.getItemMeta();
                        types[slot]=meta.getBasePotionData().getType().name();
                    }
                }
                stackCache.put(inv.getLocation() , types);
                ownerCache.put(inv.getLocation() , p);
            }
        }
    }



}
