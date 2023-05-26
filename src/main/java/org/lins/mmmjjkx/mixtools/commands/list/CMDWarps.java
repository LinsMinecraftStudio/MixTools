package org.lins.mmmjjkx.mixtools.commands.list;

import org.bukkit.command.CommandSender;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsWarp;

import java.util.List;

public class CMDWarps implements ListCMD<MixToolsWarp> {
    @Override
    public List<MixToolsWarp> list(CommandSender sender) {
        return MixTools.warpManager.getWarps();
    }

    @Override
    public void sendLineMessage(CommandSender sender, MixToolsWarp object, int number) {
        sendMessage(sender, "Info.List.Styles.Default", number, object.name());
    }

    @Override
    public String name() {
        return "warps";
    }

    @Override
    public String requirePlugin() {
        return null;
    }
}
