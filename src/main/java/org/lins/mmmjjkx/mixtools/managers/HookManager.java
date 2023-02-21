package org.lins.mmmjjkx.mixtools.managers;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.logging.Logger;

public class HookManager {
    private final Logger log = MixTools.INSTANCE.getLogger();
    public HookManager(){
        setup();
    }

    private void setup(){
        if (checkPAPIInstalled()){
            new MixToolsPAPIAddon().register();
            log.info("Hooked into PlaceholderAPI.");
        }
        if (checkVaultInstalled()){

        }
    }

    public boolean checkPAPIInstalled(){
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public boolean checkVaultInstalled() {
        return Bukkit.getPluginManager().isPluginEnabled("Vault");
    }

    public String setPlaceHolders(Player p, String s){
        if (checkPAPIInstalled()){
            return PlaceholderAPI.setPlaceholders(p,s);
        }else {
            log.warning("PlaceholderAPI is not installed. You can't parse placeholders!");
            return s;
        }
    }
}
