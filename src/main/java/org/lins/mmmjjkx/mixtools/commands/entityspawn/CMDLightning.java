package org.lins.mmmjjkx.mixtools.commands.entityspawn;

import org.bukkit.entity.EntityType;

public class CMDLightning implements EntitySpawnCMD {
    @Override
    public String name() {
        return "lightning";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public EntityType entityType() {
        return EntityType.LIGHTNING;
    }
}
