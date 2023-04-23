package org.lins.mmmjjkx.mixtools.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.hookaddon.MixToolsEconomy;
import org.lins.mmmjjkx.mixtools.objects.listener.MixToolsListener;
import org.lins.mmmjjkx.mixtools.utils.MixStringUtil;

import java.util.List;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.*;

public class PlayerListener implements MixToolsListener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if (MixTools.settingsManager.getBoolean(NAME_CHECK)){
            String name = p.getName();
            if (!MixStringUtil.matchNameRegex(name)) {
                p.kickPlayer(getMessageHandler().getColored("Value.NoMatchNameRegex"));
                return;
            }
        }
        MixToolsEconomy economy = MixTools.hookManager.getEconomy();
        economy.createPlayerAccount(p);
        e.setJoinMessage(getPlayerMessage(p, PLAYER_JOIN_MESSAGE));
        if (!p.hasPlayedBefore()){
            economy.depositPlayer(e.getPlayer(), MixTools.settingsManager.getInt(INITIAL_CURRENCY));
            Bukkit.broadcastMessage(getPlayerMessage(p, PLAYER_WELCOME_MESSAGE));
        }
        if (!p.isOp()){
            List<String> fixedOps = MixTools.settingsManager.getStrList(FIXED_OPERATORS);
            if (!fixedOps.isEmpty()){
                if (fixedOps.contains(p.getName())) {
                    p.setOp(true);
                }
            }
        }
        checkVersion(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        e.setQuitMessage(getPlayerMessage(p, PLAYER_QUIT_MESSAGE));
    }

    @EventHandler
    public void openTrashBin(InventoryOpenEvent e){
        String title = getMessageHandler().getColored("GUI.TrashBin");
        if (e.getView().getTitle().equals(title)){
            getMessageHandler().sendMessage(e.getPlayer(),"GUI.OpenTrashBin");
        }
    }

    @EventHandler
    public void closeTrashBin(InventoryCloseEvent e){
        String title = getMessageHandler().getColored("GUI.TrashBin");
        if (e.getView().getTitle().equals(title)){
            getMessageHandler().sendMessage(e.getPlayer(),"GUI.CloseTrashBin");
        }
    }

    @EventHandler
    public void clickTrashBin(InventoryClickEvent e){
        String title = getMessageHandler().getColored("GUI.TrashBin");
        if (e.getView().getTitle().equals(title)) {
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
        if (MixTools.settingsManager.getSpawnLocation()!=null){
            e.getPlayer().teleport(MixTools.settingsManager.getSpawnLocation());
        }
    }

    @EventHandler
    public void onOp(PlayerCommandPreprocessEvent e){
        if (MixTools.settingsManager.getBoolean(OPERATOR_LOCK_ENABLED)) {
            String[] cmdSplit = e.getMessage().split(" ");
            String prefix = cmdSplit[0].replace("/", "");
            if (cmdSplit.length != 2) return;
            String player = cmdSplit[1];
            if (prefix.equalsIgnoreCase("op") & !player.isBlank()) {
                List<String> fixedOps = MixTools.settingsManager.getStrList(FIXED_OPERATORS);
                if (!fixedOps.isEmpty()){
                    if (!fixedOps.contains(player)) {
                        getMessageHandler().sendMessage(e.getPlayer(), "Command.UnableToGetOp");
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeOp(PlayerCommandPreprocessEvent e){
        if (MixTools.settingsManager.getBoolean(OPERATOR_LOCK_ENABLED)) {
            String[] cmdSplit = e.getMessage().split(" ");
            String prefix = cmdSplit[0].replace("/", "");
            if (cmdSplit.length != 2) return;
            String player = cmdSplit[1];
            if (prefix.equalsIgnoreCase("deop") & !player.isBlank()) {
                List<String> fixedOps = MixTools.settingsManager.getStrList(FIXED_OPERATORS);
                if (!fixedOps.isEmpty()){
                    if (!fixedOps.contains(player)) {
                        getMessageHandler().sendMessage(e.getPlayer(), "Command.UnableToDeOp");
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    private String getPlayerMessage(Player p,String key){
        String str = getSettingString(key);
        if (MixTools.hookManager.checkPAPIInstalled()) {
            str = PlaceholderAPI.setPlaceholders(p, str);
        }
        str = str.replaceAll("%player%",p.getName());
        return MixTools.messageHandler.colorize(str);
    }

    private void checkVersion(Player p){
        if (MixTools.settingsManager.getBoolean(CHECK_UPDATE) & p.isOp()) {
            String latest = "";
            try {latest = MixStringUtil.getPluginLatestVersion();
            }catch (Exception ignored){}
            String ver = MixTools.INSTANCE.getDescription().getVersion();
            if (latest.equals("")){
                getMessageHandler().sendMessage(p, "Check-Update.Failed");
            } else if (ver.equals(latest)) {
                getMessageHandler().sendMessage(p, "Check-Update.UsingLatestVersion");
            } else {
                getMessageHandler().sendMessage(p, "Check-Update.Line1");
                getMessageHandler().sendMessage(p, "Check-Update.Line2", ver, latest);
                TextComponent component = LegacyComponentSerializer.legacyAmpersand().
                        deserialize(getMessageHandler().getColored("Check-Update.GetVer"));
                component = component.clickEvent(ClickEvent.openUrl("https://www.spigotmc.org/resources/mixtools-an-essentials-plugin.109130/"));
                MixTools.adventure.player(p).sendMessage(component);
            }
            getMessageHandler().sendMessage(p, "Check-Update.Line3");
        }
    }
}
