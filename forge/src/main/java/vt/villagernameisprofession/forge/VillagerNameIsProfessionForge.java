package vt.villagernameisprofession.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import vt.villagernameisprofession.VillagerNameIsProfession;
import vt.villagernameisprofession.config.ConfigScreen;
import vt.villagernameisprofession.forge.commands.VNIPCommand;

@SuppressWarnings("unused")
@Mod(VillagerNameIsProfession.MOD_ID)
public final class VillagerNameIsProfessionForge {
    public VillagerNameIsProfessionForge() {
        VillagerNameIsProfession.init();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void ClientSetup(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraftClient, screen) -> ConfigScreen.createScreen(screen)
                )
        );

    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onCommandRegister(RegisterClientCommandsEvent event) {
        VNIPCommand.register(event.getDispatcher());
    }
}
