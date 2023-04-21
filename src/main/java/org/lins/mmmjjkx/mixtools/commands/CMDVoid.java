package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDVoid implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return StringUtil.copyPartialMatches(args[0],getPlayerNames(),new ArrayList<>());
        }
        return null;
    }

    @Override
    public String name() {
        return "void";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
