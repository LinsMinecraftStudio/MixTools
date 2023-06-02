package org.lins.mmmjjkx.mixtools.commands.entityspawn;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public abstract class EntitySpawnCMD extends PolymerCommand {
    public EntitySpawnCMD(@NotNull String name) {
        super(name);
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {
        MixTools.messageHandler.sendMessage(sender, message, args);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if (args.length==1){
            return copyPartialMatches(args[0],getPlayerNames());
        } else if (args.length==2) {
            return copyPartialMatches(args[1], List.of("1","2","3","4","5"));
        }
        return new ArrayList<>();
    }

    public abstract EntityType entityType();

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (hasPermission(sender)){
            if (args.length==0) {
                Player p = toPlayer(sender);
                if (p != null) {
                    World world = p.getWorld();
                    world.spawnEntity(p.getLocation(), entityType());
                    sendMessage(sender, "");
                    return true;
                }
            }else if (args.length==1) {
                Player p = findPlayerNoMessage(args[0]);
                if (p != null) {
                    World world = p.getWorld();
                    world.spawnEntity(p.getLocation(), entityType());
                    return true;
                } else {
                    Player self = toPlayer(sender);
                    if (self != null) {
                        int amount = toInteger(sender, args[0], 1);
                        if (amount != -100) {
                            for (int i = 0; i < amount; i++) {
                                self.getWorld().spawnEntity(self.getLocation(), entityType());
                            }
                            return true;
                        }
                    }
                }
            } else if (args.length==2) {
                Player p = findPlayer(sender,args[0]);
                if (p != null) {
                    World world = p.getWorld();
                    int amount = toInteger(sender, args[1], 2);
                    if (amount != -100) {
                        for (int i = 0; i < amount; i++) {
                            world.spawnEntity(p.getLocation(), entityType());
                        }
                        return true;
                    }
                }
            } else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }
}
