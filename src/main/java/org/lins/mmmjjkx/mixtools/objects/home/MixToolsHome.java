package org.lins.mmmjjkx.mixtools.objects.home;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MixToolsHome {
    private final String name;
    private final Player owner;
    private final Location loc;
    public MixToolsHome(String name, Player owner, Location loc) {
        this.name = name;
        this.owner = owner;
        this.loc = loc;
    }

    public String getName() {
        return name;
    }

    public Player getOwner() {
        return owner;
    }

    public Location getLoc() {
        return loc;
    }
}
