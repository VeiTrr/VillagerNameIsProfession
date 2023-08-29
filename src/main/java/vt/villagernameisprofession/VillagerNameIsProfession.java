package vt.villagernameisprofession;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import net.minecraft.village.VillagerProfession;
import vt.villagernameisprofession.client.VillagerNameIsProfessionClient;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VillagerNameIsProfession implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            for (VillagerEntity villagerEntity : world.getEntitiesByType(EntityType.VILLAGER, entity -> true)) {
                if (villagerEntity.getCustomName() == null) {
                    SetCustomName(villagerEntity);
                } else {
                    if (isCustomNameIsProfession(villagerEntity)) {
                        SetCustomName(villagerEntity);
                    }

                }
            }
        });
    }

    void SetCustomName(VillagerEntity villagerEntity) {
        Text customname = Text.of(I18n.translate("entity.minecraft.villager." + villagerEntity.getVillagerData().getProfession().toString().toLowerCase()));
        villagerEntity.setCustomName(customname);
    }

    boolean isCustomNameIsProfession(VillagerEntity villagerEntity) {
        List<String> professions;
        String customname = Objects.requireNonNull(villagerEntity.getCustomName()).getString();
        if (VillagerNameIsProfessionClient.isConfigOn) {

            professions = VillagerNameIsProfessionClient.CLIENT_CONFIG.profession;
        } else {
            professions = getProfessions(VillagerProfession.class, VillagerProfession.class);
        }
        for (String profession : professions) {
            if (customname.equals(I18n.translate(profession))) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getProfessions(Class<?> clazz, Class<?> fieldType) {
        List<String> professions = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (fieldType.isAssignableFrom(field.getType())) {
                professions.add("entity.minecraft.villager." + field.getName().toLowerCase());
            }
        }
        return professions;
    }


}
