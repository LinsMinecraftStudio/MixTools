package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.kit.KitCreator;
import org.lins.mmmjjkx.mixtools.managers.features.kit.KitManager;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsKit;

import java.util.List;

public class CMDKit implements MixTabExecutor {
    private final KitManager kitManager = MixTools.kitManager;

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],List.of("remove","create","give"));
        }
        if (args.length==2 & args[0].equals("give")){
            return copyPartialMatches(args[1],getPlayerNames());
        }
        return null;
    }

    @Override
    public String name() {
        return "kit";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasCustomPermission(sender,"kit")){
            if (args.length==2) {
                switch (args[0]) {
                    case "remove" -> {
                        boolean result = kitManager.removeKit(args[1]);
                        if (!result) {
                            sendMessage(sender, "Kit.notFound");
                            return false;
                        }
                        return true;
                    }
                    case "create" -> {
                        Player p = toPlayer(sender);
                        if (p != null) {
                            new KitCreator(p, args[1]).openInventory();
                            return true;
                        }
                        return false;
                    }
                    default -> {
                        sendMessage(sender, "Command.ArgError");
                        return false;
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equals("give")) {
                    Player p = findPlayer(sender, args[1]);
                    MixToolsKit kit = MixTools.kitManager.getKitByName(args[2]);
                    if (kit == null) {
                        sendMessage(sender, "Kit.notFound");
                        return false;
                    }
                    MixTools.kitManager.giveKit(p, kit);
                    return true;
                }
            } else {
                sendMessage(sender, "Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
