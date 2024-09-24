package com.gyzer.legendaryrealms.Listeners.Other;

import com.gyzer.legendaryrealms.Data.Quest.Categorize;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.API.Events.NewDayEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class NewDay implements Listener {
    final static LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();

     /*
        检测某个类别是否达到刷新的天数
     */
    @EventHandler
    public void onNewDay(NewDayEvent e){
        legendaryDailyQuests.getDataProvider().setDate("last",e.getDate());
        legendaryDailyQuests.getCategorizesManager().getCategorizes().forEach(categorize -> {
            int already = legendaryDailyQuests.getDataProvider().getDate(categorize.getId());
            if (already + 1 == categorize.getCycle()){
                legendaryDailyQuests.sendConsoleMessage(legendaryDailyQuests.plugin+" &6refresh all user's &b"+categorize.getId()+" &6data.");
                legendaryDailyQuests.getDataProvider().setDate(categorize.getId(),0);
                legendaryDailyQuests.getDataProvider().setRefreshUID(categorize.getId(),UUID.randomUUID());
               // removeQuests(categorize);
            }
            else {
                legendaryDailyQuests.getDataProvider().setDate(categorize.getId(),(already+1));
            }
        });
    }
    //删除全体玩家该类别的任务
    public static void removeQuests(Categorize categorize){
        //legendaryDailyQuests.getPlayerDataManager().saveCaches();
        int amount = 0;
        String id = categorize.getId();
        List<UUID> uuids = legendaryDailyQuests.getDataProvider().getPlayerDatas();
        for (UUID uuid : uuids) {
            PlayerData data = legendaryDailyQuests.getPlayerDataManager().getPlayerData(uuid);
            data.clearQuests(id);
            data.clearAccepted(id);
            data.clearClaimed(id);
            data.clearCompleted(id);
            data.update(true);
            amount ++;
        }
        legendaryDailyQuests.info("成功刷新"+amount+"个玩家数据 [" + id+"]", Level.INFO);
    }
}
