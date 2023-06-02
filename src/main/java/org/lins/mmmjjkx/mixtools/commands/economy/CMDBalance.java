package org.lins.mmmjjkx.mixtools.commands.economy;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

import static org.lins.mmmjjkx.mixtools.objects.keys.DataKey.ECONOMY_MONEY;
import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.CURRENCY_SYMBOL;

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
                        sendMessage(p, "Economy.Balance",
                                MixTools.settingsManager.getString(CURRENCY_SYMBOL),
                                MixTools.getDataManager().getDoubleData(ECONOMY_MONEY, p.getUniqueId()));
                        return true;
                    }
                    return false;
                }
            } else if (args.length == 1) {
                Player p2 = findPlayer(sender,args[0]);
                if (p2!=null) {
                    if (hasCustomPermission(sender, "economy.balance.others")) {
                        sendMessage(sender, "Economy.OtherBalance", p2.getName(), MixTools.settingsManager.getString(CURRENCY_SYMBOL),
                                MixTools.getDataManager().getDoubleData(ECONOMY_MONEY, p2.getUniqueId()));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
