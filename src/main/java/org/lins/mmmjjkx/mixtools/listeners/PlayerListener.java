package org.lins.mmmjjkx.mixtools.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.hookaddon.MixToolsEconomy;
import org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey;

public class PlayerListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        MixToolsEconomy economy = MixTools.hookManager.getEconomy();
        economy.createPlayerAccount(e.getPlayer());
        if (!e.getPlayer().hasPlayedBefore()){
            economy.depositPlayer(e.getPlayer(), MixTools.settingsManager.getInt(SettingsKey.INITIAL_CURRENCY));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){

    }
}
