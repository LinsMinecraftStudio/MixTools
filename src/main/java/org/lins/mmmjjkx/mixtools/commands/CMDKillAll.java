package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.objects.command.MixCommandExecutor;

public class CMDKillAll implements MixCommandExecutor {
    @Override
    public String name() {
        return "killall";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (hasPermission(sender)){
            for (Player p: Bukkit.getOnlinePlayers()){
                p.setHealth(0);
            }
            broadcastMessage("Command.KillAll",sender.getName());
            return true;
        }
        return false;
    }
}
