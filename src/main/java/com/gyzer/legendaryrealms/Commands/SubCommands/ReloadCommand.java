package com.gyzer.legendaryrealms.Commands.SubCommands;

import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends com.gyzer.legendaryrealms.Commands.LegendaryCommand {
    public ReloadCommand( ) {
        super("", "reload", 1, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        legendaryDailyQuests.reload();
        sender.sendMessage(lang.PLUGIN+lang.reload);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return null;
    }
}
