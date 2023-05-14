package org.lins.mmmjjkx.mixtools.api;

import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.lins.mmmjjkx.mixtools.MixTools;

public abstract class MixToolsAddon extends JavaPlugin {
    @Override
    public void onLoad(){
        register();
    }

    @Override
    public void onEnable() {
        MixTools.INSTANCE.getLogger().info("Loaded addon "+getDescription().getName()+" version "+getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        MixTools.INSTANCE.getLogger().info("Disabling addon "+getDescription().getName()+" version "+getDescription().getVersion());
    }

    private void register(){
        if (verIsAtLeast(requiredVersion())) {
            MixTools.api.registerAddon(this);
        }else {
            MixTools.warn("Addon "+getDescription().getName()+" requires MixTools version "+requiredVersion() + " or higher, " +
                    "but the MixTools version is "+MixTools.INSTANCE.getDescription().getVersion()+" .");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    private boolean verIsAtLeast(String version) {
        String formatted_version = version.replaceAll("\\.","").replaceAll("-SNAPSHOW","");
        int ver_worth = Integer.parseInt(formatted_version);
        return ver_worth >= MixToolsAPI.VERSION_WORTH;
    }

    /**
     * Register chunk generator(s) into MixTools.
     * Can be overridden.
     * @param worldName the name of the world
     * @param generatorName the input
     * @return a chunk generator
     */
    public ChunkGenerator addGenerator(String worldName, String generatorName) {
        return null;
    }

    public MixToolsAPI.Managers getManagers() {
        return MixToolsAPI.managers;
    }

    public abstract String requiredVersion();
}
