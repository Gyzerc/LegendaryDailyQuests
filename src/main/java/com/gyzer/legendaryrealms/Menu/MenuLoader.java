package com.gyzer.legendaryrealms.Menu;

import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Utils.MsgUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class MenuLoader {
    final LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();
    private YamlConfiguration yml;
    private String title;
    private Optional<Sound> sound;
    private int size;
    public HashMap<Integer,MenuItem> item;
    public List<MenuItem> buttons;
    public HashMap<String,String> placeholder;
    public List<String> layout;

    public MenuLoader(YamlConfiguration yml) {
        item = new HashMap<>();
        placeholder = new HashMap<>();
        layout = new ArrayList<>();
        buttons = new ArrayList<>();
        this.yml = yml;
        readEssentails();
    }


    private Optional<ConfigurationSection> getSection(String path) {
        return yml.getConfigurationSection(path) != null ? Optional.ofNullable(yml.getConfigurationSection(path)) : Optional.empty();
    }

    private void readEssentails(){


        this.layout = yml.getStringList("gui.layout");
        this.title = MsgUtils.msg(yml.getString("gui.title"," "));
        this.size = yml.getInt("gui.size",54);
        this.sound = getSound(yml.getString("gui.sound"));

        getSection("gui.Items").ifPresent(sec -> {
            for (String key:sec.getKeys(false)){
                String display = MsgUtils.msg(sec.getString(key+".display",""));
                Material material = getMaterial(sec.getString(key+".material","STONE"));
                int amount = sec.getInt(key+".amount",1);
                int data = sec.getInt(key+".data",0);
                int model = sec.getInt(key+".model",0);
                boolean skull = sec.getBoolean(key+".player-skull",false);
                List<String> lore = MsgUtils.msg(sec.getStringList(key+".lore"));
                String fuction = sec.getString(key+".function.type","none");
                ItemStack i = new ItemStack(material,amount,(short) data);
                ItemMeta id = i.getItemMeta();
                id.setDisplayName(display);
                id.setLore(lore);
                if (model != 0 && legendaryDailyQuests.version_high) {
                    id.setCustomModelData(model);
                }
                i.setItemMeta(id);
                MenuItem menuItem = new MenuItem(key,i,fuction,skull);
                if (sec.getBoolean(key+".playerhead",false)){
                    menuItem.setUseHead(true);
                }
                buttons.add(menuItem);
            }
        });
        getSection("gui.placeholder").ifPresent(sec -> {
            for (String id:sec.getKeys(false)){
                placeholder.put(id,MsgUtils.msg(sec.getString(id)));
            }
        });

        int lineAmount = 0;
        for (String line : layout){
            char[] chars = line.toCharArray();
            for (int a = 0 ; a < 9 ; a++){
                if (chars.length > a ){
                    char c = chars[a];
                    List<MenuItem> get = buttons.stream().filter(i -> i.getId().equals(c+"")).collect(Collectors.toList());
                    if (!get.isEmpty()){
                        item.put((lineAmount * 9 + a),get.get(0));
                    }
                }
            }
            lineAmount++;
        }


        size = Math.max((lineAmount*9),9);
    }

    public Optional<Sound> getSound() {
        return sound;
    }

    public HashMap<Integer, MenuItem> getItem() {
        return item;
    }

    public List<MenuItem> getButtons() {
        return buttons;
    }

    public List<String> getLayout() {
        return layout;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public MenuItem getMenuItem(int slot){
        return item.get(slot);
    }

    public String getPlaceHolder(String key){
        return placeholder.get(key) != null ? placeholder.get(key) : "";
    }

    private Optional<Sound> getSound(String sound){
        if (sound == null){
            return Optional.empty();
        }
        try {
            return Optional.of(Sound.valueOf(sound.toUpperCase()));
        } catch (Exception e){
            legendaryDailyQuests.info("Cant find the sound named "+sound+" in current server version. ->"+yml.getName(), Level.SEVERE,e);
            return Optional.empty();
        }
    }

    public Material getMaterial(String arg){
        String str=arg.toUpperCase();
        Material material = Material.getMaterial(str);
        if (material == null){
            material = Material.STONE;
            legendaryDailyQuests.info("Cant find the material id named "+arg+" in current server version. -> "+yml.getName(),Level.SEVERE);
        }
        return material;
    }
}
