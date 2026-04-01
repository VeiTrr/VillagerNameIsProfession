package vt.villagernameisprofession.config;

import net.minecraft.client.gui.components.*;
import org.jspecify.annotations.NonNull;
import vt.villagernameisprofession.VillagerNameIsProfession;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

import static vt.villagernameisprofession.VillagerNameIsProfession.CLIENT_CONFIG;

public class ListWidget extends ContainerObjectSelectionList<ListWidget.Entry> {
    private final ConfigScreen parent;

    public ListWidget(ConfigScreen parent) {
        super(Minecraft.getInstance(), parent.width, parent.height, 40, 25);
        this.parent = parent;
        this.addEntry(new ListWidget.Entry());
        for (String profession : CLIENT_CONFIG.getProfession()) {
            this.addEntry(new ListWidget.Entry(profession));
        }
    }


    public void addNewEntry(ListWidget.Entry entry) {
        this.addEntry(entry);
    }

    public void updateConfig() {
        List<String> professions = new ArrayList<>();
        for (ListWidget.Entry entry : this.children()) {
            if (entry.getProfession() != null && !entry.getProfession().isEmpty()) {
                professions.add(entry.getProfession());
            }
        }
        CLIENT_CONFIG.setProfession(professions);
        CLIENT_CONFIG.save();
        VillagerNameIsProfession.loadConfig();
        parent.reInit(parent.parent);
    }

    @Override
    public int getRowWidth() {
        return 400;
    }

    @Override
    protected int scrollBarX() {
        return super.scrollBarX() + width / 10;
    }

    public class Entry extends ContainerObjectSelectionList.Entry<ListWidget.Entry> {
        private String profession;
        private Button editButton;
        private final Button deleteButton;
        private final EditBox textField;
        private final StringWidget textFieldNoEdit;
        private boolean isEditing = false;
        private Checkbox modeSwitchCheckbox = Checkbox.builder(Component.nullToEmpty(""), Minecraft.getInstance().font).build();

        //Entry for the list
        public Entry(String profession) {
            this.modeSwitchCheckbox.visible = false;
            this.modeSwitchCheckbox.active = false;
            this.profession = profession;
            this.textField = new EditBox(Minecraft.getInstance().font, 0, 0, 200, 20, Component.nullToEmpty(""));
            this.textField.setMaxLength(256);
            this.textField.setValue(profession);
            this.textField.setVisible(false);
            this.textFieldNoEdit = new StringWidget(0, 0, 200, 20, Component.nullToEmpty(profession), Minecraft.getInstance().font);

            this.editButton = new Button.Builder(Component.nullToEmpty(I18n.get("config.villagernameisprofession.edit")), button -> {
                if (!this.isEditing) {
                    this.textField.setVisible(true);
                    this.textField.setEditable(true);
                    this.textField.setFocused(true);
                    this.editButton.setMessage(Component.nullToEmpty(I18n.get("config.villagernameisprofession.save")));
                    this.isEditing = true;
                    this.textFieldNoEdit.visible = false;
                } else {
                    this.profession = textField.getValue();
                    this.textField.setEditable(false);
                    this.editButton.setMessage(Component.nullToEmpty(I18n.get("config.villagernameisprofession.edit")));
                    this.textField.setVisible(false);
                    this.isEditing = false;
                    this.textFieldNoEdit.visible = true;
                    updateConfig();
                }
            }).pos(0, 0).size(75, 20).build();

            this.deleteButton = new Button.Builder(Component.nullToEmpty(I18n.get("config.villagernameisprofession.delete")), button -> {
                this.profession = "";
                updateConfig();
            }).pos(0, 0).size(75, 20).build();
        }

