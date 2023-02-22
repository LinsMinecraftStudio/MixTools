package org.lins.mmmjjkx.mixtools.managers.hookaddon;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;

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
        return "1.0-SNAPSHOT";
    }
}
