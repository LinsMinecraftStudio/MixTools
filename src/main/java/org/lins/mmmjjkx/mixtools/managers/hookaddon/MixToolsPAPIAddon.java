package org.lins.mmmjjkx.mixtools.managers.hookaddon;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * I don't know what I can write in this class
 */
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
        return null;
    }
}