package vt.villagernameisprofession.config;

import org.jetbrains.annotations.NotNull;
import vt.villagernameisprofession.VillagerNameIsProfession;

import static vt.villagernameisprofession.VillagerNameIsProfession.CLIENT_CONFIG;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public class ConfigScreen extends Screen {
    protected final Screen parent;
    private ListWidget professionListWidget;

    Button DoneButton;

    int radiusX;
    int radiusY;
    int radiusLabelX;
    int radiusLabelY;

    protected ConfigScreen(Screen parent) {
        super(Component.translatable("config.villagernameisprofession.title"));
        this.parent = parent;
    }

    public static Screen createScreen(Screen parent) {
        return new ConfigScreen(parent);
    }

    @Override
    protected void init() {
        professionListWidget = new ListWidget(this);
        professionListWidget.setPosition(0, 40);
        professionListWidget.setSize(width, Math.max(0, height - 80));
        addRenderableWidget(professionListWidget);

        Checkbox alwaysVisibleProfessionCheckbox = getAVPCheckbox();
        addRenderableWidget(alwaysVisibleProfessionCheckbox);

        radiusX = width / 4;
        radiusY = 6;
        EditBox radius = new EditBox(Minecraft.getInstance().font, radiusX, radiusY, 30, 15, Component.nullToEmpty(""));
        radius.setMaxLength(256);
        radius.setValue(String.valueOf(CLIENT_CONFIG.getRadius()));
        int radiusLabelwidth = (I18n.get("config.villagernameisprofession.radius").length() - 1) * 5;
        radiusLabelX = radiusX - radiusLabelwidth;
        radiusLabelY = radiusY + (radius.getHeight() - 8) / 2;

        addRenderableWidget(new StringWidget(radiusLabelX, radiusY, radiusLabelwidth, 20, Component.nullToEmpty(I18n.get("config.villagernameisprofession.radius")), Minecraft.getInstance().font));

        radius.setResponder(text -> {
            if (!text.isEmpty()) {
                CLIENT_CONFIG.setRadius(Integer.parseInt(text));
            }
        });
        addRenderableWidget(radius);

        int buttonX = width / 2 - Button.DEFAULT_WIDTH / 2;
        int buttonY = height - 25;
        DoneButton = new Button.Builder(
                Component.nullToEmpty(I18n.get("gui.done")),
                button -> {
                    professionListWidget.setFocused(false);
                    CLIENT_CONFIG.save();
                    VillagerNameIsProfession.loadConfig();
                    Minecraft.getInstance().setScreen(parent);
                }
        ).pos(buttonX, buttonY).build();
        addRenderableWidget(DoneButton);
    }

    private @NotNull Checkbox getAVPCheckbox() {
        int checkBoxX = width / 2 + width / 6;
        int checkBoxY = 3;
        return  Checkbox.builder(Component.nullToEmpty(I18n.get("config.villagernameisprofession.alwaysVisibleProfession")), font)
                .pos(checkBoxX, checkBoxY)
                .selected(CLIENT_CONFIG.isAlwaysVisibleProfession())
                .onValueChange((checkbox, checked) -> CLIENT_CONFIG.setAlwaysVisibleProfession(checked))
                .build();
    }

    @Override
    public void tick() {
        super.tick();
    }


    @Override
    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredString(font, title, width / 2, 15, 0xFFFFFF);
    }

    protected void reInit(Screen parent) {
        Minecraft.getInstance().setScreen(new ConfigScreen(parent));
    }
}