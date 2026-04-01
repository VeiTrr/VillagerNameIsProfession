package vt.villagernameisprofession.forge.client;


import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;

import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vt.villagernameisprofession.VillagerNameIsProfession;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = VillagerNameIsProfession.MOD_ID, value = Dist.CLIENT)
public class VillagerNameIsProfessionClient {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent.Post event) {
        VillagerNameIsProfession.ClientTickEvent(Minecraft.getInstance());
    }

}
