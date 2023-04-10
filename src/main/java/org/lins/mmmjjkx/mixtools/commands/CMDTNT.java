package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDTNT implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return StringUtil.copyPartialMatches(args[0],getPlayerNames(),new ArrayList<>());
        } else if (args.length==2) {
            return StringUtil.copyPartialMatches(args[0],
                    List.of("1","2","3","4","5"),new ArrayList<>());
        }
        return null;
    }

    @Override
    public String name() {
        return "tnt";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)){
            if (args.length==0) {
                Player p = toPlayer(sender);
                if (p != null) {
                    World world = p.getWorld();
                    world.spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
                    return true;
                }
            }else if (args.length==1) {
                Player p = findPlayer(sender, args[0]);
                if (p != null) {
                    World world = p.getWorld();
                    world.spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
                    return true;
                }
            } else if (args.length==2) {
                Player p = findPlayer(sender,args[0]);
                if (p != null) {
                    World world = p.getWorld();
                    int amount = toInteger(sender, args[1]);
                    if (amount != -100) {
                        for (int i = 0; i < amount; i++) {
                            world.spawnEntity(p.getLocation(), EntityType.PRIMED_TNT);
                        }
                        return true;
                    }
                }
            } else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
