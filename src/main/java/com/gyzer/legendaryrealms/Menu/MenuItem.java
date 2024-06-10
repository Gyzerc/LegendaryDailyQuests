package com.gyzer.legendaryrealms.Menu;

import org.bukkit.inventory.ItemStack;

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

    public ItemStack getItem() {
        return item;
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