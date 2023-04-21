package org.lins.mmmjjkx.mixtools.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.lins.mmmjjkx.mixtools.MixTools;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Set;

public class FileUtils {
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
            YamlConfiguration configuration2 = new YamlConfiguration();
            configuration2.load(file);
            Set<String> keys = configuration.getKeys(true);
            for (String key : keys) {
                Object value = configuration.get(key);
                if (!configuration2.contains(key)) {
                    configuration2.set(key, value);
                }
            }
            configuration2.save(file);
        } catch (Exception e) {
            e.printStackTrace();
            MixTools.INSTANCE.getLogger().warning("File completion of '"+resourceFile+"' is failed.");
        }
    }

    public static boolean deleteDir(String sPath) {
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);

        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;

        File[] files = dirFile.listFiles();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
            } else {
                flag = deleteDir(files[i].getAbsolutePath());
            }
            if (!flag) break;
        }
        if (!flag) return false;

        return dirFile.delete();
    }

    public static boolean deleteDir(File dirFile) {
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;

        File[] files = dirFile.listFiles();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
            } else {
                flag = deleteDir(files[i].getAbsolutePath());
            }
            if (!flag) break;
        }
        if (!flag) return false;

        return dirFile.delete();
    }

    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);

        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}