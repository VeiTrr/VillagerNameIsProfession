package vt.villagernameisprofession.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vt.villagernameisprofession.VillagerNameIsProfession;
import vt.villagernameisprofession.config.Configuration;
import vt.villagernameisprofession.fabric.client.commands.VNIPCommand;

import static vt.villagernameisprofession.VillagerNameIsProfession.CLIENT_CONFIG;

public class VillagerNameIsProfessionClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("VillagerNameIsProfession");


    @Override
    public void onInitializeClient() {
        loadConfig();
        ClientCommandRegistrationCallback.EVENT.register(VNIPCommand::register);
        ClientTickEvents.END_CLIENT_TICK.register(VillagerNameIsProfession::ClientTickEvent);
    }


    public static void saveConfig() {
        CLIENT_CONFIG.save();
        LOGGER.info("Saved new config file.");
    }

    public static void loadConfig() {
        CLIENT_CONFIG = Configuration.load();
        LOGGER.info("Loaded config file.");
    }

    public static Configuration getConfig() {
        return CLIENT_CONFIG;
    }
}
