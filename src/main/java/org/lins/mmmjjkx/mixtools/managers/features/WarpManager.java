package org.lins.mmmjjkx.mixtools.managers.features;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsWarp;
import org.lins.mmmjjkx.mixtools.utils.OtherUtil;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WarpManager {
    private final YamlConfiguration warpConfiguration = new YamlConfiguration();
    private final File cfgFile = new File(MixTools.INSTANCE.getDataFolder(), "warps.yml");
    private final List<MixToolsWarp> warps = new ArrayList<>();
    public WarpManager() {
        if (!cfgFile.exists()){
            try {cfgFile.createNewFile();
            } catch (IOException e) {throw new RuntimeException(e);}
        }
        try {warpConfiguration.load(cfgFile);
        } catch (Exception e) {throw new RuntimeException(e);}
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
        MixTools.warn("The warp " + name + " is not found.");
        return false;
    }
    @Nullable
    public MixToolsWarp getWarpByName(String name){
        Optional<MixToolsWarp> warp = OtherUtil.listGetIf(warps, w -> w.name().equals(name));
        return warp.orElse(null);
    }
}
