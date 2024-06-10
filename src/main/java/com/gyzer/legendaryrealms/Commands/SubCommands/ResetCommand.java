package com.gyzer.legendaryrealms.Commands.SubCommands;

import com.gyzer.legendaryrealms.API.LegendaryDailyQuestsAPI;
import com.gyzer.legendaryrealms.Commands.CommandTabBuilder;
import com.gyzer.legendaryrealms.Data.Quest.Categorize;
import com.gyzer.legendaryrealms.Data.Quest.Progress.ProgressData;
import com.gyzer.legendaryrealms.Data.Quest.Quest;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ResetCommand extends com.gyzer.legendaryrealms.Commands.LegendaryCommand {
    public ResetCommand( ) {
        super("", "reset", 4, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        Player p = Bukkit.getPlayerExact(args[1]);
        Categorize categorize = legendaryDailyQuests.getCategorizesManager().getCategorize(args[2]);
        Quest quest = legendaryDailyQuests.getQuestsManager().getQuest(args[3]);
        if (p != null) {
            if (categorize != null) {
                if (quest != null) {

                    PlayerData data = legendaryDailyQuests.getPlayerDataManager().getPlayerData(p);
                    data.getAccepts(categorize.getId()).remove(quest.getId());
                    data.getCompleteds(categorize.getId()).remove(quest.getId());
                    ProgressData progressData = data.getProgressData(categorize.getId());
                    progressData.getProgress().remove(quest.getId());
                    data.getProgressData().put(categorize.getId(),progressData);
                    data.update(false);
                    sender.sendMessage(lang.PLUGIN + lang.reset.replace("%player%",args[1]).replace("%categorize%",categorize.getDisplay()).replace("%quest%",quest.getDisplay()));
                    return;

                }
                sender.sendMessage(lang.PLUGIN+ lang.unknown_quest);
                return;
            }
            sender.sendMessage(lang.PLUGIN + lang.unknown_categorize);
            return;
        }
        sender.sendMessage(lang.PLUGIN + lang.notOnline);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(getOnlines(),1, Arrays.asList("reset"),0)
                .addTab((legendaryDailyQuests.getCategorizesManager().getCategorizes().stream().map(e -> e.getId()).collect(Collectors.toList())) , 2 , Arrays.asList("reset") , 0)
                .addTab((args.length > 2 ? getTargetAccepts(args[1],args[2],null) : Arrays.asList("Not have accepted quests.")) , 3 , Arrays.asList("reset") , 0)
                .build(args);
    }
}
