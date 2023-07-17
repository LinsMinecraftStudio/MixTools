package org.lins.mmmjjkx.mixtools.commands.economy;

import io.github.linsminecraftstudio.polymer.Polymer;
import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.CURRENCY_SYMBOL;

public class CMDPay extends PolymerCommand {
    public CMDPay(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return copyPartialMatches(strings[0], getPlayerNames());
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
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if (hasPermission(commandSender)) {
            if (strings.length == 2) {
                Player p = toPlayer(commandSender);
                if (p != null) {
                    Player p2 = findPlayer(commandSender, strings[0]);
                    double amount = toDouble(commandSender, strings[1], 2);
                    if (p2 != null & amount != -100) {
                        double balance = MixTools.hookManager.getEconomy().getBalance(p);
                        double cost = balance - amount;
                        if (cost < 0.01){
                            sendMessage(commandSender, "Economy.NotEnoughMoneyToPay");
                            return false;
                        }
                        MixTools.hookManager.getEconomy().withdrawPlayer(p, cost);
                        MixTools.hookManager.getEconomy().depositPlayer(p2, amount);
                        sendMessage(p2, "Economy.PaySuccess",p.getName(),
                                MixTools.settingsManager.getString(CURRENCY_SYMBOL),amount);
                        return true;
                    }
                }
            }else {
                Polymer.messageHandler.sendMessage(commandSender, "Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
