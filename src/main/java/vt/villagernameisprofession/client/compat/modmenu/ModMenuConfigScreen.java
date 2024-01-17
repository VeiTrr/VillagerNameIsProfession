package vt.villagernameisprofession.client.compat.modmenu;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import vt.villagernameisprofession.client.config.ConfigManager;
import vt.villagernameisprofession.client.config.Configuration;

@Environment(EnvType.CLIENT)
public class ModMenuConfigScreen extends Screen {
    private final Screen parent;
    private final Configuration config;
    private ListWidget professionListWidget;

    protected ModMenuConfigScreen(Screen parent, Configuration config) {
        super(Text.translatable("config.villagernameisprofession.title"));
        this.parent = parent;
        this.config = config;
    }

    public static Screen createScreen(Screen parent, Configuration config) {
        return new ModMenuConfigScreen(parent, config);
    }

    @Override
    protected void init() {
        professionListWidget = new ListWidget(this, config);
        addSelectableChild(professionListWidget);


        int checkBoxX = width / 2 + width / 4;
        int checkBoxY = height / 56;
        CheckboxWidget alwaysVisibleProfessionCheckbox = new CheckboxWidget(checkBoxX, checkBoxY, I18n.translate("config.villagernameisprofession.alwaysVisibleProfession").length() * 5, 20, Text.of(I18n.translate("config.villagernameisprofession.alwaysVisibleProfession")), config.AlwaysVisbleProfession) {
            @Override
            public void onPress() {
                super.onPress();
                config.AlwaysVisbleProfession = this.isChecked();
            }
        };
        addDrawableChild(alwaysVisibleProfessionCheckbox);

        int radiusX = width / 4;
        int radiusY = height / 55;
        TextFieldWidget radius = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, radiusX, radiusY, 30, 15, Text.of(""));
        radius.setMaxLength(256);
        radius.setText(String.valueOf(config.Radius));
        int radiusLabelwidth = I18n.translate("config.villagernameisprofession.radius").length() * 5;
        int radiusLabelX = radiusX - radiusLabelwidth - 10;
        addDrawableChild(new TextWidget( radiusLabelX, radiusY, radiusLabelwidth, 20, Text.of(I18n.translate("config.villagernameisprofession.radius")), MinecraftClient.getInstance().textRenderer));

        radius.setChangedListener(text -> {
            if (!text.isEmpty()) {
                config.Radius = Integer.parseInt(text);
            }
        });
        addDrawableChild(radius);

        int buttonX = width / 2 - ButtonWidget.DEFAULT_WIDTH / 2;
        int buttonY = height - 25;
        addDrawableChild(new ButtonWidget.Builder(
                Text.of(I18n.translate("gui.done")),
                button -> {
                    professionListWidget.setFocused(false);
                    ConfigManager.save();
                    MinecraftClient.getInstance().setScreen(parent);
                }
        ).position(buttonX, buttonY).build());
        super.init();
    }

    @Override
    public void tick() {
        professionListWidget.tick();
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
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        professionListWidget.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredTextWithShadow(matrices, textRenderer, title, width / 2, 15, 0xFFFFFF);
    }

    public void reInit() {
        init();
    }


}
