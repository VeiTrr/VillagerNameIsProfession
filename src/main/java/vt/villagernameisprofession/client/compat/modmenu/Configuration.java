package vt.villagernameisprofession.client.compat.modmenu;

import java.util.List;
import vt.villagernameisprofession.VillagerNameIsProfession;

public class Configuration {
    public List<String> profession;

    public Configuration() {
        this.profession = VillagerNameIsProfession.getProfessions();
    }
}
