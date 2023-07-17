package org.lins.mmmjjkx.mixtools.commands.economy;

import io.github.linsminecraftstudio.polymer.Polymer;
import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public class CMDBalance extends PolymerCommand {
    public CMDBalance(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],getPlayerNames());
        }
        return new ArrayList<>();
    }

    @Override
    public String requirePlugin() {
        return "Vault";
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {
        MixTools.messageHandler.sendMessage(sender, message, args);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)) {
            Player p = toPlayer(sender);
            if (args.length == 0) {
                if (p != null) {
                    if (hasCustomPermission(p, "economy.balance")) {
                        sendMessage(p, "Economy.Balance", MixTools.hookManager.getEconomy().getBalance(p));
                        return true;
                    }
                    return false;
                }
            } else if (args.length == 1) {
                Player p2 = findPlayer(sender,args[0]);
                if (p2!=null) {
                    if (hasCustomPermission(sender, "economy.balance.others")) {
                        sendMessage(sender, "Economy.OtherBalance", MixTools.hookManager.getEconomy().getBalance(p2));
                        return true;
                    }
                }
            } else {
                Polymer.messageHandler.sendMessage(sender, "Command.ArgError");
            }
        }
        return false;
    }
}
