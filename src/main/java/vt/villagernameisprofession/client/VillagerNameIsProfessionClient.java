package vt.villagernameisprofession.client;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vt.villagernameisprofession.client.compat.modmenu.ConfigManager;
import vt.villagernameisprofession.client.compat.modmenu.Configuration;

public class VillagerNameIsProfessionClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("VillagerNameIsProfession");
    public static Configuration CLIENT_CONFIG = ConfigManager.getConfig();

    @Override
    public void onInitializeClient() {
        loadConfig();
    }

    public static void saveConfig() {
        ConfigManager.save();
        LOGGER.info("Saved new config file.");
    }

    public static void loadConfig() {
        ConfigManager.load();
        CLIENT_CONFIG = ConfigManager.getConfig();
        LOGGER.info("Loaded config file.");
    }

    public static Configuration getConfig() {
        return CLIENT_CONFIG;
    }
}
