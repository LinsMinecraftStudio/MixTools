package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixCommandExecutor;

public class CMDBack implements MixCommandExecutor {
    @Override
    public String name() {
        return "back";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (p != null){
                Location loc = MixTools.miscFeatureManager.getBackPlayerLocation(p);
                if (loc != null) {
                    p.teleport(MixTools.miscFeatureManager.getBackPlayerLocation(p));
                    sendMessage(p, "Command.Back");
                    return true;
                } else {
                    sendMessage(p, "Command.NoBackLocation");
                    return false;
                }
            }
        }
        return false;
    }
}
