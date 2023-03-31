package org.lins.mmmjjkx.mixtools.objects.keys;

import com.zaxxer.hikari.HikariDataSource;
import org.lins.mmmjjkx.mixtools.MixTools;
import org.lins.mmmjjkx.mixtools.managers.SettingsManager;

public class SettingsKey {
    public static String CHECK_UPDATE = "checkUpdate";
    public static String CURRENCY_SYMBOL = "economy.currencySymbol";
    public static String INITIAL_CURRENCY = "economy.initial-currency";
    public static String PLAYER_JOIN_MESSAGE = "messages.join";
    public static String PLAYER_QUIT_MESSAGE = "messages.quit";
    public static String PLAYER_WELCOME_MESSAGE = "messages.welcome";
    public static String STRING_REGEX = "regex.string";
    public static String NAME_REGEX = "regex.name";
    public static String NAME_CHECK = "nameCheck";
    public static String TPA_EXPIRE_TIME = "tpaExpireTime";
    public static String MYSQL_ENABLED = "mysql.enabled";
    public static String TRASH_PUT_THING_SLOTS = "trash.putThingSlots";
    public static String TRASH_PUT_THING = "trash.putThing";
    public static String TRASH_SIZE = "trash.size";
    public static HikariDataSource getDataSource() {
        SettingsManager sm = MixTools.settingsManager;
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(sm.getString("mysql.driver-class-name"));
        dataSource.setJdbcUrl(sm.getString("mysql.jdbc-url"));
        dataSource.setUsername(sm.getString("mysql.username"));
        dataSource.setPassword(sm.getString("mysql.password"));
        dataSource.setMaxLifetime(sm.getLong("mysql.properties.max-lifetime"));
        dataSource.setMinimumIdle(sm.getInt("mysql.properties.minimum-idle"));
        dataSource.setIdleTimeout(sm.getLong("mysql.properties.idle-timeout"));
        dataSource.setMaximumPoolSize(sm.getInt("mysql.properties.maximum-pool-size"));
        dataSource.setConnectionTimeout(sm.getLong("mysql.properties.connection-timeout"));
        return dataSource;
    }
}
