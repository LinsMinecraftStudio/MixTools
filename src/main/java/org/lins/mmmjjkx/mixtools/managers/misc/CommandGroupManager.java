package org.lins.mmmjjkx.mixtools.managers.misc;

import io.github.linsminecraftstudio.polymer.objects.plugin.AbstractFeatureManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.records.MixToolsCommandGroup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandGroupManager extends AbstractFeatureManager {
    private YamlConfiguration cmdgroup;
    private final File cfgFile = new File(MixTools.getInstance().getDataFolder(), "commandGroup.yml");
    private final List<MixToolsCommandGroup> groups;

    public CommandGroupManager() {
        super(MixTools.getInstance());
        cmdgroup = handleConfig("commandGroup.yml");
        groups = getAllGroups();
    }

    public List<MixToolsCommandGroup> getAllGroups(){
        List<MixToolsCommandGroup> groups = new ArrayList<>();
        for (String key: cmdgroup.getKeys(false)){
            List<String> list = cmdgroup.getStringList(key);
            groups.add(new MixToolsCommandGroup(key, list));
        }
        return groups;
    }

    public void addGroup(String name){
        addGroup(new MixToolsCommandGroup(name, new ArrayList<>()));
    }

    public void addGroup(MixToolsCommandGroup group){
        cmdgroup.set(group.name(), group.commands());
        try {cmdgroup.save(cfgFile);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    public void addCommands(String name, List<String> commands){
        List<String> cmds = cmdgroup.getStringList(name);
        cmds.addAll(commands);
        cmdgroup.set(name, cmds);
        try {cmdgroup.save(cfgFile);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    public Set<String> getAllGroupsName(){
        return cmdgroup.getKeys(false);
    }

    public boolean runCommandGroup(Player p, String name){
        List<String> commands = cmdgroup.getStringList(name);
        if (commands.isEmpty()) {
            MixTools.warn("Command group "+name+" has not commands or it is not exists.");
            return false;
        }
        for (String cmd:commands){
            cmd = parseVariable(p,cmd);
            p.performCommand(cmd);
        }
        return true;
    }

    public boolean runCommandGroupAsConsole(String name){
        List<String> commands = cmdgroup.getStringList(name);
        if (commands.isEmpty()) {
            MixTools.warn("Command group "+name+" has not commands or it is not exists.");
            return false;
        }
        for (String cmd:commands){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd);
        }
        return true;
    }

    private String parseVariable(Player p,String cmd) {
        if (MixTools.hookManager.checkPAPIInstalled()) {
            cmd = PlaceholderAPI.setPlaceholders(p, cmd);
        }
        return cmd.replaceAll("%player%",p.getName());
    }

    public boolean removeGroup(String name) {
        if (groups.removeIf(cg -> cg.name().equals(name))){
            cmdgroup.set(name, null);
            return true;
        }
        return false;
    }

    public boolean containsGroup(String name) {
        return cmdgroup.contains(name);
    }

    @Override
    public void reload() {
        cmdgroup = handleConfig("commandGroup.yml");
    }
}
