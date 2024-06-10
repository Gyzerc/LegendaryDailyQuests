package com.gyzer.legendaryrealms.Manager;

import com.gyzer.legendaryrealms.Data.User.LoginData;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class PlayerDataManager {
    final LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();
    private HashMap<UUID, PlayerData> caches;

    public PlayerDataManager() {
        caches = new HashMap<>();
    }

    public PlayerData getPlayerData(Player p){
        return getPlayerData(p.getUniqueId());
    }
    public PlayerData getPlayerData(UUID uuid){
        PlayerData data = caches.get(uuid);
        if (data == null) {
            data = legendaryDailyQuests.getDataProvider().getPlayerData(uuid).orElse(new PlayerData(uuid));
            caches.put(uuid,data);
        }
        return data;
    }
    public void update(PlayerData data,boolean removeFormCaches){
        legendaryDailyQuests.getDataProvider().savePlayerData(data);
        if (removeFormCaches){
            caches.remove(data.getUuid());
            return;
        }
        caches.put(data.getUuid(),data);
    }
    public void saveCaches(){
        caches.forEach((uuid, data) -> legendaryDailyQuests.getDataProvider().savePlayerData(data));
    }
}
