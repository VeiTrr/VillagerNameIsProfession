package vt.villagernameisprofession.neoforge.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import vt.villagernameisprofession.VillagerNameIsProfession;
import vt.villagernameisprofession.config.ConfigScreen;
import vt.villagernameisprofession.neoforge.commands.VNIPCommand;

@SuppressWarnings("unused")
@EventBusSubscriber(value = Dist.CLIENT, modid = VillagerNameIsProfession.MOD_ID)
@Mod(VillagerNameIsProfession.MOD_ID)
public final class VillagerNameIsProfessionNeoForgeClient {
    public VillagerNameIsProfessionNeoForgeClient(IEventBus modEventBus, ModContainer modContainer) {
        VillagerNameIsProfession.init();
        modEventBus.addListener(VillagerNameIsProfessionNeoForgeClient::onClientSetup);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, parent) -> ConfigScreen.createScreen(parent));
    }

    @SubscribeEvent
    public static void onClientTickEvent(ClientTickEvent.Post event) {
        VillagerNameIsProfession.ClientTickEvent(Minecraft.getInstance());
    }

    public static void onClientSetup(FMLClientSetupEvent ignored) {
        NeoForge.EVENT_BUS.addListener((RegisterClientCommandsEvent event) -> VNIPCommand.register(event.getDispatcher()));
    }
}