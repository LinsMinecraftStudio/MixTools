package org.lins.mmmjjkx.mixtools.managers.misc;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.lins.mmmjjkx.mixtools.MixTools;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
        for (World world: Bukkit.getWorlds()){
            if (!configuration.contains(world.getName())){
                addWorld(world);
            }
        }
        loadWorldsFromConfig();
        applyConfigToWorld();
    }
    public void addWorld(World world){addWorld(world, WorldType.NORMAL);}

    public void addWorld(World world, WorldType type){
        addWorld(world, "", type);
    }

    public void addWorld(World world, String alias, WorldType type){
        ConfigurationSection section = configuration.getConfigurationSection(world.getName());
        if (section == null) section = configuration.createSection(world.getName());
        section.set("environment", world.getEnvironment().toString());
        section.set("alias", alias);
        section.set("pvp", world.getPVP());
        section.set("type", type.getName());
        section.set("generator", world.getGenerator().getClass().getSimpleName());
        try {configuration.save(cfgfile);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    public void removeWorldFromConfig(String name){
        if (configuration.contains(name)){
            configuration.set(name, null);
            try {configuration.save(cfgfile);
            } catch (IOException e) {throw new RuntimeException(e);}
        }else {
            MixTools.warn("Cannot remove the world '"+name+"' folder.");
        }
    }

    public boolean loadWorld(String folderName, WorldType type){
        File server_root = MixTools.INSTANCE.getDataFolder().getParentFile().getParentFile();
        File folder = new File(server_root,folderName);
        if (folder.exists()) {
            World w = new WorldCreator(folderName).createWorld();
            if (w==null){
                return false;
            }
            addWorld(w, type);
            return true;
        }else {
            return false;
        }
    }

    public void loadWorldsFromConfig(){
        for (String name : configuration.getKeys(false)){
            if (Bukkit.getWorld(name)!=null) continue;//world is loaded
            File folder = MixTools.INSTANCE.getDataFolder().getParentFile().getParentFile();
            File world_folder = new File(folder,name);
            if (world_folder.exists()) {
                new WorldCreator(name).createWorld();
            }
        }
    }

    @Nonnull
    public String getWorldAlias(String name){
        ConfigurationSection section = configuration.getConfigurationSection(name);
        if (section != null) {
            return MixTools.messageHandler.colorize(section.getString("alias", name));
        }else {
            return "";
        }
    }

    @Nullable
    public World.Environment getWorldEnvironment(String name){
        ConfigurationSection section = configuration.getConfigurationSection(name);
        if (section != null) {
            try {
                return World.Environment.valueOf(section.getString("environment"));
            }catch (Exception e) {
                return null;
            }
        }else {
            return null;
        }
    }

    @Nullable
    public ChunkGenerator getWorldGenerator(String name){
        ConfigurationSection section = configuration.getConfigurationSection(name);
        if (section != null) {
            String generator = section.getString("generator");
            try {
                Class<?> c = Class.forName(generator);
                return (ChunkGenerator) c.getDeclaredConstructor().newInstance();
            }catch (Exception e) {return null;}
        }
        return null;
    }

    @Nullable
    public WorldType getWorldType(String name){
        ConfigurationSection section = configuration.getConfigurationSection(name);
        if (section != null) {
            try {
                return WorldType.valueOf(section.getString("type"));
            }catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    public void setWorldAlias(String name, String alias){
        ConfigurationSection section = configuration.getConfigurationSection(name);
        if (section != null & alias != null) {
            section.set("alias", alias);
            try {configuration.save(cfgfile);
            } catch (IOException e) {throw new RuntimeException(e);}
        }
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
