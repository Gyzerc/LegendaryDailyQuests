package com.gyzer.legendaryrealms.Utils;

import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Manager.IntegrationsManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemCheck {
    private final String value;
    private ItemType type = ItemType.Default;
    final IntegrationsManager integrationsManager = LegendaryDailyQuests.getLegendaryDailyQuests().getIntegrationsManager();
    String[] values ;
    String v1 ;
    String v2 ;
    String v3 ;
   public ItemCheck(String value) {
       this.value = value;
       this.values = value.split(";");
       this.v1 = values[0];
       this.v2 = values.length > 1 ? values[1] : null;
       this.v3 = values.length > 2 ? values[2] : null;
       if (values.length > 1) {
           String tag = values[0];
           switch (tag.toLowerCase()) {
               case "custom" :
                   type = ItemType.Custom;
                   break;
           }
       }
    }
    public boolean Compare(ItemStack input) {
       if (input != null && !input.getType().equals(Material.AIR)) {
           switch (type) {
               case Default:
                   Material material = ItemUtils.getMaterial(value.toUpperCase()).orElse(null);
                   if (material != null) {
                       return input.getType().equals(material);
                   }
                   break;
               case Custom:
                   if (v2 != null) {
                       String check = MsgUtils.msg(v2);
                       if (input.hasItemMeta() && input.getItemMeta().hasDisplayName()) {
                           String name = input.getItemMeta().getDisplayName();
                           return check.equals(name);
                       }
                   }
                   break;
           }
       }
       return false;
    }

    public int hasAmountPlayerInventory(Player p){
       switch (type) {
           case Default :
               Material material = ItemUtils.getMaterial(value.toUpperCase()).orElse(null);
               if (material != null){
                   return ItemUtils.hasPlayerMcItem(p,material);
               }
               return 0;
           case Custom :
               return ItemUtils.hasPlayerItem(p,MsgUtils.msg(v2));
       }
       return 0;
    }

    public void takePlayerInventory(Player p,int amount){
       switch (type) {
           case Default :
               Material material = ItemUtils.getMaterial(value.toUpperCase()).orElse(null);
               if (material != null) {
                   ItemUtils.takePlayerMcItem(p,material,amount);
               }
               break;
           case Custom :
               if (v2 != null) {
                   ItemUtils.takePlayerItem(p, MsgUtils.msg(v2), amount);
               }
               break;
       }
    }

    public enum ItemType {
        Default,
        Custom
    }
}
