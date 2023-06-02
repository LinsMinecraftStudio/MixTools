package org.lins.mmmjjkx.mixtools.commands;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public class CMDFly extends PolymerCommand {
    public CMDFly(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],getPlayerNames());
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
            Player p = toPlayer(sender);
            if (args.length==0) {
                if (p.getAllowFlight()){
                    p.setAllowFlight(false);
                    sendMessage(sender,"Fly.Disabled");
                }else {
                    p.setAllowFlight(true);
                    p.setFallDistance(0);
                    sendMessage(sender,"Fly.Enabled");
                }
                return true;
            }else if (args.length==1) {
                if (hasSubPermission(sender, "others")) {
                    Player p2 = findPlayer(p, args[0]);
                    if (p2 != null) {
                        if (p2.getAllowFlight()){
                            p2.setAllowFlight(false);
                            sendMessage(sender,"Fly.OthersDisabled",p2.getName());
                        }else {
                            p2.setAllowFlight(true);
                            p2.setFallDistance(0);
                            sendMessage(sender,"Fly.OthersEnabled",p2.getName());
                        }
                        return true;
                    }
                }
            }else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
