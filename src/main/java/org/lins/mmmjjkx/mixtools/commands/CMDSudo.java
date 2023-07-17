package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.Polymer;
import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public class CMDSudo extends PolymerCommand {
    public CMDSudo(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0], getPlayerNames());
        } else if (args.length > 1) {
            return List.of("cmd:","chat:","cmdgroup:");
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
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (hasPermission(sender)){
            if (args.length>=2){
                Player p = findPlayer(sender,args[0]);
                if (p != null){
                    for (String arg:args){
                        if (!arg.contains(":"))continue;
                        String[] str = arg.split(":");
                        if (arg.length()<4) continue;
                        if (str.length!=2) continue;
                        String key = str[0];
                        String action = str[1].replaceAll("<sp>"," ");
                        switch (key) {
                            case "chat" -> p.chat(action);
                            case "cmd" -> p.performCommand(action);
                            case "cmdgroup" -> MixTools.miscFeatureManager.getCommandGroupManager().runCommandGroup(p, action);
                        }
                    }
                    return true;
                }
            }else {
                Polymer.messageHandler.sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
