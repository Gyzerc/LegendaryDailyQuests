package com.gyzer.legendaryrealms.Manager;

import com.gyzer.legendaryrealms.Data.Quest.Condition.Condition;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Utils.ItemCheck;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConditionsManager {
    private final LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();
    private static HashMap<String, Condition> caches;
    public ConditionsManager() {
        caches = new HashMap<>();
        //[y]
        registerCondition(new Condition("y",conditionCompare -> {
            conditionCompare.setaBoolean(MathCompare(conditionCompare.getWithOutTagStr(), conditionCompare.getPlayer().getLocation().getBlockY()));
        }));
        //[health]
        registerCondition(new Condition("health", conditionCompare -> {
            String inputStr = conditionCompare.getWithOutTagStr();
            if (inputStr.contains("%")) {
                inputStr = inputStr.replace("%","");
                double input = Double.parseDouble(inputStr) * conditionCompare.getPlayer().getMaxHealth();
                conditionCompare.setaBoolean(MathCompare(conditionCompare.getWithOutTagStr(),input,conditionCompare.getPlayer().getHealth()));
                return;
            }
            conditionCompare.setaBoolean(MathCompare(conditionCompare.getWithOutTagStr(),conditionCompare.getPlayer().getHealth()));
        }));
        //[maxhealth]
        registerCondition(new Condition("maxhealth", conditionCompare -> {
            conditionCompare.setaBoolean(MathCompare(conditionCompare.getWithOutTagStr(), conditionCompare.getPlayer().getMaxHealth()));
        }));
        //[world]
        registerCondition(new Condition("world", conditionCompare -> conditionCompare.setaBoolean(conditionCompare.getPlayer().getWorld().getName().equals(conditionCompare.getWithOutTagStr()))));
        //[permission]
        registerCondition(new Condition("permission", conditionCompare -> conditionCompare.setaBoolean(conditionCompare.getPlayer().hasPermission(conditionCompare.getWithOutTagStr()))));
        //[biome]
        registerCondition(new Condition("biome", conditionCompare -> {
            Player p = conditionCompare.getPlayer();
            Location loc = p.getLocation();
            conditionCompare.setaBoolean(loc.getWorld().getBiome(loc.getBlockX(), loc.getBlockZ()).name().equals(conditionCompare.getWithOutTagStr().toUpperCase()));
        }));
        //[sneak]
        registerCondition(new Condition("sneak", conditionCompare -> conditionCompare.setaBoolean(conditionCompare.getPlayer().isSneaking())));
        //[fly]
        registerCondition(new Condition("fly", conditionCompare -> conditionCompare.setaBoolean(conditionCompare.getPlayer().isFlying())));
        //[vehicle]
        registerCondition(new Condition("vehicle", conditionCompare -> conditionCompare.setaBoolean(conditionCompare.getPlayer().isInsideVehicle())));
        //[chance]
        registerCondition(new Condition("chance", conditionCompare -> {
            conditionCompare.setaBoolean((new Random().nextInt(100)) <= Integer.parseInt(conditionCompare.getWithOutTagStr()));
        }));
        //[stand]
        registerCondition(new Condition("stand",conditionCompare -> {
            Block block = conditionCompare.getPlayer().getLocation().getBlock();
            conditionCompare.setaBoolean(block.getType().name().equals(conditionCompare.getWithOutTagStr().toUpperCase()));
        }));
        //[distance]
        registerCondition(new Condition("distance",conditionCompare -> {
            Player p = conditionCompare.getPlayer();
            Location location = p.getLocation();
            conditionCompare.setaBoolean(MathCompare(conditionCompare.getWithOutTagStr(), p.getWorld().getSpawnLocation().distance(location)));
        }));
        //[level]
        registerCondition(new Condition("level",conditionCompare -> conditionCompare.setaBoolean(MathCompare(conditionCompare.getWithOutTagStr(),conditionCompare.getPlayer().getLevel()))));
        //[food]
        registerCondition(new Condition("food",conditionCompare -> conditionCompare.setaBoolean(MathCompare(conditionCompare.getWithOutTagStr(),conditionCompare.getPlayer().getFoodLevel()))));
        //[onfire]
        registerCondition(new Condition("onfire",conditionCompare -> conditionCompare.setaBoolean(conditionCompare.getPlayer().getFireTicks() > 0)));
        //[onground]
        registerCondition(new Condition("onground",conditionCompare -> conditionCompare.setaBoolean(conditionCompare.getPlayer().isOnGround())));
        //[sleep]
        registerCondition(new Condition("onlava", conditionCompare -> conditionCompare.setaBoolean(conditionCompare.getPlayer().isSleeping())));
        //[run]
        registerCondition(new Condition("run", conditionCompare -> conditionCompare.setaBoolean(conditionCompare.getPlayer().isSprinting())));
        //[night]
        registerCondition(new Condition("night", conditionCompare -> {
            Player p = conditionCompare.getPlayer();
            int hour= (int) (p.getWorld().getTime()/1000);
            conditionCompare.setaBoolean(!isDay(hour));
        }));
        //[day]
        registerCondition(new Condition("day",conditionCompare -> {
            Player p = conditionCompare.getPlayer();
            int hour= (int) (p.getWorld().getTime()/1000);
            conditionCompare.setaBoolean(isDay(hour));
        }));
        //[hasitem]
        registerCondition(new Condition("hasitem", conditionCompare -> {
            Player p = conditionCompare.getPlayer();
            ItemCheck check = new ItemCheck(conditionCompare.getWithOutTagStr());
            conditionCompare.setaBoolean(check.hasAmountPlayerInventory(p) > 0);
        }));
        //[handitem]
        registerCondition(new Condition("handitem", conditionCompare -> {
            ItemCheck check = new ItemCheck(conditionCompare.getWithOutTagStr());
            conditionCompare.setaBoolean(check.Compare(conditionCompare.getPlayer().getInventory().getItemInMainHand()));
        }));
        //[offhanditem]
        registerCondition(new Condition("offhanditem", conditionCompare -> {
            ItemCheck check = new ItemCheck(conditionCompare.getWithOutTagStr());
            conditionCompare.setaBoolean(check.Compare(conditionCompare.getPlayer().getInventory().getItemInOffHand()));
        }));
        //[helmetitem]
        registerCondition(new Condition("helmetitem", conditionCompare -> {
            ItemCheck check = new ItemCheck(conditionCompare.getWithOutTagStr());
            conditionCompare.setaBoolean(check.Compare(conditionCompare.getPlayer().getInventory().getHelmet()));
        }));
        //[chestitem]
        registerCondition(new Condition("chestitem", conditionCompare -> {
            ItemCheck check = new ItemCheck(conditionCompare.getWithOutTagStr());
            conditionCompare.setaBoolean(check.Compare(conditionCompare.getPlayer().getInventory().getChestplate()));
        }));
        //[legsitem]
        registerCondition(new Condition("legsitem", conditionCompare -> {
            ItemCheck check = new ItemCheck(conditionCompare.getWithOutTagStr());
            conditionCompare.setaBoolean(check.Compare(conditionCompare.getPlayer().getInventory().getLeggings()));
        }));
        //[bootsitem]
        registerCondition(new Condition("bootsitem", conditionCompare -> {
            ItemCheck check = new ItemCheck(conditionCompare.getWithOutTagStr());
            conditionCompare.setaBoolean(check.Compare(conditionCompare.getPlayer().getInventory().getBoots()));
        }));
        //[placeholderapi]
        registerCondition(new Condition("placeholderapi",conditionCompare -> {
            conditionCompare.setaBoolean(papiCompare(conditionCompare.getPlayer(),conditionCompare.getWithOutTagStr()));
        }));
        //[swim]
        if (legendaryDailyQuests.version_high) {
            registerCondition(new Condition("swim", conditionCompare -> {
                conditionCompare.setaBoolean(conditionCompare.getPlayer().isSwimming());
            }));
        }

    }
    public static void registerCondition(Condition condition) {
        caches.put(condition.getSymbol(),condition);
    }
    public Condition[] matchCondition(List<String> list){
        List<Condition> conditions = new ArrayList<>();
        for (String str:list) {
            String id = Condition.getSymbol(str);
            if (!id.isEmpty() && caches.containsKey(id)) {
                conditions.add(caches.get(id).clone(str));
            }
        }
        return conditions.stream().toArray(Condition[]::new);
    }

    public boolean MathCompare(String dealArgWithOutTag, double input) {
        Pattern r = Pattern.compile("\\W+");
        Matcher m = r.matcher(dealArgWithOutTag);
        String tag = "";
        while (m.find()) {
            tag = m.group();
        }
        String deal = dealArgWithOutTag.replace(tag, "");
        double value = Double.parseDouble(deal);
        switch (tag) {
            case ">=":
                return input >= value;
            case "<=":
                return input <= value;
            case ">":
                return input > value;
            case "<":
                return input < value;
            case "=":
                return input == value;
        }
        return false;
    }
    private boolean isDay(int hour) {
        List<Integer> night= Arrays.asList(18,19,20,21,22,23,24,0,1,2,3,4,5);
        return !night.contains(hour);
    }
    public boolean MathCompare(String dealArgWithOutTag,double preset, double input) {
        Pattern r = Pattern.compile("\\W+");
        Matcher m = r.matcher(dealArgWithOutTag);
        String tag = "";
        while (m.find()) {
            tag = m.group();
        }
        switch (tag) {
            case ">=":
                return input >= preset;
            case "<=":
                return input <= preset;
            case ">":
                return input > preset;
            case "<":
                return input < preset;
            case "=":
                return input == preset;
        }
        return false;
    }
    public boolean papiCompare(Player p,String dealArgWithOutTag)
    {
        String[] values = dealArgWithOutTag.split(";");
        String tag = values[1];
        switch (tag) {
            case ">=" :
                double input = values.length == 3 ? Double.parseDouble(values[2]) : 0.0;
                return Double.parseDouble(LegendaryDailyQuests.getLegendaryDailyQuests().getIntegrationsManager().getPlaceholderAPIHook().getHolder(p,values[0])) >= input;
            case ">" :
                input =values.length == 3 ? Double.parseDouble(values[2]) : 0.0;
                return Double.parseDouble(LegendaryDailyQuests.getLegendaryDailyQuests().getIntegrationsManager().getPlaceholderAPIHook().getHolder(p,values[0])) > input;
            case "<=" :
                input =values.length == 3 ? Double.parseDouble(values[2]) : 0.0;
                return Double.parseDouble(LegendaryDailyQuests.getLegendaryDailyQuests().getIntegrationsManager().getPlaceholderAPIHook().getHolder(p,values[0])) <= input;
            case "<" :
                input = values.length == 3 ? Double.parseDouble(values[2]) : 0.0;
                return Double.parseDouble(LegendaryDailyQuests.getLegendaryDailyQuests().getIntegrationsManager().getPlaceholderAPIHook().getHolder(p,values[0])) < input;
            case "=":
                return LegendaryDailyQuests.getLegendaryDailyQuests().getIntegrationsManager().getPlaceholderAPIHook().getHolder(p,values[0]).equals((values.length == 3 ? values[2] : ""));
        }
        return false;
    }

}
