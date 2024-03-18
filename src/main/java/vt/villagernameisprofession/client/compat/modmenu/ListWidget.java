package vt.villagernameisprofession.client.compat.modmenu;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import vt.villagernameisprofession.client.VillagerNameIsProfessionClient;
import vt.villagernameisprofession.client.config.ConfigManager;
import vt.villagernameisprofession.client.config.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListWidget extends ElementListWidget<ListWidget.Entry> {
    private final ModMenuConfigScreen parent;

    public ListWidget(ModMenuConfigScreen parent, Configuration config) {
        super(MinecraftClient.getInstance(), parent.width, parent.height, parent.height - 30, 25);
        this.parent = parent;
        this.addEntry(new Entry());
        for (String profession : config.profession) {
            this.addEntry(new Entry(profession));
        }
    }

    public class Entry extends ElementListWidget.Entry<Entry> {
        private String profession;
        private ButtonWidget editButton;
        private final ButtonWidget deleteButton;
        private final TextFieldWidget textField;
        private boolean isEditing = false;

        //Entry for the list
        public Entry(String profession) {
            this.profession = profession;
            this.textField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 200, 20, Text.of(""));
            this.textField.setMaxLength(256);
            this.textField.setText(profession);
            this.textField.setVisible(false);

            this.editButton = new ButtonWidget.Builder(Text.of(I18n.translate("config.villagernameisprofession.edit")), button -> {
                if (!this.isEditing) {
                    this.textField.setVisible(true);
                    this.textField.setEditable(true);
                    this.textField.setFocused(true);
                    this.editButton.setMessage(Text.of(I18n.translate("config.villagernameisprofession.save")));
                    this.isEditing = true;
                } else {
                    this.profession = textField.getText();
                    this.textField.setEditable(false);
                    this.editButton.setMessage(Text.of(I18n.translate("config.villagernameisprofession.edit")));
                    this.textField.setVisible(false);
                    this.isEditing = false;
                    updateConfig();
                }
            }).position(0, 0).size(75, 20).build();

            this.deleteButton = new ButtonWidget.Builder(Text.of(I18n.translate("config.villagernameisprofession.delete")), button -> {
                this.profession = "";
                updateConfig();
            }).position(0, 0).size(75, 20).build();
        }

        //Entry for the adding field and button
        public Entry() {
            this.textField = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, 0, 0, 200, 20, Text.of(""));
            this.textField.setMaxLength(256);
            this.textField.setText("");
            this.textField.setVisible(true);
            this.deleteButton = new ButtonWidget.Builder(Text.of(I18n.translate("config.villagernameisprofession.delete")), button -> {
            }).position(0, 0).size(0, 0).build();
            this.deleteButton.visible = false;
            this.deleteButton.active = false;

            this.editButton = new ButtonWidget.Builder(Text.of(I18n.translate("config.villagernameisprofession.add")), button -> {
                if (textField.getText().isEmpty()) {
                    return;
                }
                addNewEntry(new Entry(textField.getText()));
                this.textField.setText("");
                this.isEditing = false;
                updateConfig();

            }).position(0, 0).size(75, 20).build();
        }


        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, profession, x + 100, y + 5, 0xFFFFFF);
            textField.setX(x);
            textField.setY(y);
            editButton.setX(textField.getX() + textField.getWidth() + 5);
            editButton.setY(textField.getY());
            deleteButton.setX(editButton.getX() + editButton.getWidth() + 5);
            deleteButton.setY(editButton.getY());
            textField.render(context, mouseX, mouseY, tickDelta);
            editButton.render(context, mouseX, mouseY, tickDelta);
            deleteButton.render(context, mouseX, mouseY, tickDelta);
        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return editButton.mouseClicked(mouseX, mouseY, button) || deleteButton.mouseClicked(mouseX, mouseY, button) || textField.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean charTyped(char chr, int modifiers) {
            if (this.textField.isFocused()) {
                return this.textField.charTyped(chr, modifiers);
            }
            return super.charTyped(chr, modifiers);
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            super.keyPressed(keyCode, scanCode, modifiers);
            return textField.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public List<? extends Element> children() {
            return Collections.singletonList(textField);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return Collections.singletonList(editButton);
        }

        public String getProfession() {
            return profession;
        }
    }

    public void addNewEntry(Entry entry) {
        this.addEntry(entry);
    }

    public void updateConfig() {
        List<String> professions = new ArrayList<>();
        for (Entry entry : this.children()) {
            if (entry.getProfession() != null && !entry.getProfession().isEmpty()) {
                professions.add(entry.getProfession());
            }
        }
        ConfigManager.getConfig().profession = professions;
        ConfigManager.save();
        VillagerNameIsProfessionClient.loadConfig();
        parent.reInit();
    }

    @Override
    public int getRowWidth() {
        return 400;
    }

    @Override
    protected int getScrollbarPositionX() {
        return super.getScrollbarPositionX() + width / 10;
    }
}
