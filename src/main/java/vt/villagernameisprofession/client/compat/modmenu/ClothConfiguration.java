package vt.villagernameisprofession.client.compat.modmenu;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.village.VillagerProfession;
import java.util.List;
import static vt.villagernameisprofession.VillagerNameIsProfession.getProfessions;

@Config(name = "villagernameisprofession-client")
public class ClothConfiguration implements ConfigData {
    @ConfigEntry.Gui.Tooltip(count = 1)
    @ConfigEntry.Gui.RequiresRestart
    public List<String> profession  = getProfessions();


}
