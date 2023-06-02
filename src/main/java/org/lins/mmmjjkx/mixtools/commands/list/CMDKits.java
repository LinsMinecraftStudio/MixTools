package org.lins.mmmjjkx.mixtools.commands.list;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsKit;

import java.util.List;

public class CMDKits extends ListCMD<MixToolsKit> {
    public CMDKits(@NotNull String name) {
        super(name);
    }

    @Override
    public List<MixToolsKit> list(CommandSender sender) {
        return MixTools.kitManager.getKits();
    }

    @Override
    public void sendLineMessage(CommandSender sender, int number, MixToolsKit object) {
        sendMessage(sender, "Info.List.Styles.Default", number, object.kitName());
    }

    @Override
    public String requirePlugin() {
        return null;
    }
}
