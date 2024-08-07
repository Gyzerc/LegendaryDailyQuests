package com.gyzer.legendaryrealms.Configurations;

import com.gyzer.legendaryrealms.Utils.MsgUtils;
import java.util.Arrays;
import java.util.List;

public class Language extends ConfigurationProvider {
    public String accept;
    public String refresh_notenough;
    public String refresh;
    public String completed;
    public String completed_title;
    public String completed_subtitle;
    public String completed_all;
    public String finalyReward_already;
    public String finalyReward_cant;
    public String finalyReward_claim;
    public String reload;
    public String random_categorize;
    public String random_target;
    public String refresh_math;
    public String refresh_gain;
    public String refresh_lost;
    public String refresh_beset;
    public String refresh_give;
    public String refresh_take;
    public String refresh_set;
    public String refresh_accepted;
    public String not_accepted;
    public String force_completed;
    public String unknown_quest;
    public String reset;
    private boolean useChines;
    public List<String> help_default;
    public List<String> help_admin;
    public List<String> top;
    public String top_norecord;
    public String unknown_command;
    public String permission;
    public String unknown_categorize;
    public String notOnline;
    public String default_no_player;
    public String broad_when_accept;
    public String refresh_all;

    public Language(boolean Chinese) {
        super("./plugins/LegendaryDailyQuests", "", "Language.yml");
        this.useChines = Chinese;
        read();
    }

    public boolean isUseChines() {
        return useChines;
    }

    public String PLUGIN;

