package org.lins.mmmjjkx.mixtools.objects.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.MessageHandler;

public interface MixToolsListener extends Listener {
    default String getSettingString(String key){
        return MixTools.settingsManager.getString(key);
    }

    default MessageHandler getMessageHandler() {return MixTools.messageHandler;}

    default void register() {Bukkit.getPluginManager().registerEvents(this,MixTools.INSTANCE);}
}
