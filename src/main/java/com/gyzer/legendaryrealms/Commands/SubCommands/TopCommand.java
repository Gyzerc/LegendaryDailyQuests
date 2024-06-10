package com.gyzer.legendaryrealms.Commands.SubCommands;

import com.gyzer.legendaryrealms.API.LegendaryDailyQuestsAPI;
import com.gyzer.legendaryrealms.Commands.CommandTabBuilder;
import com.gyzer.legendaryrealms.Data.Quest.Categorize;
import com.gyzer.legendaryrealms.Data.System.CompletedData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class TopCommand extends com.gyzer.legendaryrealms.Commands.LegendaryCommand {
    public TopCommand( ) {
        super("LegendaryDailyQuests.top", "top", 3, false);
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Categorize categorize = legendaryDailyQuests.getCategorizesManager().getCategorize(args[1]);
            int pageStr = StringUtils.getInt(args[2], 1);
            if (categorize != null) {

                int perPage = lang.top.stream().filter(s -> s.contains("%top_name%")).collect(Collectors.toList()).size();
                LinkedList<UUID> uuids = getPage(legendaryDailyQuests.getSystemDataManager().getCompletedData().getUUIDs(categorize.getId()), perPage, pageStr);
                if (uuids.isEmpty()) {
                    sender.sendMessage(lang.PLUGIN + lang.top_norecord);
                    return;
                }

                CompletedData completedData = legendaryDailyQuests.getSystemDataManager().getCompletedData();
                int me = 1 + completedData.getTop(categorize.getId(), p.getUniqueId()).orElse(-1);

                List<String> send = new ArrayList<>();
                int index = 0;
                for (String str : lang.top) {
                    if (str.contains("%top_name")) {
                        String name = lang.default_no_player;
                        UUID uuid = uuids.size() > index ? uuids.get(index) : null;
                        if (uuid != null) {
                            OfflinePlayer of = Bukkit.getOfflinePlayer(uuid);
                            name = of.getName();
                        }
                        send.add(str.replace("%categorize%",categorize.getDisplay()).replace("%me%",String.valueOf(me)).replace("%top_name%",name));
                        index ++;
                    } else {
                        send.add(str.replace("%categorize%",categorize.getDisplay()).replace("%me%",String.valueOf(me)));
                    }
                }
                send.forEach(s -> p.sendMessage(s));
                return;
            }
            sender.sendMessage(lang.PLUGIN + lang.unknown_categorize);
            return;
        }
    }

    private LinkedList<UUID> getPage(LinkedList<UUID> input,int perPage,int page) {
        LinkedList<UUID> uuids = new LinkedList<>();
        int start = 0 + (page - 1) * perPage;
        int end = perPage + (page - 1) * perPage;
        for (int a = start; a < end ; a++) {
            if (input.size() > a) {
                uuids.add(input.get(a));
            }
        }
        return uuids;
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new CommandTabBuilder()
                .addTab(legendaryDailyQuests.getCategorizesManager().getCategorizes().stream().map(c -> c.getId()).collect(Collectors.toList()) , 1 , Arrays.asList("top"), 0)
                .addTab(Arrays.asList("1","2","3","4","5","...") , 2 , Arrays.asList("top"),0)
                .build(args);
    }
}