    private void read() {
        boolean useChines = isUseChines();
        PLUGIN = MsgUtils.msg(getValue("plugin","&f[&#FF8C69Legen&#EE8262daryDa&#CD7054ilyQue&#8B4C39sts&f]"));
        help_default = MsgUtils.msg(getValue("help",
                Arrays.asList(
                        "&f[&#FF8C69Legen&#EE8262daryDa&#CD7054ilyQue&#8B4C39sts&f] &7&l指令帮助",
                        "&8 - /ldq open [分类ID] (玩家ID) &e打开指令分类的任务面板.",
                        "&7 - /ldq top [分类ID] [页数] &e查看任务完成速度排行榜"
                )
                ,Arrays.asList(
                        "&f[&#FF8C69Legen&#EE8262daryDa&#CD7054ilyQue&#8B4C39sts&f] &7&lCommands Help",
                        "&8 - /ldq open [categorize] (player) &eOpen the task panel of the specified categorize.",
                        "&7 - /ldq top [分类ID] [页数] &eView quests completion speed ranking list"
                ),useChines));
        help_admin = MsgUtils.msg(getValue("help_admin",
                Arrays.asList(
                        "&f[&#FF8C69Legen&#EE8262daryDa&#CD7054ilyQue&#8B4C39sts&f] &7&l指令帮助 &f- &d&l管理员",
                        "&7 - /ldq reload &e重载配置文件.",
                        "&7 - /lqd random [分类ID] (玩家ID) &e强制刷新指定玩家指定分类的任务.",
                        "&7 - /lqd refresh add/take/set [分类ID] (玩家ID) (数量) &e操作玩家指定分类的刷新机会.",
                        "&7 - /ldq trigger [玩家] [分类ID] [任务ID] [目标ID] [数量] &e增加玩家指定任务 Trigger 类型的目标进度值.（该指令不会有提示语）",
                        "&7 - /ldq complete [玩家] [分类ID] [任务ID] &e强制完成玩家指定任务",
                        "&7 - /ldq refresh-all [分类ID] &e强制刷新全服玩家该类别的数据",

                        "&7 - /ldq reset [玩家] [分类ID] [任务ID] &e强制重置玩家指定任务"
                )
                ,Arrays.asList(
                        "&f[&#FF8C69Legen&#EE8262daryDa&#CD7054ilyQue&#8B4C39sts&f] &7&lCommands Help &f- &d&lAdmin",
                        "&7 - /ldq reload &eReload plugin configurations.",
                        "&7 - /lqd random [categorize] (player) &eForce refresh of tasks for specified player categories.",
                        "&7 - /lqd refresh add/take/set [categorize] (player) (amount) &eOperate the player's specified category refresh points.",
                        "&7 - /ldq trigger [player] [categorize] [quest] [goal] [amount] &eIncrease the target progress value of the &eTrigger &7type for player specified quest (This command will not have any tip message return)",
                        "&7 - /ldq complete [player] [categorize] [quest] &eForce players to complete quest",
                        "&7 - /ldq refresh-all [categorize] &eForce refresh of data for players in this category across the entire server",
                        "&7 - /ldq reset [player] [categorize] [quest] &eForce players to reset quest"
                ),useChines));
        top = MsgUtils.msg(getValue("top",Arrays.asList(
                "&f[&6完成速度排行榜&f] - &f%categorize%",
                        "&f &f No.1 %top_name%",
                        "&f &f No.2 %top_name%",
                        "&f &f No.3 %top_name%",
                        "&f &f No.4 %top_name%",
                        "&f &f No.5 %top_name%",
                        "&f &f No.6. %top_name%",
                        "&f &f No.7 %top_name%",
                        "&f &f No.8 %top_name%",
                        "&f &f No.9. %top_name%",
                        "&f &f No.10. %top_name%",
                "&f ",
                "&b你当前位于 &eNo.%me%"),
                Arrays.asList(
                "&f[&6Completed Speed Ranking&f] - &f%categorize%",
                "&f &f No.1 %top_name%",
                "&f &f No.2 %top_name%",
                "&f &f No.3 %top_name%",
                "&f &f No.4 %top_name%",
                "&f &f No.5 %top_name%",
                "&f &f No.6. %top_name%",
                "&f &f No.7 %top_name%",
                "&f &f No.8 %top_name%",
                "&f &f No.9. %top_name%",
                "&f &f No.10. %top_name%",
                "&f ",
                "&bYou are currently in &eNo.%me%"),
                useChines));
        top_norecord = MsgUtils.msg(getValue("top-no-record","&c该页排行榜暂无记录.","&cThere is currently no record of this page's ranking list",useChines));
        reload = MsgUtils.msg(getValue("reload","&b插件已重载！","&BReloaded.",useChines));
        not_accepted = MsgUtils.msg(getValue("not-accept","&c玩家 &f%player% &c未接受该任务.","&cPlayer &f%player% &cdo not accepted this quest. ",useChines));
        force_completed = MsgUtils.msg(getValue("force-completed","&e成功强制玩家 &f%player% &e完成 &f%categorize% &e下的任务 &b%quest%","&eSuccessfully forces player &f%player% &eto complete &f%categorize% &equest &b%quest%",useChines));
        reset = MsgUtils.msg(getValue("reset","&e成功重置 &f%player% &e的 &f%categorize% &e类别的任务: &b%quest%","&eTask for successfully resetting &f%player% &ecategorize &f%categorize%&e's quest: &b%quest%",useChines));
        unknown_command = MsgUtils.msg(getValue("unknown_command","未知指令.","Unknown Command.",useChines));
        permission = MsgUtils.msg(getValue("permission","你没有该权限.","You don't have permission to execute the command.",useChines));
        unknown_categorize = MsgUtils.msg(getValue("unknown_categorize","未知的分类ID","Unknown Categorize.",useChines));
        unknown_quest = MsgUtils.msg(getValue("unknown_quest","未知的任务ID","Unknown Quest.",useChines));
        notOnline = MsgUtils.msg(getValue("notOnline","该玩家不在线或是不存在.","The player is not online or does not exist",useChines));
        accept = MsgUtils.msg(getValue("accept","&#00EE00你接受了任务 &e%quest%","&#00EE00You accepted &e%quest%",useChines));
        completed = MsgUtils.msg(getValue("completed","&#00FF7F你完成了任务 &e%quest%","&#00FF7FYou have completed quest &e%quest%",useChines));
        completed_all = MsgUtils.msg(getValue("completed-all","&6&l你完成了 &f%categorize% &6&l的所有任务!","&6&lYou have completed all quests for &f%categorize%&6&l!",useChines));
        completed_title = MsgUtils.msg(getValue("completed-title","&#00FF7F任务完成！"));
        completed_subtitle = MsgUtils.msg(getValue("completed-subtitle","&e%quest%"));
        refresh = MsgUtils.msg(getValue("refresh.message","&a你刷新了本次的任务列表.","You have refreshed the task list for this time",useChines));
        refresh_notenough = MsgUtils.msg(getValue("refresh.notenough","&c你的刷新次数不足.","&cYour refresh times is not enough",useChines));
        refresh_accepted = MsgUtils.msg(getValue("refresh.accepted","&c你已经接受了一个任务, 本轮无法再刷新了..","&cYou have already accepted a task and cannot be refreshed in this round",useChines));
        finalyReward_already = MsgUtils.msg(getValue("finalyReward.already","&c你已经领取过最终奖励了.","&CYou have already received the final reward",useChines));
        finalyReward_cant = MsgUtils.msg(getValue("finalyReward.cant","&c你还未完成 %categorize% &c的所有任务.","&CYou have not completed all the tasks for %categorize% &cthis round yet",useChines));
        finalyReward_claim = MsgUtils.msg(getValue("finalyReward.claim","&#00FF7F你领取了 %categorize% &#00FF7F的最终奖励.","&#00FF7FYou have received the %categorize% &#00FF7Ffinal reward for this round",useChines));
        random_categorize = MsgUtils.msg(getValue("random.categorize","&c没有该分类.","&cThere is no such categorize.",useChines));
        random_target = MsgUtils.msg(getValue("random.target","&f玩家 &f%player% &f的 %categorize% &f任务列表已被刷新.","&fThe %categorize% &ftask list for player &3%player% &fhas been refreshed",useChines));
        refresh_math = MsgUtils.msg(getValue("refresh.math","&f请输入一个大于0的整数.","&FPlease enter an integer greater than 0",useChines));
        refresh_gain = MsgUtils.msg(getValue("refresh.gain","&a你的 %categorize% &a刷新次数增加了 &f%amount%","&aYour %categorize% &arefresh points has increased by &f%amount%",useChines));
        refresh_lost = MsgUtils.msg(getValue("refresh.lost","&e你的 %categorize% &e刷新次数减少了 &f%amount%","&eYour %categorize% &erefresh points has decreased by &f%amount%",useChines));
        refresh_beset = MsgUtils.msg(getValue("refresh.beset","&e你的 %categorize% &e刷新次数被设置为 &f%amount%","&eYour %categorize% &erefresh points has be set by &f%amount%",useChines));
        refresh_give = MsgUtils.msg(getValue("refresh.give","&a你使玩家 &f%player% &a的 %categorize% &a刷新次数增加了 &f%amount%","&aYou have increased the refresh points of player &f%player% &a's %categorize% &aby &f%amount%",useChines));
        refresh_take = MsgUtils.msg(getValue("refresh.take","&e你使玩家 &f%player% &e的 %categorize% &e刷新次数减少了 &f%amount%","&eYou have decreased the refresh points of player &f%player% &e's %categorize% &eby &f%amount%",useChines));
        refresh_set = MsgUtils.msg(getValue("refresh.set","&e你使玩家 &f%player% &e的 %categorize% &e刷新次数设置为 &f%amount%","&eYou have set the refresh points of player &f%player% &e's %categorize% &eby &f%amount%",useChines));
        default_no_player = MsgUtils.msg(getValue("default.no-player","&7暂无玩家","&7No Player.",useChines));
        broad_when_accept = MsgUtils.msg(getValue("broad-when-accept","&e玩家%player%接受了个%rarity%&e级别的每日任务 &f%quest%","&ePlayer %player% accepted daily quest at the %rarity% &elevel: %quest%",useChines));
        refresh_all = MsgUtils.msg(getValue("refresh.all","&6成功刷新该类别的所有玩家数据","&6Successfully refreshed all player data for this category",useChines));
        saveYml();
    }

    @Override
    protected void readDefault() {

    }
}
