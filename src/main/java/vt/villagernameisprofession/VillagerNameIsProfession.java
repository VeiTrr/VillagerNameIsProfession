package vt.villagernameisprofession;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;
import vt.villagernameisprofession.client.VillagerNameIsProfessionClient;

import java.util.List;
import java.util.Objects;

public class VillagerNameIsProfession implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerTickEvents.START_WORLD_TICK.register(world -> {
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
            professions = getProfessions();
        }
        for (String profession : professions) {
            if (customname.equals(I18n.translate(profession))) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getProfessions(){
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



}
