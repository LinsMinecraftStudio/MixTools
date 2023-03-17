package org.lins.mmmjjkx.mixtools.managers.misc;

import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.MixToolsTeleportRequest;

import java.util.HashMap;
import java.util.Map;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.TPA_COOLDOWN;

public class TpaManager {
    private final Map<MixToolsTeleportRequest,Integer> requestMap = new HashMap<>();
    public final int DEFAULT_COOLDOWN = MixTools.settingsManager.getInt(TPA_COOLDOWN);
    public void setCooldown(MixToolsTeleportRequest request, int time){
        if(time < 1) {
            requestMap.remove(request);
        } else {
            requestMap.put(request, time);
        }
    }

    public int getCooldown(MixToolsTeleportRequest request){
        return requestMap.getOrDefault(request, 0);
    }

    public MixToolsTeleportRequest getRequest(Player to){
        for (MixToolsTeleportRequest r: requestMap.keySet()) {
            if (r.to().getName().equals(to.getName())) {
                return r;
            }
        }
        return null;
    }
}
