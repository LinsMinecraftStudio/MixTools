package org.lins.mmmjjkx.mixtools.utils;

import org.lins.mmmjjkx.mixtools.MixTools;

import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.NAME_REGEX;
import static org.lins.mmmjjkx.mixtools.objects.keys.SettingsKey.STRING_REGEX;

public class MTUtils {
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
}