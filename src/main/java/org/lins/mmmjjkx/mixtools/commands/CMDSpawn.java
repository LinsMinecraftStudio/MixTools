package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixCommandExecutor;

import java.util.List;

public class CMDSpawn implements MixCommandExecutor {
    @Override
    public String name() {
        return "spawn";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public String usage() {
        return "/<command>";
    }

    @Override
    public List<String> aliases() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(hasPermission(sender)) {
            Player p = toPlayer(sender);
            if (p != null) {
                p.teleport(MixTools.settingsManager.getSpawnLocation());
                sendMessage(p, "Spawn.Teleported");
                return true;
            }
        }
        return false;
    }
}
