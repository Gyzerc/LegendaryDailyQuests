package com.gyzer.legendaryrealms.Utils;

import org.bukkit.Material;
import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {
    public static Optional<Material> getMaterial(String str){
        return Material.getMaterial(str) != null ? Optional.of(Material.getMaterial(str)) : Optional.empty();
    }
    public static int hasPlayerMcItem(Player p, Material id) {
        int sl = 0;
        int maxid = 0;
        ItemStack[] var8;
        int var7 = (var8 = p.getInventory().getContents()).length;
        for (int var6 = 0; var6 < var7; var6++) {
            ItemStack item = var8[var6];
            if (maxid < 36 && item != null && item.getType() == id)
               if (!item.getItemMeta().hasDisplayName()) {
                    sl += item.getAmount();
               }
            maxid++;
        }
        return sl;
    }

    public static int hasPlayerItem(Player p, String itemname) {
        int nbsl = 0;
        ItemStack[] var12;
        int var11 = (var12 = p.getInventory().getContents()).length;
        for (int var10 = 0; var10 < var11; var10++) {
            ItemStack s = var12[var10];
            if (s != null && s.getItemMeta().hasDisplayName() && s.getItemMeta().getDisplayName().equals(itemname))
                nbsl += s.getAmount();
        }
        return nbsl;
    }

    public static void takePlayerMcItem(Player p,Material material,int amount) {
        int sl = amount;
        for (ItemStack i : p.getInventory().getContents()) {
            if (i != null && !i.getType().equals(Material.AIR)) {
                if (i.getType().equals(material)) {
                    if (!i.getItemMeta().hasDisplayName() && !i.getItemMeta().hasLore()) {
                        int has = i.getAmount();
                        if (has >= sl) {
                            i.setAmount( has - sl);
                            return;
                        }
                        i.setAmount(0);
                        sl=sl-has;
                    }
                }
            }
        }
    }

    public static void takePlayerItem(Player p,String name,int amount) {
        int sl = amount;
        for (ItemStack i : p.getInventory().getContents()) {
            if (i != null && !i.getType().equals(Material.AIR) && i.hasItemMeta() && i.getItemMeta().hasDisplayName()) {
                if (i.getItemMeta().getDisplayName().equals(name)) {
                    int has = i.getAmount();
                    if (has >= sl) {
                        i.setAmount(has-sl);
                        return;
                    }
                    i.setAmount(0);
                    sl = sl - has;
                }
            }
        }
    }
}
