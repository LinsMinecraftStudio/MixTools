package org.lins.mmmjjkx.mixtools.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHandler {
    private static final FileConfiguration config = MixTools.INSTANCE.getConfig();
    private static FileConfiguration message;
    public MessageHandler() {
        setup();
    }

    private void setup() {
        String language = config.getString("language","en-us");
        String fileName = "lang/"+language.toLowerCase()+".yml";
        File file = new File(MixTools.INSTANCE.getDataFolder(),fileName);
        if (!file.exists()) {
            InputStream is = MixTools.INSTANCE.getResource(fileName);
            if (is != null) {
                MixTools.INSTANCE.saveResource(fileName,false);
            }else {
                file = new File(MixTools.INSTANCE.getDataFolder(),"lang/en-us.yml");
            }
        }
        message = YamlConfiguration.loadConfiguration(file);
    }

    public String get(String node){
        return message.getString(node);
    }

    public String getColored(String node){
        return colorize(get(node));
    }

    public List<String> getMessages(String node){
        return message.getStringList(node);
    }

    public List<String> getColoredMessages(String node){
        List<String> list = new ArrayList<>();
        for (String message : getMessages(node)){
            list.add(colorize(message));
        }
        return list;
    }

    private String colorize(String string) {
        Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");
        for (Matcher matcher = pattern.matcher(string); matcher.find(); matcher = pattern.matcher(string)) {
            String str = string.substring(matcher.start(), matcher.end());
            String color = str.replace("&","");
            string = string.replace(str, net.md_5.bungee.api.ChatColor.of(color)+""); // You're missing this replacing
        }
        string = ChatColor.translateAlternateColorCodes('&', string); // Translates any & codes too
        return string;
    }
}
