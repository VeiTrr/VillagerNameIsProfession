package vt.villagernameisprofession.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vt.villagernameisprofession.client.compat.modmenu.ConfigManager;
import vt.villagernameisprofession.client.compat.modmenu.Configuration;

import java.util.List;

public class VillagerNameIsProfessionClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("VillagerNameIsProfession");
    public static Configuration CLIENT_CONFIG = ConfigManager.getConfig();

    @Override
    public void onInitializeClient() {
        loadConfig();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                World world = client.world;
                assert client.player != null;
                Box box = client.player.getBoundingBox().expand(10.0D);
                List<VillagerEntity> villagers = world.getEntitiesByClass(VillagerEntity.class, box, entity -> true);
                for (VillagerEntity villagerEntity : villagers) {
                    if (villagerEntity.hasCustomName()) {
                        if (isCustomNameIsProfession(villagerEntity)) {
                            String professionKey = villagerEntity.getVillagerData().getProfession().toString().toLowerCase();
                            Text customName = Text.of(I18n.translate("entity.minecraft.villager." + professionKey));
                            villagerEntity.setCustomName(customName);
                        }
                    } else {
                        String professionKey = villagerEntity.getVillagerData().getProfession().toString().toLowerCase();
                        Text customName = Text.of(I18n.translate("entity.minecraft.villager." + professionKey));
                        villagerEntity.setCustomName(customName);
                    }
                }
            }
        });
    }

    boolean isCustomNameIsProfession(VillagerEntity villagerEntity) {
        List<String> professions;
        String customname = villagerEntity.getCustomName().getString();
        if (!(CLIENT_CONFIG.profession.size() == 0)) {
            professions = CLIENT_CONFIG.profession;
        } else {
            professions = getProfessions();
        }
        for (String profession : professions) {
            if (customname.equals(profession)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getProfessions() {
        List<String> professions = List.of(
                "entity.minecraft.villager.none",
                "entity.minecraft.villager.armorer",
                "entity.minecraft.villager.butcher",
                "entity.minecraft.villager.cartographer",
                "entity.minecraft.villager.cleric",
                "entity.minecraft.villager.farmer",
                "entity.minecraft.villager.fisherman",
                "entity.minecraft.villager.fletcher",
                "entity.minecraft.villager.leatherworker",
                "entity.minecraft.villager.librarian",
                "entity.minecraft.villager.mason",
                "entity.minecraft.villager.nitwit",
                "entity.minecraft.villager.shepherd",
                "entity.minecraft.villager.toolsmith",
                "entity.minecraft.villager.weaponsmith"
        );
        return professions;
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
