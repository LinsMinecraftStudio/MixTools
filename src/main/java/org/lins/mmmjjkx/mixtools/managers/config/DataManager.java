package org.lins.mmmjjkx.mixtools.managers.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.io.File;
import java.io.IOException;

public class DataManager {
    public void setData(String key, String playerName, double d) {
        FileConfiguration cs = checkPlayerInData(playerName);
        cs.set(key,d);
    }
    public void setData(String key, String playerName, int i){
        FileConfiguration cs = checkPlayerInData(playerName);
        cs.set(key,i);
    }
    public void setData(String key, String playerName, boolean b){
        FileConfiguration cs = checkPlayerInData(playerName);
        cs.set(key,b);
    }
    public void setData(String key, String playerName, String s){
        FileConfiguration cs = checkPlayerInData(playerName);
        cs.set(key,s);
    }
    public String getStringData(String key, String playerName){
        FileConfiguration cs = checkPlayerInData(playerName);
        return cs.getString(key);
    }
    public boolean getBooleanData(String key, String playerName){
        FileConfiguration cs = checkPlayerInData(playerName);
        return cs.getBoolean(key);
    }
    public int getIntegerData(String key, String playerName){
        FileConfiguration cs = checkPlayerInData(playerName);
        return cs.getInt(key);
    }
    public double getDoubleData(String key, String playerName){
        FileConfiguration cs = checkPlayerInData(playerName);
        return cs.getDouble(key);
    }
    private FileConfiguration checkPlayerInData(String playerName){
        File f = new File(MixTools.INSTANCE.getDataFolder(),"data/"+playerName+".yml");
        if (!f.exists()) {
            f.mkdirs();
            try {f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);}
        }
        return YamlConfiguration.loadConfiguration(f);
    }
}
