package org.lins.mmmjjkx.mixtools.commands.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CMDEnderChest extends GUICMD {

    public CMDEnderChest(@NotNull String name, List<String> aliases) {
        super(name, aliases);
    }

    @Override
    public void openGUI(Player player) {
        player.openInventory(player.getEnderChest());
    }

    @Override
    public void openGUI2(Player player, Player player2) {
        player.openInventory(player2.getEnderChest());
    }
}
