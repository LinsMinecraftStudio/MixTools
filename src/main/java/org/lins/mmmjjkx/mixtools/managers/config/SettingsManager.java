package org.lins.mmmjjkx.mixtools.managers.config;

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
    public void setInt(String key, int value){
        config.set(key, value);
    }
    public void setString(String key, String value){
        config.set(key, value);
    }
    public void setBoolean(String key, boolean value){
        config.set(key, value);
    }
}
