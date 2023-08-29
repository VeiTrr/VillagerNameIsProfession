package vt.villagernameisprofession.client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vt.villagernameisprofession.client.compat.modmenu.ClothConfiguration;
import vt.villagernameisprofession.client.compat.modmenu.ConfigManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class VillagerNameIsProfessionClient implements ClientModInitializer {
    public static final Logger LOGGER;

    private static final Gson GSON;
    private static final File CONFIG_FILE;
    public static final ClothConfiguration CLIENT_CONFIG;

    public static Boolean isConfigOn = false;

    static {
        LOGGER = LoggerFactory.getLogger("VillagerNameIsProfession");

        GSON = new GsonBuilder().setPrettyPrinting().create();
        CONFIG_FILE = new File(String.format("%s%svillagernameisprofession-client.json", FabricLoader.getInstance().getConfigDir(), File.separator));
        CLIENT_CONFIG = createConfig();
    }

    @Override
    public void onInitializeClient() {

        if (FabricLoader.getInstance().isModLoaded("cloth-config") && FabricLoader.getInstance().isModLoaded("modmenu")) {
            isConfigOn = true;
            ConfigManager.registerAutoConfig();
            ClothConfiguration config = AutoConfig.getConfigHolder(ClothConfiguration.class).getConfig();
            try {
                config.validatePostLoad();
            } catch (ConfigData.ValidationException e) {
                e.printStackTrace();
            }
        }
    }

    private static ClothConfiguration createConfig() {
        ClothConfiguration finalConfig;
        LOGGER.info("Trying to read config file...");
        try {
            if(CONFIG_FILE.createNewFile()) {
                LOGGER.info("No config file found, creating a new one...");
                writeConfig(GSON.toJson(JsonParser.parseString(GSON.toJson(new ClothConfiguration()))));
                finalConfig = new ClothConfiguration();
                LOGGER.info("Successfully created default config file.");
            } else {
                LOGGER.info("A config file was found, loading it..");
                finalConfig = GSON.fromJson(new String(Files.readAllBytes(CONFIG_FILE.toPath())), ClothConfiguration.class);
                if(finalConfig == null) {
                    throw new NullPointerException("The config file was empty.");
                } else {
                    LOGGER.info("Successfully loaded config file.");
                }
            }
        } catch(Exception e) {
            LOGGER.error("There was an error creating/loading the config file!", e);
            finalConfig = new ClothConfiguration();
            LOGGER.warn("Defaulting to original config.");
        }
        return finalConfig;
    }

    public static void saveConfig(ClothConfiguration modConfig) {
        try {
            writeConfig(GSON.toJson(JsonParser.parseString(GSON.toJson(modConfig))));
            LOGGER.info("Saved new config file.");
        } catch(Exception e) {
            LOGGER.error("There was an error saving the config file!", e);
        }
    }

    private static void writeConfig(String json) {
        try(PrintWriter printWriter = new PrintWriter(CONFIG_FILE)) {
            printWriter.write(json);
            printWriter.flush();
        } catch(IOException e) {
            LOGGER.error("Failed to write config file", e);
        }
    }
}
