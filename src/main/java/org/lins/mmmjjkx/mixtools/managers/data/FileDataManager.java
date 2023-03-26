package org.lins.mmmjjkx.mixtools.managers.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.MixToolsHome;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class FileDataManager {
    public void addHome(MixToolsHome home){
        FileConfiguration cs = checkPlayerInData(home.owner().getUniqueId());
        ConfigurationSection section = cs.getConfigurationSection("homes");
        if (section == null){
            section = cs.createSection("homes");
        }
        Location loc = home.loc();
        ConfigurationSection section2 = section.getConfigurationSection(home.name());
        if (section2 == null){
            section2 = section.createSection(home.name());
        }else {
            MixTools.messageHandler.sendMessage(home.owner(),"Home.Exists");
            return;
        }
        if (loc.getWorld() != null) {
            section2.set("world", loc.getWorld().getName());
            section2.set("x", loc.getX());
            section2.set("y", loc.getY());
            section2.set("z", loc.getZ());
            section2.set("pitch", loc.getPitch());
            section2.set("yaw", loc.getYaw());
        }
    }
    public Location getHomeLocation(UUID owner, String name){
        FileConfiguration cs = checkPlayerInData(owner);
        ConfigurationSection section = cs.getConfigurationSection("homes");
        if (section == null){return null;}
        ConfigurationSection section2 = section.getConfigurationSection(name);
        if (section2 == null){return null;}
        return new Location(Bukkit.getWorld(section2.getString("world","world")),
                section2.getDouble("x"), section2.getDouble("y"), section2.getDouble("z"),
                Float.parseFloat(section2.getString("pitch","0")), Float.parseFloat(section2.getString("yaw","0")));
    }
    public int getPlayerOwnedHomesAmount(Player p){
        FileConfiguration cs = checkPlayerInData(p.getUniqueId());
        ConfigurationSection section = cs.getConfigurationSection("homes");
        if (section!=null){
            return section.getKeys(false).size();
        }
        return 0;
    }
    public Set<String> getPlayerOwnedHomesName(Player p){
        FileConfiguration cs = checkPlayerInData(p.getUniqueId());
        ConfigurationSection section = cs.getConfigurationSection("homes");
        if (section!=null){
            return section.getKeys(false);
        }
        return null;
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
        return i>getPlayerOwnedHomesAmount(p);
    }
    public void removeHome(Player p,String name){
        FileConfiguration cs = checkPlayerInData(p.getUniqueId());
        ConfigurationSection section = cs.getConfigurationSection("homes");
        if (section == null){
            MixTools.messageHandler.sendMessage(p,"Home.NoAnyHomes");
            return;
        }
        ConfigurationSection section2 = section.getConfigurationSection(name);
        if (section2 == null){
            MixTools.messageHandler.sendMessage(p,"Home.NotExists");
            return;
        }
        section.set(name,null);
    }
    /////////////////////////////////////////////////////////////////
    public void setData(String key, UUID playerUUID, Object o) {
        FileConfiguration cs = checkPlayerInData(playerUUID);
        cs.set(key,o);
    }
    public String getStringData(String key, UUID playerUUID){
        FileConfiguration cs = checkPlayerInData(playerUUID);
        return cs.getString(key,"");
    }
    public boolean getBooleanData(String key, UUID playerUUID){
        FileConfiguration cs = checkPlayerInData(playerUUID);
        return cs.getBoolean(key);
    }
    public int getIntegerData(String key, UUID playerUUID){
        FileConfiguration cs = checkPlayerInData(playerUUID);
        return cs.getInt(key);
    }
    public double getDoubleData(String key, UUID playerUUID){
        FileConfiguration cs = checkPlayerInData(playerUUID);
        return cs.getDouble(key);
    }
    public FileConfiguration checkPlayerInData(UUID playerUUID) {
        File f2 = new File(MixTools.INSTANCE.getDataFolder()+File.separator+
                "data", playerUUID + ".yml");
        if (!f2.exists()) {
            try {
                f2.getParentFile().mkdirs();
                f2.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        f2 = new File(MixTools.INSTANCE.getDataFolder(), "data/" + playerUUID.toString() + ".yml");
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(f2);
        return configuration;
    }
}
