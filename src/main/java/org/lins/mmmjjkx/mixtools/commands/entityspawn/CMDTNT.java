package org.lins.mmmjjkx.mixtools.commands.entityspawn;

import org.bukkit.entity.EntityType;

public class CMDTNT implements EntitySpawnCMD {
    @Override
    public String name() {
        return "tnt";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public EntityType entityType() {
        return EntityType.PRIMED_TNT;
    }
}
