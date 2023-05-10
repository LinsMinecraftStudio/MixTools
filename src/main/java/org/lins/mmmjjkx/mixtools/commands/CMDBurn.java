package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CMDBurn implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],getPlayerNames());
        }else if (args.length==2){
            return copyPartialMatches(args[1],List.of("1","5","10","20","40","60"));
        }
        return null;
    }

    @Override
    public String name() {
        return "burn";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (hasPermission(sender)){
            if (args.length==2) {
                Player p = findPlayer(sender,args[0]);
                if (p != null){
                    int sec = toInteger(sender,args[1],2);
                    if (!(sec<1)){
                        int tick = sec*20;
                        p.setFireTicks(tick);
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
