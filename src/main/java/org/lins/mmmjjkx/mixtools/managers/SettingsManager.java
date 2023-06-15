package org.lins.mmmjjkx.mixtools.managers;

import io.github.linsminecraftstudio.polymer.objects.plugin.SimpleSettingsManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class SettingsManager extends SimpleSettingsManager {

    public SettingsManager(@NotNull FileConfiguration configuration) {
        super(configuration);
    }
}
