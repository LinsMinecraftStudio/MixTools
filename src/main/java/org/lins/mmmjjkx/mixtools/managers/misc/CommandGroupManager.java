package org.lins.mmmjjkx.mixtools.managers.misc;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.MixToolsCommandGroup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommandGroupManager {
    private FileConfiguration cmdgroup;

    public CommandGroupManager() {
        setup();
    }

    private void setup() {
        File f = new File(MixTools.INSTANCE.getDataFolder(), "commandGroup.yml");
        if (!f.exists()) {
            try {f.createNewFile();
            } catch (IOException e) {throw new RuntimeException(e);}
        }
        cmdgroup = YamlConfiguration.loadConfiguration(f);
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
    }

    public Set<String> getAllGroupsName(){
        return cmdgroup.getKeys(false);
    }

    public void runCommandGroup(CommandSender from,Player p, String name){
        List<String> commands = cmdgroup.getStringList(name);
        if (commands.isEmpty()) {
            MixTools.messageHandler.sendMessage(from, "Command.CommandGroupNotFound");
            return;
        }
        for (String cmd:commands){
            cmd = parseVariable(p,cmd);
            p.performCommand(cmd);
        }
    }

    private String parseVariable(Player p,String cmd) {
        if (MixTools.hookManager.checkPAPIInstalled()){
            return PlaceholderAPI.setPlaceholders(p,cmd);
        }else {
            return cmd.replaceAll("<player>",p.getName());
        }
    }
}
