package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;
import org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.lins.mmmjjkx.mixtools.objects.keys.DataKey.*;
import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.CURRENCY_SYMBOL;

public class CMDEconomy implements MixTabExecutor {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length==0){
            List<String> list = new ArrayList<>();
            list.add("balance");
            list.add("clear");
            list.add("add");
            list.add("take");
            list.add("currency-symbol");
            return list;
        }
        if (args.length==1) {
            return getPlayerNames();
        }
        return null;
    }

    @Override
    public String name() {
        return "economy";
    }

    @Override
    public String requirePlugin() {
        return "Vault";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player p = toPlayer(sender);
        if (p != null) {
            if (args.length == 0) {
                sendMessage(p, "Economy.Balance",
                        MixTools.dataManager.getDoubleData(ECONOMY_MONEY, p.getName()),
                        MixTools.settingsManager.getString(CURRENCY_SYMBOL));
                return true;
            } else if (args.length == 1 && args[0].equals("balance")) {
                sendMessage(p, "Economy.Balance",
                        MixTools.dataManager.getDoubleData(ECONOMY_MONEY, p.getName()),
                        MixTools.settingsManager.getString(CURRENCY_SYMBOL));
                return true;
            }
        }else {
            sendMessage(sender,"Command.RunAsConsole");
            return false;
        }
        if (args.length == 2) {
            String name = args[1];
            switch (args[0]) {
                case "balance" -> {
                    if (MixTools.dataManager.getBooleanData(HAS_ECONOMY_ACCOUNT, name)) {
                        sendMessage(sender, "Economy.Balance", MixTools.settingsManager.getString(CURRENCY_SYMBOL), name,
                                MixTools.dataManager.getDoubleData(ECONOMY_MONEY, name));
                        return true;
                    } else {
                        sendMessage(p, "Economy.AccountNotFound");
                        return false;
                    }
                }
                case "clear" -> {
                    if (hasSubPermission(sender, "clear")) {
                        if (MixTools.dataManager.getBooleanData(HAS_ECONOMY_ACCOUNT, name)) {
                            MixTools.dataManager.setData(ECONOMY_MONEY, name, 0);
                            sendMessage(sender, "Economy.ClearSuccess");
                            return true;
                        } else {
                            sendMessage(p, "Economy.AccountNotFound");
                            return false;
                        }
                    }
                    return false;
                }
                case "currency-symbol" -> {
                    if (hasSubPermission(sender, "currency-symbol")) {
                        MixTools.settingsManager.set(CURRENCY_SYMBOL, args[1]);
                        sendMessage(sender, "Economy.ChangeCurrencySymbol", args[1]);
                    }
                }
            }
            return true;
        }else if (args.length == 3) {
            String name = args[1];
            double amount = Double.parseDouble(args[2]);
            switch (args[0]){
                case "add":
                    if (hasSubPermission(sender,"add")) {
                        if (MixTools.dataManager.getBooleanData(HAS_ECONOMY_ACCOUNT, name)) {
                            String str = MixTools.settingsManager.getString(SettingsKey.MAXIMUM_MONEY);
                            double max = Double.parseDouble(new BigDecimal(str).toPlainString());
                            if (MixTools.dataManager.getDoubleData(ECONOMY_MONEY, name)<max) {
                                MixTools.dataManager.setData(ECONOMY_MONEY, name, MixTools.dataManager.getDoubleData(ECONOMY_MONEY, name) + amount);
                                sendMessage(p, "Economy.AddSuccess", name,
                                        MixTools.settingsManager.getString(CURRENCY_SYMBOL), amount);
                                return true;
                            }else {
                                sendMessage(p, "Economy.Max");
                                return false;
                            }
                        } else {
                            sendMessage(p, "Economy.AccountNotFound");
                            return false;
                        }
                    }
                case "take":
                    if (hasSubPermission(sender,"take")) {
                        if (MixTools.dataManager.getBooleanData(HAS_ECONOMY_ACCOUNT, name)) {
                            MixTools.dataManager.setData(ECONOMY_MONEY, name, MixTools.dataManager.getDoubleData(ECONOMY_MONEY, name) - amount);
                            sendMessage(p, "Economy.TakeSuccess", name,
                                    MixTools.settingsManager.getString(CURRENCY_SYMBOL), amount);
                            return true;
                        } else {
                            sendMessage(p, "Economy.AccountNotFound");
                            return false;
                        }
                    }
            }
        }else {
            sendMessage(p, "Command.ArgError");
            return false;
        }
        return false;
    }
}