        //Entry for the adding field and button
        public Entry() {
            this.textField = new EditBox(Minecraft.getInstance().font, 0, 0, 200, 20, Component.nullToEmpty(""));
            this.textFieldNoEdit = new StringWidget(0, 0, 200, 20, Component.nullToEmpty(""), Minecraft.getInstance().font);
            this.textFieldNoEdit.visible = false;
            this.textField.setMaxLength(256);
            this.textField.setValue("");
            this.textField.setVisible(true);
            this.textField.setEditable(true);
            this.deleteButton = new Button.Builder(Component.nullToEmpty(I18n.get("config.villagernameisprofession.delete")), button -> {
            }).pos(0, 0).size(0, 0).build();
            this.deleteButton.visible = false;
            this.deleteButton.active = false;

            this.editButton = new Button.Builder(Component.nullToEmpty(I18n.get("config.villagernameisprofession.add")), button -> {
                if (textField.getValue().isEmpty()) {
                    return;
                }
                addNewEntry(new ListWidget.Entry(textField.getValue()));
                this.textField.setValue("");
                updateConfig();
            }).pos(0, 0).size(75, 20).build();

            this.modeSwitchCheckbox = Checkbox.builder(Component.nullToEmpty(I18n.get("config.villagernameisprofession.modeSwitch")), Minecraft.getInstance().font)
                    .pos(0, 0)
                    .selected(CLIENT_CONFIG.isProfessionListBlocking())
                    .onValueChange((checkbox, checked) -> {
                        CLIENT_CONFIG.setProfessionListBlocking(checked);
                        CLIENT_CONFIG.save();
                    })
                    .build();
        }


        @Override
        public void renderContent(@NonNull GuiGraphics context, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int rowIndex = ListWidget.this.children().indexOf(this);
            int x = ListWidget.this.getRowLeft();
            int y = ListWidget.this.getRowTop(rowIndex);

            this.textField.setX(x);
            this.textField.setY(y);
            this.textFieldNoEdit.setX(this.textField.getX());
            this.textFieldNoEdit.setY(this.textField.getY());


            editButton.setX(textField.getX() + textField.getWidth() + 5);
            editButton.setY(textField.getY());
            deleteButton.setX(editButton.getX() + editButton.getWidth() + 5);
            deleteButton.setY(editButton.getY());
            modeSwitchCheckbox.setX(deleteButton.getX() + deleteButton.getWidth() + 5);
            modeSwitchCheckbox.setY(deleteButton.getY());

            textField.render(context, mouseX, mouseY, tickDelta);
            textFieldNoEdit.render(context, mouseX, mouseY, tickDelta);
            editButton.render(context, mouseX, mouseY, tickDelta);
            deleteButton.render(context, mouseX, mouseY, tickDelta);
            modeSwitchCheckbox.render(context, mouseX, mouseY, tickDelta);
        }

        @Override
        public boolean mouseClicked(@NonNull MouseButtonEvent click, boolean doubled) {
            boolean textFieldClicked = this.textField.mouseClicked(click, doubled);
            this.textField.setFocused(textFieldClicked);
            return modeSwitchCheckbox.mouseClicked(click, doubled) || editButton.mouseClicked(click, doubled) || deleteButton.mouseClicked(click, doubled) || textFieldClicked;
        }

        @Override
        public boolean charTyped(@NonNull CharacterEvent input) {
            if (this.textField.isFocused()) {
                return this.textField.charTyped(input);
            }
            return super.charTyped(input);
        }

        @Override
        public boolean keyPressed(@NonNull KeyEvent input) {
            if (this.textField.isFocused()) {
                return this.textField.keyPressed(input);
            }
            return super.keyPressed(input);
        }

        @Override
        public @NonNull List<? extends GuiEventListener> children() {
            return List.of(textField, editButton, deleteButton, modeSwitchCheckbox);
        }

        @Override
        public @NonNull List<? extends NarratableEntry> narratables() {
            return List.of(textField, editButton, deleteButton, modeSwitchCheckbox);
        }

        public String getProfession() {
            return profession;
        }
    }


}