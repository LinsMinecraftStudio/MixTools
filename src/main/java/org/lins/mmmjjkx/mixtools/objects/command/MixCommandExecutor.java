package org.lins.mmmjjkx.mixtools.objects.command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.List;

public interface MixCommandExecutor extends CommandExecutor {
    String name();
    String requirePlugin();
    String usage();
    List<String> aliases();
    default boolean hasPermission(CommandSender cs){
        boolean b = cs.hasPermission("mixtools.command."+name());
        if (!b){
            sendMessage(cs,"Command.NoPermission");
        }
        return b;
    }
    default boolean hasSubPermission(CommandSender cs,String sub){
        boolean b = cs.hasPermission("mixtools.command."+name()+"."+sub);
        if (!b){
            sendMessage(cs,"Command.NoPermission");
        }
        return b;
    }
    default void register(){
        String require = requirePlugin();
        if (require == null){
            require = "";
        }
        if (!require.isBlank()){
            if (Bukkit.getPluginManager().isPluginEnabled(require)){
                PluginCommand cmd = MixTools.INSTANCE.getCommand(name());
                cmd.setExecutor(this);
                cmd.setAliases(aliases());
                cmd.setUsage(usage());
            }
        }else {
            PluginCommand cmd = MixTools.INSTANCE.getCommand(name());
            cmd.setExecutor(this);
            cmd.setAliases(aliases());
            cmd.setUsage(usage());
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

    default void sendMessage(CommandSender cs,String node,Object... args){
        MixTools.messageHandler.sendMessage(cs,node,args);
    }

    default void broadcastMessage(String node,Object... args){
        MixTools.messageHandler.broadcastMessage(node,args);
    }

    default String getMessage(String node,Object... args){return MixTools.messageHandler.getColored(node,args);}
}
