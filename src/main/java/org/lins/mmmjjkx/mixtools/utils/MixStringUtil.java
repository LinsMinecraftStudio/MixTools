package org.lins.mmmjjkx.mixtools.utils;

import org.bukkit.ChatColor;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.NAME_REGEX;
import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.STRING_REGEX;

public class MixStringUtil {
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

    public static long deserializeTime(String str) {
        if (str == null || str.isEmpty())
            return 0L;
        String copy = str.replaceAll("[^0-9smhdw:]", "");
        if (copy.isEmpty())
            return 0L;
        long total = 0;
        String[] values = copy.split(":");
        for (String string : values) {
            if (string.isEmpty())
                continue;
            if (string.contains("w")) {
                string = string.replaceAll("[^0-9]", "");
                total += TimeUnit.DAYS.toSeconds(Long.parseLong(string) * 7);
                continue;
            }
            TimeUnit unit = string.contains("d") ? TimeUnit.DAYS
                    : string.contains("h") ? TimeUnit.HOURS
                    : string.contains("m") ? TimeUnit.MINUTES : TimeUnit.SECONDS;
            string = string.replaceAll("[^0-9]", "");
            total += unit.toSeconds(Long.parseLong(string));
        }
        return total;
    }

    public static String openFile(String filePath) {
        String ee = "";
        try {URL url =new URL(filePath);
            HttpURLConnection httpconn =(HttpURLConnection)url.openConnection();
            int HttpResult = httpconn.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_OK) {
                InputStreamReader isReader = new InputStreamReader(httpconn.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(isReader);
                StringBuilder buffer = new StringBuilder();
                String line;
                line = reader.readLine();
                while (line != null) {
                    buffer.append(line);
                    buffer.append("\n");
                    line = reader.readLine();
                }
                ee = buffer.toString();
            }
        } catch (IOException ignored) {}
        return  ee;
    }
}
