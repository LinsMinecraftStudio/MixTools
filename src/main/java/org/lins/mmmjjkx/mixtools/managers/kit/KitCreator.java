package org.lins.mmmjjkx.mixtools.managers.kit;

import org.bukkit.Bukkit;
import org.bukkit.Warning;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.lins.mmmjjkx.mixtools.MixTools;

public class KitCreator implements Listener {
    private Player player;
    private String kitName;

    public KitCreator(Player p, String kitName) {
        this.player = p;
        this.kitName = kitName;
        Bukkit.getPluginManager().registerEvents(this, MixTools.INSTANCE);
    }

    @EventHandler
    public void onCreate(InventoryCloseEvent e){
        Inventory inventory = e.getInventory();
    }
}
