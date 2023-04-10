package org.lins.mmmjjkx.mixtools.objects.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;

public interface MixCommandExecutor extends CommandExecutor {
    String name();
    String requirePlugin();
    default boolean hasPermission(CommandSender cs){
        return hasCustomPermission(cs,"command."+name());
    }
    default boolean hasSubPermission(CommandSender cs,String sub){
        return hasCustomPermission(cs,"command."+name()+"."+sub);
    }
    default boolean hasCustomPermission(CommandSender cs,String perm){
        boolean b = cs.hasPermission("mixtools."+perm);
        if (!b){
            sendMessage(cs,"Command.NoPermission");
        }
        return b;
    }
    default void register(){
        String require = requirePlugin();
        if (!require.isBlank()){
            if (Bukkit.getPluginManager().isPluginEnabled(require)){
                PluginCommand cmd = MixTools.INSTANCE.getCommand(name());
                cmd.setExecutor(this);
            }
        }else {
            PluginCommand cmd = MixTools.INSTANCE.getCommand(name());
            cmd.setExecutor(this);
        }
    }
    default Player toPlayer(CommandSender cs){
        if (cs instanceof Player){
            return (Player)cs;
        }else {
            sendMessage(cs,"Command.RunAsConsole");
            return null;
        }
    }

    default Player findPlayer(CommandSender from,String name){
        Player p = Bukkit.getPlayer(name);
        if (p == null){
            MixTools.messageHandler.sendMessage(from, "Command.PlayerNotFound");
        }
        return p;
    }

    default Player findPlayerNoMessage(String name){
        return Bukkit.getPlayer(name);
    }

    default void sendMessage(CommandSender cs,String node,Object... args){
        MixTools.messageHandler.sendMessage(cs,node,args);
    }

    default void broadcastMessage(String node,Object... args){
        MixTools.messageHandler.broadcastMessage(node,args);
    }

    default String getMessage(String node,Object... args){return MixTools.messageHandler.getColored(node,args);}

    default int toInteger(CommandSender cs,String s){
        try {
            int i = Integer.parseInt(s);
            if (i < 1){
                sendMessage(cs,"Value.TooLow");
                return -100;
            }
            return i;
        }catch (NumberFormatException e){
            sendMessage(cs,"Value.NotInt");
            return -100;
        }
    }
}
