package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CMDGamemode implements MixTabExecutor {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> result = new ArrayList<>();
        if (args.length==0){
            for (GameMode mode: GameMode.values()) {
                result.add(mode.name());
            }
        }
        if (args.length==1){
            return getPlayerNames();
        }
        return result;
    }

    @Override
    public String name() {
        return "gamemode";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = toPlayer(sender);
        if (hasPermission(p)){
            switch (args.length){
                case 1:
                    GameMode mode = GameMode.valueOf(args[0]);
                    p.setGameMode(mode);
                case 2:
                    GameMode mode2 = GameMode.valueOf(args[0]);
                    Player p2 = findPlayer(args[1]);
                    p2.setGameMode(mode2);
            }
        }
        return false;
    }
}
