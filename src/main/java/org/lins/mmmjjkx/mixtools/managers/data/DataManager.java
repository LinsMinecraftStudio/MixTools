package org.lins.mmmjjkx.mixtools.managers.data;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.home.MixToolsHome;
import org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey;

import java.sql.SQLException;
import java.util.Set;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.MYSQL_ENABLED;

public class DataManager {
    private final boolean isMYSQLEnabled = MixTools.settingsManager.getBoolean(MYSQL_ENABLED);

    private final FileDataManager dataManager = isMYSQLEnabled ? null : new FileDataManager();

    private final MySQLDataManager mysqlDataManager = isMYSQLEnabled ? new MySQLDataManager(SettingsKey.getDataSource()) : null;

    public DataManager() throws SQLException {}

    public void addHome(MixToolsHome home){
        if (isMYSQLEnabled){
            try {mysqlDataManager.addHome(home);
            } catch (SQLException e) {throw new RuntimeException(e);}
        }else {
            dataManager.addHome(home);
        }
    }
    public Location getHomeLocation(String owner, String name){
    }
    public int getPlayerOwnedHomesAmount(Player p){
    }
    public Set<String> getPlayerOwnedHomesName(Player p){
    }
    public boolean canCreateHomes(Player p){
    }
    public void removeHome(Player p,String name){
    }
    /////////////////////////////////////////////////////////////////
    public void setData(String key, String playerName, Object o) {
    }
    public String getStringData(String key, String playerName){
    }
    public boolean getBooleanData(String key, String playerName){
    }
    public int getIntegerData(String key, String playerName){
    }
    public double getDoubleData(String key, String playerName){
    }
    private FileConfiguration checkPlayerInData(String playerName){
    }
}
