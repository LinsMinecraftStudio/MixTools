package org.lins.mmmjjkx.mixtools.managers.misc;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.managers.features.setters.TpaSetter;

import java.util.HashMap;
import java.util.Map;

public class MiscFeatureManager {
    private final Map<Player, Location> back_map = new HashMap<>();
    private final CommandGroupManager commandGroupManager;
    private TpaSetter tpaSetter;
    private final WorldManager worldManager;

    public MiscFeatureManager(){
        commandGroupManager = new CommandGroupManager();
        tpaSetter = new TpaSetter();
        worldManager = new WorldManager();
    }

    public void reload(){
        tpaSetter = new TpaSetter();
        commandGroupManager.reload();
        worldManager.reload();
    }

    public CommandGroupManager getCommandGroupManager() {
        return commandGroupManager;
    }

    public TpaSetter getTpaManager() {
        return tpaSetter;
    }

    public WorldManager getWorldManager() {return worldManager;}

    public Location getBackPlayerLocation(Player player){
        return back_map.get(player);
    }

    public void addBackPlayer(Player p, Location from) {
        back_map.remove(p);
        back_map.put(p, from);
    }
}
