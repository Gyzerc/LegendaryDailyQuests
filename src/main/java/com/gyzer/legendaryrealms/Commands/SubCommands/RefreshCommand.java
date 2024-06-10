package com.gyzer.legendaryrealms.Commands.SubCommands;

import com.gyzer.legendaryrealms.API.LegendaryDailyQuestsAPI;
import com.gyzer.legendaryrealms.Commands.CommandTabBuilder;
import com.gyzer.legendaryrealms.Data.Quest.Categorize;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import com.gyzer.legendaryrealms.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RefreshCommand extends com.gyzer.legendaryrealms.Commands.LegendaryCommand {
    public RefreshCommand( ) {
        super("", "refresh", 5, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        String type = args[1].toLowerCase();
        Categorize categorize = legendaryDailyQuests.getCategorizesManager().getCategorize(args[2]);
        Player p = Bukkit.getPlayerExact(args[3]);
        int amount = StringUtils.getInt(args[4],0);
        if (categorize == null){
            sender.sendMessage(lang.PLUGIN+lang.random_categorize);
            return;
        }
        if (p == null){
            sender.sendMessage(lang.PLUGIN+lang.notOnline);
            return;
        }
        if (amount <= 0){
            sender.sendMessage(lang.PLUGIN+lang.refresh_math);
            return;
        }
        PlayerData data = LegendaryDailyQuestsAPI.getPlayerData(p);
        switch (type) {
            case "add" :
                data.addRefresh(categorize.getId(),amount);
                data.update(false);
                p.sendMessage(lang.PLUGIN+lang.refresh_gain.replace("%categorize%",categorize.getDisplay()).replace("%amount%",""+amount));
                sender.sendMessage(lang.PLUGIN+lang.refresh_give.replace("%player%",args[3]).replace("%categorize%",categorize.getDisplay()).replace("%amount%",""+amount));
                break;
            case "take" :
                data.takeRefresh(categorize.getId(),amount);
                data.update(false);
                p.sendMessage(lang.PLUGIN+lang.refresh_lost.replace("%categorize%",categorize.getDisplay()).replace("%amount%",""+amount));
                sender.sendMessage(lang.PLUGIN+lang.refresh_take.replace("%player%",args[3]).replace("%categorize%",categorize.getDisplay()).replace("%amount%",""+amount));
                break;
            case "set" :
                data.setRefresh(categorize.getId(),amount);
                data.update(false);
                p.sendMessage(lang.PLUGIN+lang.refresh_set.replace("%player%",args[3]).replace("%categorize%",categorize.getDisplay()).replace("%amount%",""+amount));
                break;
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(Arrays.asList("add","take","set"),1, Arrays.asList("refresh"),0)
                .addTab(legendaryDailyQuests.getCategorizesManager().getCategorizes().stream().map(Categorize::getId).collect(Collectors.toList()), 2, Arrays.asList("refresh"),0)
                .addTab(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()),3, Arrays.asList("refresh"),0)
                .addTab(Arrays.asList("1","5","10","64"),4,Arrays.asList("refresh"),0)
                .build(args);
    }
}
