package org.lins.mmmjjkx.mixtools.objects.interfaces;

import io.github.linsminecraftstudio.polymer.command.PolymerCommandExecutor;
import org.bukkit.command.CommandSender;
import org.lins.mmmjjkx.mixtools.MixTools;

public interface MixCommandExecutor extends PolymerCommandExecutor {
    default void sendMessage(CommandSender cs,String node, Object... args){
        MixTools.messageHandler.sendMessage(cs,node,args);
    }

    default void broadcastMessage(String node,Object... args){
        MixTools.messageHandler.broadcastMessage(node,args);
    }

    default String getMessage(String node,Object... args){return MixTools.messageHandler.getColored(node,args);}
}
