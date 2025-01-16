package com.gyzer.legendaryrealms.Manager;

import com.gyzer.legendaryrealms.Data.Quest.Categorize;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Menu.MenuLoader;
import com.gyzer.legendaryrealms.Utils.MsgUtils;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CategorizesManager {
    final LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();
    private HashMap<String, Categorize> caches;

    public CategorizesManager( ) {
        this.caches = new HashMap<>();
        loadAllCategorizes();
    }

    public Categorize getCategorize(String id){
        return caches.get(id);
    }
    private void loadAllCategorizes() {
        File file = new File(legendaryDailyQuests.getDataFolder()+"/Categorizes");
        if (!file.exists()){
            legendaryDailyQuests.saveResource("Categorizes/DailyQuests.yml",false);
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File catFile : files) {
                if (!catFile.isDirectory()) {
                    String cat = catFile.getName().replace(".yml", "");
                    YamlConfiguration yml = YamlConfiguration.loadConfiguration(catFile);

                    MenuLoader loader = new MenuLoader(yml);
                    String display = MsgUtils.msg(yml.getString("settings.display","Quests"));
                    int refresh = yml.getInt("settings.refresh",1);
                    int amount = yml.getInt("settings.amount",5);
                    int default_refresh_points = yml.getInt("settings.default-refresh-points",-1);

                    List<String> quests = yml.getStringList("settings.quests");
                    List<String> rewards = yml.getStringList("settings.rewards");
                    caches.put(cat,new Categorize(cat,display,refresh,amount,quests,loader,rewards,default_refresh_points));
                    legendaryDailyQuests.sendConsoleMessage(legendaryDailyQuests.plugin+" &fSuccessfully loaded categorize &6"+cat);
                }
            }
        }
    }
    public List<Categorize> getCategorizes(){
        return caches.values().stream().collect(Collectors.toList());
    }
}
