package com.gyzer.legendaryrealms.Commands.SubCommands;

import com.gyzer.legendaryrealms.Data.Quest.Categorize;
import com.gyzer.legendaryrealms.Listeners.Other.NewDay;
import org.bukkit.command.CommandSender;

import java.util.List;

public class RefreshAllCommand extends com.gyzer.legendaryrealms.Commands.LegendaryCommand {
    public RefreshAllCommand( ) {
        super("", "refresh-all", 2, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        String cat = args[1];
        Categorize categorize = legendaryDailyQuests.getCategorizesManager().getCategorize(cat);
        if (categorize != null) {
            NewDay.removeQuests(categorize);
            sender.sendMessage(lang.PLUGIN + lang.refresh_all);
            return;
        }
        sender.sendMessage(lang.PLUGIN + lang.unknown_categorize);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return null;
    }
}
