package org.lins.mmmjjkx.mixtools.managers.kit;

import org.bukkit.entity.Player;

public class KitManager {
    public void startCreateKit(Player p, String kitName){
        new KitCreator(p, kitName);
    }
}
