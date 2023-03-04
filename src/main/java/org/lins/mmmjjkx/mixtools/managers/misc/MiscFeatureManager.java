package org.lins.mmmjjkx.mixtools.managers.misc;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;

public class MiscFeatureManager {
    private Map<Player, Location> back_map;
    public CommandGroupManager commandGroupManager;

    public void addBackPlayer(Player p) {
        back_map.put(p, p.getLocation());
    }

    public void removeBackPlayer(Player p) {
        back_map.remove(p);
    }
}
