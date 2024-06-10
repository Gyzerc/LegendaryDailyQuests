package com.gyzer.legendaryrealms.Manager;

import com.gyzer.legendaryrealms.Data.System.CompletedData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;

public class SystemDataManager {
    private CompletedData completedData;

    public SystemDataManager() {
        completedData = LegendaryDailyQuests.getLegendaryDailyQuests().getDataProvider().getCompletedData();
    }
    public void update(CompletedData completedData) {
        this.completedData = completedData;
        LegendaryDailyQuests.getLegendaryDailyQuests().getDataProvider().setCompletedData(completedData);
    }
    public CompletedData getCompletedData() {
        return completedData;
    }
}
