package com.gyzer.legendaryrealms.Commands.SubCommands;

import com.gyzer.legendaryrealms.API.LegendaryDailyQuestsAPI;
import com.gyzer.legendaryrealms.Commands.CommandTabBuilder;
import com.gyzer.legendaryrealms.Data.Quest.Categorize;
import com.gyzer.legendaryrealms.Data.Quest.Quest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import sun.dc.pr.PRError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomCommand extends com.gyzer.legendaryrealms.Commands.LegendaryCommand {
    public RandomCommand( ) {
        super("", "random", 3, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        String cat = getCategorize(args[1]);
        List<String> vars = getArgs(args[1]);

        Categorize categorize = legendaryDailyQuests.getCategorizesManager().getCategorize(cat);
        if (categorize != null){
            Player p = Bukkit.getPlayerExact(args[2]);
            if (p != null){
                LegendaryDailyQuestsAPI.randomPlayerQuests(p.getUniqueId(),categorize,checkOrRandomQuests(vars));
                sender.sendMessage(lang.PLUGIN+lang.random_target.replace("%player%",args[2]).replace("%categorize%",categorize.getDisplay()));
                return;
            }
            sender.sendMessage(lang.PLUGIN+lang.notOnline);
            return;
        }
        sender.sendMessage(lang.PLUGIN+lang.random_categorize);
    }

    private List<String> checkOrRandomQuests(List<String> selects) {
        List<String> quests = new ArrayList<>();
        for (String id : selects) {
            //检测该id是否为某个品质的类别

            //最后进行检测该id是否是个任务
            Quest quest = legendaryDailyQuests.getQuestsManager().getQuest(id);
            if (quest != null) {
                quests.add(id);
            }

        }
        return quests;
    }

    private List<String> getArgs(String arg) {
        List<String> args = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        boolean begin = false;
        for (char c : arg.toCharArray()) {
            if (begin) {
                if (c == '}') {
                    args.add(builder.toString());
                    break;
                } else if (c == ',') {
                    args.add(builder.toString());
                    builder = new StringBuilder();
                    continue;
                }
                builder.append(c);
            } else {
                if (c == '{') {
                    begin = true;
                }
            }
        }
        return args;
    }

    private String getCategorize(String arg) {
        StringBuilder builder = new StringBuilder();
        for (char c : arg.toCharArray()) {
            if (c == '{') {
                break;
            }
            builder.append(c);
        }
        return builder.toString();
    }
    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(legendaryDailyQuests.getCategorizesManager().getCategorizes().stream().map(categorize -> categorize.getId()).collect(Collectors.toList()), 1,Arrays.asList("random"),0)
                .addTab(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()), 2,Arrays.asList("random"),0)
                .build(args);
    }
}
