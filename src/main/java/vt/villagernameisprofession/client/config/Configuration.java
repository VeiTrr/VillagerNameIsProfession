package vt.villagernameisprofession.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE_NAME = "VillagerNameIsProfession.json";
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE_NAME).toFile();

    private boolean alwaysVisibleProfession;
    private int radius;
    private List<String> profession;
    private boolean isProfessionListBlocking;

    private Configuration() {
        this.radius = 100;
        this.alwaysVisibleProfession = true;
        this.profession = new ArrayList<>();
        this.profession.add("none");
        this.isProfessionListBlocking = true;
    }

    public boolean isProfessionListBlocking() {
        return isProfessionListBlocking;
    }

    public void setProfessionListBlocking(boolean isProfessionListBlocking) {
        this.isProfessionListBlocking = isProfessionListBlocking;
    }

    public boolean isAlwaysVisibleProfession() {
        return alwaysVisibleProfession;
    }

    public void setAlwaysVisibleProfession(boolean alwaysVisibleProfession) {
        this.alwaysVisibleProfession = alwaysVisibleProfession;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public List<String> getProfession() {
        return profession;
    }

    public void setProfession(List<String> profession) {
        this.profession = profession;
    }

    public static Configuration load() {
        if (CONFIG_FILE.exists()) {
            try {
                return GSON.fromJson(FileUtils.readFileToString(CONFIG_FILE, StandardCharsets.UTF_8), Configuration.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Configuration();
    }

    public void save() {
        try {
            FileUtils.writeStringToFile(CONFIG_FILE, GSON.toJson(this), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getConfigFields() {
        Map<String, Object> fields = new HashMap<>();
        fields.put("alwaysVisibleProfession", alwaysVisibleProfession);
        fields.put("radius", radius);
        fields.put("profession", profession);
        fields.put("isProfessionListBlocking", isProfessionListBlocking);
        return fields;
    }
}