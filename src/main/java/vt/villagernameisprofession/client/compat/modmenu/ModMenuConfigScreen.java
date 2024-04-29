package vt.villagernameisprofession.client.compat.modmenu;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import vt.villagernameisprofession.client.VillagerNameIsProfessionClient;

@Environment(EnvType.CLIENT)
public class ModMenuConfigScreen extends Screen {
    protected final Screen parent;
    private ListWidget professionListWidget;

    ButtonWidget DoneButton;

    int radiusX;
    int radiusY;
    int radiusLabelX;
    int radiusLabelY;

    protected ModMenuConfigScreen(Screen parent) {
        super(Text.translatable("config.villagernameisprofession.title"));
        this.parent = parent;
    }

    public static Screen createScreen(Screen parent) {
        return new ModMenuConfigScreen(parent);
    }

    @Override
    protected void init() {
        professionListWidget = new ListWidget(this);
        addDrawableChild(professionListWidget);
        addSelectableChild(professionListWidget);

        CheckboxWidget alwaysVisibleProfessionCheckbox = getAVPCheckbox();
        addDrawableChild(alwaysVisibleProfessionCheckbox);

        radiusX = width / 4;
        radiusY = 6;
        TextFieldWidget radius = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, radiusX, radiusY, 30, 15, Text.of(""));
        radius.setMaxLength(256);
        radius.setText(String.valueOf(VillagerNameIsProfessionClient.CLIENT_CONFIG.getRadius()));
        int radiusLabelwidth = (I18n.translate("config.villagernameisprofession.radius").length() - 1) * 5;
        radiusLabelX = radiusX - radiusLabelwidth;
        radiusLabelY = radiusY + (radius.getHeight() - 8) / 2;

        addDrawableChild(new TextWidget(radiusLabelX, radiusY, radiusLabelwidth, 20, Text.of(I18n.translate("config.villagernameisprofession.radius")), MinecraftClient.getInstance().textRenderer));

        radius.setChangedListener(text -> {
            if (!text.isEmpty()) {
                VillagerNameIsProfessionClient.CLIENT_CONFIG.setRadius(Integer.parseInt(text));
            }
        });
        addDrawableChild(radius);

        int buttonX = width / 2 - ButtonWidget.DEFAULT_WIDTH / 2;
        int buttonY = height - 25;
        DoneButton = new ButtonWidget.Builder(
                Text.of(I18n.translate("gui.done")),
                button -> {
                    professionListWidget.setFocused(false);
                    VillagerNameIsProfessionClient.CLIENT_CONFIG.save();
                    VillagerNameIsProfessionClient.loadConfig();
                    MinecraftClient.getInstance().setScreen(parent);
                }
        ).position(buttonX, buttonY).build();
        addDrawableChild(DoneButton);
        super.init();
    }

    private @NotNull CheckboxWidget getAVPCheckbox() {
        int checkBoxX = width / 2 + width / 6;
        int checkBoxY = 3;
        return  CheckboxWidget.builder(Text.of(I18n.translate("config.villagernameisprofession.alwaysVisibleProfession")), textRenderer)
                .pos(checkBoxX, checkBoxY)
                .checked(VillagerNameIsProfessionClient.CLIENT_CONFIG.isAlwaysVisibleProfession())
                .callback((checkbox, checked) -> VillagerNameIsProfessionClient.CLIENT_CONFIG.setAlwaysVisibleProfession(checked))
                .build();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        professionListWidget.setPosition(0, 40);
        professionListWidget.setDimensions(width, height - DoneButton.getHeight() - 60);
        professionListWidget.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 15, 0xFFFFFF);
    }

    protected void reInit(Screen parent) {
        MinecraftClient.getInstance().setScreen(new ModMenuConfigScreen(parent));
        init();
    }


}
