package org.lins.mmmjjkx.mixtools.managers.data;

import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.lins.mmmjjkx.mixtools.objects.MixToolsHome;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class MySQLDataManager {
    private final Connection conn;
    public MySQLDataManager(HikariDataSource dataSource) throws SQLException {
        try {conn = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);}
        setupTables();
    }
    private void setupTables() throws SQLException{
        PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS mixtools_data " +
                "(name text NOT NULL PRIMARY KEY, has_economy_account BOOLEAN NOT NULL, economy_money, NOT NULL)");
        ps.execute();
        PreparedStatement ps2 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS mixtools_homes (name text NOT NULL, " +
                "owner text NOT NULL PRIMARY KEY, " +
                "world TEXT NOT NULL, x DOUBLE NOT NULL, y DOUBLE NOT NULL, z DOUBLE NOT NULL, pitch FLOAT NOT NULL, yaw FLOAT NOT NULL)");
        ps2.execute();
    }

    public void checkPlayerInData(String playerName) throws SQLException {
        ResultSet rs = conn.prepareStatement("select * from mixtools_data where name = "+playerName).executeQuery();
        if (rs.getRow()==0){
            conn.prepareStatement("INSERT INTO mixtools_data (name, has_economy_account, economy_money) VALUES ("+playerName
                    +",true,1").execute();
        }
    }

    public void addHome(MixToolsHome home) throws SQLException {
        Location location = home.getOwner().getLocation();
        World w = location.getWorld();
        if (w ==null){
            return;
        }
        conn.prepareStatement("INSERT INTO mixtools_homes (name, owner, world, x, y, z, pitch, yaw) VALUES ("+
                home.getName()+", "+home.getOwner().getName()+", "+w.getName()+", "+location.getX() + ", "+location.getY() + ", "+location.getZ() + ", "+
                location.getPitch() + ", "+location.getYaw() + ")").execute();
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
    public void removeHome(Player p,String name) throws SQLException {
        conn.prepareStatement("DELETE FORM mixtools_homes WHERE owner = "+p.getName()+" AND name = "+name).execute();
    }
    /////////////////////////////////////////////////////////////////
    public void setData(String key, String playerName, Object obj) throws SQLException {
        checkPlayerInData(playerName);
        conn.prepareStatement("UPDATE mixtools_data SET "+key+" = "+obj.toString()+" WHERE name = "+playerName).execute();
    }
    public String getStringData(String key, String playerName) throws SQLException {
        checkPlayerInData(playerName);
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM mixtools_data WHERE name = "+playerName);
        ResultSet rs = ps.executeQuery();
        return rs.getString(key);
    }
    public boolean getBooleanData(String key, String playerName) throws SQLException {
        checkPlayerInData(playerName);
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM mixtools_data WHERE name = "+playerName);
        ResultSet rs = ps.executeQuery();
        return rs.getBoolean(key);
    }
    public int getIntegerData(String key, String playerName) throws SQLException {
        checkPlayerInData(playerName);
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM mixtools_data WHERE name = "+playerName);
        ResultSet rs = ps.executeQuery();
        return rs.getInt(key);
    }
    public double getDoubleData(String key, String playerName) throws SQLException {
        checkPlayerInData(playerName);
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM mixtools_data WHERE name = "+playerName);
        ResultSet rs = ps.executeQuery();
        return rs.getDouble(key);
    }
}
