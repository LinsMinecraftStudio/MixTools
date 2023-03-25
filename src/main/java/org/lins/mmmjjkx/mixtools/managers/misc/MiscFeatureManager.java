package org.lins.mmmjjkx.mixtools.managers.misc;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MiscFeatureManager {
    private final Map<Player, Location> back_map = new HashMap<>();
    private final CommandGroupManager commandGroupManager;
    private final TpaManager tpaManager;

    public MiscFeatureManager(){
        commandGroupManager = new CommandGroupManager();
        tpaManager = new TpaManager();
    }

    public CommandGroupManager getCommandGroupManager() {
        return commandGroupManager;
    }

    public TpaManager getTpaManager() {
        return tpaManager;
    }

    public Location getBackPlayerLocation(Player player){
        return back_map.get(player);
    }

    public void addBackPlayer(Player p, Location from) {
        if (from==null||back_map.containsKey(p)) {
            back_map.remove(p);
            return;
        }
        back_map.put(p, from);
    }
}
