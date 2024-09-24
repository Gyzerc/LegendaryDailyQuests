package com.gyzer.legendaryrealms.Data.User;

import com.gyzer.legendaryrealms.Utils.StringUtils;

import java.util.HashMap;

public class LoginData {
    private int date;
    private HashMap<String,Integer> login;

    public LoginData(int date, HashMap<String, Integer> login) {
        this.date = date;
        this.login = login;
    }

    public int getLastLoginDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setLogin(HashMap<String, Integer> login) {
        this.login = login;
    }

    public HashMap<String, Integer> getLoginData() {
        return login;
    }

    public static String toString(LoginData data){
        StringBuilder builder = new StringBuilder();
        if (data != null && data.getLoginData().size() > 0) {
            builder.append(data.getLoginData()).append("/");
            data.getLoginData().forEach(((s, integer) -> builder.append(s).append(":").append(integer).append(";")));
        }
        return builder.toString();
    }
    public static LoginData toData(String str){
        HashMap<String,Integer> map = new HashMap<>();
        int last = 99;
        if (str != null && !str.isEmpty()){
            String[] args = str.split("/");
            last = StringUtils.getInt(args[0],99);
            if (args.length > 1){
                for (String arg : args[1].split(";")){
                    String[] values = arg.split(":");
                    int value = StringUtils.getInt((values.length > 1 ? values[1] : "0"),0);
                    map.put(values[0],value);
                }
            }
        }
        return new LoginData(last,map);
    }
}
