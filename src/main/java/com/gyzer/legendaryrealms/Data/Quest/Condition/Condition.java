package com.gyzer.legendaryrealms.Data.Quest.Condition;

import org.bukkit.entity.Player;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Condition {
    public static String getSymbol(String arg) {
        if (arg != null && !arg.isEmpty()) {
            if (arg.startsWith("[")) {
                StringBuilder builder = new StringBuilder();
                char[] chars = arg.toCharArray();
                boolean b = false;
                for (char c : chars) {
                    if (b) {
                        if (c == ']') {
                            break;
                        }
                        builder.append(c);
                    }
                    else {
                        if (c == '[') {
                            b = true;
                        }
                    }
                }
                return builder.toString().toLowerCase();
            }
        }
        return "";
    }
    private String symbol;
    private String value;
    private Consumer<ConditionCompare> playerConsumer;

    public Condition(String symbol, Consumer<ConditionCompare> playerConsumer) {
        this.symbol = symbol;
        this.value = "";
        this.playerConsumer = playerConsumer;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getValue() {
        return value;
    }

    public boolean canPass(Player p) {
        ConditionCompare conditionCompare = new ConditionCompare(p,value,false);
        playerConsumer.accept(conditionCompare);
        return conditionCompare.isaBoolean();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Condition clone(String value) {
        Condition condition = new Condition(symbol,playerConsumer);
        condition.setValue(value);
        return condition;
    }
    public class ConditionCompare {
        private Player p;
        private boolean aBoolean;
        private String str;

        public ConditionCompare(Player p, String str, boolean aBoolean) {
            this.p = p;
            this.aBoolean = aBoolean;
            this.str = str;
        }

        public String getStr() {
            return str;
        }

        public String getWithOutTagStr() {
            return str.replace("["+symbol+"]","");

        }
        public Player getPlayer() {
            return p;
        }

        public boolean isaBoolean() {
            return aBoolean;
        }

        public void setaBoolean(boolean aBoolean) {
            this.aBoolean = aBoolean;
        }
    }


}
