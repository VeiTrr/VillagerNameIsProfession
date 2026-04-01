package vt.villagernameisprofession;

import vt.villagernameisprofession.config.Configuration;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public final class VillagerNameIsProfession {
    public static final String MOD_ID = "villagernameisprofession";
    public static Configuration CLIENT_CONFIG = Configuration.load();
    public static List<String> UpdatedVillagers = new ArrayList<>();

    public static void init() {
        // Write common init code here.
    }

    public static void ClientTickEvent(Minecraft client) {
        if (client.level != null) {
            Level world = client.level;
            if (client.player == null) {
                return;
            }
            AABB box = client.player.getBoundingBox().inflate(CLIENT_CONFIG.getRadius());
            List<Villager> villagers = world.getEntitiesOfClass(Villager.class, box, entity -> true);
            for (Villager villagerEntity : villagers) {
                if (CheckBlockMode(villagerEntity)) {
                    if (villagerEntity.hasCustomName()) {
                        if (isCustomNameIsProfession(villagerEntity)) {
                            updateName(villagerEntity);
                        }
                    } else {
                        updateName(villagerEntity);
                    }
                } else {
                    if (UpdatedVillagers.contains(villagerEntity.getUUID().toString())) {
                        villagerEntity.setCustomName(null);
                        villagerEntity.setCustomNameVisible(false);
                        UpdatedVillagers.remove(villagerEntity.getUUID().toString());
                    }
                }
            }
        } else {
            UpdatedVillagers.clear();
        }
    }

    private static void updateName(Villager villagerEntity) {
        String professionKey = villagerEntity.getVillagerData().profession().getRegisteredName().toLowerCase();
        Component customName = Component.nullToEmpty(I18n.get("entity.minecraft.villager." + professionKey));
        if (professionKey.contains(":") && customName.contains(Component.nullToEmpty("entity.minecraft.villager." + professionKey))) {
            professionKey = professionKey.substring(professionKey.lastIndexOf(":") + 1);
        }
        customName = Component.nullToEmpty(I18n.get("entity.minecraft.villager." + professionKey));
        if (!customName.contains(Component.nullToEmpty("entity.minecraft.villager." + professionKey))) {
            villagerEntity.setCustomName(customName);
            villagerEntity.setCustomNameVisible(CLIENT_CONFIG.isAlwaysVisibleProfession());
            if (!UpdatedVillagers.contains(villagerEntity.getUUID().toString())) {
                UpdatedVillagers.add(villagerEntity.getUUID().toString());
            }
        }

    }

    static boolean isCustomNameIsProfession(Villager villagerEntity) {
        return UpdatedVillagers.contains(villagerEntity.getUUID().toString());
    }

    public static boolean CheckBlockMode(Villager villagerEntity) {
        if (CLIENT_CONFIG.isProfessionListBlocking()) {
            if (!(CLIENT_CONFIG.getProfession().size() == 0)) {
                return !(CLIENT_CONFIG.getProfession().contains(villagerEntity.getVillagerData().profession().getRegisteredName()));
            } else {
                return true;
            }
        } else {
            if (!(CLIENT_CONFIG.getProfession().size() == 0)) {
                return CLIENT_CONFIG.getProfession().contains(villagerEntity.getVillagerData().profession().getRegisteredName());
            } else {
                return false;
            }
        }
    }

    public static void loadConfig() {
        CLIENT_CONFIG = Configuration.load();
    }
}
