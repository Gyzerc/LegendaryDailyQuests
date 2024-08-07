package com.gyzer.legendaryrealms.Commands;

import com.gyzer.legendaryrealms.Commands.SubCommands.*;
import com.gyzer.legendaryrealms.Configurations.Language;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DailyQuestsCommands implements CommandExecutor, TabExecutor {
    static final LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();
    static final Language lang = legendaryDailyQuests.getConfigurationsManager().getLanguage();
    private static HashMap<String,LegendaryCommand> commands;
    public static void register(){
        commands = new HashMap<>();
        commands.put("open",new OpenCommand());
        commands.put("random",new RandomCommand());
        commands.put("reload",new ReloadCommand());
        commands.put("refresh",new RefreshCommand());
        commands.put("trigger",new TriggerCommand());
        commands.put("refresh-all",new RefreshAllCommand());
        commands.put("top",new TopCommand());
        commands.put("complete", new CompleteCommnd());
        commands.put("reset",new ResetCommand());
    }
    private List<String> list = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings)
    {
        int length = strings.length;

        if (length == 0){

            //发送指令提示
            lang.help_default.forEach(m -> sender.sendMessage(m));
            if (sender.isOp()){
                lang.help_admin.forEach(m -> sender.sendMessage(m));
            }
        }
        else {


            String subCommandName = strings[0];
            HashMap<String,LegendaryCommand> map=commands;
            LegendaryCommand cmd = map.get(subCommandName);
            if (cmd == null){
                sender.sendMessage(lang.PLUGIN+lang.unknown_command);
                return false;
            }
            if ( (cmd.isAdmin() && sender.isOp()) || (!cmd.getPermission().isEmpty() && sender.hasPermission(cmd.getPermission())) || cmd.getPermission().isEmpty()) {
                if (cmd.getLength().contains(length)) {
                    cmd.handle(sender, strings);
                    return true;
                }
            }
            else {
                sender.sendMessage(lang.PLUGIN + lang.permission);
                return true;
            }
            sender.sendMessage(lang.PLUGIN+lang.unknown_command);
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        int length = strings.length;
        List<String> tab=new ArrayList<>();
        if (length == 1 ){
            for (Map.Entry<String,LegendaryCommand> entry:commands.entrySet()){
                LegendaryCommand legendaryCommand=entry.getValue();
                if ((legendaryCommand.isAdmin() && commandSender.isOp()) || (commandSender.hasPermission(legendaryCommand.getPermission()))){
                    tab.add(entry.getKey());
                }
            }
            return tab;
        }
        else {
            String subCommand = strings[0];
            HashMap<String,LegendaryCommand> map=commands;
            LegendaryCommand legendaryCommand = map.get(subCommand);
            if (legendaryCommand != null){
                if ((legendaryCommand.isAdmin() && commandSender.isOp()) || (commandSender.hasPermission(legendaryCommand.getPermission()))){
                    return legendaryCommand.complete(commandSender,strings);
                }
            }
        }
        return null;
    }
}
