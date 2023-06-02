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

public class CMDEconomy extends PolymerCommand {
    public CMDEconomy(@NotNull String name) {
        super(name);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],List.of("balance", "clear", "add", "take", "currency-symbol"));
        }
        if (args.length==2) {
            return copyPartialMatches(args[1],getPlayerNames());
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
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        Player p = toPlayer(sender);
        if (p != null) {
            if (args.length == 0) {
                if (hasCustomPermission(p, "economy.balance")) {
                    sendMessage(p, "Economy.Balance",
                            MixTools.settingsManager.getString(CURRENCY_SYMBOL),
                            MixTools.getDataManager().getDoubleData(ECONOMY_MONEY, p.getUniqueId()));
                    return true;
                }
                return false;
            } else if (args.length == 1 && args[0].equals("balance")) {
                if (hasCustomPermission(p, "economy.balance")) {
                    sendMessage(p, "Economy.Balance", MixTools.settingsManager.getString(CURRENCY_SYMBOL),
                            MixTools.getDataManager().getDoubleData(ECONOMY_MONEY, p.getUniqueId()));
                    return true;
                }
                return false;
            }
        }
        if (args.length == 2) {
            String name = args[1];
            Player p2 = findPlayer(sender,name);
            if (p2 != null) {
                switch (args[0]) {
                    case "balance" -> {
                        if (hasCustomPermission(sender,"economy.balance.others")) {
                            sendMessage(sender, "Economy.OtherBalance", name, MixTools.settingsManager.getString(CURRENCY_SYMBOL),
                                    MixTools.getDataManager().getDoubleData(ECONOMY_MONEY, p2.getUniqueId()));
                            return true;
                        }
                    }
                    case "clear" -> {
                        if (hasCustomPermission(sender, "economy.clear")) {
                            MixTools.getDataManager().setData(ECONOMY_MONEY, p2.getUniqueId(), 0);
                            sendMessage(sender, "Economy.ClearSuccess");
                            return true;
                        }
                        return false;
                    }
                    case "currency-symbol" -> {
                        if (hasCustomPermission(sender, "economy.currency-symbol")) {
                            MixTools.settingsManager.set(CURRENCY_SYMBOL, args[1]);
                            sendMessage(sender, "Economy.ChangeCurrencySymbol", args[1]);
                        }
                    }
                }
            }
            return false;
        }else if (args.length == 3) {
            String name = args[1];
            Player p3 = findPlayer(sender,name);
            double amount = Double.parseDouble(args[2]);
            if (p3 != null) {
                switch (args[0]) {
                    case "add" -> {
                        if (hasCustomPermission(sender, "economy.add")) {
                            MixTools.getDataManager().setData(ECONOMY_MONEY, p3.getUniqueId(),
                                    MixTools.getDataManager().getDoubleData(ECONOMY_MONEY, p3.getUniqueId()) + amount);
                            sendMessage(p, "Economy.AddSuccess", name,
                                    MixTools.settingsManager.getString(CURRENCY_SYMBOL), amount);
                            return true;
                        }
                        return false;
                    }
                    case "take" -> {
                        if (hasCustomPermission(sender, "economy.take")) {
                            MixTools.getDataManager().setData(ECONOMY_MONEY, p3.getUniqueId(),
                                    MixTools.getDataManager().getDoubleData(ECONOMY_MONEY, p3.getUniqueId()) - amount);
                            sendMessage(p, "Economy.TakeSuccess", name,
                                    MixTools.settingsManager.getString(CURRENCY_SYMBOL), amount);
                            return true;
                        }
                        return false;
                    }
                }
            }
            return false;
        }else {
            sendMessage(p, "Command.ArgError");
            return false;
        }
    }
}
