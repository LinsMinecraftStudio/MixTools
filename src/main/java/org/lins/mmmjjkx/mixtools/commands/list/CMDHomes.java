package org.lins.mmmjjkx.mixtools.commands.list;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public class CMDHomes implements ListCMD<String> {
    @Override
    public List<String> list(CommandSender sender) {
        return sender instanceof Player ? new ArrayList<>(MixTools.getDataManager().getPlayerOwnedHomesName((Player) sender)) : new ArrayList<>();
    }

    @Override
    public void sendLineMessage(CommandSender sender, String object, int number) {
        sendMessage(sender, "Info.List.Styles.Default", number, object);
    }


    @Override
    public String name() {
        return "homes";
    }

    @Override
    public String requirePlugin() {
        return null;
    }
}
