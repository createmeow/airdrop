package xaero.hud.category.ui.entry;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/EditorListEntry.class */
public abstract class EditorListEntry {
    protected final int entryRelativeX;
    protected final int entryRelativeY;
    protected final int entryW;
    protected final int entryH;
    protected final int index;
    protected final GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList;
    protected final Supplier<Tooltip> tooltipSupplier;
    protected EditorListEntry hoveredSubEntry;
    protected final List<EditorListEntry> subEntries = new ArrayList();
    protected int focusedSubEntryIndex = -1;

    public abstract Component getMessage();

    protected abstract boolean selectAction();

    public EditorListEntry(int entryX, int entryY, int entryW, int entryH, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList, Supplier<Tooltip> tooltipSupplier) {
        this.entryRelativeX = entryX;
        this.entryRelativeY = entryY;
        this.entryW = entryW;
        this.entryH = entryH;
        this.index = index;
        this.rowList = rowList;
        this.tooltipSupplier = tooltipSupplier;
    }

    public EditorListEntry onSelected() {
        if (!this.subEntries.isEmpty() && this.focusedSubEntryIndex >= 0) {
            EditorListEntry subEntry = this.subEntries.get(this.focusedSubEntryIndex);
            return subEntry.onSelected();
        }
        if (!selectAction()) {
            return this;
        }
        if (!(this instanceof EditorListEntryWidget)) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
        }
        this.rowList.updateEntries();
        return this;
    }

    public boolean mouseClicked(GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList.Entry entry, double relativeMouseX, double relativeMouseY, int i) {
        for (int subIndex = 0; subIndex < this.subEntries.size(); subIndex++) {
            EditorListEntry subEntry = this.subEntries.get(subIndex);
            if (subEntry.isHoveredOver(relativeMouseX, relativeMouseY)) {
                double subRelativeMouseX = relativeMouseX - subEntry.entryRelativeX;
                double subRelativeMouseY = relativeMouseY - subEntry.entryRelativeY;
                if (this.focusedSubEntryIndex != subIndex) {
                    unfocusRecursively();
                    this.focusedSubEntryIndex = subIndex;
                }
                if (!subEntry.mouseClicked(entry, subRelativeMouseX, subRelativeMouseY, subIndex)) {
                    subEntry.confirmSelection();
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    public EditorListEntry confirmSelection() {
        if (this.focusedSubEntryIndex >= 0) {
            return this.subEntries.get(this.focusedSubEntryIndex).confirmSelection();
        }
        return onSelected();
    }

    public boolean mouseReleased(double relativeMouseX, double relativeMouseY, int i) {
        for (EditorListEntry subEntry : this.subEntries) {
            double subRelativeMouseX = relativeMouseX - subEntry.entryRelativeX;
            double subRelativeMouseY = relativeMouseY - subEntry.entryRelativeY;
            subEntry.mouseReleased(subRelativeMouseX, subRelativeMouseY, i);
        }
        return false;
    }

    public boolean mouseScrolled(double relativeMouseX, double relativeMouseY, double f, double g) {
        for (EditorListEntry subEntry : this.subEntries) {
            double subRelativeMouseX = relativeMouseX - subEntry.entryRelativeX;
            double subRelativeMouseY = relativeMouseY - subEntry.entryRelativeY;
            if (subEntry.isHoveredOver(relativeMouseX, relativeMouseY)) {
                return subEntry.mouseScrolled(subRelativeMouseX, subRelativeMouseY, f, g);
            }
        }
        return false;
    }

    public boolean mouseDragged(double relativeMouseX, double relativeMouseY, int i, double f, double g) {
        for (EditorListEntry subEntry : this.subEntries) {
            double subRelativeMouseX = relativeMouseX - subEntry.entryRelativeX;
            double subRelativeMouseY = relativeMouseY - subEntry.entryRelativeY;
            subEntry.mouseDragged(subRelativeMouseX, subRelativeMouseY, i, f, g);
        }
        return false;
    }

    public void mouseMoved(double relativeMouseX, double relativeMouseY) {
    }

    public boolean keyPressed(int i, int j, int k, boolean isRoot) {
        if (isRoot) {
            if (i == 263 && moveFocus(-1)) {
                return false;
            }
            if (i == 262 && moveFocus(1)) {
                return false;
            }
        }
        if (this.subEntries.isEmpty() || this.focusedSubEntryIndex < 0) {
            return false;
        }
        EditorListEntry subEntry = this.subEntries.get(this.focusedSubEntryIndex);
        return subEntry.keyPressed(i, j, k, false);
    }

    public boolean keyReleased(int i, int j, int k) {
        if (this.subEntries.isEmpty()) {
            return false;
        }
        for (EditorListEntry subEntry : this.subEntries) {
            subEntry.keyReleased(i, j, k);
        }
        return false;
    }

    public boolean charTyped(char c, int i) {
        if (this.subEntries.isEmpty() || this.focusedSubEntryIndex < 0) {
            return false;
        }
        EditorListEntry subEntry = this.subEntries.get(this.focusedSubEntryIndex);
        return subEntry.charTyped(c, i);
    }

    public void tick() {
        if (this.subEntries.isEmpty()) {
            return;
        }
        for (EditorListEntry subEntry : this.subEntries) {
            subEntry.tick();
        }
    }

    public String getSubNarration() {
        if (this.hoveredSubEntry == null) {
            return getSelectedNarration();
        }
        return getHoveredNarration();
    }

    public String getHoveredNarration() {
        if (this.hoveredSubEntry == null) {
            return getHoverNarration();
        }
        return this.hoveredSubEntry.getHoveredNarration();
    }

    public String getSelectedNarration() {
        if (this.focusedSubEntryIndex == -1) {
            return getNarration();
        }
        EditorListEntry subEntry = this.subEntries.get(this.focusedSubEntryIndex);
        return subEntry.getSelectedNarration();
    }

    public Supplier<Tooltip> getTooltipSupplier() {
        return this.tooltipSupplier;
    }

    public Component getNarrationMessage() {
        return getMessage();
    }

    public String getNarration() {
        StringBuilder narrationBuilder = new StringBuilder();
        narrationBuilder.append(getNarrationMessage().getString());
        if (this.tooltipSupplier == null) {
            return narrationBuilder.toString();
        }
        Tooltip tooltip = this.tooltipSupplier.get();
        if (tooltip != null) {
            narrationBuilder.append(" . ").append(this.tooltipSupplier.get().getPlainText());
        }
        return narrationBuilder.toString();
    }

    public String getHoverNarration() {
        return getNarration();
    }

    public void preRender(GuiGraphics guiGraphics, boolean includesSelected, boolean isRoot) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(this.entryRelativeX, this.entryRelativeY, 0.0f);
        if (!includesSelected || this.focusedSubEntryIndex != -1) {
            return;
        }
        guiGraphics.fill(0, 0, this.entryW, this.entryH, this.rowList.isFocused() ? -1 : -8355712);
        guiGraphics.fill(1, 1, this.entryW - 1, this.entryH - 1, -16777216);
    }

    public EditorListEntry render(GuiGraphics guiGraphics, int index, int rowWidth, int rowHeight, int relativeMouseX, int relativeMouseY, boolean isMouseOver, float partialTicks, Font font, int globalMouseX, int globalMouseY, boolean includesSelected, boolean isRoot) {
        this.hoveredSubEntry = null;
        EditorListEntry result = isMouseOver ? this : null;
        int i = 0;
        while (i < this.subEntries.size()) {
            EditorListEntry subEntry = this.subEntries.get(i);
            boolean subIsHovered = subEntry.isHoveredOver(relativeMouseX, relativeMouseY);
            boolean subIncludesSelected = includesSelected && this.focusedSubEntryIndex == i;
            subEntry.preRender(guiGraphics, subIncludesSelected, false);
            EditorListEntry subResult = subEntry.render(guiGraphics, index, rowWidth, rowHeight, relativeMouseX - subEntry.entryRelativeX, relativeMouseY - subEntry.entryRelativeY, subIsHovered, partialTicks, font, globalMouseX, globalMouseY, subIncludesSelected, false);
            subEntry.postRender(guiGraphics);
            if (subIsHovered) {
                this.hoveredSubEntry = subEntry;
                result = subResult;
            }
            i++;
        }
        return result;
    }

    public void postRender(GuiGraphics guiGraphics) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.popPose();
    }

    public boolean isHoveredOver(double relativeMouseX, double relativeMouseY) {
        return relativeMouseX >= ((double) this.entryRelativeX) && relativeMouseX < ((double) (this.entryRelativeX + this.entryW)) && relativeMouseY >= ((double) this.entryRelativeY) && relativeMouseY < ((double) (this.entryRelativeY + this.entryH));
    }

    public void setFocused(boolean bl) {
    }

    public void unhoverRecursively() {
        if (this.hoveredSubEntry == null) {
            return;
        }
        this.hoveredSubEntry.unhoverRecursively();
        this.hoveredSubEntry = null;
    }

    public boolean moveFocus(int direction) {
        unhoverRecursively();
        if (!moveFocus(direction, true)) {
            return false;
        }
        this.rowList.narrateSelection();
        return true;
    }

    public boolean moveFocus(int direction, boolean isRoot) {
        if (this.subEntries.isEmpty()) {
            return false;
        }
        if (this.focusedSubEntryIndex >= 0 && this.subEntries.get(this.focusedSubEntryIndex).moveFocus(direction, false)) {
            return true;
        }
        int potentialValue = this.focusedSubEntryIndex + direction;
        if (potentialValue < 0 || potentialValue >= this.subEntries.size()) {
            if (!isRoot) {
                return false;
            }
            potentialValue = potentialValue < 0 ? this.subEntries.size() - 1 : 0;
        }
        if (this.focusedSubEntryIndex == potentialValue) {
            return false;
        }
        this.focusedSubEntryIndex = potentialValue;
        EditorListEntry focusedSub = this.subEntries.get(this.focusedSubEntryIndex);
        if (direction < 0) {
            focusedSub.focusLastRecursively();
            return true;
        }
        focusedSub.focusFirstRecursively();
        return true;
    }

    public void unfocusRecursively() {
        setFocused(false);
        if (this.subEntries.isEmpty()) {
            return;
        }
        if (this.focusedSubEntryIndex >= 0) {
            this.subEntries.get(this.focusedSubEntryIndex).unfocusRecursively();
        }
        this.focusedSubEntryIndex = -1;
    }

    public void focusFirstRecursively() {
        setFocused(true);
        if (this.subEntries.isEmpty()) {
            return;
        }
        this.focusedSubEntryIndex = 0;
        this.subEntries.get(this.focusedSubEntryIndex).focusFirstRecursively();
    }

    public void focusLastRecursively() {
        setFocused(true);
        if (this.subEntries.isEmpty()) {
            return;
        }
        this.focusedSubEntryIndex = this.subEntries.size() - 1;
        this.subEntries.get(this.focusedSubEntryIndex).focusLastRecursively();
    }

    public EditorListEntry withSubEntry(EditorListEntry entry) {
        this.subEntries.add(entry);
        return this;
    }

    public int getEntryRelativeX() {
        return this.entryRelativeX;
    }

    public int getEntryRelativeY() {
        return this.entryRelativeY;
    }
}
