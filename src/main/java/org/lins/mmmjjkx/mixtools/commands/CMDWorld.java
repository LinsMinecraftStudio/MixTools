package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.misc.WorldManager;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CMDWorld implements MixTabExecutor {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            return StringUtil.copyPartialMatches(args[0],
                    List.of("create","info","reset","reset-seed","remove"),new ArrayList<>());
        }
        if (args.length==2) {
            if (args[0].equals("create")) {
                List<String> typeNames = new ArrayList<>();
                for (WorldType worldType : WorldType.values()) {
                    typeNames.add(worldType.toString());
                }
                return StringUtil.copyPartialMatches(args[1], typeNames, new ArrayList<>());
            } else {
                List<String> worldNames = new ArrayList<>();
                for (World w : Bukkit.getWorlds()) {
                    worldNames.add(w.getName());
                }
                return StringUtil.copyPartialMatches(args[1], worldNames, new ArrayList<>());
            }
        }
        return null;
    }

    @Override
    public String name() {
        return "world";
    }

    @Override
    public String requirePlugin() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (hasCustomPermission(sender,"world")){
            if (args.length==4 & args[0].equals("create")) {
                String worldName = args[1];
                WorldType wt = stringToWorldType(args[2].toUpperCase());
                World.Environment environment = stringToWorldEnvironment(args[3].toUpperCase());
                if (wt == null) {
                    sendMessage(sender, "World.TypeInvalid");
                    return false;
                }
                if (environment == null) {
                    sendMessage(sender, "World.EnvironmentTypeInvalid");
                    return false;
                }
                if (Bukkit.getWorld(worldName) != null){
                    sendMessage(sender, "World.Exists", worldName);
                    return false;
                }
                WorldCreator worldCreator = new WorldCreator(worldName);
                worldCreator = worldCreator.type(wt);
                Bukkit.createWorld(worldCreator);
                sendMessage(sender, "Created", worldName);
            } else if (args.length==2) {
                World w = Bukkit.getWorld(args[1]);
                WorldManager worldManager = MixTools.miscFeatureManager.getWorldManager();
                if (w == null) {
                    sendMessage(sender, "Location.WorldNotFound");
                    return false;
                }
                switch (args[0]) {
                    case "info" -> {
                        List<String> messages = MixTools.messageHandler.
                                getColoredMessagesParseVarPerLine("World.Info",
                                        w.getName(),worldManager.getWorldType(w.getName()),
                                        worldManager.getWorldAlias(w.getName()),
                                        worldManager.getWorldEnvironment(w.getName()),
                                        w.getPVP());
                        MixTools.messageHandler.sendMessages(sender, messages);
                        return true;
                    }
                    case "reset" -> {
                        String name = w.getName();
                        long seed = w.getSeed();
                        File wf = w.getWorldFolder();
                        Bukkit.unloadWorld(w, false);
                        wf.delete();
                        WorldType type = worldManager.getWorldType(name);
                        WorldCreator worldCreator = new WorldCreator(name).seed(seed);
                        if (type != null) {
                            worldCreator = worldCreator.type(type);
                        }
                        Bukkit.createWorld(worldCreator);
                        sendMessage(sender, "World.Reset", w.getName());
                        return true;
                    }
                    case "reset-seed" -> {
                        String name2 = w.getName();
                        File wf2 = w.getWorldFolder();
                        Bukkit.unloadWorld(w, false);
                        wf2.delete();
                        WorldType type2 = worldManager.getWorldType(name2);
                        WorldCreator worldCreator2 = new WorldCreator(name2);
                        if (type2 != null) {
                            worldCreator2 = worldCreator2.type(type2);
                        }
                        Bukkit.createWorld(worldCreator2);
                        sendMessage(sender, "World.ResetNewSeed", w.getName());
                        return true;
                    }
                    case "remove" -> {
                        File wf3 = w.getWorldFolder();
                        Bukkit.unloadWorld(w, false);
                        wf3.delete();
                        worldManager.removeWorld(w.getName());
                        sendMessage(sender, "World.Removed", w.getName());
                        return true;
                    }
                }
            }else {
                sendMessage(sender,"Command.ArgError");
                return false;
            }
        }
        return false;
    }

    private WorldType stringToWorldType(String s){
        WorldType wt;
        try {
            wt = WorldType.valueOf(s);
        }catch (Exception e){
            return null;
        }
        return wt;
    }

    private World.Environment stringToWorldEnvironment(String s){
        World.Environment environment;
        try {
            environment = World.Environment.valueOf(s);
        }catch (Exception e){
            return null;
        }
        return environment;
    }
}
