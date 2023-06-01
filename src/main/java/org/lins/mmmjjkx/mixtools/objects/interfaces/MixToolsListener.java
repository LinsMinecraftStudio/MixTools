package org.lins.mmmjjkx.mixtools.objects.interfaces;

import io.github.linsminecraftstudio.polymer.objects.PolymerMessageHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.lins.mmmjjkx.mixtools.MixTools;

public interface MixToolsListener extends Listener {
    default String getSettingString(String key){
        return MixTools.settingsManager.getString(key);
    }

    default PolymerMessageHandler getMessageHandler() {return MixTools.messageHandler;}

    default void register() {Bukkit.getPluginManager().registerEvents(this,MixTools.INSTANCE);}
}
