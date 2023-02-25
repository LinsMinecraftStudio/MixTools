package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

import static org.lins.mmmjjkx.mixtools.objects.keys.DataKey.*;
import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.CURRENCY_SYMBOL;

public class CMDEconomy implements MixTabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length==0){
            list.add("balance");
            list.add("clear");
            list.add("add");
            list.add("take");
        }
        if (args.length==1) {
            return getPlayerNames();
        }
        return list;
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
    public List<String> aliases() {
        return List.of("eco","money");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = toPlayer(sender);
        if (p != null) {
            if (args.length == 0) {
                sendMessage(p, "Economy.Balance",
                        MixTools.settingsManager.getString(CURRENCY_SYMBOL),
                        MixTools.dataManager.getDoubleData(ECONOMY_MONEY, p.getName()));
                return true;
            } else if (args.length == 1 && args[0].equals("balance")) {
                sendMessage(p, "Economy.Balance",
                        MixTools.settingsManager.getString(CURRENCY_SYMBOL),
                        MixTools.dataManager.getDoubleData(ECONOMY_MONEY, p.getName()));
                return true;
            }
        }else {
            sendMessage(sender,"Command.RunAsConsole");
            return false;
        }
        if (args.length == 2) {
            String name = args[1];
            switch (args[0]) {
                case "balance":
                    if (MixTools.dataManager.getBooleanData(HAS_ECONOMY_ACCOUNT,name)) {
                        sendMessage(sender, "Economy.Balance", MixTools.settingsManager.getString(CURRENCY_SYMBOL), name,
                                MixTools.dataManager.getDoubleData(ECONOMY_MONEY, name));
                        return true;
                    }else {
                        sendMessage(p,"Economy.AccountNotFound");
                        return false;
                    }
                case "clear":
                    if (hasSubPermission(sender,"clear")) {
                        if (MixTools.dataManager.getBooleanData(HAS_ECONOMY_ACCOUNT, name)) {
                            MixTools.dataManager.setData(ECONOMY_MONEY, name, 0);
                            sendMessage(sender,"Economy.ClearSuccess");
                            return true;
                        }else {
                            sendMessage(p,"Economy.AccountNotFound");
                            return false;
                        }
                    }
                    return false;
                case "currency":
                    if (hasSubPermission(sender,"currency-symbol")) {
                        MixTools.settingsManager.setString(CURRENCY_SYMBOL, args[1]);
                        sendMessage(sender,"Economy.ChangeCurrencySymbol");
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
                            MixTools.dataManager.setData(ECONOMY_MONEY, name, MixTools.dataManager.getDoubleData(ECONOMY_MONEY, name) + amount);
                            sendMessage(p, "Economy.AddSuccess", name,
                                    MixTools.settingsManager.getString(CURRENCY_SYMBOL), amount);
                            return true;
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
            sendMessage(p, "Command.NoEnoughArgs");
            return false;
        }
        return false;
    }
}
