package vt.villagernameisprofession.client.compat.modmenu;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.List;

public class ListWidget extends ElementListWidget<ListWidget.Entry> {
    private final ModMenuConfigScreen parent;
    private final Configuration config;
    private Entry editingElement = null;

    public ListWidget(ModMenuConfigScreen parent, Configuration config) {
        super(MinecraftClient.getInstance(), parent.width, parent.height, 25, parent.height - 30, 25);
        this.parent = parent;
        this.config = config;

        for (String profession : config.profession) {
            this.addEntry(new Entry(profession));
        }
    }

    public void tick() {
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }

    public class Entry extends ElementListWidget.Entry<Entry> {
        private String profession;
        private ButtonWidget editButton;
        private TextFieldWidget textField;
        private boolean isEditing = false;

        public Entry(String profession) {
            this.profession = profession;
            this.textField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 150, 20, Text.of(""));
            this.textField.setMaxLength(256);
            this.textField.setText(profession);
            this.textField.setVisible(false);

            this.editButton = new ButtonWidget.Builder(Text.of(I18n.translate("Edit")), button -> {
                if (!isEditing) {
                    this.textField.setVisible(true);
                    this.textField.setEditable(true);
                    this.textField.setFocused(true);
                    this.editButton.setMessage(Text.of(I18n.translate("Save")));
                    this.isEditing = true;
                } else {
                    this.profession = textField.getText();
                    this.textField.setEditable(false);
                    this.editButton.setMessage(Text.of(I18n.translate("Edit")));
                    this.textField.setVisible(false);
                    this.isEditing = false;
                }

            }).position(0, 0).size(50, 20).build();

        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            drawCenteredTextWithShadow(matrices, MinecraftClient.getInstance().textRenderer, profession, x + 25, y + 5, 0xFFFFFF);
            textField.setX(x);
            textField.setY(y);
            editButton.setX(x + entryWidth - 50);
            editButton.setY(y);
            textField.render(matrices, mouseX, mouseY, tickDelta);
            editButton.render(matrices, mouseX, mouseY, tickDelta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return editButton.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public List<? extends Element> children() {
            return Collections.singletonList(textField);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return Collections.singletonList(editButton);
        }
    }
}
