package com.gyzer.legendaryrealms.Data.Store;

import com.gyzer.legendaryrealms.Data.Quest.Progress.ProgressData;
import com.gyzer.legendaryrealms.Data.System.CompletedData;
import com.gyzer.legendaryrealms.Data.User.PlayerData;
import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class MySQL extends DataProvider{
    private final LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();
    private HikariDataSource connectPool;

    public MySQL() {
        initDataBase();
    }

    @Override
    void initDataBase(){
        setConnection();
        createTable(DatabaseTable.USER_DATA);
        createTable(DatabaseTable.DATE_DATA);
        createTable(DatabaseTable.SYSTEM_DATA);
    }

    @Override
    public void closeCon(Connection connection){
        try {
            if (connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void setConnection() {
        YamlConfiguration yml = legendaryDailyQuests.getConfigurationsManager().getConfig().getYml();
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        ConfigurationSection section = yml.getConfigurationSection("HikariCP");

        hikariConfig.setConnectionTimeout(section.getLong("connectionTimeout"));
        hikariConfig.setMinimumIdle(section.getInt("minimumIdle"));
        hikariConfig.setMaximumPoolSize(section.getInt("maximumPoolSize"));
        section = yml.getConfigurationSection("Mysql");

        String url = "jdbc:mysql://" + section.getString("address") + ":" + section.getString("port") + "/" + section.getString("database") + "?useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai";
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(section.getString("user"));
        hikariConfig.setPassword(section.getString("password"));
        hikariConfig.setAutoCommit(true);
        connectPool = new HikariDataSource(hikariConfig);
        legendaryDailyQuests.info("Successfully connected the MySQL database.",Level.INFO);
        return;


    }
    public boolean executeUpdate(String execute) {
        Connection connection=null;
        Statement stat = null;
        try {
            connection = connectPool.getConnection();
            stat = connection.createStatement();
            stat.executeUpdate(execute);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeCon(connection);
        }
    }

    @Override
    public void closeDataBase() {
        if (connectPool != null && !connectPool.isClosed()){
            connectPool.close();
             legendaryDailyQuests.info("Successfully disconnected MySQL database connection.", Level.INFO);
        }
    }

    @Override
    public void createTable(DatabaseTable table) {
        Connection connection = null;
        if (isExist(table)){
            try {
                connection = connectPool.getConnection();
                checkTable(table,connection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                closeCon(connection);
            }
            return;
        }
        if (executeUpdate(table.getBuilder().toString())){
            legendaryDailyQuests.info("created table "+table.getName(),Level.INFO);
        }
    }

    @Override
    public boolean isExist(DatabaseTable table) {
        if (connectPool == null){
            return false;
        }
        Connection connection = null;
        try {
            connection = connectPool.getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet resultSet = metaData.getTables(null, null, table.getName(), types);
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
        } finally {
            closeCon(connection);
        }
        return false;
    }

    @Override
    public List<UUID> getPlayerDatas() {
        Connection connection = null;
        List<UUID> uuids = new ArrayList<>();
        try {
            connection = connectPool.getConnection();
            Optional<ResultSet> resultSet = getDataStrings(connection,DatabaseTable.USER_DATA.getBuilder());
            if (resultSet.isPresent()) {
                ResultSet rs = resultSet.get();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    uuids.add(UUID.fromString(uuid));
                }
            }
        }
        catch (SQLException e) {
            legendaryDailyQuests.info("Failed to get all users data",Level.SEVERE,e);
        } finally {
            closeCon(connection);
        }
        return uuids;
    }

    @Override
    public Optional<PlayerData> getPlayerData(UUID uuid) {
        Connection connection = null;
        try {
            connection = connectPool.getConnection();
            Optional<ResultSet> resultSet = getDataStringResult(connection,DatabaseTable.USER_DATA.getBuilder(), uuid.toString());
            if (resultSet.isPresent()) {
                ResultSet rs = resultSet.get();
                while (rs.next()) {

                    String questsStr = rs.getString("quests");
                    String acceptsStr = rs.getString("accepts");
                    String completedsStr = rs.getString("completeds");
                    String claimStr = rs.getString("claims");
                    String refreshStr = rs.getString("refresh");
                    String progressStr = rs.getString("progress");

                    HashMap<String, LinkedList<String>> quests = StrToMapLink(questsStr);
                    HashMap<String, List<String>> accepts = StrToMap(acceptsStr);
                    HashMap<String, List<String>> completeds = StrToMap(completedsStr);
                    HashMap<String, ProgressData> process = toProgress(progressStr);
                    HashMap<String, Integer> refresh = StrToIntMap(refreshStr);
                    HashMap<String, Boolean> claimData = StrToClaimData(claimStr);

                    return Optional.of(new PlayerData(uuid, quests, accepts, completeds, process, claimData, refresh));
                }
            }
        } catch (SQLException e) {
            legendaryDailyQuests.info("Failed to get user data.",Level.SEVERE,e);
        } finally {
            closeCon(connection);
        }
        return Optional.empty();
    }

    @Override
    public void savePlayerData(PlayerData data) {
        Connection connection = null;

        try {

            connection = connectPool.getConnection();
            setData(connection,DatabaseTable.USER_DATA.getBuilder(), data.getUuid().toString(),
                    data.getUuid().toString(),
                    LinkMapToStr(data.getQuests()),
                    mapToStr(data.getAccepts()),
                    mapToStr(data.getCompleteds()),
                    claimDataToStr(data.getClaimFinallyRewards()),
                    IntMapToStr(data.getRefresh()),
                    ProgressToStr(data.getProgressData())
            );
        } catch (SQLException e){
            legendaryDailyQuests.info("Failed to save user data.",Level.SEVERE,e);
        } finally {
            closeCon(connection);
        }
    }

    @Override
    public int getDate(String cat) {
        Connection connection = null;
        try {
            connection = connectPool.getConnection();
            Optional<ResultSet> resultSet = getDataStringResult(connection,DatabaseTable.DATE_DATA.getBuilder(), cat);
            if (resultSet.isPresent()) {
                ResultSet rs = resultSet.get();
                return rs.getInt("date");
            }
        } catch (SQLException ex) {
            legendaryDailyQuests.info("Failed to get data.",Level.SEVERE,ex);
        } finally {
            closeCon(connection);
        }
        return 0;
    }

    @Override
    public void setDate(String cat,int date) {
        Connection connection = null;
        try {
            connection = connectPool.getConnection();
            setData(connection,DatabaseTable.DATE_DATA.getBuilder(), cat,cat,date);
        } catch (SQLException ex) {
            legendaryDailyQuests.info("Failed to save date data.",Level.SEVERE,ex);
        } finally {
            closeCon(connection);
        }
    }

    @Override
    public CompletedData getCompletedData() {
        Connection connection = null;
        try {
            connection = connectPool.getConnection();
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

        } finally {
            closeCon(connection);
        }
        return new CompletedData(new HashMap<>());
    }

    @Override
    public void setCompletedData(CompletedData data) {
        Connection connection = null;
        try {
            connection = connectPool.getConnection();
            setData(connection,DatabaseTable.SYSTEM_DATA.getBuilder(), "completed",
                    "completed",
                    LinkMapToStr(data.getCompleted()));
        } catch (SQLException e) {

        } finally {
            closeCon(connection);
        }
    }
}
