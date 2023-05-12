package org.lins.mmmjjkx.mixtools.managers;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.hookaddon.MixToolsEconomy;
import org.lins.mmmjjkx.mixtools.managers.hookaddon.MixToolsPAPIAddon;

import java.util.logging.Logger;

public class HookManager {
    private final Logger log = MixTools.INSTANCE.getLogger();
    private MixToolsEconomy economy;
    public HookManager(){
        setup();
    }

    private void setup(){
        if (checkPAPIInstalled()){
            new MixToolsPAPIAddon().register();
            log.info("Hooked into PlaceholderAPI.");
        }else {
            log.warning("PlaceholderAPI is not installed. You can't parse placeholders!");
        }
        if (checkVaultInstalled()){
            economy = new MixToolsEconomy();
            Bukkit.getServicesManager().register(Economy.class,economy,MixTools.INSTANCE,ServicePriority.High);
            log.info("Hooked into Vault.");
        }else {
            log.warning("Vault is not installed. You can't use commands about economy!");
        }
    }

    public boolean checkPAPIInstalled(){
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }
    public boolean checkVaultInstalled() {
        return Bukkit.getPluginManager().isPluginEnabled("Vault");
    }

    public MixToolsEconomy getEconomy(){
        return economy;
    }
}
