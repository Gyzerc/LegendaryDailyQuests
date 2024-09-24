package com.gyzer.legendaryrealms.Utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ReplaceUtils {
    private final List<SingleHolder> singles;
    private final List<ListHolder> lists;


    public ReplaceUtils(){
        this.singles = new ArrayList<>();
        this.lists = new ArrayList<>();
    }

    public ReplaceUtils addSinglePlaceHolder(String target,String placeText){
        this.singles.add(new SingleHolder(target,placeText));
        return this;
    }

    public ReplaceUtils addListPlaceHolder(String target,List<String> placeText){
        this.lists.add(new ListHolder(target,placeText));
        return this;
    }

    public ItemStack startReplace(ItemStack item){
        ItemStack i = item.clone();
        ItemMeta id = i.getItemMeta();
        if (id.hasDisplayName()){
            String display = replaceSingleText(id.getDisplayName());
            id.setDisplayName(display);
        }
        List<String> oldLore = id.hasLore() ? id.getLore() : new ArrayList<>();
        if (lists.isEmpty()){
            oldLore.replaceAll( l -> {
                return replaceSingleText(l);
            });
            id.setLore(oldLore);
            i.setItemMeta(id);
            return i;
        }
        List<String> newLore = new ArrayList();
        for (String l : oldLore){
            boolean hasChange = false;
            for (ListHolder listHolder : lists){
                String target = "%"+listHolder.getTarget() + "%";
                if (l.contains(target)){
                    for (String a : listHolder.getValue()){
                        newLore.add(l.replace(target,a));
                    }
                    hasChange = true;
                    break;
                }
            }
            if (!hasChange){
                newLore.add(replaceSingleText(l));
            }
        }
        id.setLore(newLore);
        i.setItemMeta(id);
        return i;
    }

    private String replaceSingleText(String str){
        String returnStr = str;
        for (SingleHolder singleHolder : singles){
            returnStr = returnStr.replace("%"+singleHolder.getTarget()+"%",singleHolder.getValue());
        }
        return returnStr;
    }

    public class ListHolder {
        private final String target;
        private final List<String> value;

        public ListHolder(String target, List<String> value) {
            this.target = target;
            this.value = value;
        }

        public String getTarget() {
            return target;
        }

        public List<String> getValue() {
            return value;
        }
    }
    public class SingleHolder {

        private final String target;
        private final String value;

        public SingleHolder(String target, String value) {
            this.target = target;
            this.value = value;
        }

        public String getTarget() {
            return target;
        }

        public String getValue() {
            return value;
        }
    }
}
