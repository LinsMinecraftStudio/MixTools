package org.lins.mmmjjkx.mixtools.managers.data;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.MixToolsHome;

import java.sql.SQLException;
import java.util.Set;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.MYSQL_ENABLED;

public class DataManager {
    private final boolean isMYSQLEnabled = MixTools.settingsManager.getBoolean(MYSQL_ENABLED);
    private final FileDataManager dataManager;
    private final MySQLDataManager mysqlDataManager;

    public DataManager(HikariDataSource dataSource) throws SQLException {
        dataManager = new FileDataManager();
        mysqlDataManager = isMYSQLEnabled ? new MySQLDataManager(dataSource) : null;
    }

    public void addHome(MixToolsHome home){
        if (isMYSQLEnabled){
            try {mysqlDataManager.addHome(home);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            dataManager.addHome(home);
        }
    }
    public Location getHomeLocation(String owner, String name){
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
    public void setData(String key, String playerName, Object o) {
        if (isMYSQLEnabled){
            try {mysqlDataManager.setData(key, playerName, o);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
           dataManager.setData(key, playerName, o);
        }
    }
    public String getStringData(String key, String playerName){
        if (isMYSQLEnabled){
            try {return mysqlDataManager.getStringData(key, playerName);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            return dataManager.getStringData(key, playerName);
        }
    }
    public boolean getBooleanData(String key, String playerName){
        if (isMYSQLEnabled){
            try {return mysqlDataManager.getBooleanData(key, playerName);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            return dataManager.getBooleanData(key, playerName);
        }
    }
    public int getIntegerData(String key, String playerName){
        if (isMYSQLEnabled){
            try {return mysqlDataManager.getIntegerData(key, playerName);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            return dataManager.getIntegerData(key, playerName);
        }
    }
    public double getDoubleData(String key, String playerName){
        if (isMYSQLEnabled){
            try {return mysqlDataManager.getDoubleData(key, playerName);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            return dataManager.getDoubleData(key, playerName);
        }
    }

    public void checkPlayerInMysqlData(String name){
        try {mysqlDataManager.checkPlayerInData(name);
        } catch (SQLException e) {throw new RuntimeException(e);}
    }

    public void checkPlayerInData(String name){
        dataManager.checkPlayerInData(name);
    }
}
