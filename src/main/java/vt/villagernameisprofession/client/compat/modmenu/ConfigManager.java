package vt.villagernameisprofession.client.compat.modmenu;

import java.io.File;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.FileUtils;
import vt.villagernameisprofession.client.compat.modmenu.Configuration;

public class ConfigManager {
    private static final String CONFIG_FILE_NAME = "config.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Configuration config;

    public static void load() {
        File configFile = new File(CONFIG_FILE_NAME);

        if (configFile.exists()) {
            try {
                config = GSON.fromJson(FileUtils.readFileToString(configFile, "UTF-8"), Configuration.class);
            } catch (JsonSyntaxException | IOException e) {
                e.printStackTrace();
                config = new Configuration();
            }
        } else {
            config = new Configuration();
            save();
        }
    }

    public static Configuration getConfig() {
        if (config == null) {
            load();
        }

        return config;
    }

    public static void save() {
        if (config == null) {
            config = new Configuration();
        }

        File configFile = new File(CONFIG_FILE_NAME);
        try {
            FileUtils.writeStringToFile(configFile, GSON.toJson(config), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
