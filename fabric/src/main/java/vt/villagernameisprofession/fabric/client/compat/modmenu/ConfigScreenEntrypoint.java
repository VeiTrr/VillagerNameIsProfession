package vt.villagernameisprofession.fabric.client.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import vt.villagernameisprofession.config.ConfigScreen;

@Environment(EnvType.CLIENT)
public class ConfigScreenEntrypoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigScreen::createScreen;
    }
}
