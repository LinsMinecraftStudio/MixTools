package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public class CMDVoid extends PolymerCommand {

    public CMDVoid(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],getPlayerNames());
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
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)){
            if (args.length==0){
                Player p = toPlayer(sender);
                if (p != null){
                    Location loc = p.getLocation();
                    p.teleport(new Location(loc.getWorld(), loc.getX(), -70,
                            loc.getZ(), loc.getPitch(), loc.getYaw()));
                    return true;
                }
            } else if (args.length==1) {
                Player p = findPlayer(sender,args[0]);
                if (p != null){
                    Location loc = p.getLocation();
                    p.teleport(new Location(loc.getWorld(), loc.getX(), -70,
                            loc.getZ(), loc.getPitch(), loc.getYaw()));
                    sendMessage(sender,"Command.TeleportToVoid",p.getName());
                    return true;
                }
            } else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
