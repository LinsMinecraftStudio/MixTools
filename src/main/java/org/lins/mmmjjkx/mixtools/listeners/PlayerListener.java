package org.lins.mmmjjkx.mixtools.listeners;

import io.github.linsminecraftstudio.polymer.utils.OtherUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.setters.ScoreBoardSetter;
import org.lins.mmmjjkx.mixtools.managers.hookaddon.MixToolsEconomy;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixToolsListener;
import org.lins.mmmjjkx.mixtools.utils.MTUtils;

import java.util.List;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.*;

public class PlayerListener implements MixToolsListener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if (MixTools.settingsManager.getBoolean(NAME_CHECK)){
            String name = p.getName();
            if (!MTUtils.matchNameRegex(name)) {
                p.kick(getMessageHandler().getColored("Value.NoMatchNameRegex"));
                return;
            }
        }
        boolean vaultInstalled = MixTools.hookManager.checkVaultInstalled();
        if (vaultInstalled) {
            MixToolsEconomy economy = MixTools.hookManager.getEconomy();
            economy.createPlayerAccount(p);
        }
        e.joinMessage(getPlayerMessage(p, PLAYER_JOIN_MESSAGE));
        if (!p.hasPlayedBefore()){
            if (vaultInstalled) {
                MixToolsEconomy economy = MixTools.hookManager.getEconomy();
                economy.depositPlayer(e.getPlayer(), MixTools.settingsManager.getInt(INITIAL_CURRENCY));
                Bukkit.broadcast(getPlayerMessage(p, PLAYER_WELCOME_MESSAGE));
            }
        }
        //ScoreBoardSetter.addPlayer(p);
        checkVersion(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        //ScoreBoardSetter.removePlayer(p);
        e.quitMessage(getPlayerMessage(p, PLAYER_QUIT_MESSAGE));
    }

    @EventHandler
    public void openTrashBin(InventoryOpenEvent e){
        Component title = getMessageHandler().getColored("GUI.TrashBin");
        if (e.getView().title().equals(title)){
            getMessageHandler().sendMessage(e.getPlayer(),"GUI.OpenTrashBin");
        }
    }

    @EventHandler
    public void closeTrashBin(InventoryCloseEvent e){
        Component title = getMessageHandler().getColored("GUI.TrashBin");
        if (e.getView().title().equals(title)){
            getMessageHandler().sendMessage(e.getPlayer(),"GUI.CloseTrashBin");
        }
    }

    @EventHandler
    public void clickTrashBin(InventoryClickEvent e){
        Component title = getMessageHandler().getColored("GUI.TrashBin");
        if (e.getView().title().equals(title)) {
            List<Integer> slots = MixTools.settingsManager.getIntList(TRASH_PUT_ITEM_SLOTS);
            if (MixTools.settingsManager.getBoolean(TRASH_CLOSE_BUTTON_ENABLED)) {
                if (e.getRawSlot() == MixTools.settingsManager.getInt(TRASH_CLOSE_BUTTON_SLOT)) {
                    e.getWhoClicked().closeInventory();
                }
            }
            if (slots.contains(e.getRawSlot())){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        MixTools.miscFeatureManager.addBackPlayer(e.getEntity(),e.getEntity().getLocation());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        Location from = e.getFrom();
        MixTools.miscFeatureManager.addBackPlayer(e.getPlayer(),from);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        if (MixTools.settingsManager.getBoolean("respawnOnSpawnLocation")) {
            Location location = MixTools.settingsManager.getLocation("spawn");
            if (location != null) {
                e.getPlayer().teleport(location);
            }
        }
    }

    private Component getPlayerMessage(Player p, String key){
        String str = MixTools.settingsManager.getString(key);
        if (MixTools.hookManager.checkPAPIInstalled()) {
            str = PlaceholderAPI.setPlaceholders(p, str);
        }
        str = str.replaceAll("%player%",p.getName());
        return MixTools.messageHandler.colorize(str);
    }

    private void checkVersion(Player p){
        if (MixTools.settingsManager.getBoolean(CHECK_UPDATE) & p.isOp()) {
            String latest = OtherUtils.getPluginLatestVersion("109130");
            String ver = MixTools.getInstance().getPluginMeta().getVersion();
            if (latest.equals("")){
                getMessageHandler().sendMessage(p, "Check-Update.Failed");
            } else if (ver.equals(latest)) {
                getMessageHandler().sendMessage(p, "Check-Update.UsingLatestVersion");
            } else {
                getMessageHandler().sendMessage(p, "Check-Update.Line1");
                getMessageHandler().sendMessage(p, "Check-Update.Line2", ver, latest);
                Component component = getMessageHandler().getColored("Check-Update.GetVer");
                component = component.clickEvent(ClickEvent.openUrl("https://www.spigotmc.org/resources/109130/"));
                p.sendMessage(component);
            }
            getMessageHandler().sendMessage(p, "Check-Update.Line3");
        }
    }
}
