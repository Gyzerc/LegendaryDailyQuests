package com.gyzer.legendaryrealms.Commands.SubCommands;

import com.gyzer.legendaryrealms.Commands.CommandTabBuilder;
import com.gyzer.legendaryrealms.Configurations.Language;
import com.gyzer.legendaryrealms.Data.Quest.Categorize;
import com.gyzer.legendaryrealms.Menu.MenuPanel;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OpenCommand extends com.gyzer.legendaryrealms.Commands.LegendaryCommand {
    public OpenCommand() {
        super("LegendaryDailyQuests.open", "open", Arrays.asList(2,3), false);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        String cat = args[1];
        String target = sender.getName();
        Categorize categorize = legendaryDailyQuests.getCategorizesManager().getCategorize(cat);
        if (categorize != null){
            if (args.length == 3) {
                target = args[2];
            }
            Player p = Bukkit.getPlayerExact(target);
            if ( p != null){
                MenuPanel panel = new MenuPanel(p,categorize);
                panel.open();
                return;
            }
            sender.sendMessage(lang.PLUGIN+lang.notOnline);
            return;
        }
        sender.sendMessage(lang.PLUGIN+lang.unknown_categorize);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder().addTab(legendaryDailyQuests.getCategorizesManager().getCategorizes().stream().map(Categorize::getId).collect(Collectors.toList()), 1, Arrays.asList("open"),0)
                .addTab(Bukkit.getOnlinePlayers().stream().map(player -> player.getName()).collect(Collectors.toList()), 2,Arrays.asList("open"),0)
                .build(args);
    }
}
