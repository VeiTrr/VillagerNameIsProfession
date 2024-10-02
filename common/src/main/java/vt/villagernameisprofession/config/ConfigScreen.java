package vt.villagernameisprofession.config;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import vt.villagernameisprofession.VillagerNameIsProfession;

import static vt.villagernameisprofession.VillagerNameIsProfession.CLIENT_CONFIG;

public class ConfigScreen extends Screen {
    protected final Screen parent;
    private ListWidget professionListWidget;

    int radiusX;
    int radiusY;
    int radiusLabelX;
    int radiusLabelY;

    protected ConfigScreen(Screen parent) {
        super(Text.translatable("config.villagernameisprofession.title"));
        this.parent = parent;
    }

    public static Screen createScreen(Screen parent) {
        return new ConfigScreen(parent);
    }

    @Override
    protected void init() {
        professionListWidget = new ListWidget(this);
        addSelectableChild(professionListWidget);

        CheckboxWidget alwaysVisibleProfessionCheckbox = getAVPCheckbox();
        addDrawableChild(alwaysVisibleProfessionCheckbox);

        radiusX = width / 4;
        radiusY = 6;
        TextFieldWidget radius = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, radiusX, radiusY, 30, 15, Text.of(""));
        radius.setMaxLength(256);
        radius.setText(String.valueOf(CLIENT_CONFIG.getRadius()));
        int radiusLabelwidth = (I18n.translate("config.villagernameisprofession.radius").length() - 1) * 5;
        radiusLabelX = radiusX - radiusLabelwidth;
        radiusLabelY = radiusY + (radius.getHeight() - 8) / 2;


        radius.setChangedListener(text -> {
            if (!text.isEmpty()) {
                CLIENT_CONFIG.setRadius(Integer.parseInt(text));
            }
        });
        addDrawableChild(radius);

        int buttonX = width / 2 - 120 / 2;
        int buttonY = height - 25;
        addDrawableChild(new ButtonWidget(buttonX, buttonY, 120, 20,
                Text.of(I18n.translate("gui.done")),
                button -> {
                    CLIENT_CONFIG.save();
                    VillagerNameIsProfession.loadConfig();
                    MinecraftClient.getInstance().setScreen(parent);
                }
        ));
        super.init();
    }

    private @NotNull CheckboxWidget getAVPCheckbox() {
        int checkBoxX = width / 2 + width / 6;
        int checkBoxY = 3;
        return new CheckboxWidget(checkBoxX, checkBoxY, I18n.translate("config.villagernameisprofession.alwaysVisibleProfession").length() * 5, 20, Text.of(I18n.translate("config.villagernameisprofession.alwaysVisibleProfession")), CLIENT_CONFIG.isAlwaysVisibleProfession()) {
            @Override
            public void onPress() {
                super.onPress();
                CLIENT_CONFIG.setAlwaysVisibleProfession(this.isChecked());
            }
        };
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
        drawCenteredText(matrices, textRenderer, title, width / 2, 10, 0xFFFFFF);
        drawCenteredText(matrices, textRenderer, Text.of(I18n.translate("config.villagernameisprofession.radius")), radiusLabelX, radiusLabelY, 0xFFFFFF);
    }

    protected void reInit(Screen parent) {
        MinecraftClient.getInstance().setScreen(new ConfigScreen(parent));
        init();
    }


}
