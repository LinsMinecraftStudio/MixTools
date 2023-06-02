package org.lins.mmmjjkx.mixtools.commands.entityspawn;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class CMDLightning extends EntitySpawnCMD {
    public CMDLightning(@NotNull String name) {
        super(name);
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
