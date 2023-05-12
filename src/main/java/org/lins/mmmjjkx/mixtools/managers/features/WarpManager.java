package org.lins.mmmjjkx.mixtools.managers.features;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsWarp;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WarpManager {
    private final YamlConfiguration warpConfiguration = new YamlConfiguration();
    private final File cfgFile = new File(MixTools.INSTANCE.getDataFolder(), "warps.yml");
    private final List<MixToolsWarp> warps = new ArrayList<>();
    public WarpManager() {
        try {warpConfiguration.load(cfgFile);
        } catch (Exception e) {e.printStackTrace();}
    }
    public List<MixToolsWarp> getWarps() {
        return warps;
    }
    public void addWarp(String name, Location location, String owner){
        ConfigurationSection section = warpConfiguration.createSection(name);
        section.set("name", name);
        section.set("owner", owner);
        ConfigurationSection locationSection = section.createSection("location");
        locationSection.set("world", location);
        locationSection.set("x", location.getX());
        locationSection.set("y", location.getY());
        locationSection.set("z", location.getZ());
        try {warpConfiguration.load(cfgFile);
        }catch (Exception e) {e.printStackTrace();}
        warps.add(new MixToolsWarp(name, location, owner));
    }
    public void removeWarp(String name){
        if (warps.removeIf(warp -> warp.name().equals(name))){
            warpConfiguration.set(name, null);
            return;
        }
        MixTools.warn("The warp " + name + " is not found.");
    }
    @Nullable
    public MixToolsWarp getWarpByName(String name){
        for (MixToolsWarp warp : warps){
            if (warp.name().equals(name)){
                return warp;
            }
        }
        return null;
    }
}
