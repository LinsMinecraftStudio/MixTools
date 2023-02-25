package org.lins.mmmjjkx.mixtools.objects.listener;

import org.bukkit.event.Listener;
import org.lins.mmmjjkx.mixtools.MixTools;

public interface MixToolsListener extends Listener {
    default String getSettingString(String key){
        return MixTools.settingsManager.getString(key);
    }
}
