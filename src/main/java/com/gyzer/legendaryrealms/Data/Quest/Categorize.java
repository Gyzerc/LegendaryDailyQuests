package com.gyzer.legendaryrealms.Data.Quest;

import com.gyzer.legendaryrealms.Menu.MenuLoader;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;

public class Categorize {
    private String id;
    private String display;
    private int cycle;
    private int amount;
    private List<String> quests;
    private MenuLoader loader;
    private List<String> rewards;
    private int default_refresh_points;
    public Categorize(String id, String display, int cycle, int amount, List<String> quests,MenuLoader loader,List<String> rewards,int default_refresh_points) {
        this.id = id;
        this.display = display;
        this.cycle = cycle;
        this.amount = amount;
        this.quests = quests;
        this.loader = loader;
        this.rewards = rewards;
        this.default_refresh_points = default_refresh_points;
    }

    public int getDefault_refresh_points() {
        return default_refresh_points;
    }

    public String getDisplay() {
        return display;
    }

    public List<String> getRewards() {
        return rewards;
    }

    public MenuLoader getLoader() {
        return loader;
    }

    public String getId() {
        return id;
    }

    public List<String> getQuests() {
        return quests;
    }

    public void setQuests(List<String> quests) {
        this.quests = quests;
    }

    public int getCycle() {
        return cycle;
    }

    public int getAmount() {
        return amount;
    }

    public int getRefreshPoints(Player p) {
        String perm="legendarydailyquests.default_refresh_points." + id.toLowerCase() +".";
        int points = default_refresh_points;
        if (p != null) {
            for (PermissionAttachmentInfo info : p.getEffectivePermissions()) {
                String permission = info.getPermission();
                if (!permission.startsWith(perm)) {
                    continue;
                }
                String valueString = permission.substring(perm.lastIndexOf(".") + 1);

                if ("*".equals(valueString)) {
                    return Integer.MAX_VALUE;
                }


                try {
                    int t = Integer.parseInt(valueString);
                    if (t > points) {
                        points = t;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
        return points;
    }
}
