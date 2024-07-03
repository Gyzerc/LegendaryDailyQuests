package com.gyzer.legendaryrealms.Listeners.Other;

import com.gyzer.legendaryrealms.Data.Quest.Checker.StringGoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;


public class BlockEvent implements Listener {
    public static final NamespacedKey namespacedKey_break = new NamespacedKey(LegendaryDailyQuests.getLegendaryDailyQuests(),"break");

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if (!e.isCancelled()){
            Player p = e.getPlayer();
            e.getBlock().setMetadata("LegendaryDailyQuest_PlayerPlace", (MetadataValue)new FixedMetadataValue(LegendaryDailyQuests.getLegendaryDailyQuests(), p.getName()));
            if (!isPlacedBlock(p)) {
                new StringGoalChecker(e.getBlockPlaced().getType().name()).check(p, ObjectiveType.PLACE,1);
            }
        }
    }

    private boolean isPlacedBlock(Player p) {
        if (LegendaryDailyQuests.getLegendaryDailyQuests().version_high) {
            ItemStack i = p.getInventory().getItemInMainHand();
            if (i != null && !i.getType().equals(Material.AIR)) {
                ItemMeta id = i.getItemMeta();
                PersistentDataContainer data = id.getPersistentDataContainer();
                String key = data.get(namespacedKey_break, PersistentDataType.STRING);
                if (key != null && key.equals(p.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if (!e.isCancelled() ) {
            if (e.getBlock().getMetadata("LegendaryDailyQuest_PlayerPlace").isEmpty()) {
                new StringGoalChecker(e.getBlock().getType().name()).check(e.getPlayer(), ObjectiveType.BREAK, 1);
            }
        }
    }

}
