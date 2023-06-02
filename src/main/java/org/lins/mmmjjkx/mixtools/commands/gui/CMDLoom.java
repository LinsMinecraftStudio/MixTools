package org.lins.mmmjjkx.mixtools.commands.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDLoom extends GUICMD{
    public CMDLoom(@NotNull String name) {
        super(name);
    }

    @Override
    public void openGUI(Player player) {
        player.openLoom(null, true);
    }

    @Override
    public void openGUI2(Player player, Player player2) {
        player2.openLoom(null, true);
    }
}
