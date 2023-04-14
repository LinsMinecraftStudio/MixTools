package org.lins.mmmjjkx.mixtools.managers.misc;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.io.File;

public class WorldManager {
    private final YamlConfiguration configuration;

    public WorldManager() {
        configuration = new YamlConfiguration();
        try {
            configuration.load(new File(MixTools.INSTANCE.getDataFolder(), "world.yml"));
        }catch (Exception ignored){}
    }

    public void addWorld(World world){
        addWorld(world, WorldType.NORMAL, "");
    }

    public void addWorld(World world, WorldType type){
        addWorld(world, type, "");
    }

    public void addWorld(World world, WorldType type, String alias){
        ConfigurationSection section = configuration.getConfigurationSection(world.getName());
        if (section == null) section = configuration.createSection(world.getName());
        section.set("type", type.toString());
        section.set("environment", world.getEnvironment().toString());
        section.set("alias", alias);
        section.set("pvp", world.getPVP());
    }

    public boolean removeWorld(String name){
        if (configuration.contains(name)){
            configuration.set(name, null);
            return true;
        }else {
            return false;
        }
    }

    public String getWorldAlias(String name){
        ConfigurationSection section = configuration.getConfigurationSection(name);
        if (section != null) {
            return MixTools.messageHandler.colorize(section.getString("alias", name));
        }else {
            return "";
        }
    }

    public World.Environment getWorldEnvironment(String name){
        ConfigurationSection section = configuration.getConfigurationSection(name);
        if (section != null) {
            return World.Environment.valueOf(section.getString("environment", ""));
        }else {
            return null;
        }
    }

    public void setWorldAlias(String name, String alias){
        ConfigurationSection section = configuration.getConfigurationSection(name);
        if (section != null & alias != null) {
            section.set("alias", alias);
        }
    }

    public boolean isWorldLoaded(String name){
        return Bukkit.getWorld(name) != null;
    }

    public WorldType getWorldType(String name) {
        ConfigurationSection section = configuration.getConfigurationSection(name);
        if (section != null) {
            WorldType type;
            try {type = WorldType.valueOf(section.getString("type"));
            }catch (Exception e) {
                section.set("type","NORMAL");
                return null;
            }
            return type;
        }
        return null;
    }
}
