package org.lins.mmmjjkx.mixtools.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;

public class DataManager {
    private final FileConfiguration data;
    public DataManager() {
        File f = new File(MixTools.INSTANCE.getDataFolder(), "data.yml");
        if (!f.exists()) {
            try {f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);}
        }
        data = YamlConfiguration.loadConfiguration(f);
    }
    //keys

    //////////////////////////////////
    public void setData(String key, String playerName, int i){
        ConfigurationSection cs = checkPlayerInData(playerName);
        cs.set(key,i);
    }
    public void setData(String key, String playerName, boolean b){
        ConfigurationSection cs = checkPlayerInData(playerName);
        cs.set(key,b);
    }
    public void setData(String key, String playerName, String s){
        ConfigurationSection cs = checkPlayerInData(playerName);
        cs.set(key,s);
    }
    private ConfigurationSection checkPlayerInData(String playerName){
        ConfigurationSection cs = data.getConfigurationSection(playerName);
        if (cs == null){
            cs = data.createSection(playerName);
        }
        return cs;
    }
}
