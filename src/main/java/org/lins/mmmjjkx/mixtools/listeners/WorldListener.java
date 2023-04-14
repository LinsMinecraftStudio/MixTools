package org.lins.mmmjjkx.mixtools.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.world.WorldLoadEvent;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.listener.MixToolsListener;

public class WorldListener implements MixToolsListener {
    @EventHandler
    public void onLoadWorld(WorldLoadEvent e){
        MixTools.miscFeatureManager.getWorldManager().addWorld(e.getWorld());
    }
}
