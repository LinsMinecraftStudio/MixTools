package org.lins.mmmjjkx.mixtools.managers.hookaddon;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;

public class MixToolsPAPIAddon extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "mixtools";
    }

    @Override
    public @NotNull String getAuthor() {
        return "mmmjjkx";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        String[] split = params.split("_");
        if (params.equals("world_alias")){
            return MixTools.miscFeatureManager.getWorldManager().getWorldAlias(
                    player.getWorld().getName());
        }
        if (split.length == 3){
            if (split[0].equals("world") & split[1].equals("alias")){
                return MixTools.miscFeatureManager.getWorldManager().getWorldAlias(split[2]);
            }
        }
        return null;
    }
}