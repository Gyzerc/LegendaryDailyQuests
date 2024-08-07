package com.gyzer.legendaryrealms.Data.Store;

import com.gyzer.legendaryrealms.Data.Quest.Progress.Progress;
import com.gyzer.legendaryrealms.Data.Quest.Progress.ProgressData;
import com.gyzer.legendaryrealms.Data.System.CompletedData;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Utils.StringUtils;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public abstract class DataProvider {
    abstract void initDataBase() throws RuntimeException;
    public abstract void setConnection() throws SQLException;
    public abstract void closeDataBase();
    public abstract void createTable(DatabaseTable table);
    public abstract boolean isExist(DatabaseTable table);
    public abstract List<UUID> getPlayerDatas();
    public abstract Optional<PlayerData> getPlayerData(UUID uuid);
    public abstract void savePlayerData(PlayerData data);
    public abstract int getDate(String cat);
    public abstract void setDate(String cat,int date);
    public abstract CompletedData getCompletedData();
    public abstract void setCompletedData(CompletedData data);
    public abstract void closeCon(Connection connection);
    public abstract Optional<UUID> getLastRefreshUID(String categorize);
    public abstract void setRefreshUID(String categorize,UUID uuid);

    public void checkTable(DatabaseTable table, Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            Builder builder = table.getBuilder();
            for (Builder.Column key : builder.keys) {
                if (!metaData.getColumns(null,null,builder.getTableName(), key.getColumn()).next()) {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate("ALTER TABLE " + builder.getTableName() + " ADD COLUMN " + key.getColumn()+" " + key.getType());
                    LegendaryDailyQuests.getLegendaryDailyQuests().info("检测到表 "+builder.getTableName() +" 缺失列 "+key.getColumn()+" 已自动补全..", Level.INFO);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    protected Optional<ResultSet> getDataStringResult(Connection connection, DataProvider.Builder builder, String target) {
        if (connection != null) {
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            try {
                statement = connection.prepareStatement("SELECT * FROM " + builder.getTableName() + " WHERE `" + builder.getMainKey() + "` = '" + target + "' LIMIT 1;");
                resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    return Optional.of(resultSet);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    protected Optional<ResultSet> getDataStrings(Connection connection, DataProvider.Builder builder) {
        if (connection != null) {
            PreparedStatement statement = null;
            ResultSet rs = null;
            try {
                statement = connection.prepareStatement("SELECT * FROM "+builder.getTableName()+";");
                rs = statement.executeQuery();
                return Optional.of(rs);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }
    protected void delData(Connection connection, DataProvider.Builder builder, String target) {
        if (connection != null) {
            PreparedStatement statement = null;
            try {
                statement = connection.prepareStatement("DELETE FROM `"+builder.getTableName()+"` WHERE `"+builder.getMainKey()+"` = ?");
                statement.setString(1, target);
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    protected <T> void setData(Connection connection, DataProvider.Builder builder, String target, T... ts) {
        if (connection != null) {
            PreparedStatement ps = null;
            try {
                ps = connection.prepareStatement(builder.getInsertString(target));
                int a = 1;
                for (T t : ts) {
                    ps.setObject(a, t);
                    a++;
                }
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public enum DatabaseType {
        MySQL,SQLite
    }
    public String mapToStr(HashMap<String, List<String>> map){
        StringBuilder builder = new StringBuilder();
        if (!map.isEmpty()){
            map.forEach(((s, strings) -> builder.append("{").append(s).append("=").append(ListToStr(strings)).append("}")));
        }
        return builder.toString();
    }
    public String uuidMapToStr(HashMap<String, UUID> map){
        StringBuilder builder = new StringBuilder();
        if (!map.isEmpty()){
            map.forEach(((s, strings) -> builder.append(s).append("=").append(strings.toString()).append(",")));
        }
        return builder.toString();
    }
    public HashMap<String,UUID> StrToUUIDMap(String str) {
        HashMap<String,UUID> map = new HashMap<>();
        if (str != null &&!str.isEmpty()) {
            for (String arg : str.split(",")) {
                String[] values = arg.split("=");
                if (values.length == 2) {
                    map.put(values[0],UUID.fromString(values[1]));
                }
            }
        }
        return map;
    }

    public<T> String LinkMapToStr(HashMap<String, LinkedList<T>> map){
        StringBuilder builder = new StringBuilder();
        if (!map.isEmpty()){
            map.forEach(((s, strings) -> builder.append("{").append(s).append("=").append(ListToStr(strings)).append("}")));
        }
        return builder.toString();
    }
    public HashMap<String,List<String>> StrToMap(String str){
        HashMap<String,List<String>> map = new HashMap<>();
        if (str != null && !str.isEmpty()){
            for (String listStr : StringUtils.split(str,'{','}')){
                String[] args = listStr.split("=");
                String name = args[0];
                if (args.length > 1){
                    map.put(name,StrToList(args[1]));
                }
            }
        }
        return map;
    }

    public HashMap<String,Integer> StrToIntMap(String str){
        HashMap<String,Integer> map = new HashMap<>();
        if (str != null && !str.isEmpty()) {
            for (String arg : str.split(";")) {
                String[] args = arg.split(":");
                String cat = args[0];
                int amount = StringUtils.getInt(args[1],0);
                map.put(cat,amount);
            }
        }
        return map;
    }

    public String IntMapToStr(HashMap<String,Integer> l){
        StringBuilder builder = new StringBuilder();
        if (l != null && !l.isEmpty()) {
            l.forEach((s,i) -> builder.append(s).append(":").append(i).append(";"));
        }
        return builder.toString();
    }
    public HashMap<String,UUID> StrToMapUUID(String str){
        HashMap<String,UUID> map = new HashMap<>();
        if (str != null && !str.isEmpty()){
            for (String listStr : StringUtils.split(str,'{','}')){
                String[] args = listStr.split("=");
                String name = args[0];
                if (args.length > 1){
                    map.put(name,UUID.fromString(args[1]));
                }
            }
        }
        return map;
    }
    public HashMap<String,LinkedList<String>> StrToMapLink(String str){
        HashMap<String,LinkedList<String>> map = new HashMap<>();
        if (str != null && !str.isEmpty()){
            for (String listStr : StringUtils.split(str,'{','}')){
                String[] args = listStr.split("=");
                String name = args[0];
                if (args.length > 1){
                    map.put(name,StrToLinkedList(args[1]));
                }
            }
        }
        return map;
    }
    private<T> String ListToStr(List<T> l){
        StringBuilder builder = new StringBuilder();
        if (l != null && !l.isEmpty()) {
            l.forEach(s -> builder.append(s.toString()).append(";"));
        }
        return builder.toString();
    }
    private<T> String ListToStr(LinkedList<T> l){
        return ListToStr(new ArrayList<>(l));
    }
    private List<String> StrToList(String str){
        if (str != null && !str.isEmpty()){
            return Arrays.stream(str.split(";")).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    private LinkedList<String> StrToLinkedList(String str){
        return new LinkedList<>(StrToList(str));
    }
    public String claimDataToStr(HashMap<String,Boolean> map){
        StringBuilder builder = new StringBuilder();
        if (map != null && !map.isEmpty()){
            map.forEach(((s, aBoolean) -> builder.append(s).append(":").append(aBoolean).append(";")));
        }
        return builder.toString();
    }
    public HashMap<String,Boolean> StrToClaimData(String str){
        HashMap<String,Boolean> map = new HashMap<>();
        if (str != null && !str.isEmpty()){
            for (String s:str.split(";")){
                String[] args = s.split(":");
                if (args.length > 1){
                    map.put(args[0], StringUtils.getBoolean(args[1],false));
                }
            }
        }
        return map;
    }

    public String ProgressToStr(HashMap<String, ProgressData> data){
        StringBuilder builder = new StringBuilder();
        List<String> list_cat = new ArrayList<>();
        for (Map.Entry<String,ProgressData> entry:data.entrySet()){
            String cat = entry.getKey();
            if (!list_cat.contains(cat)) {
                builder.append("[").append(cat).append("=").append(entry.getValue().toString()).append("]");
                list_cat.add(cat);
            }
        }
        return builder.toString();
    }
    public HashMap<String, ProgressData> toProgress(String str){
        HashMap<String, ProgressData> data = new HashMap<>();
        if (str != null && !str.isEmpty()){
            for (String catStr : StringUtils.split(str,'[',']')){
                HashMap<String, Progress[]> categorize = new HashMap<>();
                String[] catStrs = catStr.split("=");
                String cat = catStrs[0];
                if (catStrs.length > 1) {
                    String quests = catStrs[1];
                    for (String questsStr : StringUtils.split(quests,'{','}')) {
                        List<Progress> progresses = new ArrayList<>();
                        String[] args = questsStr.split("~");
                        String questId = args[0];
                        if (args.length > 1) {
                            String progressStr = args[1];
                            for (String progress : progressStr.split(";")) {
                                String[] attributes = progress.split(":");
                                String name = attributes[0];
                                if (attributes.length > 1) {
                                    double amount = StringUtils.getDouble(attributes[1], 0);
                                    progresses.add(new Progress(name,amount));
                                }
                            }
                        }
                        categorize.put(questId,progresses.stream().toArray(Progress[]::new));
                    }
                }
                data.put(cat,new ProgressData(categorize));
            }
        }
        return data;
    }

    public enum DatabaseTable{
        USER_DATA("LegendaryDailyQuests_User",
                new Builder("LegendaryDailyQuests_User")
                        .addVarcharKey("uuid",64)
                        .addTextKey("quests")
                        .addTextKey("accepts")
                        .addTextKey("completeds")
                        .addTextKey("claims")
                        .addTextKey("refresh")
                        .addTextKey("progress")
                        .addTextKey("cycle")
                        .build("uuid")
        ),
        DATE_DATA("LegendaryDailyQuests_Date",
                new Builder("LegendaryDailyQuests_Date")
                    .addVarcharKey("categorize",32)
                    .addIntegerKey("date")
                    .build("categorize")),
        SYSTEM_DATA("LegendaryDailyQuests_System",
                new Builder("LegendaryDailyQuests_System")
                        .addVarcharKey("name",64)
                        .addTextKey("data")
                        .build("name")),
        REFRESH_DATA("LegendaryDailyQuests_RefreshCycles",
                new Builder("LegendaryDailyQuests_RefreshCycles")
                        .addVarcharKey("id",32)
                        .addTextKey("uuid")
                        .build("id"));


        private String name;
        private Builder builder;
        DatabaseTable(String name,Builder builder){
            this.name = name;
            this.builder = builder;
        }
        public String getName() {
            return name;
        }

        public Builder getBuilder() {
            return builder;
        }
    }

    public static class Builder {
        private String tableName;
        private String mainKey;
        private StringBuilder stringBuilder;
        private List<Column> keys;

        public Builder(String tableName) {
            this.keys = new ArrayList<>();
            this.tableName = tableName;
            stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS "+tableName+" (");
        }

        public Builder addTextKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("").append(keyName).append(" TEXT DEFAULT NULL");
            keys.add(new Column(keyName,"TEXT DEFAULT NULL"));
            return this;
        }

        public Builder addUUIDKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("").append(keyName).append(" UUID DEFAULT NULL");
            keys.add(new Column(keyName,"UUID DEFAULT NULL"));
            return this;
        }

        public Builder addBlobKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("").append(keyName).append(" BLOB DEFAULT NULL");
            keys.add(new Column(keyName,"BLOB DEFAULT NULL"));
            return this;
        }

        public Builder addIntegerKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` INTEGER DEFAULT 0");
            keys.add(new Column(keyName,"INTEGER DEFAULT 0"));
            return this;
        }

        public Builder addDoubleKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` DOUBLE DEFAULT 0");
            keys.add(new Column(keyName,"DOUBLE DEFAULT 0"));
            return this;
        }
        public Builder addLongKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` LONG NOT NULL");
            keys.add(new Column(keyName,"LONG NOT NULL"));
            return this;
        }
        public Builder addVarcharKey(String keyName,int length){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` varchar("+length+") NOT NULL");
            keys.add(new Column(keyName,"varchar(" + length + ") NOT NULL"));
            return this;
        }
        public Builder addBooleanKey(String keyName){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            stringBuilder.append("`").append(keyName).append("` BOOLEAN NOT NULL");
            keys.add(new Column(keyName,"BOOLEAN NOT NULL"));
            return this;
        }
        public Builder build(String mainKey){
            if (!stringBuilder.toString().endsWith(",") && !stringBuilder.toString().endsWith("(")){
                stringBuilder.append(",");
            }
            this.mainKey = mainKey;
            stringBuilder.append("PRIMARY KEY (`"+mainKey+"`));");
            return this;
        }
        public Builder build() {
            stringBuilder.append(");");
            return this;
        }

        public String getTableName() {
            return tableName;
        }

        public String getMainKey() {
            return mainKey;
        }

        @Override
        public String toString(){
            return stringBuilder.toString();
        }

        public String getInsertString(String target) { //`
            StringBuilder main = new StringBuilder("REPLACE INTO "+tableName+" ");
            StringBuilder keys = new StringBuilder("(");
            StringBuilder keys_unknow = new StringBuilder("(");
            for (int i =0 ; i < this.keys.size() ; i ++) {
                keys.append("`").append(this.keys.get(i).getColumn()).append("`");
                keys_unknow.append("?");
                if (i == this.keys.size() - 1 ) {
                    keys.append(")");
                    keys_unknow.append(")");
                    break;
                } else {
                    keys.append(",");
                    keys_unknow.append(",");
                }
            }
            main.append(keys).append(" VALUES ").append(keys_unknow);
            return main.toString();
        }


        public class Column {
            private String column;
            private String type;

            public Column(String column, String type) {
                this.column = column;
                this.type = type;
            }

            public String getColumn() {
                return column;
            }

            public String getType() {
                return type;
            }
        }
    }

}
