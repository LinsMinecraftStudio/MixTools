package org.lins.mmmjjkx.mixtools.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.misc.WorldManager;
import org.lins.mmmjjkx.mixtools.objects.command.MixTabExecutor;
import org.lins.mmmjjkx.mixtools.utils.FileUtils;
import org.reflections.Reflections;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CMDWorld implements MixTabExecutor {
    private final WorldManager worldManager = MixTools.miscFeatureManager.getWorldManager();

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length==1){
            List<String> list = List.of("create","info","reset","reset-seed","remove","gamerule","load");
            return StringUtil.copyPartialMatches(args[0],list,new ArrayList<>());
        }
        if (args.length==3) {
            switch (args[0]) {
                case "create" -> {
                    List<String> typeNames = new ArrayList<>();
                    for (WorldType worldType : WorldType.values()) {
                        typeNames.add(worldType.toString().toLowerCase());
                    }
                    return StringUtil.copyPartialMatches(args[2], typeNames, new ArrayList<>());
                }
                case "gamerule" -> {
                    List<String> ruleNames = new ArrayList<>();
                    for (GameRule<?> rule : GameRule.values()) {
                        ruleNames.add(rule.getName());
                    }
                    return StringUtil.copyPartialMatches(args[2], ruleNames, new ArrayList<>());
                }
            }
        }else if (args.length==4 & args[0].equals("create")){
            List<String> typeNames = new ArrayList<>();
            for (World.Environment environmentType : World.Environment.values()) {
                typeNames.add(environmentType.toString().toLowerCase());
            }
            return StringUtil.copyPartialMatches(args[3], typeNames, new ArrayList<>());
        }else if (args.length == 6 & args[0].equals("create")){
            Reflections reflections = new Reflections("");
            Set<Class<? extends ChunkGenerator>> customGenerators = reflections.getSubTypesOf(ChunkGenerator.class);
            List<String> generatorNames = new ArrayList<>();
            for (Class<? extends ChunkGenerator> clazz : customGenerators) {
                generatorNames.add(clazz.getSimpleName());
            }
            return StringUtil.copyPartialMatches(args[5], generatorNames, new ArrayList<>());
        }
        if (args.length==2) {
            if (!args[0].equals("create") && !args[0].equals("load")) {
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
        if (hasCustomPermission(sender, "world")) {
            if (args.length == 4) {
                switch (args[0]) {
                    case "create" -> {
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
                        if (Bukkit.getWorld(worldName) != null) {
                            sendMessage(sender, "World.Exists", worldName);
                            return false;
                        }
                        WorldCreator worldCreator = new WorldCreator(worldName);
                        worldCreator = worldCreator.type(wt);
                        World w = Bukkit.createWorld(worldCreator);
                        worldManager.addWorld(w, wt);
                        sendMessage(sender, "World.CreateSuccess", worldName);
                        return true;
                    }
                    case "gamerule" -> {
                        World w = Bukkit.getWorld(args[1]);
                        if (w == null) {
                            sendMessage(sender, "Location.WorldNotFound");
                            return false;
                        }
                        GameRule rule = GameRule.getByName(args[2]);
                        if (rule == null) {
                            sendMessage(sender, "World.GameRuleNotFound");
                            return false;
                        }
                        Object obj = args[3];
                        w.setGameRule(rule, obj);
                        sendMessage(sender, "World.GameRuleSet",
                                w.getName(),
                                rule.getName(), args[3]);
                        return true;
                    }
                }
            } else if (args.length == 5) {
                if (args[0].equals("create")) {
                    String worldName = args[1];
                    WorldType wt = stringToWorldType(args[2].toUpperCase());
                    World.Environment environment = stringToWorldEnvironment(args[3].toUpperCase());
                    long seed;
                    try {
                        seed = Long.parseLong(args[4]);
                    } catch (Exception e) {
                        sendMessage(sender, "Value.NotLong");
                        return false;
                    }
                    if (wt == null) {
                        sendMessage(sender, "World.TypeInvalid");
                        return false;
                    }
                    if (environment == null) {
                        sendMessage(sender, "World.EnvironmentTypeInvalid");
                        return false;
                    }
                    if (Bukkit.getWorld(worldName) != null) {
                        sendMessage(sender, "World.Exists", worldName);
                        return false;
                    }
                    WorldCreator worldCreator = new WorldCreator(worldName);
                    worldCreator = worldCreator.type(wt);
                    worldCreator = worldCreator.seed(seed);
                    World w = Bukkit.createWorld(worldCreator);
                    worldManager.addWorld(w, wt);
                    sendMessage(sender, "Created", worldName);
                    return true;
                }
            } else if (args.length == 6) {
                if (args[0].equals("create")){
                    String worldName = args[1];
                    WorldType wt = stringToWorldType(args[2].toUpperCase());
                    World.Environment environment = stringToWorldEnvironment(args[3].toUpperCase());
                    ChunkGenerator generator = stringToChunkGenerator(args[4]);
                    long seed;
                    try {
                        seed = Long.parseLong(args[4]);
                    } catch (Exception e) {
                        sendMessage(sender, "Value.NotLong");
                        return false;
                    }
                    if (wt == null) {
                        sendMessage(sender, "World.TypeInvalid");
                        return false;
                    }
                    if (environment == null) {
                        sendMessage(sender, "World.EnvironmentTypeInvalid");
                        return false;
                    }
                    if (generator == null) {
                        sendMessage(sender, "World.InvalidGenerator");
                        return false;
                    }
                    if (Bukkit.getWorld(worldName) != null) {
                        sendMessage(sender, "World.Exists", worldName);
                        return false;
                    }
                    WorldCreator worldCreator = new WorldCreator(worldName);
                    worldCreator = worldCreator.type(wt);
                    worldCreator = worldCreator.seed(seed);
                    worldCreator = worldCreator.generator(generator);
                    World w = Bukkit.createWorld(worldCreator);
                    worldManager.addWorld(w, wt);
                    sendMessage(sender, "Created", worldName);
                    return true;
                }
            } else if (args.length == 2) {
                World w = Bukkit.getWorld(args[1]);
                switch (args[0]) {
                    case "info" -> {
                        if (w == null) {
                            sendMessage(sender, "Location.WorldNotFound");
                            return false;
                        }
                        List<String> messages = MixTools.messageHandler.
                                getColoredMessagesParseVarPerLine("World.Info",
                                        w.getName(), worldManager.getWorldType(w.getName()),
                                        worldManager.getWorldAlias(w.getName()),
                                        worldManager.getWorldEnvironment(w.getName()),
                                        w.getSeed(), w.getPVP());
                        MixTools.messageHandler.sendMessages(sender, messages);
                        return true;
                    }
                    case "reset" -> {
                        if (w == null) {
                            sendMessage(sender, "Location.WorldNotFound");
                            return false;
                        }
                        String name = w.getName();
                        long seed = w.getSeed();
                        File wf = w.getWorldFolder();
                        teleportPlayersToSpawn(name);
                        Bukkit.unloadWorld(w, false);
                        FileUtils.deleteDir(wf);
                        WorldCreator worldCreator = new WorldCreator(name).seed(seed);
                        teleportPlayersToSpawn(name);
                        Bukkit.createWorld(worldCreator);
                        sendMessage(sender, "World.Reset", w.getName());
                        return true;
                    }
                    case "reset-seed" -> {
                        if (w == null) {
                            sendMessage(sender, "Location.WorldNotFound");
                            return false;
                        }
                        String name2 = w.getName();
                        File wf2 = w.getWorldFolder();
                        teleportPlayersToSpawn(name2);
                        Bukkit.unloadWorld(w, false);
                        FileUtils.deleteDir(wf2);
                        WorldCreator worldCreator2 = new WorldCreator(name2);
                        worldCreator2 = worldCreator2.type(Objects.requireNonNull(worldManager.getWorldType(name2)));
                        Bukkit.createWorld(worldCreator2);
                        sendMessage(sender, "World.ResetNewSeed", w.getName());
                        return true;
                    }
                    case "remove" -> {
                        if (w == null) {
                            sendMessage(sender, "Location.WorldNotFound");
                            return false;
                        }
                        File wf3 = w.getWorldFolder();
                        teleportPlayersToSpawn(w.getName());
                        if (Bukkit.unloadWorld(w, false)) {
                            FileUtils.deleteDir(wf3);
                        }
                        worldManager.removeWorldFromConfig(w.getName());
                        sendMessage(sender, "World.Removed", w.getName());
                        return true;
                    }
                    default -> {
                        sendMessage(sender, "Command.ArgError");
                        return false;
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equals("load")) {
                    WorldType type = stringToWorldType(args[2]);
                    if (type == null) sendMessage(sender, "World.TypeInvalid");
                    boolean result = worldManager.loadWorld(args[1], type);
                    if (!result) {
                        sendMessage(sender, "World.LoadFailed");
                        return false;
                    }
                    sendMessage(sender, "World.LoadSuccess");
                    return true;
                }
            } else {
                sendMessage(sender, "Command.ArgError");
                return false;
            }
            return false;
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

    private void teleportPlayersToSpawn(String worldName){
        for (Player p : Bukkit.getOnlinePlayers()){
            Location spawn = MixTools.settingsManager.getSpawnLocation();
            if (worldName.equals(p.getWorld().getName())){
                p.teleport(Objects.requireNonNullElse(spawn, Bukkit.getWorlds().get(0).getSpawnLocation()));
            }
        }
    }

    private ChunkGenerator stringToChunkGenerator(String s){
        if (s == null) return null;
        if (s.isBlank()) return null;
        try{
            Class<?> c = Class.forName(s);
            if (!ChunkGenerator.class.isAssignableFrom(c))return null;
            return (ChunkGenerator) c.getDeclaredConstructor().newInstance();
        }catch (Exception e) {
            return null;
        }
    }
}
