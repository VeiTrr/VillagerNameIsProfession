package vt.villagernameisprofession.neoforge.client;

import net.minecraft.client.MinecraftClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.ConfigScreenHandler;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;
import vt.villagernameisprofession.VillagerNameIsProfession;
import vt.villagernameisprofession.config.ConfigScreen;
import vt.villagernameisprofession.neoforge.commands.VNIPCommand;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = VillagerNameIsProfession.MOD_ID)
@Mod(VillagerNameIsProfession.MOD_ID)
public final class VillagerNameIsProfessionNeoForgeClient {
    public VillagerNameIsProfessionNeoForgeClient(IEventBus modEventBus, ModContainer modContainer) {
        VillagerNameIsProfession.init();
        modEventBus.addListener(VillagerNameIsProfessionNeoForgeClient::onClientSetup);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            VillagerNameIsProfession.ClientTickEvent(MinecraftClient.getInstance());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onClientSetup(FMLClientSetupEvent ignored) {
        NeoForge.EVENT_BUS.addListener((RegisterClientCommandsEvent event) -> {
            VNIPCommand.register(event.getDispatcher());
        });
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraftClient, screen) -> ConfigScreen.createScreen(screen)
                )
        );

    }
}