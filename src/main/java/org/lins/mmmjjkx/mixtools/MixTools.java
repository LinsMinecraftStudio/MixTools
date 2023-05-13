package org.lins.mmmjjkx.mixtools;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
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
import org.lins.mmmjjkx.mixtools.commands.list.CMDHomes;
import org.lins.mmmjjkx.mixtools.commands.home.CMDSethome;
import org.lins.mmmjjkx.mixtools.commands.list.CMDBanList;
import org.lins.mmmjjkx.mixtools.commands.list.CMDKits;
import org.lins.mmmjjkx.mixtools.commands.list.CMDOPList;
import org.lins.mmmjjkx.mixtools.commands.speed.CMDAttackSpeed;
import org.lins.mmmjjkx.mixtools.commands.speed.CMDFlySpeed;
import org.lins.mmmjjkx.mixtools.commands.speed.CMDWalkSpeed;
import org.lins.mmmjjkx.mixtools.commands.teleport.*;
import org.lins.mmmjjkx.mixtools.listeners.*;
import org.lins.mmmjjkx.mixtools.managers.*;
import org.lins.mmmjjkx.mixtools.managers.data.DataManager;
import org.lins.mmmjjkx.mixtools.managers.features.SchedulerManager;
import org.lins.mmmjjkx.mixtools.managers.features.WarpManager;
import org.lins.mmmjjkx.mixtools.managers.features.kit.KitManager;
import org.lins.mmmjjkx.mixtools.managers.misc.MiscFeatureManager;
import org.lins.mmmjjkx.mixtools.utils.FileUtils;

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
    public static BukkitAudiences adventure;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        saveResources();
        settingsManager = new SettingsManager();
        messageHandler = new MessageHandler();
        dataManager = new DataManager();
        hookManager = new HookManager();
        adventure = BukkitAudiences.create(this);
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
        new CMDAttackSpeed().register();
        new CMDBalance().register();
        new CMDOPList().register();
        new CMDBanList().register();
        new CMDLightning().register();
        new CMDTNT().register();
        new CMDWorld().register();
        new CMDBroadcast().register();
        new CMDBurn().register();
        new CMDHeal().register();
        new CMDRepair().register();
        new CMDVoid().register();
        new CMDHomes().register();
        new CMDScheduler().register();
        new CMDTPAAll().register();
        new CMDEconomy().register();
        new CMDPay().register();
        new CMDRepairAll().register();
        new CMDKit().register();
        new CMDKits().register();
    }

    private void registerListeners() {
        new PlayerListener().register();
        new SignListener().register();
    }

    private void saveResources() {
        String configVer = getConfig().getString("config-version","UNDEFINED");
        if (!configVer.equals("UNDEFINED")){
            FileUtils.completingFile("config.yml", false);
            getConfig().set("config-version","2");
            saveDefaultConfig();
        }else {//has no config version
            File file = new File(getDataFolder(),"config.yml");
            if (file.exists()){
                getLogger().warning("Config version is not exists. The config.yml has rename to config-backup.yml!");
                file.renameTo(new File(getDataFolder(),"config-backup.yml"));
            }
            saveDefaultConfig();
        }
        FileUtils.completingLangFile("lang/en-us.yml");
        FileUtils.completingLangFile("lang/zh-cn.yml");
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
