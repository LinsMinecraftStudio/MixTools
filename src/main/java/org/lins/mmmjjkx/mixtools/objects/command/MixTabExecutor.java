package org.lins.mmmjjkx.mixtools.objects.command;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.util.ArrayList;
import java.util.List;

public interface MixTabExecutor extends MixCommandExecutor, TabCompleter {
    @Override
    default void register() {
        String require = requirePlugin();
        if (require == null){
            require = "";
        }
        if (!require.isBlank()){
            if (Bukkit.getPluginManager().isPluginEnabled(require)){
                try {
                    PluginCommand cmd = MixTools.INSTANCE.getCommand(name());
                    cmd.setExecutor(this);
                    cmd.setTabCompleter(this);
                }catch (Exception e) {
                    MixTools.INSTANCE.getLogger().warning("Failed to register command '"+name()+"' : "+e.getMessage());
                }

            }
        }else {
            try {
                PluginCommand cmd = MixTools.INSTANCE.getCommand(name());
                cmd.setExecutor(this);
                cmd.setTabCompleter(this);
            }catch (Exception e) {
                MixTools.INSTANCE.getLogger().warning("Failed to register command '"+name()+"' : "+e.getMessage());
            }
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
