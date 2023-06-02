package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import javax.annotation.Nonnull;
import java.util.List;

public class CMDBroadcast extends PolymerCommand {
    public CMDBroadcast(@NotNull String name, List<String> aliases) {
        super(name, aliases);
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {
        MixTools.messageHandler.sendMessage(sender, message, args);
    }

    @Override
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String label, @Nonnull String[] args) {
        if (hasPermission(sender)){
            if (args.length==1){
                MixTools.messageHandler.broadcastCustomMessage(args[0].replace("<sp>"," "));
                return true;
            }else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
