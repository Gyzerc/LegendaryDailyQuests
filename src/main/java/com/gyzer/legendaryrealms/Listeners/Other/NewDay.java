package com.gyzer.legendaryrealms.Listeners.Other;

import com.gyzer.legendaryrealms.Data.Quest.Categorize;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.API.Events.NewDayEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NewDay implements Listener {
    final LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();

     /*
        检测某个类别是否达到刷新的天数
     */
    @EventHandler
    public void onNewDay(NewDayEvent e){
        legendaryDailyQuests.sync(()->{
            legendaryDailyQuests.getDataProvider().setDate("last",e.getDate());
            legendaryDailyQuests.getCategorizesManager().getCategorizes().forEach(categorize -> {
                int already = legendaryDailyQuests.getDataProvider().getDate(categorize.getId());
                if (already + 1 == categorize.getCycle()){
                    legendaryDailyQuests.sendConsoleMessage(legendaryDailyQuests.plugin+" &6refresh all user's &b"+categorize.getId()+" &6data.");
                    legendaryDailyQuests.getDataProvider().setDate(categorize.getId(),0);
                    removeQuests(categorize);
                }
                else {
                    legendaryDailyQuests.getDataProvider().setDate(categorize.getId(),(already+1));
                }
            });
        });
    }
    //删除全体玩家该类别的任务
    private void removeQuests(Categorize categorize){
        legendaryDailyQuests.getPlayerDataManager().saveCaches();
        legendaryDailyQuests.getDataProvider().getPlayerDatas().forEach(uuid -> {
           PlayerData data = legendaryDailyQuests.getPlayerDataManager().getPlayerData(uuid);
           data.getQuests().remove(categorize.getId());
           data.getAccepts().remove(categorize.getId());
           data.getCompleteds().remove(categorize.getId());
           data.getClaimFinallyRewards().remove(categorize.getId());
           legendaryDailyQuests.getPlayerDataManager().update(data,true);
        });
    }
}
