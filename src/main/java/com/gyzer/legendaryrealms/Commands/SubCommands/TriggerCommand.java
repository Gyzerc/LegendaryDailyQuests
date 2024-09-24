package com.gyzer.legendaryrealms.Commands.SubCommands;

import com.gyzer.legendaryrealms.Commands.CommandTabBuilder;
import com.gyzer.legendaryrealms.Data.Quest.Categorize;
import com.gyzer.legendaryrealms.Data.Quest.Checker.GoalNoCheck;
import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import com.gyzer.legendaryrealms.Data.Quest.Objective.QuestObjective;
import com.gyzer.legendaryrealms.Data.Quest.Quest;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TriggerCommand extends com.gyzer.legendaryrealms.Commands.LegendaryCommand {
    public TriggerCommand( ) {
        super("", "trigger", 6, true);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        String target = args[1];
        String cat = args[2];
        String quest = args[3];
        String goal = args[4];
        int amount = Integer.parseInt(args[5]);
        if (Bukkit.getPlayerExact(target) != null) {
            Player p = Bukkit.getPlayerExact(target);
            Categorize categorize = legendaryDailyQuests.getCategorizesManager().getCategorize(cat);
            if (categorize != null) {
                PlayerData data = legendaryDailyQuests.getPlayerDataManager().getPlayerData(p);
                if (data.getAccepts(cat).contains(quest)) {
                    Quest q = legendaryDailyQuests.getQuestsManager().getQuest(quest);
                    if (q.getObjectives().stream().filter(objective -> objective.getId().equals(goal) && objective.getType().equals(ObjectiveType.TRIGGER)).findFirst().isPresent()) {
                        new GoalNoCheck().addProgress(p,data,cat,q,goal,amount);
                    }
                    return;
                }
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
                .addTab(getOnlines() , 1 , Arrays.asList("trigger") , 0)
                .addTab(legendaryDailyQuests.getCategorizesManager().getCategorizes().stream().map(c -> c.getId()).collect(Collectors.toList()) , 2 , Arrays.asList("trigger") , 0)
                .addTab(args.length >= 3 ? getTargetAccepts(args[1],args[2],ObjectiveType.TRIGGER) : null, 3 ,Arrays.asList("trigger") , 0)
                .addTab(args.length >= 4 ? getTargetQuestGolas(args[3]) : null, 4 ,Arrays.asList("trigger"), 0)
                .addTab(Arrays.asList("1","2","5","10","16","32","..."), 5 , Arrays.asList("trigger") , 0)
                .build(args);
    }

    private List<String> getTargetQuestGolas(String arg) {
        Quest quest = legendaryDailyQuests.getQuestsManager().getQuest(arg);
        if (quest == null) {
            return Arrays.asList("This quest doesn't have goals.");
        }
        return quest.getObjectives().stream().map(QuestObjective::getId).collect(Collectors.toList());
    }


}
