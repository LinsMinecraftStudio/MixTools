package org.lins.mmmjjkx.mixtools.objects.command;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public interface MixTabExecutor extends MixCommandExecutor, TabExecutor {
    @Override
    default void register() {
        String require = requirePlugin();
        if (require == null){
            require = "";
        }
        if (!require.isBlank()){
            if (Bukkit.getPluginManager().isPluginEnabled(require)){
                PluginCommand cmd = MixTools.INSTANCE.getCommand(name());
                cmd.setExecutor(this);
                cmd.setTabCompleter(this);
            }
        }else {
            PluginCommand cmd = MixTools.INSTANCE.getCommand(name());
            cmd.setExecutor(this);
            cmd.setTabCompleter(this);
        }
    }

    default List<String> getPlayerNames(){
        List<String> list = new ArrayList<>();
        for (Player p: Bukkit.getOnlinePlayers()){
            list.add(p.getName());
        }
        return list;
    }
}
