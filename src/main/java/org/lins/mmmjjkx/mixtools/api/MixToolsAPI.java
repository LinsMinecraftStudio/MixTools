package org.lins.mmmjjkx.mixtools.api;

import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.MessageHandler;
import org.lins.mmmjjkx.mixtools.managers.features.SchedulerManager;
import org.lins.mmmjjkx.mixtools.managers.features.WarpManager;
import org.lins.mmmjjkx.mixtools.managers.features.kit.KitManager;
import org.lins.mmmjjkx.mixtools.managers.misc.MiscFeatureManager;

import java.util.ArrayList;
import java.util.List;

public class MixToolsAPI {
    public static int VERSION_WORTH = 105;
    public static Managers managers = new Managers();
    private final List<MixToolsAddon> addons = new ArrayList<>();
    public static class Managers {
        public static SchedulerManager schedulerManager = MixTools.schedulerManager;
        public static WarpManager warpManager = MixTools.warpManager;
        public static KitManager kitManager = MixTools.kitManager;
        public static MiscFeatureManager miscFeatureManager = MixTools.miscFeatureManager;
        public static MessageHandler messageHandler = MixTools.messageHandler;
    }

    public void registerAddon(MixToolsAddon addon){
        addons.add(addon);
    }

    public List<MixToolsAddon> getAddons() {
        return addons;
    }
}
