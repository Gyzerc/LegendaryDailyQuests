package com.gyzer.legendaryrealms.Utils;

import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class RunUtils {
    public static void run(Player p, List<String> list){
        LegendaryDailyQuests.getLegendaryDailyQuests().getScheduler().runTask(LegendaryDailyQuests.getLegendaryDailyQuests(),()->{
            list.forEach(s -> {
                String[] args = MsgUtils.msg(s).replace("%player%",p.getName()).split(";");
                if (args.length > 1){
                    switch (args[0].toLowerCase()){
                        case "console_command" :
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),args[1]);
                            break;
                        case "player_command" :
                            p.performCommand(args[1]);
                            break;
                        case "op_command" :
                            boolean isOp = p.isOp();
                            p.setOp(true);
                            p.performCommand(args[1]);
                            p.setOp(isOp);
                            break;
                        case "message" :
                            p.sendMessage(args[1]);
                            break;
                        case "broadcast" :
                            Bukkit.broadcastMessage(args[1]);
                            break;
                    }
                }
            });
        });
    }
}
