package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.features.kit.KitManager;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDKit implements MixTabExecutor {
    private final KitManager kitManager = MixTools.kitManager;

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],List.of("list","remove","create","give"));
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
                switch (args[0]){
                    case "remove":
                        boolean result = kitManager.removeKit(args[1]);
                        if (!result){
                            sendMessage(sender,"Kit.");
                            return false;
                        }
                        return true;
                    case "create":

                }
            }else {
                sendMessage(sender, "Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
