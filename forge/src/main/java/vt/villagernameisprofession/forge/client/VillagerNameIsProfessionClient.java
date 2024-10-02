package vt.villagernameisprofession.forge.client;

import net.minecraft.client.MinecraftClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vt.villagernameisprofession.VillagerNameIsProfession;
import vt.villagernameisprofession.config.ConfigScreen;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = VillagerNameIsProfession.MOD_ID, value = Dist.CLIENT)
public class VillagerNameIsProfessionClient {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            VillagerNameIsProfession.ClientTickEvent(MinecraftClient.getInstance());
        }
    }

}
