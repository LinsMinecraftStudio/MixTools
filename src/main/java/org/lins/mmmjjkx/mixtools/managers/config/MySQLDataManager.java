package org.lins.mmmjjkx.mixtools.managers.config;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.objects.home.MixToolsHome;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.MYSQL_ENABLED;

public class MySQLDataManager {
    private Connection conn;
    public MySQLDataManager(HikariDataSource dataSource){
        boolean isMYSQLEnabled = MixTools.settingsManager.getBoolean(MYSQL_ENABLED);
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
        PreparedStatement ps2 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS mixtools_homes (name text NOT NULL, " +
                "owner text NOT NULL PRIMARY KEY, " +
                "world TEXT NOT NULL, x DOUBLE NOT NULL, y DOUBLE NOT NULL, z DOUBLE NOT NULL, pitch FLOAT NOT NULL, yaw FLOAT NOT NULL)");
        ps2.execute();
    }


    public void addHome(MixToolsHome home) throws SQLException {
        Location location = home.getOwner().getLocation();
        World w = location.getWorld();
        if (w ==null){
            return;
        }
        PreparedStatement ps = conn.prepareStatement("INSERT INTO mixtools_homes (name, owner, world, x, y, z, pitch, yaw) VALUES ("+
                home.getName()+", "+home.getOwner().getName()+", "+w.getName()+", "+location.getX() + ", "+location.getY() + ", "+location.getZ() + ", "+
                location.getPitch() + ", "+location.getYaw() + ")");
        ps.execute();
    }
    public Location getHomeLocation(String owner, String name) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FORM mixtools_homes WHERE owner = "+owner+" AND name = "+name);
        ResultSet rs = ps.executeQuery();
        return new Location(Bukkit.getWorld(rs.getString("world")), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"),
                rs.getFloat("pitch"), rs.getFloat("yaw"));
    }
    public int getPlayerOwnedHomesAmount(Player p) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM mixtools_homes WHERE owner = "+p.getName());
        return ps.executeQuery().getRow();
    }
    public Set<String> getPlayerOwnedHomesName(Player p) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM mixtools_homes WHERE owner = "+p.getName());
        Set<String> names = new HashSet<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            names.add(rs.getString("name"));
        }
        return names;
    }
    public boolean canCreateHomes(Player p) throws SQLException {
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
