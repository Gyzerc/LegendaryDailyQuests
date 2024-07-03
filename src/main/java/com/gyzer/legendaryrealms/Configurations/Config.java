package com.gyzer.legendaryrealms.Configurations;

import com.gyzer.legendaryrealms.LegendaryDailyQuests;
import com.gyzer.legendaryrealms.Utils.ItemUtils;
import org.bukkit.Sound;

import java.util.Optional;
import java.util.logging.Level;

public class Config extends ConfigurationProvider{
    public Config() {
        super( "config.yml");
    }
    public boolean Chinese;
    public boolean useMySQL;
    public boolean can_refresh_accepted;
    public Optional<Sound> accept;
    public Optional<Sound> completed;
    public Optional<Sound> claim;
    public int MAX_ROUND;
    @Override
    protected void readDefault() {
        Chinese = getValue("lang","English").equals("Chinese");
        useMySQL = getValue("store.mode","SQLite").equals("MySQL");
        can_refresh_accepted = getValue("can-refresh-accepted",false);

        accept = getSound(getValue("sounds.accept","item_trident_thunder"));
        completed = getSound(getValue("sounds.completed","entity_firework_rocket_launch"));
        claim = getSound(getValue("sounds.claim","item_totem_use"));
        MAX_ROUND = getValue("refresh-round-max",20);
    }

    private Optional<Sound> getSound(String sound){
        if (sound == null){
            return Optional.empty();
        }
        try {
            return Optional.of(Sound.valueOf(sound.toUpperCase()));
        } catch (Exception e){
            legendaryDailyQuests.info("Cant find the sound named "+sound+" in current server version. ->"+getYml().getName(), Level.SEVERE);
            return Optional.empty();
        }
    }
}
