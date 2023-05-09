package vt.villagernameisprofession;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.text.Text;

import java.util.Objects;

public class VTVillagerNameIsProfession implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (entity instanceof VillagerEntity villagerEntity) {
                if (villagerEntity.getCustomName() == null) {
                    Text customname = Text.of(I18n.translate("entity.minecraft.villager." + villagerEntity.getVillagerData().getProfession().toString().toLowerCase()));
                    villagerEntity.setCustomName(customname);
                } else {
                    if (isCustomNameIsProfession(villagerEntity)) {
                        Text customname = Text.of(I18n.translate("entity.minecraft.villager." + villagerEntity.getVillagerData().getProfession().toString().toLowerCase()));
                        villagerEntity.setCustomName(customname);
                    }
                }
            }
        });

    }

    boolean isCustomNameIsProfession(VillagerEntity villagerEntity) {
        String customname = Objects.requireNonNull(villagerEntity.getCustomName()).getString();
        if (customname.equals(I18n.translate("entity.minecraft.villager.farmer"))) {
            return true;
        } else {
            if (customname.equals(I18n.translate("entity.minecraft.villager.fisherman"))) {
                return true;
            } else {
                if (customname.equals(I18n.translate("entity.minecraft.villager.shepherd"))) {
                    return true;
                } else {
                    if (customname.equals(I18n.translate("entity.minecraft.villager.fletcher"))) {
                        return true;
                    } else {
                        if (customname.equals(I18n.translate("entity.minecraft.villager.librarian"))) {
                            return true;
                        } else {
                            if (customname.equals(I18n.translate("entity.minecraft.villager.cartographer"))) {
                                return true;
                            } else {
                                if (customname.equals(I18n.translate("entity.minecraft.villager.cleric"))) {
                                    return true;
                                } else {
                                    if (customname.equals(I18n.translate("entity.minecraft.villager.armorer"))) {
                                        return true;
                                    } else {
                                        if (customname.equals(I18n.translate("entity.minecraft.villager.weapon_smith"))) {
                                            return true;
                                        } else {
                                            if (customname.equals(I18n.translate("entity.minecraft.villager.tool_smith"))) {
                                                return true;
                                            } else {
                                                if (customname.equals(I18n.translate("entity.minecraft.villager.butcher"))) {
                                                    return true;
                                                } else {
                                                    if (customname.equals(I18n.translate("entity.minecraft.villager.leatherworker"))) {
                                                        return true;
                                                    } else {
                                                        if (customname.equals(I18n.translate("entity.minecraft.villager.nitwit"))) {
                                                            return true;
                                                        } else {
                                                            if (customname.equals(I18n.translate("entity.minecraft.villager.mason"))) {
                                                                return true;
                                                            } else {
                                                                return customname.equals(I18n.translate("entity.minecraft.villager.unknown"));
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
