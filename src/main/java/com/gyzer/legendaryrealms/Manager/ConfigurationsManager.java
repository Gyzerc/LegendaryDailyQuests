package com.gyzer.legendaryrealms.Manager;

import com.gyzer.legendaryrealms.Configurations.Config;
import com.gyzer.legendaryrealms.Configurations.Language;

public class ConfigurationsManager {
    public Language language;
    public Config config;
    public ConfigurationsManager() {
        config = new Config();
        language = new Language(config.Chinese);
    }

    public Language getLanguage() {
        return language;
    }

    public Config getConfig() {
        return config;
    }
}
