package vt.villagernameisprofession.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Box;
import vt.villagernameisprofession.config.Configuration;

import java.util.List;

import static vt.villagernameisprofession.VillagerNameIsProfession.CLIENT_CONFIG;

public class VNIPCommand {

    public static final SuggestionProvider<Object> CONFIG_FIELDS = (context, builder) -> {
        for (String field : CLIENT_CONFIG.getConfigFields().keySet()) {
            builder.suggest(field);
        }
        return builder.buildFuture();
    };

    public static final SuggestionProvider<Object> FIELDS_VALUE = (context, builder) -> {
        String key = StringArgumentType.getString(context, "key");
        switch (key) {
            case "alwaysVisibleProfession", "isProfessionListBlocking":
                builder.suggest("true");
                builder.suggest("false");
                break;
            case "radius":
                builder.suggest("100");
                builder.suggest("200");
                break;
            case "profession":
                builder.suggest("add");
                builder.suggest("remove");
                break;
        }
        return builder.buildFuture();
    };

    public static final SuggestionProvider<Object> ADD_PROFESSION = (context, builder) -> {
        Registries.VILLAGER_PROFESSION.forEach(profession -> {
            String temp = "\"" + profession.toString() + "\"";
            builder.suggest(temp);
        });
        return builder.buildFuture();
    };

    public static final SuggestionProvider<Object> REMOVE_PROFESSION = (context, builder) -> {
        for (String profession : CLIENT_CONFIG.getProfession()) {
            String temp = "\"" + profession + "\"";
            builder.suggest(temp);
        }
        return builder.buildFuture();
    };


    public static void addProfession(String value, ClientPlayerEntity player) {
        List<String> professions = CLIENT_CONFIG.getProfession();
        if (!professions.contains(value)) {
            professions.add(value);
        } else {
            player.sendMessage(Text.of("Profession already exists"), false);
            return;
        }
        CLIENT_CONFIG.setProfession(professions);
        CLIENT_CONFIG.save();
        player.sendMessage(Text.of("Added " + value + " to " + "profession"), false);
    }

    public static void removeProfession(String value, ClientPlayerEntity player) {
        List<String> professions = CLIENT_CONFIG.getProfession();
        if (professions.contains(value)) {
            professions.remove(value);
        } else {
            player.sendMessage(Text.of("Profession not found"), false);
            return;
        }
        CLIENT_CONFIG.setProfession(professions);
        CLIENT_CONFIG.save();
        player.sendMessage(Text.of("Removed " + value + " from " + "profession"), false);
    }

    public static void setConfigValue(String key, String value, ClientPlayerEntity player) {
        switch (key) {
            case "alwaysVisibleProfession":
                CLIENT_CONFIG.setAlwaysVisibleProfession(Boolean.parseBoolean(value));
                break;
            case "isProfessionListBlocking":
                CLIENT_CONFIG.setProfessionListBlocking(Boolean.parseBoolean(value));
                break;
            case "radius":
                CLIENT_CONFIG.setRadius(Integer.parseInt(value));
                break;
            default:
                player.sendMessage(Text.of("Invalid key"), false);
                return;
        }

        CLIENT_CONFIG.save();
        player.sendMessage(Text.of("Set " + key + " to " + value), false);
    }

    public static void showConfigValue(String key, ClientPlayerEntity player) {
        Configuration CLIENT_CONFIG = Configuration.load();
        if (CLIENT_CONFIG.getConfigFields().containsKey(key)) {
            player.sendMessage(Text.of(key + ": " + CLIENT_CONFIG.getConfigFields().get(key)), false);
        } else {
            player.sendMessage(Text.of("Invalid key"), false);
        }
    }

    public static void getProfession(ClientCommandSource source, int radius) {
        if (MinecraftClient.getInstance().player == null) {
            return;
        }
        Box box = MinecraftClient.getInstance().player.getBoundingBox().expand(radius);
        List<VillagerEntity> villagers = MinecraftClient.getInstance().player.getWorld().getEntitiesByClass(VillagerEntity.class, box, entity -> true);
        for (VillagerEntity villagerEntity : villagers) {
            String xyz = "X: " + villagerEntity.getBlockPos().getX() + " Y: " + villagerEntity.getBlockPos().getY() + " Z: " + villagerEntity.getBlockPos().getZ();
            MutableText message = Text.literal(villagerEntity.getVillagerData().getProfession().toString() + " at " + xyz);
            MutableText addButton = Text.literal(" " + I18n.translate("config.villagernameisprofession.add")).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vnip config profession add " + "\"" + villagerEntity.getVillagerData().getProfession().toString() + "\""))).formatted(Formatting.GREEN);
            MutableText removeButton = Text.literal(" " + I18n.translate("config.villagernameisprofession.delete")).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vnip config profession remove " + "\"" + villagerEntity.getVillagerData().getProfession().toString() + "\""))).formatted(Formatting.RED);
            MinecraftClient.getInstance().player.sendMessage(message.append(addButton).append(removeButton), false);
        }
    }

    public static void getProfession(ClientCommandSource source) {
        if (MinecraftClient.getInstance().player == null) {
            return;
        }
        Box box = MinecraftClient.getInstance().player.getBoundingBox().expand(CLIENT_CONFIG.getRadius());
        List<VillagerEntity> villagers = MinecraftClient.getInstance().player.getWorld().getEntitiesByClass(VillagerEntity.class, box, entity -> true);
        for (VillagerEntity villagerEntity : villagers) {
            String xyz = "X: " + villagerEntity.getBlockPos().getX() + " Y: " + villagerEntity.getBlockPos().getY() + " Z: " + villagerEntity.getBlockPos().getZ();
            MutableText message = Text.literal(villagerEntity.getVillagerData().getProfession().toString() + " at " + xyz);
            MutableText addButton = Text.literal(" " + I18n.translate("config.villagernameisprofession.add")).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vnip config profession add " + "\"" + villagerEntity.getVillagerData().getProfession().toString() + "\""))).formatted(Formatting.GREEN);
            MutableText removeButton = Text.literal(" " + I18n.translate("config.villagernameisprofession.delete")).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vnip config profession remove " + "\"" + villagerEntity.getVillagerData().getProfession().toString() + "\""))).formatted(Formatting.RED);
            MinecraftClient.getInstance().player.sendMessage(message.append(addButton).append(removeButton), false);
        }
    }
}
