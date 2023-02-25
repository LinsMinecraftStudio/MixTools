package org.lins.mmmjjkx.mixtools;

import org.bukkit.plugin.java.JavaPlugin;
import org.lins.mmmjjkx.mixtools.commands.*;
import org.lins.mmmjjkx.mixtools.managers.HookManager;
import org.lins.mmmjjkx.mixtools.managers.MessageHandler;
import org.lins.mmmjjkx.mixtools.managers.config.DataManager;
import org.lins.mmmjjkx.mixtools.managers.config.SettingsManager;

public final class MixTools extends JavaPlugin {
    public static MixTools INSTANCE;
    public static MessageHandler messageHandler;
    public static HookManager hookManager;
    public static DataManager dataManager;
    public static SettingsManager settingsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        saveResources();
        messageHandler = new MessageHandler();
        hookManager = new HookManager();
        dataManager = new DataManager();
        settingsManager = new SettingsManager(getConfig());
        registerCommands();
        getLogger().info("MixTools enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("MixTools disabled!");
    }

    private void registerCommands() {
        new CMDGamemode().register();
        new CMDKillAll().register();
        new CMDKill().register();
        new CMDSuicide().register();
        new CMDFly().register();
        new CMDAnvil().register();
        new CMDWorkbench().register();
        new CMDSudo().register();
        new CMDItemName().register();
        new CMDItemLore().register();
    }

    private void saveResources() {
        saveDefaultConfig();
        saveResource("lang/en-us.yml",false);
        saveResource("lang/zh-cn.yml",false);
    }
}
