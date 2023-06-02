package org.lins.mmmjjkx.mixtools.commands.list;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsWarp;

import java.util.List;

public class CMDWarps extends ListCMD<MixToolsWarp> {
    public CMDWarps(@NotNull String name) {
        super(name);
    }

    @Override
    public List<MixToolsWarp> list(CommandSender sender) {
        return MixTools.warpManager.getWarps();
    }

    @Override
    public void sendLineMessage(CommandSender sender, int number, MixToolsWarp object) {
        sendMessage(sender, "Info.List.Styles.Default", number, object.name());
    }

    @Override
    public String requirePlugin() {
        return null;
    }
}
