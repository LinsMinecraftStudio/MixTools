package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CMDBurn extends PolymerCommand {
    public CMDBurn(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String label, @Nonnull String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],getPlayerNames());
        }else if (args.length==2){
            return copyPartialMatches(args[1],List.of("1","5","10","20","40","60"));
        }
        return new ArrayList<>();
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
