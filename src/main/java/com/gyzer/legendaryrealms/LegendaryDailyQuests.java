package com.gyzer.legendaryrealms;

import com.gyzer.legendaryrealms.API.DailyQuestPlaceholder;
import com.gyzer.legendaryrealms.Commands.DailyQuestsCommands;
import com.gyzer.legendaryrealms.Data.Quest.Checker.DoubleGoalChecker;
import com.gyzer.legendaryrealms.Data.Quest.Checker.GoalNoCheck;
import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import com.gyzer.legendaryrealms.Data.Store.DataProvider;
import com.gyzer.legendaryrealms.Data.Store.MySQL;
import com.gyzer.legendaryrealms.Data.Store.SQLite;
import com.gyzer.legendaryrealms.Listeners.Other.*;
import com.gyzer.legendaryrealms.API.Events.NewDayEvent;
import com.gyzer.legendaryrealms.Listeners.PlayerEvents;
import com.gyzer.legendaryrealms.Manager.*;
import com.gyzer.legendaryrealms.Utils.MsgUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

public class LegendaryDailyQuests extends JavaPlugin {
    public String plugin;
    public boolean version_high;
    private DailyQuestPlaceholder dailyQuestPlaceholder;
    @Override
    public void onEnable() {

        legendaryDailyQuests = this;
        //获取是否高版本
        version_high = BukkitVersionHigh();
        objectivesManager = new ObjectivesManager();
        conditionsManager = new ConditionsManager();
        integrationsManager = new IntegrationsManager();
        reload();
        initDataBase(configurationsManager.getConfig().useMySQL);
        playerDataManager = new PlayerDataManager();
        systemDataManager = new SystemDataManager();
        registerEvents();
        Bukkit.getPluginCommand("LegendaryDailyQuests").setExecutor(new DailyQuestsCommands());
        Bukkit.getPluginCommand("LegendaryDailyQuests").setTabCompleter(new DailyQuestsCommands());
        DailyQuestsCommands.register();

        sync(()->{
            Calendar calendar = Calendar.getInstance();
            int today = calendar.get(Calendar.DATE);
            int record = dataProvider.getDate("last");
            if (today != record){
                Bukkit.getScheduler().runTask(this,()->Bukkit.getPluginManager().callEvent(new NewDayEvent(today)));
            }
        },0,20 * 60);
        sync(()->{
            Bukkit.getOnlinePlayers().forEach(player -> {
                new GoalNoCheck().check(player, ObjectiveType.DELAY,1);
                new DoubleGoalChecker().check(player,ObjectiveType.PLACEHOLDERAPI,0);
            });
        },20,20);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            dailyQuestPlaceholder = new DailyQuestPlaceholder();
            dailyQuestPlaceholder.register();
        }

        Bukkit.getScheduler().runTaskLater(this,()->new Metrics(this,22212),20);
    }

    private boolean BukkitVersionHigh() {
        String name = Bukkit.getServer().getBukkitVersion();
        String versionStr =  name.substring(0,name.indexOf("-"));

        List<String> groups = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        for (char c : versionStr.toCharArray()) {
            if (c == '.') {
                groups.add(builder.toString());
                builder = new StringBuilder();
                continue;
            }
            builder.append(c);
        }
        groups.add(builder.toString());

        int version = Integer.parseInt(groups.get(1));
        return (version >= 13);
    }

    private void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(),this);
        Bukkit.getPluginManager().registerEvents(new CraftEvent(),this);
        Bukkit.getPluginManager().registerEvents(new NewDay(),this);
        if (version_high) {
            Bukkit.getPluginManager().registerEvents(new HarvestEvent(), this);
            Bukkit.getPluginManager().registerEvents(new BlockEvent(), this);
        }
        Bukkit.getPluginManager().registerEvents(new BrewQuestEvent(),this);
    }

    @Override
    public void onDisable() {
        dataProvider.closeDataBase();
        if (dailyQuestPlaceholder != null) {
            dailyQuestPlaceholder.unregister();
        }
    }

    public void reload(){
        configurationsManager = new ConfigurationsManager();
        plugin = configurationsManager.getLanguage().PLUGIN;
        questsManager = new QuestsManager();
        categorizesManager = new CategorizesManager();
        questRaritiesManager = new QuestRaritiesManager(this);
    }



    private void initDataBase(boolean useMySQL) {
        if (useMySQL) {
            dataProvider = new MySQL();
            return;
        } else {
            dataProvider = new SQLite();
        }
    }

    private static LegendaryDailyQuests legendaryDailyQuests;
    private ConditionsManager conditionsManager;
    private QuestsManager questsManager;
    private ConfigurationsManager configurationsManager;
    private ObjectivesManager objectivesManager;
    private DataProvider dataProvider;
    private PlayerDataManager playerDataManager;
    private CategorizesManager categorizesManager;
    private IntegrationsManager integrationsManager;
    private SystemDataManager systemDataManager;
    private QuestRaritiesManager questRaritiesManager;

    public QuestRaritiesManager getQuestRaritiesManager() {
        return questRaritiesManager;
    }

    public static LegendaryDailyQuests getLegendaryDailyQuests() {
        return legendaryDailyQuests;
    }

    public ObjectivesManager getObjectivesManager() {
        return objectivesManager;
    }

    public ConditionsManager getConditionsManager() {
        return conditionsManager;
    }

    public QuestsManager getQuestsManager() {
        return questsManager;
    }

    public DataProvider getDataProvider() {
        return dataProvider;
    }

    public ConfigurationsManager getConfigurationsManager() {
        return configurationsManager;
    }

    public CategorizesManager getCategorizesManager() {
        return categorizesManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public SystemDataManager getSystemDataManager() {
        return systemDataManager;
    }

    public IntegrationsManager getIntegrationsManager() {
        return integrationsManager;
    }

    public void info(String msg, Level level, Throwable throwable){
        getLogger().log(level,msg,throwable);
    }
    public void info(String msg, Level level){
        getLogger().log(level,msg);
    }
    public void sync(Runnable consumer){
        Bukkit.getScheduler().runTaskAsynchronously(this,consumer);
    }
    public void sync(Runnable runnable,int delay){
        Bukkit.getScheduler().runTaskLaterAsynchronously(this,runnable,delay);
    }
    public void sync(Runnable runnable,int delay,int timer){
        Bukkit.getScheduler().runTaskTimerAsynchronously(this,runnable,delay,timer);
    }
    public void sendConsoleMessage(String str){
        Bukkit.getConsoleSender().sendMessage(MsgUtils.msg(str));
    }
    public String toSmallCount(double a) {
        char[] chars= String.valueOf(a).toCharArray();
        boolean begin = false;
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (char c : chars) {
            if (begin) {
                if (i == 1) {
                    break;
                }
                builder.append(c);
                i++;
            }
            else {
                builder.append(c);
                if (c == '.') {
                    begin = true;
                }
            }
        }
        return builder.toString();
    }
}