package org.lins.mmmjjkx.mixtools.managers.features;

import io.github.linsminecraftstudio.polymer.objects.plugin.AbstractFeatureManager;
import io.github.linsminecraftstudio.polymer.utils.ListUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsWarp;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WarpManager extends AbstractFeatureManager {
    private YamlConfiguration warpConfiguration;
    private final File cfgFile = new File(MixTools.getInstance().getDataFolder(), "warps.yml");
    private final List<MixToolsWarp> warps = new ArrayList<>();
    public WarpManager() {
        super(MixTools.getInstance());
        warpConfiguration = handleConfig("warps.yml");
        loadWarps();
    }

    @Override
    public void reload() {
        warpConfiguration = handleConfig("warps.yml");
    }

    public void loadWarps() {
        for (String key : warpConfiguration.getKeys(false)){
            ConfigurationSection section = warpConfiguration.getConfigurationSection(key);
            if (section==null) continue;
            ConfigurationSection locationSection = section.getConfigurationSection("location");
            if (locationSection==null) continue;
            Location location = new Location(Bukkit.getWorld(locationSection.getString("world","")), locationSection.getDouble("x"),
                    locationSection.getDouble("y"), locationSection.getDouble("z"),
                    (float) locationSection.getDouble("yaw"), (float) locationSection.getDouble("pitch"));
            warps.add(new MixToolsWarp(key, location, section.getString("owner")));
        }
    }
    public List<MixToolsWarp> getWarps() {
        return warps;
    }
    public void addWarp(String name, Location location, String owner){
        ConfigurationSection section = warpConfiguration.createSection(name);
        section.set("owner", owner);
        ConfigurationSection locationSection = section.createSection("location");
        locationSection.set("world", location);
        locationSection.set("x", location.getX());
        locationSection.set("y", location.getY());
        locationSection.set("z", location.getZ());
        locationSection.set("pitch", location.getPitch());
        locationSection.set("yaw", location.getYaw());
        try {warpConfiguration.load(cfgFile);
        }catch (Exception e) {throw new RuntimeException(e);}
        warps.add(new MixToolsWarp(name, location, owner));
    }
    public boolean removeWarp(String name){
        if (warps.removeIf(warp -> warp.name().equals(name))){
            warpConfiguration.set(name, null);
            try {warpConfiguration.save(cfgFile);
            } catch (IOException e) {throw new RuntimeException(e);}
            return true;
        }
        return false;
    }
    public List<String> getWarpNames() {
        List<String> names = new ArrayList<>();
        for (MixToolsWarp war : warps){
            names.add(war.name());
        }
        return names;
    }
    @Nullable
    public MixToolsWarp getWarpByName(String name){
        return ListUtil.getIf(warps, w -> w.name().equals(name)).orElse(null);
    }
}
