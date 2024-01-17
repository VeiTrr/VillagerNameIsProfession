package vt.villagernameisprofession.client.config;

import java.util.List;
import vt.villagernameisprofession.client.VillagerNameIsProfessionClient;

public class Configuration {

    public boolean AlwaysVisbleProfession;

    public int Radius;
    public List<String> profession;

    public Configuration() {
        this.Radius = 100;
        this.AlwaysVisbleProfession = true;
        this.profession = VillagerNameIsProfessionClient.getProfessions();
    }
}
