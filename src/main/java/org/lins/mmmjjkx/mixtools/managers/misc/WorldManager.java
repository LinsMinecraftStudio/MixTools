package org.lins.mmmjjkx.mixtools.managers.misc;

import io.github.linsminecraftstudio.polymer.objects.plugin.AbstractFeatureManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.generators.VoidWorldGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public class WorldManager extends AbstractFeatureManager {
    private YamlConfiguration configuration;
    private final File cfgfile = new File(MixTools.getInstance().getDataFolder(), "world.yml");

    public WorldManager() {
        super(MixTools.getInstance());
        configuration = handleConfig("world.yml");
        for (World world: Bukkit.getWorlds()){
            if (!configuration.contains(world.getName())){
                addWorld(world);
            }
        }
        loadWorldsFromConfig();
        applyConfigToWorld();
    }

    @Override
    public void reload() {
        configuration = handleConfig("world.yml");
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
        String simpleName = world.getGenerator() != null ? world.getGenerator().getClass().getSimpleName() : "";
        section.set("generator", simpleName);
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
        File server_root = MixTools.getInstance().getDataFolder().getParentFile().getParentFile();
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
            File folder = MixTools.getInstance().getDataFolder().getParentFile().getParentFile();
            File world_folder = new File(folder,name);
            if (world_folder.exists()) {
                new WorldCreator(name).createWorld();
            }
        }
    }

    @Nonnull
    public Component getWorldAlias(String name){
        ConfigurationSection section = configuration.getConfigurationSection(name);
        if (section != null) {
            return MixTools.messageHandler.colorize(section.getString("alias", name));
        }else {
            return Component.empty();
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

    @Nonnull
    public String getChunkGeneratorName(ChunkGenerator chunkGenerator){
        return chunkGenerator == null ? "Default" : chunkGenerator.getClass().getSimpleName();
    }

    @Nullable
    public ChunkGenerator getWorldGenerator(String worldName){
        ConfigurationSection section = configuration.getConfigurationSection(worldName);
        if (section != null) {
            return getChunkGenerator(section.getString("generator",""));
        }
        return null;
    }

    @Nullable
    public ChunkGenerator getChunkGenerator(String name){
        if (name.equals("VoidWorldGenerator")) {
            return new VoidWorldGenerator();
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
