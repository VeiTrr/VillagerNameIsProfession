package vt.villagernameisprofession.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vt.villagernameisprofession.client.compat.modmenu.Configuration;
import vt.villagernameisprofession.client.compat.modmenu.ConfigManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class VillagerNameIsProfessionClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("VillagerNameIsProfession");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(String.valueOf(FabricLoader.getInstance().getConfigDir()), "villagernameisprofession-client.json");
    public static final Configuration CLIENT_CONFIG = new Configuration();

    @Override
    public void onInitializeClient() {
    }

    public static void saveConfig() {
        ConfigManager.save();
        LOGGER.info("Saved new config file.");
    }

    public static void loadConfig() {
        ConfigManager.load();
        LOGGER.info("Loaded config file.");
    }

    public static Configuration getConfig() {
        return CLIENT_CONFIG;
    }

    private static void writeConfig(String json) {
        try (PrintWriter printWriter = new PrintWriter(CONFIG_FILE)) {
            printWriter.write(json);
            printWriter.flush();
        } catch (IOException e) {
            LOGGER.error("Failed to write config file", e);
        }
    }
}
