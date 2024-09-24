package com.gyzer.legendaryrealms.Configurations;

import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;

public abstract class ConfigurationProvider {
    public final LegendaryDailyQuests legendaryDailyQuests = LegendaryDailyQuests.getLegendaryDailyQuests();
    private final File file;
    private final YamlConfiguration yml;
    public ConfigurationProvider(String path,String internalPath,String fileName){
        this.file = new File(path,fileName);
        if (!file.exists()){
            legendaryDailyQuests.saveResource(internalPath+fileName,false);
            legendaryDailyQuests.info("Successfully created file "+fileName+". ", Level.INFO);
        }
        this.yml = YamlConfiguration.loadConfiguration(file);
        readDefault();
    }
    public ConfigurationProvider(String fileName){
        this.file = new File(legendaryDailyQuests.getDataFolder(),fileName);
        if (!file.exists()){
            legendaryDailyQuests.saveResource(fileName,false);
            legendaryDailyQuests.info("Successfully created file "+fileName+" . ", Level.INFO);
        }
        this.yml = YamlConfiguration.loadConfiguration(file);
        readDefault();
    }
    protected abstract void readDefault();
    public<T> T getValue(String path,T defaultValue_CN,T defaultValue_En,boolean Chinese){
        if (yml.get(path) != null){
            try {
                return (T) yml.get(path);
            } catch (ClassCastException ex){
                legendaryDailyQuests.info("强制转化出错！-> "+path, Level.SEVERE);
            }
        }
        T Default = Chinese ? defaultValue_CN : defaultValue_En;
        yml.set(path,Default);
        return Default;
    }

    public<T> T getValue(String path,T defaultValue){
        if (yml.get(path) != null){
            try {
                return (T) yml.get(path);
            } catch (ClassCastException ex){
                legendaryDailyQuests.info("强制转化出错！-> "+path, Level.SEVERE);
                return defaultValue;
            }
        }
        yml.set(path,defaultValue);
        return defaultValue;
    }

    public Optional<ConfigurationSection> getSection(String path){
        ConfigurationSection section = yml.getConfigurationSection(path);
        return section !=null ? Optional.of(section) : Optional.empty();
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getYml() {
        return yml;
    }

    public void saveYml(){
        try {
            yml.save(file);
        } catch (IOException e) {
            legendaryDailyQuests.info("Failed to save "+file.getName() +" .",Level.SEVERE);
        }
    }
}
