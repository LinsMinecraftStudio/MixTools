package org.lins.mmmjjkx.mixtools.managers.data;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.MixToolsHome;
import org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey;

import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.MYSQL_ENABLED;

public class DataManager {
    private final boolean isMYSQLEnabled = MixTools.settingsManager.getBoolean(MYSQL_ENABLED);
    private final FileDataManager dataManager = new FileDataManager();
    private MySQLDataManager mysqlDataManager;

    public DataManager() {
        try {mysqlDataManager = isMYSQLEnabled ? new MySQLDataManager(SettingsKey.getDataSource()) : null;
        }catch (Exception ignored){}
    }

    public void addHome(MixToolsHome home){
        if (isMYSQLEnabled){
            try {mysqlDataManager.addHome(home);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            dataManager.addHome(home);
        }
    }
    public Location getHomeLocation(UUID owner, String name){
        if (isMYSQLEnabled){
            try {return mysqlDataManager.getHomeLocation(owner, name);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            return dataManager.getHomeLocation(owner, name);
        }
    }
    public int getPlayerOwnedHomesAmount(Player p){
        if (isMYSQLEnabled){
            try {return mysqlDataManager.getPlayerOwnedHomesAmount(p);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            return dataManager.getPlayerOwnedHomesAmount(p);
        }
    }
    public Set<String> getPlayerOwnedHomesName(Player p){
        if (isMYSQLEnabled){
            try {return mysqlDataManager.getPlayerOwnedHomesName(p);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            return dataManager.getPlayerOwnedHomesName(p);
        }
    }
    public boolean canCreateHomes(Player p){
        if (isMYSQLEnabled){
            try {return mysqlDataManager.canCreateHomes(p);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            return dataManager.canCreateHomes(p);
        }
    }
    public void removeHome(Player p,String name){
        if (isMYSQLEnabled){
            try {mysqlDataManager.removeHome(p, name);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            dataManager.removeHome(p, name);
        }
    }
    /////////////////////////////////////////////////////////////////
    public void setData(String key, UUID playerUUID, Object o) {
        if (isMYSQLEnabled){
            try {mysqlDataManager.setData(key, playerUUID, o);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
           dataManager.setData(key, playerUUID, o);
        }
    }
    public String getStringData(String key, UUID playerUUID){
        if (isMYSQLEnabled){
            try {return mysqlDataManager.getStringData(key, playerUUID);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            return dataManager.getStringData(key, playerUUID);
        }
    }
    public boolean getBooleanData(String key, UUID playerUUID){
        if (isMYSQLEnabled){
            try {return mysqlDataManager.getBooleanData(key, playerUUID);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            return dataManager.getBooleanData(key, playerUUID);
        }
    }
    public int getIntegerData(String key, UUID playerUUID){
        if (isMYSQLEnabled){
            try {return mysqlDataManager.getIntegerData(key, playerUUID);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            return dataManager.getIntegerData(key, playerUUID);
        }
    }
    public double getDoubleData(String key, UUID playerUUID){
        if (isMYSQLEnabled){
            try {return mysqlDataManager.getDoubleData(key, playerUUID);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            return dataManager.getDoubleData(key, playerUUID);
        }
    }
}
