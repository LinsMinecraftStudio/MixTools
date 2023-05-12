package org.lins.mmmjjkx.mixtools.commands.economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

import java.util.List;

import static org.lins.mmmjjkx.mixtools.objects.keys.DataKey.ECONOMY_MONEY;
import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.CURRENCY_SYMBOL;

public class CMDPay implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return copyPartialMatches(strings[0], getPlayerNames());
        }
        return null;
    }

    @Override
    public String name() {
        return "pay";
    }

    @Override
    public String requirePlugin() {
        return "Vault";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (hasPermission(commandSender)) {
            if (strings.length == 2) {
                Player p = toPlayer(commandSender);
                if (p != null) {
                    Player p2 = findPlayer(commandSender, strings[0]);
                    double amount = toDouble(commandSender, strings[1], 2);
                    if (p2 != null & amount != -100) {
                        double balance = MixTools.getDataManager().getDoubleData(ECONOMY_MONEY, p.getUniqueId());
                        double cost = balance - amount;
                        if (cost < 0.01){
                            sendMessage(commandSender, "Economy.NotEnoughMoneyToPay");
                            return false;
                        }
                        MixTools.getDataManager().setData(ECONOMY_MONEY, p.getUniqueId(), cost);
                        double balance2 = MixTools.getDataManager().getDoubleData(ECONOMY_MONEY, p2.getUniqueId());
                        MixTools.getDataManager().setData(ECONOMY_MONEY, p2.getUniqueId(), balance2 + amount);
                        sendMessage(p2, "Economy.PaySuccess",p.getName(),
                                MixTools.settingsManager.getString(CURRENCY_SYMBOL),amount);
                        return true;
                    }
                }
            }else {
                sendMessage(commandSender, "Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
