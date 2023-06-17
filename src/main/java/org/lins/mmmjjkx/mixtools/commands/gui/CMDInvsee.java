package org.lins.mmmjjkx.mixtools.commands.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
        player.openInventory(player2.getInventory());
    }
}
