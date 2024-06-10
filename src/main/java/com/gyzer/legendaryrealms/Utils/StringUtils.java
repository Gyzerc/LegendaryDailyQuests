package com.gyzer.legendaryrealms.Utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static int getInt(String str,int def){
        try {
            return Integer.parseInt(str);
        } catch (IllegalArgumentException e){
            return def;
        }
    }
    public static double getDouble(String str,double def){
        try {
            return Double.parseDouble(str);
        } catch (IllegalArgumentException e){
            return def;
        }
    }
    public static boolean getBoolean(String str,boolean def){
        try {
            return Boolean.parseBoolean(str);
        } catch (IllegalArgumentException e){
            return def;
        }
    }

    public static List<String> split(String str, char beginChar, char endChar){
        List<String> list = new ArrayList<>();
        char[] chars = str.toCharArray();
        boolean begin = false;
        StringBuilder builder = new StringBuilder();
        for (char c : chars){
            if (begin){
                if (c == endChar){
                    list.add(builder.toString());
                    begin = false;
                    builder = new StringBuilder();
                    continue;
                }
                builder.append(c);
            } else {
                if (c == beginChar){
                    begin = true;
                }
            }
        }
        return list;
    }
    public static String findName(String str,char splitChar){
        StringBuilder builder = new StringBuilder();
        char[] chars = str.toCharArray();
        for (char c: chars){
            if (c == splitChar){
                return builder.toString();
            }
            builder.append(c);
        }
        return builder.toString();
    }
}
