package org.lins.mmmjjkx.mixtools;

import org.bukkit.plugin.java.JavaPlugin;
import org.lins.mmmjjkx.mixtools.managers.HookManager;
import org.lins.mmmjjkx.mixtools.managers.MessageHandler;

public final class MixTools extends JavaPlugin {
    public static MixTools INSTANCE;
    public static MessageHandler messageHandler;
    public static HookManager hookManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        saveDefaultConfig();
        messageHandler = new MessageHandler();
        hookManager = new HookManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void saveResources() {
        saveResource("data.yml",false);
        saveResource("lang/en-us.yml",false);
        saveResource("lang/zh-cn.yml",false);
    }
}
