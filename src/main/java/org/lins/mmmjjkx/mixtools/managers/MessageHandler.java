package org.lins.mmmjjkx.mixtools.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.io.File;
import java.io.InputStream;
import java.util.IllegalFormatException;
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
        return message.getString(node,"§4Get message '"+node+"' failed, maybe it's not exists.");
    }

    public String getColored(String node, Object... args){
        try {return colorize(String.format(get(node),args));
        } catch (IllegalFormatException e) {return colorize(get(node));}
    }

    public String getColoredReplaceToOtherMessages(String node, boolean color, String... keys){
        try {return colorize(String.format(get(node), getStrMessagesObj(color,keys)));
        } catch (IllegalFormatException e) {return colorize(get(node));}
    }

    public Object[] getStrMessagesObj(boolean color, String... keys){
        Object[] s = new Object[keys.length];
        int i = 0;
        for (String key:keys) {
            if (color) {s[i] = getColored(key);}
            else {s[i] = get(key);}
            i++;
        }
        return s;
    }
    public void sendMessage(CommandSender cs,String node,Object... args){
        cs.sendMessage(getColored(node,args));
    }

    public void broadcastMessage(String node,Object... args){
        Bukkit.broadcastMessage(getColored(node,args));
    }

    public String colorize(String string) {
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
