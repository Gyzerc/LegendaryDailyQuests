package com.gyzer.legendaryrealms.Menu;

import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.stream.Collectors;

public class MenuItem {
    private String id;
    private ItemStack item;
    private String fuction;
    private boolean useHead;
    private boolean put;
    private String value;

    public MenuItem(String id, ItemStack item, String fuction) {
        this.id = id;
        this.item = item;
        this.fuction = fuction;
        this.useHead = false;
        this.put = true;
        this.value = null;
    }

    public boolean isPut() {
        return put;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setPut(boolean put) {
        this.put = put;
    }

    public MenuItem(String id, ItemStack item, String fuction, boolean useHead) {
        this.id = id;
        this.item = item;
        this.fuction = fuction;
        this.useHead = useHead;
    }

    public void setUseHead(boolean useHead) {
        this.useHead = useHead;
    }

    public boolean isUseHead() {
        return useHead;
    }

    public String getId() {
        return id;
    }

    public ItemStack getItem(Player p) {
        ItemStack i = item.clone();
        if (p != null) {
            if (useHead) {
                if (i.getType().equals(Material.PLAYER_HEAD)) {
                    SkullMeta id = (SkullMeta) i.getItemMeta();
                    id.setOwningPlayer(p);
                    i.setItemMeta(id);
                }
            }
            ItemMeta id = i.getItemMeta();
            if (LegendaryDailyQuests.getLegendaryDailyQuests().getIntegrationsManager().getPlaceholderAPIHook().isEnable() && id.hasLore()) {
                List<String> lore = id.getLore().stream().map(l -> LegendaryDailyQuests.getLegendaryDailyQuests().getIntegrationsManager().getPlaceholderAPIHook().getHolder(p,l)).collect(Collectors.toList());
                id.setLore(lore);
                i.setItemMeta(id);
            }
        }
        return i;
    }

    public String getFuction() {
        return fuction;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void setFuction(String fuction) {
        this.fuction = fuction;
    }
}