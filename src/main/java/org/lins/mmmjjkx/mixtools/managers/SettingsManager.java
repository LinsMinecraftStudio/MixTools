package org.lins.mmmjjkx.mixtools.managers;

import org.bukkit.configuration.file.FileConfiguration;

public class SettingsManager {
    private final FileConfiguration config;
    public SettingsManager(FileConfiguration config){
        this.config = config;
    }
    public int getInt(String key){
        return config.getInt(key);
    }
    public String getString(String key){
        return config.getString(key);
    }
    public boolean getBoolean(String key){
        return config.getBoolean(key);
    }
    public long getLong(String key) {
        return config.getLong(key);
    }
    public void set(String key, Object value){
        config.set(key, value);
    }
}
