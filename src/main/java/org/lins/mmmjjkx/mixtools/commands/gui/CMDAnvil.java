package org.lins.mmmjjkx.mixtools.commands.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMDAnvil extends GUICMD{
    public CMDAnvil(@NotNull String name) {
        super(name);
    }

    @Override
    public void openGUI(Player player) {
        player.openAnvil(null, true);
    }

    @Override
    public void openGUI2(Player player, Player player2) {
        player2.openAnvil(null, true);
    }
}
