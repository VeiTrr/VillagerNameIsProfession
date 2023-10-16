package vt.villagernameisprofession.client.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import vt.villagernameisprofession.client.VillagerNameIsProfessionClient;
import vt.villagernameisprofession.client.compat.modmenu.ModMenuConfigScreen;

@Environment(EnvType.CLIENT)
public class ConfigScreenEntrypoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> ModMenuConfigScreen.createScreen(parent, VillagerNameIsProfessionClient.getConfig());
    }
}
