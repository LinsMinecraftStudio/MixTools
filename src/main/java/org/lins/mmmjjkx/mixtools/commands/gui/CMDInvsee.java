package org.lins.mmmjjkx.mixtools.commands.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.utils.InventoryFactory;

public class CMDInvsee extends GUICMD{
    public CMDInvsee(@NotNull String name) {
        super(name);
    }

    @Override
    public void openGUI(Player player) {
        sendMessage(player, "Command.ArgError");
    }

    @Override
    public void openGUI2(Player player, Player player2) {
        Inventory inventory = InventoryFactory.createDefaultStyleInventory(
                MixTools.messageHandler.getColored("GUI.InvseeTitle", player2.getName()),true);
        player.openInventory(player2.getInventory());
    }
}
