package vt.villagernameisprofession.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vt.villagernameisprofession.client.commands.VNIPCommand;
import vt.villagernameisprofession.client.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public class VillagerNameIsProfessionClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("VillagerNameIsProfession");
    public static Configuration CLIENT_CONFIG = Configuration.load();
    public static List<String> UpdatedVillagers = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        loadConfig();
        ClientCommandRegistrationCallback.EVENT.register(VNIPCommand::register);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.world != null) {
                World world = client.world;
                if (client.player == null) {
                    return;
                }
                Box box = client.player.getBoundingBox().expand(CLIENT_CONFIG.getRadius());
                List<VillagerEntity> villagers = world.getEntitiesByClass(VillagerEntity.class, box, entity -> true);
                for (VillagerEntity villagerEntity : villagers) {
                    if (CheckBlockMode(villagerEntity)) {
                        if (villagerEntity.hasCustomName()) {
                            if (isCustomNameIsProfession(villagerEntity)) {
                                updateName(villagerEntity);
                            }
                        } else {
                            updateName(villagerEntity);
                        }
                    } else {
                        if (UpdatedVillagers.contains(villagerEntity.getUuid().toString())) {
                            villagerEntity.setCustomName(null);
                            villagerEntity.setCustomNameVisible(false);
                            UpdatedVillagers.remove(villagerEntity.getUuid().toString());
                        }
                    }
                }
            } else {
                UpdatedVillagers.clear();
            }
        });
    }

    private void updateName(VillagerEntity villagerEntity) {
        String professionKey = villagerEntity.getVillagerData().getProfession().toString().toLowerCase();
        Text customName = Text.of(I18n.translate("entity.minecraft.villager." + professionKey));
        if (professionKey.contains(":") && customName.contains(Text.of("entity.minecraft.villager." + professionKey))) {
            professionKey = professionKey.substring(professionKey.lastIndexOf(":") + 1);
        }
        customName = Text.of(I18n.translate("entity.minecraft.villager." + professionKey));
        if (!customName.contains(Text.of("entity.minecraft.villager." + professionKey))) {
            villagerEntity.setCustomName(customName);
            villagerEntity.setCustomNameVisible(CLIENT_CONFIG.isAlwaysVisibleProfession());
            if (!UpdatedVillagers.contains(villagerEntity.getUuid().toString())) {
                UpdatedVillagers.add(villagerEntity.getUuid().toString());
            }
        }

    }

    boolean isCustomNameIsProfession(VillagerEntity villagerEntity) {
        return UpdatedVillagers.contains(villagerEntity.getUuid().toString());
    }

    public boolean CheckBlockMode(VillagerEntity villagerEntity) {
        if (CLIENT_CONFIG.isProfessionListBlocking()) {
            if (!(CLIENT_CONFIG.getProfession().size() == 0)) {
                return !(CLIENT_CONFIG.getProfession().contains(villagerEntity.getVillagerData().getProfession().toString()));
            } else {
                return true;
            }
        } else {
            if (!(CLIENT_CONFIG.getProfession().size() == 0)) {
                return CLIENT_CONFIG.getProfession().contains(villagerEntity.getVillagerData().getProfession().toString());
            } else {
                return false;
            }
        }
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
