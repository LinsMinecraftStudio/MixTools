package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixCommandExecutor;

public class CMDSetSpawn implements MixCommandExecutor {
    @Override
    public String name() {
        return "setspawn";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)) {
            Player p = toPlayer(sender);
            if (p != null) {
                Location loc = p.getLocation();
                MixTools.settingsManager.setSpawn(loc);
                sendMessage(sender,"Spawn.Set",loc.getX(),loc.getY(),loc.getZ(),loc.getPitch(),loc.getYaw());
                return true;
            }
        }
        return false;
    }
}
