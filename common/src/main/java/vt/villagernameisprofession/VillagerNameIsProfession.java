package vt.villagernameisprofession;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import vt.villagernameisprofession.config.Configuration;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class VillagerNameIsProfession {
    public static final String MOD_ID = "villagernameisprofession";
    public static Configuration CLIENT_CONFIG = Configuration.load();
    public static Set<UUID> UpdatedVillagers = new HashSet<>();

    private static final Text EMPTY_TEXT = Text.empty();
    private static final Map<String, Text> PROFESSION_CACHE = new ConcurrentHashMap<>();
    private static Set<String> cachedProfessionFilter = null;
    private static int lastProfessionListHash = 0;
    private static String lastLanguage = "";

    public static void init() {
        // Write common init code here.
    }

    public static void ClientTickEvent(MinecraftClient client) {
        if (client.world != null) {
            World world = client.world;
            if (client.player == null) {
                return;
            }
            if (client.getLanguageManager() != null && client.getLanguageManager().getLanguage() != null) {
                String currentLang = client.getLanguageManager().getLanguage().getCode();
                if (!Objects.equals(currentLang, lastLanguage)) {
                    lastLanguage = currentLang;
                    PROFESSION_CACHE.clear();
                }
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
                    if (UpdatedVillagers.contains(villagerEntity.getUuid())) {
                        villagerEntity.setCustomName(null);
                        villagerEntity.setCustomNameVisible(false);
                        UpdatedVillagers.remove(villagerEntity.getUuid());
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

    private static Text getProfessionComponent(String rawProfession) {
        return PROFESSION_CACHE.computeIfAbsent(rawProfession, key -> {
            String professionKey = key.toLowerCase();
            String translationKey = "entity.minecraft.villager." + professionKey;
            if (professionKey.contains(":") && !I18n.hasTranslation(translationKey)) {
                professionKey = professionKey.substring(professionKey.lastIndexOf(":") + 1);
                translationKey = "entity.minecraft.villager." + professionKey;
            }
            if (I18n.hasTranslation(translationKey)) {
                return Text.translatable(translationKey);
            }
            return EMPTY_TEXT;
        });
    }

    private static void updateName(VillagerEntity villagerEntity) {
        String professionKey = villagerEntity.getVillagerData().getProfession().toString();
        Text customName = getProfessionComponent(professionKey);
        if (customName != EMPTY_TEXT) {
            boolean visible = CLIENT_CONFIG.isAlwaysVisibleProfession();
            if (!Objects.equals(villagerEntity.getCustomName(), customName) || villagerEntity.isCustomNameVisible() != visible) {
                villagerEntity.setCustomName(customName);
                villagerEntity.setCustomNameVisible(visible);
            }
            UpdatedVillagers.add(villagerEntity.getUuid());
        }
    }

    static boolean isCustomNameIsProfession(VillagerEntity villagerEntity) {
        return UpdatedVillagers.contains(villagerEntity.getUuid());
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

    public static boolean CheckBlockMode(VillagerEntity villagerEntity) {
        Set<String> filter = getFilterProfessions();
        boolean hasFilter = !filter.isEmpty();
        String professionName = villagerEntity.getVillagerData().getProfession().toString();
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
