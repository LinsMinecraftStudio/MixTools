package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDFly implements MixTabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length==0){
            return getPlayerNames();
        }
        return null;
    }

    @Override
    public String name() {
        return "fly";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (hasPermission(sender)){
            Player p = toPlayer(sender);
            if (args.length==0) {
                if (p.getAllowFlight()){
                    p.setAllowFlight(false);
                    sendMessage(sender,"Fly.Disabled");
                }else {
                    p.setAllowFlight(true);
                    p.setFallDistance(0);
                    sendMessage(sender,"Fly.Enabled");
                }
                return true;
            }else if (args.length==1) {
                if (hasSubPermission(sender, "others")) {
                    Player p2 = findPlayer(p, args[0]);
                    if (p2 != null) {
                        if (p2.getAllowFlight()){
                            p2.setAllowFlight(false);
                            sendMessage(sender,"Fly.OthersDisabled",p2.getName());
                        }else {
                            p2.setAllowFlight(true);
                            p2.setFallDistance(0);
                            sendMessage(sender,"Fly.OthersEnabled",p2.getName());
                        }
                        return true;
                    }
                }
            }else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
