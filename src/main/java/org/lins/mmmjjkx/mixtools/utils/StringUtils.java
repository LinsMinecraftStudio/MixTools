package org.lins.mmmjjkx.mixtools.utils;

import org.bukkit.ChatColor;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.io.InputStream;
import java.net.URL;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.NAME_REGEX;
import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.STRING_REGEX;

public class StringUtils {
    private static final Pattern STRIP_ALL_PATTERN = Pattern.compile(ChatColor.COLOR_CHAR + "+([0-9a-fk-orA-FK-OR])");
    private static final Pattern STRIP_RGB_PATTERN = Pattern.compile(ChatColor.COLOR_CHAR + "x((?:" + ChatColor.COLOR_CHAR + "[0-9a-fA-F]){6})");
    public static String unformatString(final String message) {
        if (message == null) {
            return null;
        }
        return unformatString(message, EnumSet.allOf(ChatColor.class), true);
    }
    public static String unformatString(String message, final EnumSet<ChatColor> supported, boolean rgb) {
        if (message == null) {
            return null;
        }

        final StringBuilder rgbBuilder = new StringBuilder();
        final Matcher rgbMatcher = STRIP_RGB_PATTERN.matcher(message);
        while (rgbMatcher.find()) {
            final String code = rgbMatcher.group(1).replace(String.valueOf(ChatColor.COLOR_CHAR), "");
            if (rgb) {
                rgbMatcher.appendReplacement(rgbBuilder, "&#" + code);
                continue;
            }
            rgbMatcher.appendReplacement(rgbBuilder, "");
        }
        rgbMatcher.appendTail(rgbBuilder);
        message = rgbBuilder.toString();

        final StringBuilder builder = new StringBuilder();
        final Matcher matcher = STRIP_ALL_PATTERN.matcher(message);
        searchLoop:
        while (matcher.find()) {
            final char code = matcher.group(1).toLowerCase(Locale.ROOT).charAt(0);
            for (final ChatColor color : supported) {
                if (color.getChar() == code) {
                    matcher.appendReplacement(builder, "&" + code);
                    continue searchLoop;
                }
            }
            matcher.appendReplacement(builder, "");
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    public static boolean matchStringRegex(String str){
        String regex = MixTools.settingsManager.getString(STRING_REGEX);
        if (regex.isBlank()) return true;
        return str.matches(regex);
    }

    public static boolean matchNameRegex(String str){
        String regex = MixTools.settingsManager.getString(NAME_REGEX);
        if (regex.isBlank()) return true;
        return str.matches(regex);
    }

    public static String getPluginLatestVersion() {
        Callable<String> callable = () -> {
            InputStream stream = new URL("https://api.spigotmc.org/legacy/update.php?resource=109130").openStream();
            Scanner scanner = new Scanner(stream);
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()){
                builder.append(scanner.nextLine());
            }
            return builder.toString();
        };
        FutureTask<String> task = new FutureTask<>(callable);
        new Thread(task).start();
        try {return task.get();
        } catch (Exception e) {
            return "";
        }
    }
}