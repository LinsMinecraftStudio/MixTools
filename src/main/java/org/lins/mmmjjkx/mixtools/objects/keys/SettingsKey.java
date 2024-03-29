package org.lins.mmmjjkx.mixtools.objects.keys;

import com.zaxxer.hikari.HikariDataSource;
import io.github.linsminecraftstudio.polymer.objects.plugin.SimpleSettingsManager;
import org.lins.mmmjjkx.mixtools.MixTools;

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
    public static String TRASH_PUT_ITEM_SLOTS = "trash.putItemSlots";
    public static String TRASH_ITEM = "trash.item";
    public static String TRASH_SIZE = "trash.size";
    public static String TRASH_ITEM_NAME = "trash.itemName";
    public static String TRASH_CLOSE_BUTTON_ENABLED = "trash.closeButton.enabled";
    public static String TRASH_CLOSE_BUTTON_SLOT = "trash.closeButton.slot";
    public static String TRASH_CLOSE_BUTTON_ITEM = "trash.closeButton.item";
    public static String TRASH_CLOSE_BUTTON_NAME = "trash.closeButton.name";
    public static String KIT_EDITOR_CLOSE_BUTTON_ITEM = "kit.editorCloseButton.item";
    public static String KIT_EDITOR_CLOSE_BUTTON_NAME = "kit.editorCloseButton.name";
    public static String MOTD_ENABLED = "motd.enabled";
    public static String SCOREBOARD_ENABLED = "scoreboard.enabled";
    public static String INVENTORY_DEFAULT_STYLE_DECORATION_TYPE = "inventory.defaultStyle.decoration.type";
    public static String INVENTORY_DEFAULT_STYLE_DECORATION_NAME = "inventory.defaultStyle.decoration.name";
    public static String INVENTORY_DEFAULT_STYLE_CLOSE_BUTTON_TYPE = "inventory.defaultStyle.closeButton.type";
    public static String INVENTORY_DEFAULT_STYLE_CLOSE_BUTTON_NAME = "inventory.defaultStyle.closeButton.name";
    public static HikariDataSource getDataSource() {
        SimpleSettingsManager sm = MixTools.settingsManager;
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
