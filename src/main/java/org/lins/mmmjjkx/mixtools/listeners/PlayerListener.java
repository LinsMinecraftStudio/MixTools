package org.lins.mmmjjkx.mixtools.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.hookaddon.MixToolsEconomy;
import org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey;
import org.lins.mmmjjkx.mixtools.objects.listener.MixToolsListener;

public class PlayerListener implements MixToolsListener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        MixToolsEconomy economy = MixTools.hookManager.getEconomy();
        economy.createPlayerAccount(p);
        String joinMessage = MixTools.messageHandler.colorize(getSettingString(SettingsKey.PLAYER_JOIN_MESSAGE).replaceAll("<player>",p.getName()));
        e.setJoinMessage(joinMessage);
        if (!p.hasPlayedBefore()){
            economy.depositPlayer(e.getPlayer(), MixTools.settingsManager.getInt(SettingsKey.INITIAL_CURRENCY));
            String welcomeMessage = MixTools.messageHandler.colorize(getSettingString(SettingsKey.PLAYER_WELCOME_MESSAGE).replaceAll("<player>",p.getName()));
            Bukkit.broadcastMessage(welcomeMessage);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        String quitMessage = MixTools.messageHandler.colorize(getSettingString(SettingsKey.PLAYER_QUIT_MESSAGE).replaceAll("<player>",p.getName()));
        e.setQuitMessage(quitMessage);
    }
}
