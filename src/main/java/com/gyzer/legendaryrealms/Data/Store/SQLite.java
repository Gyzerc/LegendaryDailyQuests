package com.gyzer.legendaryrealms.Data.Store;

import com.gyzer.legendaryrealms.Data.Quest.Progress.Progress;
import com.gyzer.legendaryrealms.Data.Quest.Progress.ProgressData;
import com.gyzer.legendaryrealms.Data.System.CompletedData;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SQLite extends DataProvider {
    final LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();
    private Connection connection;
    File folder;
    public SQLite() {
        folder = new File(legendaryDailyQuests.getDataFolder(),"LegendaryDailyQuests_DataBase.db");
        setConnection();
        initDataBase();
    }

    @Override
    void initDataBase() {
        createTable(DatabaseTable.DATE_DATA);
        createTable(DatabaseTable.USER_DATA);
        createTable(DatabaseTable.SYSTEM_DATA);
        createTable(DatabaseTable.REFRESH_DATA);
    }

    @Override
    public void createTable(DatabaseTable table) {
        if (isExist(table)){
            checkTable(table,connection);
            return;
        }
        if (executeUpdate(table.getBuilder().toString())){
            legendaryDailyQuests.info("Successfully created table "+table.getName(), Level.INFO);
        }
    }

    private boolean executeUpdate(String execute) {
        if (connection != null) {
            try {
                Statement stat = null;
                stat = connection.createStatement();
                stat.executeUpdate(execute);
                stat.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }



    @Override
    public boolean isExist(DatabaseTable table) {
        if (!this.folder.exists()){
            return false;
        }
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet resultSet = metaData.getTables(null, null, table.getName(), types);
            if (resultSet.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public List<UUID> getPlayerDatas() {
        List<UUID> uuids = new ArrayList<>();
        try {
            Optional<ResultSet> resultSet = getDataStrings(connection,DatabaseTable.USER_DATA.getBuilder());
            if (resultSet.isPresent()) {
                ResultSet rs = resultSet.get();
                while (rs.next()) {
                    uuids.add(UUID.fromString(rs.getString("uuid")));
                }
            }
        } catch (SQLException e) {
            legendaryDailyQuests.info("Failed to get all users data",Level.SEVERE,e);
        }
        return uuids;
    }

    @Override
    public Optional<PlayerData> getPlayerData(UUID uuid) {
        try {
            Optional<ResultSet> resultSet = getDataStringResult(connection,DatabaseTable.USER_DATA.getBuilder(), uuid.toString());
            if (resultSet.isPresent()) {
                ResultSet rs = resultSet.get();
                String questsStr = rs.getString("quests");
                String acceptsStr = rs.getString("accepts");
                String completedsStr = rs.getString("completeds");
                String claimStr = rs.getString("claims");
                String refreshStr = rs.getString("refresh");
                String progressStr = rs.getString("progress");
                String cycles = rs.getString("cycle");

                HashMap<String, LinkedList<String>> quests = StrToMapLink(questsStr);
                HashMap<String, List<String>> accepts = StrToMap(acceptsStr);
                HashMap<String, List<String>> completeds = StrToMap(completedsStr);
                HashMap<String, ProgressData> process = toProgress(progressStr);
                HashMap<String, Integer> refresh = StrToIntMap(refreshStr);
                HashMap<String, Boolean> claimData = StrToClaimData(claimStr);
                HashMap<String,UUID> cyclesMap = StrToUUIDMap(cycles);
                return Optional.of(new PlayerData(uuid, quests, accepts, completeds, process, claimData, refresh,cyclesMap));
            }
        } catch (SQLException e) {
            legendaryDailyQuests.info("Failed to get user data.",Level.SEVERE,e);
        }
        return Optional.empty();
    }

    @Override
    public void savePlayerData(PlayerData data) {
            setData(connection,DatabaseTable.USER_DATA.getBuilder(),data.getUuid().toString(),
                    data.getUuid().toString(),
                    LinkMapToStr(data.getQuests()),
                    mapToStr(data.getAccepts()),
                    mapToStr(data.getCompleteds()),
                    claimDataToStr(data.getClaimFinallyRewards()),
                    IntMapToStr(data.getRefresh()),
                    ProgressToStr(data.getProgressData()),
                    uuidMapToStr(data.getCycles())
            );
    }

    @Override
    public int getDate(String cat) {
        int date = 0;
        try {
            Optional<ResultSet> resultSet = getDataStringResult(connection,DatabaseTable.DATE_DATA.getBuilder(), cat);
            if (resultSet.isPresent()) {
                ResultSet rs = resultSet.get();
                return rs.getInt("date");
            }
        } catch (SQLException ex) {
            legendaryDailyQuests.info("Failed to get data.",Level.SEVERE,ex);
        }
        return date;
    }

    @Override
    public void setDate(String cat, int date) {
        setData(connection,DatabaseTable.DATE_DATA.getBuilder(), cat , cat , date);
    }

    @Override
    public CompletedData getCompletedData() {
        try {
            Optional<ResultSet> resultSet = getDataStringResult(connection,DatabaseTable.SYSTEM_DATA.getBuilder(), "completed");
            if (resultSet.isPresent()) {
                ResultSet rs = resultSet.get();
                HashMap<String,LinkedList<String>> map = StrToMapLink(rs.getString("data"));

                HashMap<String,LinkedList<UUID>> completed = new HashMap<>();
                for (Map.Entry<String,LinkedList<String>> entry : map.entrySet()) {
                    LinkedList<UUID> uuids = new LinkedList<>();
                    entry.getValue().forEach(uuid -> uuids.add(UUID.fromString(uuid)));
                    completed.put(entry.getKey(),uuids);
                }
                return new CompletedData(completed);
            }
        } catch (SQLException e) {

        }
        return new CompletedData(new HashMap<>());
    }

    @Override
    public void setCompletedData(CompletedData data) {
        setData(connection,DatabaseTable.SYSTEM_DATA.getBuilder(), "completed",
                "completed",
                LinkMapToStr(data.getCompleted()));
    }

    @Override
    public void closeCon(Connection connection) {

    }

    @Override
    public Optional<UUID> getLastRefreshUID(String categorize) {
        Optional<ResultSet> resultSet =  getDataStringResult(connection,DatabaseTable.REFRESH_DATA.getBuilder(), categorize);
        if (resultSet.isPresent()) {
            ResultSet rs = resultSet.get();
            try {
                return Optional.of(UUID.fromString(rs.getString("uuid")));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    @Override
    public void setRefreshUID(String categorize, UUID uuid) {
        setData(connection,DatabaseTable.REFRESH_DATA.getBuilder(), categorize,categorize,uuid.toString());
    }

    @Override
    public void setConnection(){
        try {
            if (!folder.exists()) {
                folder.createNewFile();
                LegendaryDailyQuests.getLegendaryDailyQuests().info("create the sqlite database", Level.INFO);
            }
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + folder);
            this.connection = connection;
            legendaryDailyQuests.info("Successfully connected SQLite database.", Level.INFO);
        } catch (SQLException e) {
            LegendaryDailyQuests.getLegendaryDailyQuests().info("An exception occurred initializing the sqlite database", Level.SEVERE,e);
        } catch (IOException e) {
            LegendaryDailyQuests.getLegendaryDailyQuests().info("An exception occurred creating sqlite database", Level.SEVERE,e);
        } catch (ClassNotFoundException e) {
            LegendaryDailyQuests.getLegendaryDailyQuests().info("Failed to connect SQLite.",Level.SEVERE);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void closeDataBase() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                legendaryDailyQuests.info("Successfully disconnected SQLite database connection.", Level.INFO);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
