package org.lins.mmmjjkx.mixtools.commands.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDSmithingTable extends GUICMD{
    public CMDSmithingTable(@NotNull String name) {
        super(name);
    }

    @Override
    public void openGUI(Player player) {
        player.openSmithingTable(null, true);
    }

    @Override
    public void openGUI2(Player player, Player player2) {
        player2.openSmithingTable(null, true);
    }
}
