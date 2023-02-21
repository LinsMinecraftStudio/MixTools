package org.lins.mmmjjkx.mixtools.objects.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;

public interface MixCommandExecutor extends CommandExecutor {
    String name();
    default boolean hasPermission(Player p){
        return p.hasPermission("mixtools.command."+name());
    }
    default boolean hasPermission(CommandSender cs){
        return cs.hasPermission("mixtools.command."+name());
    }
    default void register(){
        PluginCommand cmd = MixTools.INSTANCE.getCommand(name());
        cmd.setExecutor(this);
    }
    default Player toPlayer(CommandSender cs){
        if (cs instanceof Player){
            return (Player)cs;
        }else {

            return null;
        }
    }

    default Player findPlayer(String name){
        Player p = Bukkit.getPlayer(name);
        if (p != null){
            return p;
        }else {

            return null;
        }
    }
}
