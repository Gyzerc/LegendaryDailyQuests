package com.gyzer.legendaryrealms.Utils;

import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MsgUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})");
    public static void sendMessage(Player p,String msg){
        p.sendMessage(msg(msg));
    }
    public static void sendMessage(String name,String msg){
        Player p = Bukkit.getPlayerExact(name);
        if (p != null){
            p.sendMessage(msg);
        }
    }
    public static String msg(String msg)
    {
        return tm(msg);
    }
    public static List<String> msg(List<String> msg)
    {
        List<String> lore=new ArrayList<>();
        for (String l:msg)
        {
            lore.add(tm(l));
        }
        return lore;
    }
    public static String tm( String textToColor) {
        if (LegendaryDailyQuests.getLegendaryDailyQuests().version_high) {
            // Use matcher to find hex patterns in given text.
            Matcher matcher = HEX_PATTERN.matcher(textToColor);
            // Increase buffer size by 32 like it is in bungee cord api. Use buffer because it is sync.
            StringBuffer buffer = new StringBuffer(textToColor.length() + 32);

            while (matcher.find()) {
                String group = matcher.group(1);

                if (group.length() == 6) {
                    // Parses #ffffff to a color text.
                    matcher.appendReplacement(buffer, ChatColor.COLOR_CHAR + "x"
                            + ChatColor.COLOR_CHAR + group.charAt(0) + ChatColor.COLOR_CHAR + group.charAt(1)
                            + ChatColor.COLOR_CHAR + group.charAt(2) + ChatColor.COLOR_CHAR + group.charAt(3)
                            + ChatColor.COLOR_CHAR + group.charAt(4) + ChatColor.COLOR_CHAR + group.charAt(5));
                } else {
                    // Parses #fff to a color text.
                    matcher.appendReplacement(buffer, ChatColor.COLOR_CHAR + "x"
                            + ChatColor.COLOR_CHAR + group.charAt(0) + ChatColor.COLOR_CHAR + group.charAt(0)
                            + ChatColor.COLOR_CHAR + group.charAt(1) + ChatColor.COLOR_CHAR + group.charAt(1)
                            + ChatColor.COLOR_CHAR + group.charAt(2) + ChatColor.COLOR_CHAR + group.charAt(2));
                }
            }
            // transform normal codes and strip spaces after color code.
            return stripSpaceAfterColorCodes(
                    ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString()));
        }
        return textToColor.replace('&',ChatColor.COLOR_CHAR);
    }
    public static String stripSpaceAfterColorCodes( String textToStrip) {
        textToStrip = textToStrip.replaceAll("(" + ChatColor.COLOR_CHAR + ".)[\\s]", "$1");
        return textToStrip;
    }

}
