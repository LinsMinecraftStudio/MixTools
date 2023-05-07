package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixCommandExecutor;

import javax.annotation.Nonnull;

public class CMDBroadcast implements MixCommandExecutor {
    @Override
    public String name() {
        return "broadcast";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (hasPermission(sender)){
            if (args.length==1){
                broadcastMessage("Command.Broadcast",
                        MixTools.messageHandler.colorize(args[0].replace("<sp>"," ")));
                return true;
            }else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
