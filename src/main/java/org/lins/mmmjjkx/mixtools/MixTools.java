package org.lins.mmmjjkx.mixtools;

import com.zaxxer.hikari.HikariDataSource;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.lins.mmmjjkx.mixtools.commands.*;
import org.lins.mmmjjkx.mixtools.listeners.PlayerListener;
import org.lins.mmmjjkx.mixtools.listeners.WorldListener;
import org.lins.mmmjjkx.mixtools.managers.HookManager;
import org.lins.mmmjjkx.mixtools.managers.MessageHandler;
import org.lins.mmmjjkx.mixtools.managers.SettingsManager;
import org.lins.mmmjjkx.mixtools.managers.data.DataManager;
import org.lins.mmmjjkx.mixtools.managers.misc.MiscFeatureManager;
import org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey;
import org.lins.mmmjjkx.mixtools.utils.FilesCompletion;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.MYSQL_ENABLED;

public final class MixTools extends JavaPlugin {
    public static MixTools INSTANCE;
    public static MessageHandler messageHandler;
    public static HookManager hookManager;
    private static DataManager dataManager;
    public static SettingsManager settingsManager;
    public static MiscFeatureManager miscFeatureManager;
    public static BukkitAudiences adventure;
    private HikariDataSource dataSource;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        saveResources();
        settingsManager = new SettingsManager();
        if (settingsManager.getBoolean(MYSQL_ENABLED)){
            dataSource = SettingsKey.getDataSource();
        }
        messageHandler = new MessageHandler();
        dataManager = new DataManager();
        hookManager = new HookManager();
        miscFeatureManager = new MiscFeatureManager();
        adventure = BukkitAudiences.create(this);
        registerCommands();
        registerListeners();
        new Metrics(this,17788);
        getLogger().info("MixTools enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (dataSource != null) {
            dataSource.close();
        }
        getLogger().info("MixTools disabled!");
    }
    private void registerCommands() {
        new CMDGamemode().register();
        new CMDKillAll().register();
        new CMDKill().register();
        new CMDSuicide().register();
        new CMDFly().register();
        new CMDWorkbench().register();
        new CMDSudo().register();
        new CMDItemName().register();
        new CMDItemLore().register();
        new CMDTrash().register();
        new CMDSethome().register();
        new CMDDelhome().register();
        new CMDCommandGroup().register();
        new CMDSetSpawn().register();
        new CMDSpawn().register();
        new CMDBack().register();
        new CMDHome().register();
        new CMDTPA().register();
        new CMDTPAAccept().register();
        new CMDTPARefuse().register();
        new CMDReload().register();
        new CMDEnderChest().register();
        new CMDTPAHere().register();
        new CMDTeleport().register();
        new CMDServerInfo().register();
        new CMDTPAll().register();
        new CMDFlySpeed().register();
        new CMDWalkSpeed().register();
        new CMDBalance().register();
        new CMDOPList().register();
        new CMDBanList().register();
        new CMDLightning().register();
        new CMDTNT().register();
        new CMDWorld().register();
    }

    private void registerListeners() {
        new PlayerListener().register();
        new WorldListener().register();
    }

    private void saveResources() {
        FilesCompletion.completingFile("config.yml", false);
        FilesCompletion.completingFile("commandGroup.yml", true);
        FilesCompletion.completingFile("lang/en-us.yml", false);
        FilesCompletion.completingFile("lang/zh-cn.yml", false);
    }

    public void Reload(){
        if (settingsManager.getBoolean(MYSQL_ENABLED)) {
            dataSource = SettingsKey.getDataSource();
        }
        saveResources();
        messageHandler = new MessageHandler();
        settingsManager = new SettingsManager();
        dataManager = new DataManager();
    }

    public static DataManager getDataManager(){
        return dataManager;
    }
}
