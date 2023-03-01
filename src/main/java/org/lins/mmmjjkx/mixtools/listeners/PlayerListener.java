package org.lins.mmmjjkx.mixtools.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.hookaddon.MixToolsEconomy;
import org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey;
import org.lins.mmmjjkx.mixtools.objects.listener.MixToolsListener;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.*;

public class PlayerListener implements MixToolsListener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        MixToolsEconomy economy = MixTools.hookManager.getEconomy();
        economy.createPlayerAccount(p);
        e.setJoinMessage(getPlayerMessage(p, PLAYER_JOIN_MESSAGE));
        if (!p.hasPlayedBefore()){
            economy.depositPlayer(e.getPlayer(), MixTools.settingsManager.getInt(SettingsKey.INITIAL_CURRENCY));
            Bukkit.broadcastMessage(getPlayerMessage(p, PLAYER_WELCOME_MESSAGE));
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        e.setQuitMessage(getPlayerMessage(p, PLAYER_QUIT_MESSAGE));
    }

    @EventHandler
    public void closeTrashBin(InventoryCloseEvent e){
        String title = getMessageHandler().getColored("GUI.TrashBin");
        if (e.getView().getTitle().equals(title)){
            e.getPlayer().sendMessage(getMessageHandler().getColored("GUI.CloseTrashBin"));
        }
    }

    private String getPlayerMessage(Player p,String key){
        String str = getSettingString(key);
        if (MixTools.hookManager.checkPAPIInstalled()) {
            return PlaceholderAPI.setPlaceholders(p, str);
        }else {
            str = str.replaceAll("<player>",p.getName());
        }
        return MixTools.messageHandler.colorize(str);
    }
}
