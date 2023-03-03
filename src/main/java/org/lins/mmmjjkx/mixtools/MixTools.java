package org.lins.mmmjjkx.mixtools;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;
import org.lins.mmmjjkx.mixtools.commands.*;
import org.lins.mmmjjkx.mixtools.managers.HookManager;
import org.lins.mmmjjkx.mixtools.managers.MessageHandler;
import org.lins.mmmjjkx.mixtools.managers.SettingsManager;
import org.lins.mmmjjkx.mixtools.managers.data.DataManager;
import org.lins.mmmjjkx.mixtools.managers.misc.CommandGroupManager;
import org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey;

import java.sql.SQLException;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.MYSQL_ENABLED;

public final class MixTools extends JavaPlugin {
    public static MixTools INSTANCE;
    ////////////////////////////////////////////////////////////////
    public static MessageHandler messageHandler;
    public static HookManager hookManager;
    public static DataManager dataManager;
    public static SettingsManager settingsManager;
    public static CommandGroupManager commandGroupManager;
    ////////////////////////////////////////////////////////////////
    private static HikariDataSource dataSource;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        saveResources();
        ////////////////////////////////
        settingsManager = new SettingsManager(getConfig());
        if (settingsManager.getBoolean(MYSQL_ENABLED)){
            dataSource = SettingsKey.getDataSource();
        }
        messageHandler = new MessageHandler();
        hookManager = new HookManager();
        commandGroupManager = new CommandGroupManager();
        try {dataManager = new DataManager();
        } catch (SQLException e) {throw new RuntimeException(e);}
        ////////////////////////////////
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
        new CMDSethome().register();
        new CMDDelhome().register();
        new CMDCommandGroup().register();
    }

    private void saveResources() {
        saveDefaultConfig();
        saveResource("commandGroup.yml",false);
        saveResource("lang/en-us.yml",false);
        saveResource("lang/zh-cn.yml",false);
    }
}
