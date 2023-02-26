package org.lins.mmmjjkx.mixtools.managers.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.home.MixToolsHome;

import java.io.File;
import java.io.IOException;

public class DataManager {
    public void addHome(MixToolsHome home){
        FileConfiguration cs = checkPlayerInData(home.getOwner().getName());
        ConfigurationSection section = cs.getConfigurationSection("homes");
        if (section == null){
            section = cs.createSection("homes");
        }
        Location loc = home.getLoc();
        ConfigurationSection section2 = section.getConfigurationSection(home.getName());
        if (section2 == null){
            section2 = section.createSection(home.getName());
        }else {
            MixTools.messageHandler.sendMessage(home.getOwner(),"Home.Exists");
            return;
        }
        section2.set("world", loc.getWorld().getName());
        section2.set("x", loc.getX());
        section2.set("y", loc.getY());
        section2.set("z", loc.getZ());
        section2.set("pitch", loc.getPitch());
        section2.set("yaw", loc.getYaw());
    }
    public Location getHomeLocation(String owner, String name){
        FileConfiguration cs = checkPlayerInData(owner);
        ConfigurationSection section = cs.getConfigurationSection("homes");
        if (section == null){return null;}
        ConfigurationSection section2 = section.getConfigurationSection(name);
        return new Location(Bukkit.getWorld(section2.getString("world")),
                section2.getDouble("x"), section2.getDouble("y"), section2.getDouble("z"),
                Float.parseFloat(section2.getString("pitch")), Float.parseFloat(section2.getString("yaw")));
    }
    public int getPlayerOwnedHomesAmount(String owner){
        FileConfiguration cs = checkPlayerInData(owner);
        ConfigurationSection section = cs.getConfigurationSection("homes");
        if (section!=null){
            return section.getKeys(false).size();
        }
        return 0;
    }
    public boolean canCreateHomes(Player p){
        int i = 0;
        for (PermissionAttachmentInfo perm : p.getEffectivePermissions()){
            String permstr = perm.getPermission();
            if (permstr.startsWith("mixtools.sethome.")&&perm.getValue()){
                String amount;
                try {
                    amount = permstr.split("\\.")[3];
                }catch (ArrayIndexOutOfBoundsException e) {
                    amount = "0";
                }
                if (amount.equals("unlimited")) return true;
                i = Integer.parseInt(amount);
            }
        }
        return i<getPlayerOwnedHomesAmount(p.getName());
    }
    public void setData(String key, String playerName, double d) {
        FileConfiguration cs = checkPlayerInData(playerName);
        cs.set(key,d);
    }
    public void setData(String key, String playerName, int i){
        FileConfiguration cs = checkPlayerInData(playerName);
        cs.set(key,i);
    }
    public void setData(String key, String playerName, boolean b){
        FileConfiguration cs = checkPlayerInData(playerName);
        cs.set(key,b);
    }
    public void setData(String key, String playerName, String s){
        FileConfiguration cs = checkPlayerInData(playerName);
        cs.set(key,s);
    }
    public String getStringData(String key, String playerName){
        FileConfiguration cs = checkPlayerInData(playerName);
        return cs.getString(key);
    }
    public boolean getBooleanData(String key, String playerName){
        FileConfiguration cs = checkPlayerInData(playerName);
        return cs.getBoolean(key);
    }
    public int getIntegerData(String key, String playerName){
        FileConfiguration cs = checkPlayerInData(playerName);
        return cs.getInt(key);
    }
    public double getDoubleData(String key, String playerName){
        FileConfiguration cs = checkPlayerInData(playerName);
        return cs.getDouble(key);
    }
    private FileConfiguration checkPlayerInData(String playerName){
        File f = new File(MixTools.INSTANCE.getDataFolder(),"data/"+playerName+".yml");
        if (!f.exists()) {
            f.mkdirs();
            try {f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);}
        }
        return YamlConfiguration.loadConfiguration(f);
    }
}
