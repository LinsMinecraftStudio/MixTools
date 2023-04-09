package org.lins.mmmjjkx.mixtools.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FilesCompletion {
    public static void completingFile(String resourceFile,boolean justCreate){
        InputStream stream = MixTools.INSTANCE.getResource(resourceFile);
        File file = new File(MixTools.INSTANCE.getDataFolder(), resourceFile);
        if (justCreate){
            if (!file.exists()){
                if (stream!=null) {
                    MixTools.INSTANCE.saveResource(resourceFile,false);
                    return;
                }
            }
        }
        if (!file.exists()){
            if (stream!=null) {
                MixTools.INSTANCE.saveResource(resourceFile,false);
                return;
            }
        }
        if (stream==null) {
            MixTools.INSTANCE.getLogger().warning("File completion of '"+resourceFile+"' is failed.");
            return;
        }
        try {File temp = File.createTempFile(resourceFile+"_temp","yml");
            Files.copy(stream, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            YamlConfiguration configuration = new YamlConfiguration();
            configuration.load(temp);
            List<String> keys = configuration.getKeys(true).stream().toList();
            for (String key : keys) {
                Object value = configuration.get(key);
                if (!configuration.contains(key)) {
                    configuration.set(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MixTools.INSTANCE.getLogger().warning("File completion of '"+resourceFile+"' is failed.");
        }
    }
}
