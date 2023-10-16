package vt.villagernameisprofession.client.compat.modmenu;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

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
        drawCenteredText(matrices, textRenderer, title, width / 2, 15, 0xFFFFFF);
    }

    public void reInit() {
        init();
    }


}
