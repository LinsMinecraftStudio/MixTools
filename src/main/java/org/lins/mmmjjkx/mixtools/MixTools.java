package org.lins.mmmjjkx.mixtools;

import io.github.linsminecraftstudio.polymer.objects.PolymerMessageHandler;
import io.github.linsminecraftstudio.polymer.utils.FileUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import org.lins.mmmjjkx.mixtools.commands.*;
import org.lins.mmmjjkx.mixtools.commands.economy.CMDBalance;
import org.lins.mmmjjkx.mixtools.commands.economy.CMDEconomy;
import org.lins.mmmjjkx.mixtools.commands.economy.CMDPay;
import org.lins.mmmjjkx.mixtools.commands.entityspawn.CMDLightning;
import org.lins.mmmjjkx.mixtools.commands.entityspawn.CMDTNT;
import org.lins.mmmjjkx.mixtools.commands.gui.CMDAnvil;
import org.lins.mmmjjkx.mixtools.commands.gui.CMDEnderChest;
import org.lins.mmmjjkx.mixtools.commands.gui.CMDLoom;
import org.lins.mmmjjkx.mixtools.commands.gui.CMDWorkbench;
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
import org.lins.mmmjjkx.mixtools.managers.SettingsManager;
import org.lins.mmmjjkx.mixtools.managers.data.DataManager;
import org.lins.mmmjjkx.mixtools.managers.features.SchedulerManager;
import org.lins.mmmjjkx.mixtools.managers.features.setters.ScoreBoardSetter;
import org.lins.mmmjjkx.mixtools.managers.features.WarpManager;
import org.lins.mmmjjkx.mixtools.managers.features.kit.KitManager;
import org.lins.mmmjjkx.mixtools.managers.misc.MiscFeatureManager;

import java.util.List;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.SCOREBOARD_ENABLED;


public final class MixTools extends JavaPlugin {
    private static DataManager dataManager;
    public static MixTools INSTANCE;
    public static PolymerMessageHandler messageHandler;
    public static HookManager hookManager;
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
        messageHandler = new PolymerMessageHandler(this);
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
        if (schedulerManager != null) schedulerManager.stopAllRunnable();
        ScoreBoardSetter.stopTasks();
        log("MixTools disabled!");
    }

    private void registerCommands() {
        new CMDGamemode("gamemode", List.of("gm"));
        new CMDKillAll("killall");
        new CMDKill("kill");
        new CMDSuicide("suicide");
        new CMDFly("fly");
        new CMDWorkbench("workbench");
        new CMDSudo("sudo");
        new CMDItemName("itemname");
        new CMDItemLore("itemlore");
        new CMDTrash("trash");
        new CMDSethome("sethome");
        new CMDDelhome("delhome");
        new CMDCommandGroup("commandgroup", List.of("cg"));
        new CMDSetSpawn("setspawn");
        new CMDSpawn("spawn");
        new CMDBack("back");
        new CMDHome("home");
        new CMDTPA("tpa");
        new CMDTPAAccept("tpaaccept");
        new CMDTPARefuse("tparefuse");
        new CMDReload("mixtoolsreload");
        new CMDEnderChest("enderchest", List.of("ec"));
        new CMDTPAHere("tpahere");
        new CMDTeleport("teleport");
        new CMDServerInfo("serverinfo");
        new CMDTPAll("tpall");
        new CMDFlySpeed("flyspeed");
        new CMDWalkSpeed("walkspeed");
        new CMDAttackSpeed("attackspeed");
        new CMDBalance("balance");
        new CMDOPList("oplist");
        new CMDBanList("banlist");
        new CMDLightning("lightning");
        new CMDTNT("tnt");
        new CMDWorld("world");
        new CMDBroadcast("broadcast", List.of("bc"));
        new CMDBurn("burn");
        new CMDHeal("heal");
        new CMDRepair("repair");
        new CMDVoid("void");
        new CMDHomes("homes");
        new CMDScheduler("scheduler");
        new CMDTPAAll("tpaall");
        new CMDEconomy("economy");
        new CMDPay("pay");
        new CMDRepairAll("repairall");
        new CMDKit("kit");
        new CMDKits("kits");
        new CMDSetWarp("setwarp");
        new CMDWarp("warp");
        new CMDDelWarp("delwarp");
        new CMDWarps("warps");
        new CMDNick("nick");
        new CMDLoom("loom");
        new CMDAnvil("anvil");
    }

    private void registerListeners() {
        new PlayerListener();
        new SignListener();
        new ServerListener();
    }

    private void saveResources() {
        FileUtils.completeFile(this, "config.yml", false);
        FileUtils.completeLangFile(this, "lang/en-us.yml");
        FileUtils.completeLangFile(this, "lang/zh-cn.yml");
    }

    public void Reload(){
        saveResources();
        messageHandler = new PolymerMessageHandler(this);
        settingsManager = new SettingsManager();
        dataManager = new DataManager();
        kitManager = new KitManager();
        warpManager.reload();
        schedulerManager.reload();
        miscFeatureManager.reload();
        if (settingsManager.getBoolean(SCOREBOARD_ENABLED)) ScoreBoardSetter.restart();
    }

    public static DataManager getDataManager(){
        return dataManager;
    }

    public static void log(String message){INSTANCE.getLogger().info(message);}

    public static void warn(String message){INSTANCE.getLogger().warning(message);}
}
