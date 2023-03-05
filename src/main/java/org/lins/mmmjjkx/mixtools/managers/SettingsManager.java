package org.lins.mmmjjkx.mixtools.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
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
    public void setSpawn(Location loc){
        ConfigurationSection cs = config.getConfigurationSection("spawn") == null ? config.createSection("spawn") : config.getConfigurationSection("spawn") ;
        cs.set("world", loc.getWorld());
        cs.set("x", loc.getX());
        cs.set("y", loc.getY());
        cs.set("z", loc.getZ());
        cs.set("pitch", loc.getPitch());
        cs.set("yaw", loc.getYaw());
    }

    public Location getSpawnLocation() {
        ConfigurationSection cs = config.getConfigurationSection("spawn");
        if (cs != null){
            double x = cs.getDouble("x");
            double y = cs.getDouble("y");
            double z = cs.getDouble("z");
            World w = Bukkit.getWorld(cs.getString("world",""));
            if (w != null) {
                return new Location(w, x, y, z,
                        Float.parseFloat(cs.getString("pitch", "0.000")), Float.parseFloat(cs.getString("yaw", "0.000")));
            }
            return null;
        }
        return null;
    }
}
