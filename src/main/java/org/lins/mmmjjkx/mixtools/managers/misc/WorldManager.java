package org.lins.mmmjjkx.mixtools.managers.misc;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.io.File;
import java.io.IOException;

public class WorldManager {
    private final YamlConfiguration configuration;
    private final File cfgfile;

    public WorldManager() {
        configuration = new YamlConfiguration();
        cfgfile = new File(MixTools.INSTANCE.getDataFolder(), "world.yml");
        try {configuration.load(cfgfile);
        }catch (Exception ignored){}
        for (World world : Bukkit.getWorlds()){
            addWorld(world);
        }
        applyConfigToWorld();
    }

    public void addWorld(World world){
        addWorld(world, "");
    }

    public void addWorld(World world, String alias){
        ConfigurationSection section = configuration.getConfigurationSection(world.getName());
        if (section == null) section = configuration.createSection(world.getName());
        section.set("environment", world.getEnvironment().toString());
        section.set("alias", alias);
        section.set("pvp", world.getPVP());
        try {configuration.save(cfgfile);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    public boolean removeWorld(String name){
        if (configuration.contains(name)){
            configuration.set(name, null);
            try {configuration.save(cfgfile);
            } catch (IOException e) {throw new RuntimeException(e);}
            return true;
        }else {
            return false;
        }
    }

    public boolean loadWorld(String folderName){
        File server_root = MixTools.INSTANCE.getDataFolder().getParentFile().getParentFile();
        File folder = new File(server_root,folderName);
        if (folder.exists()) {
            World w = new WorldCreator(folderName).createWorld();
            if (w==null){
                return false;
            }
            addWorld(w);
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
            try {configuration.save(cfgfile);
            } catch (IOException e) {throw new RuntimeException(e);}
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

    public void applyConfigToWorld(){
        for (World world: Bukkit.getWorlds()){
            ConfigurationSection section = configuration.getConfigurationSection(world.getName());
            if (section != null){
                world.setPVP(section.getBoolean("pvp"));
            }
        }
    }
}
