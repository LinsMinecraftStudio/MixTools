package org.lins.mmmjjkx.mixtools.utils;

import org.lins.mmmjjkx.mixtools.MixTools;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.NAME_REGEX;
import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.STRING_REGEX;

public class StringUtils {
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