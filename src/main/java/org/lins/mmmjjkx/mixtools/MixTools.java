package org.lins.mmmjjkx.mixtools;

import io.github.linsminecraftstudio.polymer.file.FileUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.lins.mmmjjkx.mixtools.commands.*;
import org.lins.mmmjjkx.mixtools.commands.economy.CMDBalance;
import org.lins.mmmjjkx.mixtools.commands.economy.CMDEconomy;
import org.lins.mmmjjkx.mixtools.commands.economy.CMDPay;
import org.lins.mmmjjkx.mixtools.commands.entityspawn.CMDLightning;
import org.lins.mmmjjkx.mixtools.commands.entityspawn.CMDTNT;
import org.lins.mmmjjkx.mixtools.commands.home.CMDDelhome;
import org.lins.mmmjjkx.mixtools.commands.home.CMDHome;
import org.lins.mmmjjkx.mixtools.commands.home.CMDSethome;
import org.lins.mmmjjkx.mixtools.commands.list.*;
import org.lins.mmmjjkx.mixtools.commands.speed.CMDAttackSpeed;
import org.lins.mmmjjkx.mixtools.commands.speed.CMDFlySpeed;
import org.lins.mmmjjkx.mixtools.commands.speed.CMDWalkSpeed;
import org.lins.mmmjjkx.mixtools.commands.teleport.*;
import org.lins.mmmjjkx.mixtools.commands.warp.CMDDelWarp;
import org.lins.mmmjjkx.mixtools.commands.warp.CMDSetWarp;
import org.lins.mmmjjkx.mixtools.commands.warp.CMDWarp;
import org.lins.mmmjjkx.mixtools.listeners.PlayerListener;
import org.lins.mmmjjkx.mixtools.listeners.ServerListener;
import org.lins.mmmjjkx.mixtools.listeners.SignListener;
import org.lins.mmmjjkx.mixtools.managers.HookManager;
import org.lins.mmmjjkx.mixtools.managers.MessageHandler;
import org.lins.mmmjjkx.mixtools.managers.SettingsManager;
import org.lins.mmmjjkx.mixtools.managers.data.DataManager;
import org.lins.mmmjjkx.mixtools.managers.features.SchedulerManager;
import org.lins.mmmjjkx.mixtools.managers.features.WarpManager;
import org.lins.mmmjjkx.mixtools.managers.features.kit.KitManager;
import org.lins.mmmjjkx.mixtools.managers.misc.MiscFeatureManager;

import java.io.File;

public final class MixTools extends JavaPlugin {
    public static MixTools INSTANCE;
    public static MessageHandler messageHandler;
    public static HookManager hookManager;
    private static DataManager dataManager;
    public static SettingsManager settingsManager;
    ///////////////features////////////
    public static MiscFeatureManager miscFeatureManager;
    public static SchedulerManager schedulerManager;
    public static KitManager kitManager;
    public static WarpManager warpManager;
    ///////////////////////////////////

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        saveResources();
        settingsManager = new SettingsManager();
        messageHandler = new MessageHandler();
        dataManager = new DataManager();
        hookManager = new HookManager();
        miscFeatureManager = new MiscFeatureManager();
        schedulerManager = new SchedulerManager();
        warpManager = new WarpManager();
        kitManager = new KitManager();
        registerCommands();
        registerListeners();
        new Metrics(this,17788);
        log("MixTools enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        schedulerManager.stopAllRunnable();
        log("MixTools disabled!");
    }

    private void registerCommands() {
        new CMDGamemode().register(this);
        new CMDKillAll().register(this);
        new CMDKill().register(this);
        new CMDSuicide().register(this);
        new CMDFly().register(this);
        new CMDWorkbench().register(this);
        new CMDSudo().register(this);
        new CMDItemName().register(this);
        new CMDItemLore().register(this);
        new CMDTrash().register(this);
        new CMDSethome().register(this);
        new CMDDelhome().register(this);
        new CMDCommandGroup().register(this);
        new CMDSetSpawn().register(this);
        new CMDSpawn().register(this);
        new CMDBack().register(this);
        new CMDHome().register(this);
        new CMDTPA().register(this);
        new CMDTPAAccept().register(this);
        new CMDTPARefuse().register(this);
        new CMDReload().register(this);
        new CMDEnderChest().register(this);
        new CMDTPAHere().register(this);
        new CMDTeleport().register(this);
        new CMDServerInfo().register(this);
        new CMDTPAll().register(this);
        new CMDFlySpeed().register(this);
        new CMDWalkSpeed().register(this);
        new CMDAttackSpeed().register(this);
        new CMDBalance().register(this);
        new CMDOPList().register(this);
        new CMDBanList().register(this);
        new CMDLightning().register(this);
        new CMDTNT().register(this);
        new CMDWorld().register(this);
        new CMDBroadcast().register(this);
        new CMDBurn().register(this);
        new CMDHeal().register(this);
        new CMDRepair().register(this);
        new CMDVoid().register(this);
        new CMDHomes().register(this);
        new CMDScheduler().register(this);
        new CMDTPAAll().register(this);
        new CMDEconomy().register(this);
        new CMDPay().register(this);
        new CMDRepairAll().register(this);
        new CMDKit().register(this);
        new CMDKits().register(this);
        new CMDSetWarp().register(this);
        new CMDWarp().register(this);
        new CMDDelWarp().register(this);
        new CMDWarps().register(this);
    }

    private void registerListeners() {
        new PlayerListener().register();
        new SignListener().register();
        new ServerListener().register();
    }

    private void saveResources() {
        String configVer = getConfig().getString("config-version","UNDEFINED");
        if (!configVer.equals("UNDEFINED")){
            FileUtils.completeFile(this, "config.yml", false);
            getConfig().set("config-version","3");
            saveDefaultConfig();
        }else {//has no config version
            File file = new File(getDataFolder(),"config.yml");
            if (file.exists()){
                getLogger().warning("Config version is not exists. The config.yml has rename to config-backup.yml!");
                file.renameTo(new File(getDataFolder(),"config-backup.yml"));
            }
            saveDefaultConfig();
        }
        FileUtils.completeLangFile(this, "lang/en-us.yml");
        FileUtils.completeLangFile(this, "lang/zh-cn.yml");
    }

    public void Reload(){
        saveResources();
        messageHandler = new MessageHandler();
        settingsManager = new SettingsManager();
        dataManager = new DataManager();
        kitManager = new KitManager();
        warpManager = new WarpManager();
        schedulerManager.reload();
        miscFeatureManager.reload();
    }

    public static DataManager getDataManager(){
        return dataManager;
    }

    public static void log(String message){INSTANCE.getLogger().info(message);}

    public static void warn(String message){INSTANCE.getLogger().warning(message);}
}
