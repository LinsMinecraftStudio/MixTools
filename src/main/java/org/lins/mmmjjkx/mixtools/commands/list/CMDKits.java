package org.lins.mmmjjkx.mixtools.commands.list;

import org.bukkit.command.CommandSender;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsKit;

import java.util.List;

public class CMDKits implements ListCMD<MixToolsKit> {
    @Override
    public List<MixToolsKit> list(CommandSender sender) {
        return MixTools.kitManager.getKits();
    }

    @Override
    public void sendLineMessage(CommandSender sender, MixToolsKit object, int number) {
        sendMessage(sender, "Info.List.Styles.Default", number, object.kitName());
    }

    @Override
    public String name() {
        return "kits";
    }

    @Override
    public String requirePlugin() {
        return null;
    }
}
