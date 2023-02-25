package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDKill implements MixTabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length==0){
            return getPlayerNames();
        }
        return null;
    }

    @Override
    public String name() {
        return "kill";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public String usage() {
        return "/<command> [player]";
    }

    @Override
    public List<String> aliases() {
        return new ArrayList<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (args.length==0) {
                if (p != null) {
                    p.setHealth(0);
                    return true;
                }else {
                    sendMessage(sender,"Command.SpecifyPlayer");
                    return false;
                }
            } else if (args.length==1) {
                Player p2 = findPlayer(p, args[0]);
                if (p2 != null) {
                    p2.setHealth(0);
                    sendMessage(sender,"Command.Kill",p2.getName());
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
