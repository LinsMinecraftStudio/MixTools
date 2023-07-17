package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.Polymer;
import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.kit.KitCreator;
import org.lins.mmmjjkx.mixtools.managers.features.kit.KitManager;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsKit;

import java.util.ArrayList;
import java.util.List;

public class CMDKit extends PolymerCommand {
    private final KitManager kitManager = MixTools.kitManager;

    public CMDKit(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],List.of("remove","create","give"));
        }
        if (args.length==2 & args[0].equals("give")){
            return copyPartialMatches(args[1],getPlayerNames());
        }
        return new ArrayList<>();
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {
        MixTools.messageHandler.sendMessage(sender, message, args);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
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
                        Polymer.messageHandler.sendMessage(sender, "Command.ArgError");
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
                Polymer.messageHandler.sendMessage(sender, "Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
