package org.lins.mmmjjkx.mixtools;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;
import org.lins.mmmjjkx.mixtools.commands.*;
import org.lins.mmmjjkx.mixtools.managers.HookManager;
import org.lins.mmmjjkx.mixtools.managers.MessageHandler;
import org.lins.mmmjjkx.mixtools.managers.config.FileDataManager;
import org.lins.mmmjjkx.mixtools.managers.config.SettingsManager;
import org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.MYSQL_ENABLED;

public final class MixTools extends JavaPlugin {
    public static MixTools INSTANCE;
    public static MessageHandler messageHandler;
    public static HookManager hookManager;
    public static FileDataManager dataManager;
    public static SettingsManager settingsManager;
    private static HikariDataSource dataSource;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        saveResources();
        settingsManager = new SettingsManager(getConfig());
        if (settingsManager.getBoolean(MYSQL_ENABLED)){
            dataSource = SettingsKey.getDataSource();
        }
        messageHandler = new MessageHandler();
        hookManager = new HookManager();
        dataManager = new FileDataManager();
        registerCommands();
        getLogger().info("MixTools enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        dataSource.close();
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
        new CMDFurnace().register();
        new CMDTrash().register();
    }

    private void saveResources() {
        saveDefaultConfig();
        saveResource("lang/en-us.yml",false);
        saveResource("lang/zh-cn.yml",false);
    }
}
