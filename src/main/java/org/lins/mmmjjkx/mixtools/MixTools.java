package org.lins.mmmjjkx.mixtools;

import org.bukkit.plugin.java.JavaPlugin;

public final class MixTools extends JavaPlugin {
    public static MixTools INSTANCE;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void saveResources() {
        saveResource("lang/en-us.yml",false);
        saveResource("lang/zh-cn.yml",false);
    }
}
