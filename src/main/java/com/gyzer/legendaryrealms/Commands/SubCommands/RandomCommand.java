package com.gyzer.legendaryrealms.Commands.SubCommands;

import com.gyzer.legendaryrealms.API.LegendaryDailyQuestsAPI;
import com.gyzer.legendaryrealms.Commands.CommandTabBuilder;
import com.gyzer.legendaryrealms.Data.Quest.Categorize;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RandomCommand extends com.gyzer.legendaryrealms.Commands.LegendaryCommand {
    public RandomCommand( ) {
        super("", "random", 3, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        String cat = args[1];
        Categorize categorize = legendaryDailyQuests.getCategorizesManager().getCategorize(cat);
        if (categorize != null){
            Player p = Bukkit.getPlayerExact(args[2]);
            if (p != null){
                LegendaryDailyQuestsAPI.randomPlayerQuests(p.getUniqueId(),categorize);
                sender.sendMessage(lang.PLUGIN+lang.random_target.replace("%player%",args[2]).replace("%categorize%",categorize.getDisplay()));
                return;
            }
            sender.sendMessage(lang.PLUGIN+lang.notOnline);
            return;
        }
        sender.sendMessage(lang.PLUGIN+lang.random_categorize);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(legendaryDailyQuests.getCategorizesManager().getCategorizes().stream().map(categorize -> categorize.getId()).collect(Collectors.toList()), 1,Arrays.asList("random"),0)
                .addTab(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()), 2,Arrays.asList("random"),0)
                .build(args);
    }
}
