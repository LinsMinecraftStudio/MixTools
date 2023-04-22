package org.lins.mmmjjkx.mixtools.managers.misc;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MiscFeatureManager {
    private final Map<Player, Location> back_map = new HashMap<>();
    private CommandGroupManager commandGroupManager;
    private TpaManager tpaManager;
    private WorldManager worldManager;
    private final SchedulerManager schedulerManager = new SchedulerManager();

    public MiscFeatureManager(){
        setup();
    }

    public void setup(){
        commandGroupManager = new CommandGroupManager();
        tpaManager = new TpaManager();
        worldManager = new WorldManager();
        schedulerManager.reload();
    }

    public CommandGroupManager getCommandGroupManager() {
        return commandGroupManager;
    }

    public TpaManager getTpaManager() {
        return tpaManager;
    }

    public WorldManager getWorldManager() {return worldManager;}

    public SchedulerManager getSchedulerManager() {return schedulerManager;}

    public Location getBackPlayerLocation(Player player){
        return back_map.get(player);
    }

    public void addBackPlayer(Player p, Location from) {
        back_map.remove(p);
        back_map.put(p, from);
    }
}
