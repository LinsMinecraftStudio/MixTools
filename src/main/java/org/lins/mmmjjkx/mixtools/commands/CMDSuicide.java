package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.objects.command.MixCommandExecutor;

import java.util.List;

public class CMDSuicide implements MixCommandExecutor {
    @Override
    public String name() {
        return "suicide";
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (p!=null){
                p.setHealth(0);
                sendMessage(p,"Command.Suicide");
                broadcastMessage("Command.Suicide2",p.getName());
                return true;
            }
        }
        return false;
    }
}
