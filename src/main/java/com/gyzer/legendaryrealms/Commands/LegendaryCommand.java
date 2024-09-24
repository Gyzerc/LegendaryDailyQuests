package com.gyzer.legendaryrealms.Commands;

import com.gyzer.legendaryrealms.Configurations.Language;
import com.gyzer.legendaryrealms.Data.Quest.Objective.ObjectiveType;
import com.gyzer.legendaryrealms.Data.Quest.Quest;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public abstract class LegendaryCommand {
    public final LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();
    public final Language lang = legendaryDailyQuests.getConfigurationsManager().getLanguage();
    private final String permission;
    private final String command;
    private final List<Integer> length;
    private final boolean admin;

    public LegendaryCommand(String permission, String command, int length, boolean admin) {
        this.permission = permission;
        this.command = command;
        this.length = Collections.singletonList(length);
        this.admin = admin;
    }

    public LegendaryCommand(String permission, String command, List<Integer> length, boolean admin) {
        this.permission = permission;
        this.command = command;
        this.length = length;
        this.admin = admin;
    }

    public abstract void handle(CommandSender sender, String[] args);
    public abstract List<String> complete(CommandSender sender, String[] args);

    public String getPermission() {
        return permission;
    }

    public String getCommand() {
        return command;
    }

    public List<Integer> getLength() {
        return length;
    }

    public boolean isAdmin() {
        return admin;
    }

    @SuppressWarnings("deprecation")
    public OfflinePlayer getPlayer(String name) {
        OfflinePlayer p = Bukkit.getPlayerExact(name);
        if (p == null) {
            // Not the best option, but Spigot doesn't offer a good replacement (as usual)
            p = Bukkit.getOfflinePlayer(name);
            return p.hasPlayedBefore() ? p : null;
        }
        return p;
    }
    protected List<String> getOnlines() {
        return Bukkit.getOnlinePlayers().stream().map(p -> p.getName()).collect(Collectors.toList());
    }
    public Optional<Player> getOnlinePlayer(String target){
        for (Player p : Bukkit.getOnlinePlayers()){
            if (p.getName().equals(target)){
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }
    protected List<String> getTargetAccepts(String target,String cat,ObjectiveType type) {
        Player p = Bukkit.getPlayerExact(target);
        if (p != null) {
            if (legendaryDailyQuests.getCategorizesManager().getCategorize(cat) != null) {
                PlayerData data = legendaryDailyQuests.getPlayerDataManager().getPlayerData(p);
                List<String> list = new ArrayList<>();
                for (String id : data.getAccepts(cat)) {
                    Quest quest = legendaryDailyQuests.getQuestsManager().getQuest(id);
                    quest.getObjectives().forEach(o -> {
                        if (type == null || (o.getType().equals(type))) {
                            list.add(id);
                        }
                    });
                }
                return list.isEmpty() ? Collections.singletonList("NONE...") : list;
            }
        }
        return Collections.singletonList("NONE...");
    }
}
