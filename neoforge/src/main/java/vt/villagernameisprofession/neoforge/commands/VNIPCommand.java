package vt.villagernameisprofession.neoforge.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import vt.villagernameisprofession.config.Configuration;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.phys.AABB;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static vt.villagernameisprofession.VillagerNameIsProfession.CLIENT_CONFIG;

public class VNIPCommand {

    private static final SuggestionProvider<CommandSourceStack> CONFIG_FIELDS = (context, builder) -> {
        for (String field : CLIENT_CONFIG.getConfigFields().keySet()) {
            builder.suggest(field);
        }
        return builder.buildFuture();
    };

    private static final SuggestionProvider<CommandSourceStack> FIELDS_VALUE = (context, builder) -> {
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

    private static final SuggestionProvider<CommandSourceStack> ADD_PROFESSION = (context, builder) -> {
        BuiltInRegistries.VILLAGER_PROFESSION.forEach(profession -> {
            String temp = "\"" + profession + "\"";
            builder.suggest(temp);
        });
        return builder.buildFuture();
    };

    private static final SuggestionProvider<CommandSourceStack> REMOVE_PROFESSION = (context, builder) -> {
        for (String profession : CLIENT_CONFIG.getProfession()) {
            String temp = "\"" + profession + "\"";
            builder.suggest(temp);
        }
        return builder.buildFuture();
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("vnip")
                .then(literal("getprofession")
                        .then(argument("radius", IntegerArgumentType.integer())
                                .suggests((ctx, builder) -> {
                                    builder.suggest("10");
                                    builder.suggest("20");
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    getProfession(context.getSource(), IntegerArgumentType.getInteger(context, "radius"));
                                    return 1;
                                }))
                        .executes(context -> {
                            getProfession(context.getSource());
                            return 1;
                        })
                )
                .then(literal("config")
                        .then(argument("key", StringArgumentType.string())
                                .suggests(CONFIG_FIELDS)
                                .executes(context -> {
                                    String key = StringArgumentType.getString(context, "key");
                                    showConfigValue(key, Minecraft.getInstance().player);
                                    return 1;
                                })
                                .then(argument("value", StringArgumentType.string())
                                        .suggests(FIELDS_VALUE)
                                        .executes(context -> {
                                            String key = StringArgumentType.getString(context, "key");
                                            String value = StringArgumentType.getString(context, "value");
                                            setConfigValue(key, value, Minecraft.getInstance().player);
                                            return 1;
                                        })
                                ))
                        .then(literal("profession")
                                .executes(context -> {
                                    showConfigValue("profession", Minecraft.getInstance().player);
                                    return 1;
                                })
                                .then(literal("add")
                                        .then(argument("value", StringArgumentType.string())
                                                .suggests(ADD_PROFESSION)
                                                .executes(context -> {
                                                    String value = StringArgumentType.getString(context, "value");
                                                    addProfession(value, Minecraft.getInstance().player);
                                                    return 1;
                                                })
                                        )
                                )
                                .then(literal("remove")
                                        .then(argument("value", StringArgumentType.string())
                                                .suggests(REMOVE_PROFESSION)
                                                .executes(context -> {
                                                    String value = StringArgumentType.getString(context, "value");
                                                    removeProfession(value, Minecraft.getInstance().player);
                                                    return 1;
                                                })
                                        )
                                )
                        )
                )
        );
    }


    private static void addProfession(String value, LocalPlayer player) {
        List<String> professions = CLIENT_CONFIG.getProfession();
        if (!professions.contains(value)) {
            professions.add(value);
        } else {
            player.displayClientMessage(Component.nullToEmpty("Profession already exists"), false);
            return;
        }
        CLIENT_CONFIG.setProfession(professions);
        CLIENT_CONFIG.save();
        player.displayClientMessage(Component.nullToEmpty("Added " + value + " to " + "profession"), false);
    }

    private static void removeProfession(String value, LocalPlayer player) {
        List<String> professions = CLIENT_CONFIG.getProfession();
        if (professions.contains(value)) {
            professions.remove(value);
        } else {
            player.displayClientMessage(Component.nullToEmpty("Profession not found"), false);
            return;
        }
        CLIENT_CONFIG.setProfession(professions);
        CLIENT_CONFIG.save();
        player.displayClientMessage(Component.nullToEmpty("Removed " + value + " from " + "profession"), false);
    }

    private static void setConfigValue(String key, String value, LocalPlayer player) {
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
                player.displayClientMessage(Component.nullToEmpty("Invalid key"), false);
                return;
        }

        CLIENT_CONFIG.save();
        player.displayClientMessage(Component.nullToEmpty("Set " + key + " to " + value), false);
    }

    private static void showConfigValue(String key, LocalPlayer player) {
        Configuration CLIENT_CONFIG = Configuration.load();
        if (CLIENT_CONFIG.getConfigFields().containsKey(key)) {
            player.displayClientMessage(Component.nullToEmpty(key + ": " + CLIENT_CONFIG.getConfigFields().get(key)), false);
        } else {
            player.displayClientMessage(Component.nullToEmpty("Invalid key"), false);
        }
    }

    public static void getProfession(CommandSourceStack source, int radius) {
        if (Minecraft.getInstance().player == null) {
            return;
        }
        AABB box = Minecraft.getInstance().player.getBoundingBox().inflate(radius);
        runGetProfession(box);
    }

    public static void getProfession(CommandSourceStack source) {
        if (Minecraft.getInstance().player == null) {
            return;
        }
        AABB box = Minecraft.getInstance().player.getBoundingBox().inflate(CLIENT_CONFIG.getRadius());
        runGetProfession(box);
    }

    private static void runGetProfession(AABB box) {
        List<Villager> villagers = Minecraft.getInstance().level.getEntitiesOfClass(Villager.class, box, entity -> true);
        for (Villager villagerEntity : villagers) {
            String xyz = "X: " + villagerEntity.blockPosition().getX() + " Y: " + villagerEntity.blockPosition().getY() + " Z: " + villagerEntity.blockPosition().getZ();
            MutableComponent message = Component.literal(villagerEntity.getVillagerData().profession().getRegisteredName() + " at " + xyz);
            MutableComponent addButton = Component.literal(" " + I18n.get("config.villagernameisprofession.add")).withStyle(style -> style.withClickEvent(new ClickEvent.RunCommand("/vnip config profession add " + "\"" + villagerEntity.getVillagerData().profession().getRegisteredName() + "\""))).withStyle(ChatFormatting.GREEN);
            MutableComponent removeButton = Component.literal(" " + I18n.get("config.villagernameisprofession.delete")).withStyle(style -> style.withClickEvent(new ClickEvent.RunCommand("/vnip config profession remove " + "\"" + villagerEntity.getVillagerData().profession().getRegisteredName() + "\""))).withStyle(ChatFormatting.RED);
            Minecraft.getInstance().player.displayClientMessage(message.append(addButton).append(removeButton), false);
        }
    }
}