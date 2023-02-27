package org.lins.mmmjjkx.mixtools.managers.config;

import com.zaxxer.hikari.HikariDataSource;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Set;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.MYSQL_ENABLED;

public class MySQLDataManager {
    private final boolean isMYSQLEnabled = MixTools.settingsManager.getBoolean(MYSQL_ENABLED);
    private Connection conn;
    public MySQLDataManager(HikariDataSource dataSource){
        if (isMYSQLEnabled){
            try {conn = dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);}

        }
    }
    private void setupTables() throws SQLException{
        PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS mixtools_economy " +
                "(name text NOT NULL PRIMARY KEY, has_economy_account INTEGER NOT NULL, economy_money, NOT NULL)");
        ps.execute();
        PreparedStatement ps2 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS mixtools_homes (name text NOT NULL PRIMARY KEY, owner text NOT NULL PRIMARY KEY, " +
                "x REAL NOT NULL, y REAL NOT NULL, z REAL NOT NULL, pitch REAL NOT NULL, yaw REAL NOT NULL)");
        ps2.execute();
    }


    public void addHome(MixToolsHome home){

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
    public void setData(String key, String playerName, double d) {
    }
    public void setData(String key, String playerName, int i){
    }
    public void setData(String key, String playerName, boolean b){
    }
    public void setData(String key, String playerName, String s){

    }
    public String getStringData(String key, String playerName){
    }
    public boolean getBooleanData(String key, String playerName){
    }
    public int getIntegerData(String key, String playerName){
    }
    public double getDoubleData(String key, String playerName){
    }
    private FileConfiguration checkPlayerInData(String playerName) {
    }
}
