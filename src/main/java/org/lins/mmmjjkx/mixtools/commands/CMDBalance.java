package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.interfaces.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

import static org.lins.mmmjjkx.mixtools.objects.keys.DataKey.ECONOMY_MONEY;
import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.CURRENCY_SYMBOL;

public class CMDBalance implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return StringUtil.copyPartialMatches(args[0],getPlayerNames(),new ArrayList<>());
        }
        return null;
    }

    @Override
    public String name() {
        return "balance";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
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
