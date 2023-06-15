package org.lins.mmmjjkx.mixtools.commands.teleport;

import io.github.linsminecraftstudio.polymer.command.PolymerCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public class CMDTeleport extends PolymerCommand {
    public CMDTeleport(String name, List<String> aliases){
        super(name,aliases);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (args.length>1) {
            List<String> list = getPlayerNames();
            for (World w : Bukkit.getWorlds()) {
                list.add(w.getName());
            }
            return copyPartialMatches(args[args.length-1], list);
        }
        return new ArrayList<>();
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public void sendMessage(CommandSender sender, String message, Object... args) {
        MixTools.messageHandler.sendMessage(sender, message, args);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (hasPermission(sender)) {
            Player p = toPlayer(sender);
            switch (args.length) {
                case 1 -> {
                    String s = args[0];
                    if (Bukkit.getPlayer(s) == null) {
                        World w = Bukkit.getWorld(s);
                        if (w == null) {
                            Player p2 = Bukkit.getPlayer(s);
                            if (p2!=null) {
                                p.teleport(p2.getLocation());
                                sendMessage(sender,"Location.Teleported");
                                return true;
                            }else {
                                sendMessage(sender,"Location.WorldNotFound");
                                return false;
                            }
                        }
                        p.teleport(w.getSpawnLocation());
                        sendMessage(sender, "Location.Teleported");
                        return true;
                    } else {
                        sendMessage(sender, "Location.SpecificLocation");
                        return false;
                    }
                }
                case 2 -> {
                    String s2 = args[0];
                    String w = args[1];//or p2
                    Player p2 = findPlayer(sender, s2);
                    if (p2 != null) {
                        World wo = Bukkit.getWorld(w);
                        if (wo == null) {
                            Player p3 = Bukkit.getPlayer(w);
                            if (p3 != null){
                                p2.teleport(p3.getLocation());
                                sendMessage(sender,"Location.Teleported");
                                return true;
                            }else {
                                sendMessage(sender,"Location.WorldNotFound");
                                return false;
                            }
                        }
                        p.teleport(wo.getSpawnLocation());
                        sendMessage(sender, "Location.Teleported");
                        return true;
                    }
                    return false;
                }
                case 3 -> {
                    if (!args[0].isBlank()&&!args[1].isBlank()&&!args[2].isBlank()) {
                        double x;
                        double y;
                        double z;
                        try {
                            x = Double.parseDouble(args[0]);
                            y = Double.parseDouble(args[1]);
                            z = Double.parseDouble(args[2]);
                        }catch (NumberFormatException e) {
                            sendMessage(sender,"Location.SpecificLocation");
                            return false;
                        }
                        Location l = new Location(p.getWorld(), x, y, z);
                        p.teleport(l);
                        sendMessage(sender,"Location.Teleported");
                        return true;
                    }else {
                        sendMessage(sender, "Command.ArgError");
                        return false;
                    }
                }
                default -> {
                    sendMessage(sender, "Command.ArgError");
                    return false;
                }
                case 4 -> {
                    Player p4 = findPlayer(sender,args[0]);
                    if (p4 != null) {
                        if (!args[1].isBlank() && !args[2].isBlank() && !args[3].isBlank()) {
                            double x;
                            double y;
                            double z;
                            try {
                                x = Double.parseDouble(args[1]);
                                y = Double.parseDouble(args[2]);
                                z = Double.parseDouble(args[3]);
                            } catch (NumberFormatException e) {
                                sendMessage(sender, "Location.SpecificLocation");
                                return false;
                            }
                            Location l = new Location(p.getWorld(), x, y, z);
                            p4.teleport(l);
                            sendMessage(sender, "Location.Teleported");
                            return true;
                        }else {
                            sendMessage(sender, "Command.ArgError");
                            return false;
                        }
                    }
                    return false;
                }
                case 5 -> {
                    if (!args[0].isBlank() && !args[1].isBlank() && !args[2].isBlank() && !args[3].isBlank() &&
                    !args[4].isBlank()) {
                        double x;
                        double y;
                        double z;
                        float pitch;
                        float yaw;
                        try {
                            x = Double.parseDouble(args[0]);
                            y = Double.parseDouble(args[1]);
                            z = Double.parseDouble(args[2]);
                            pitch = Float.parseFloat(args[3]);
                            yaw = Float.parseFloat(args[4]);
                        } catch (NumberFormatException e) {
                            sendMessage(sender, "Location.SpecificLocation");
                            return false;
                        }
                        Location l = new Location(p.getWorld(), x, y, z, pitch,yaw);
                        p.teleport(l);
                        sendMessage(sender, "Location.Teleported");
                        return true;
                    }else {
                        sendMessage(sender, "Command.ArgError");
                        return false;
                    }
                }
                case 6 -> {
                    Player p5 = findPlayer(sender,args[0]);
                    if (p5 != null) {
                        if (!args[1].isBlank() && !args[2].isBlank() && !args[3].isBlank() && !args[4].isBlank() &&
                                !args[5].isBlank()) {
                            double x;
                            double y;
                            double z;
                            float pitch;
                            float yaw;
                            try {
                                x = Double.parseDouble(args[1]);
                                y = Double.parseDouble(args[2]);
                                z = Double.parseDouble(args[3]);
                                pitch = Float.parseFloat(args[4]);
                                yaw = Float.parseFloat(args[5]);
                            } catch (NumberFormatException e) {
                                sendMessage(sender, "Location.SpecificLocation");
                                return false;
                            }
                            Location l = new Location(p.getWorld(), x, y, z, pitch,yaw);
                            p5.teleport(l);
                            sendMessage(sender, "Location.Teleported");
                            return true;
                        }else {
                            sendMessage(sender, "Command.ArgError");
                            return false;
                        }
                    }else {
                        if (!args[0].isBlank() && !args[1].isBlank() && !args[2].isBlank() && !args[3].isBlank() &&
                                !args[4].isBlank() && !args[5].isBlank()) {
                            World w = Bukkit.getWorld(args[0]);
                            double x;
                            double y;
                            double z;
                            float pitch;
                            float yaw;
                            try {
                                x = Double.parseDouble(args[1]);
                                y = Double.parseDouble(args[2]);
                                z = Double.parseDouble(args[3]);
                                pitch = Float.parseFloat(args[4]);
                                yaw = Float.parseFloat(args[5]);
                            } catch (NumberFormatException e) {
                                sendMessage(sender, "Location.SpecificLocation");
                                return false;
                            }
                            if (w == null) {
                                sendMessage(sender,"Location.WorldNotFound");
                                return false;
                            }
                            Location l = new Location(w, x, y, z, pitch,yaw);
                            p.teleport(l);
                            sendMessage(sender, "Location.Teleported");
                            return true;
                        }else {
                            sendMessage(sender, "Command.ArgError");
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }
}
