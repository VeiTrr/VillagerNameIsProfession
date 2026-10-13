package vt.villagernameisprofession;

import vt.villagernameisprofession.config.Configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public final class VillagerNameIsProfession {
    public static final String MOD_ID = "villagernameisprofession";
    public static Configuration CLIENT_CONFIG = Configuration.load();
    public static Set<UUID> UpdatedVillagers = new HashSet<>();

    private static final Component EMPTY_COMPONENT = Component.empty();
    private static final Map<String, Component> PROFESSION_CACHE = new ConcurrentHashMap<>();
    private static Set<String> cachedProfessionFilter = null;
    private static int lastProfessionListHash = 0;
    private static String lastLanguage = "";

    public static void init() {
        // Write common init code here.
    }

    public static void ClientTickEvent(Minecraft client) {
        if (client.level != null) {
            Level world = client.level;
            if (client.player == null) {
                return;
            }
            if (client.getLanguageManager() != null) {
                String currentLang = client.getLanguageManager().getSelected();
                if (!Objects.equals(currentLang, lastLanguage)) {
                    lastLanguage = currentLang;
                    PROFESSION_CACHE.clear();
                }
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
                    if (UpdatedVillagers.contains(villagerEntity.getUUID())) {
                        villagerEntity.setCustomName(null);
                        villagerEntity.setCustomNameVisible(false);
                        UpdatedVillagers.remove(villagerEntity.getUUID());
                    }
                }
            }
        } else {
            UpdatedVillagers.clear();
            PROFESSION_CACHE.clear();
            cachedProfessionFilter = null;
            lastProfessionListHash = 0;
            lastLanguage = "";
        }
    }

    private static Component getProfessionComponent(String rawProfession) {
        return PROFESSION_CACHE.computeIfAbsent(rawProfession, key -> {
            String professionKey = key.toLowerCase();
            String translationKey = "entity.minecraft.villager." + professionKey;
            if (professionKey.contains(":") && !I18n.exists(translationKey)) {
                professionKey = professionKey.substring(professionKey.lastIndexOf(":") + 1);
                translationKey = "entity.minecraft.villager." + professionKey;
            }
            if (I18n.exists(translationKey)) {
                return Component.translatable(translationKey);
            }
            return EMPTY_COMPONENT;
        });
    }

    private static void updateName(Villager villagerEntity) {
        String professionKey = villagerEntity.getVillagerData().profession().getRegisteredName();
        Component customName = getProfessionComponent(professionKey);
        if (customName != EMPTY_COMPONENT) {
            boolean visible = CLIENT_CONFIG.isAlwaysVisibleProfession();
            if (!Objects.equals(villagerEntity.getCustomName(), customName) || villagerEntity.isCustomNameVisible() != visible) {
                villagerEntity.setCustomName(customName);
                villagerEntity.setCustomNameVisible(visible);
            }
            UpdatedVillagers.add(villagerEntity.getUUID());
        }
    }

    static boolean isCustomNameIsProfession(Villager villagerEntity) {
        return UpdatedVillagers.contains(villagerEntity.getUUID());
    }

    private static Set<String> getFilterProfessions() {
        List<String> list = CLIENT_CONFIG.getProfession();
        if (list == null || list.isEmpty()) {
            return Collections.emptySet();
        }
        int hash = list.hashCode();
        if (cachedProfessionFilter == null || hash != lastProfessionListHash) {
            cachedProfessionFilter = new HashSet<>(list);
            lastProfessionListHash = hash;
        }
        return cachedProfessionFilter;
    }

    public static boolean CheckBlockMode(Villager villagerEntity) {
        Set<String> filter = getFilterProfessions();
        boolean hasFilter = !filter.isEmpty();
        String professionName = villagerEntity.getVillagerData().profession().getRegisteredName();
        if (CLIENT_CONFIG.isProfessionListBlocking()) {
            if (hasFilter) {
                return !filter.contains(professionName);
            } else {
                return true;
            }
        } else {
            if (hasFilter) {
                return filter.contains(professionName);
            } else {
                return false;
            }
        }
    }

    public static void loadConfig() {
        CLIENT_CONFIG = Configuration.load();
        cachedProfessionFilter = null;
        lastProfessionListHash = 0;
        PROFESSION_CACHE.clear();
        lastLanguage = "";
    }
}
