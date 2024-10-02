package vt.villagernameisprofession;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import vt.villagernameisprofession.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public final class VillagerNameIsProfession {
    public static final String MOD_ID = "villagernameisprofession";
    public static Configuration CLIENT_CONFIG = Configuration.load();
    public static List<String> UpdatedVillagers = new ArrayList<>();

    public static void init() {
        // Write common init code here.
    }

    public static void ClientTickEvent(MinecraftClient client) {
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
    }

    private static void updateName(VillagerEntity villagerEntity) {
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

    static boolean isCustomNameIsProfession(VillagerEntity villagerEntity) {
        return UpdatedVillagers.contains(villagerEntity.getUuid().toString());
    }

    public static boolean CheckBlockMode(VillagerEntity villagerEntity) {
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

    public static void loadConfig() {
        CLIENT_CONFIG = Configuration.load();
    }
}
