package com.gyzer.legendaryrealms.Listeners.Other;

import com.gyzer.legendaryrealms.Data.Quest.Checker.ItemGoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class HarvestEvent implements Listener {
    private final HashMap<Player, Location> cache;

    public HarvestEvent() {
        cache = new HashMap<>();
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e){
        if (!e.isCancelled()) {
            if (e.isDropItems()) {
                cache.put(e.getPlayer(), e.getBlock().getLocation());
            }
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onBlockDrop(BlockDropItemEvent e){
        if (e.getItems().isEmpty()){
            return;
        }
        if (cache.containsKey(e.getPlayer())) {
            if (cache.get(e.getPlayer()).equals(e.getBlock().getLocation())) {
                for (Item item : e.getItems()) {
                    ItemStack i = item.getItemStack();
                    ItemStack ci = i.clone();
                    ci.setAmount(1);
                    new ItemGoalChecker(ci).check(e.getPlayer(), ObjectiveType.HARVEST,i.getAmount());
                }
            }
        }
    }
}
