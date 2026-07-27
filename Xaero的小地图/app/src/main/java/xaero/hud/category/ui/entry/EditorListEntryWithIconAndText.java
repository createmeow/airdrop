package xaero.hud.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/EditorListEntryWithIconAndText.class */
public class EditorListEntryWithIconAndText extends EditorListEntryWithIcon {
    protected Component text;
    protected int color;
    protected int hoverColor;

    public EditorListEntryWithIconAndText(int entryX, int entryY, int entryW, int entryH, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList, Component text, EditorListRootEntry root, Supplier<Tooltip> tooltipSupplier) {
        super(entryX, entryY, entryW, entryH, index, rowList, 0, 0, 0, 0, root, tooltipSupplier);
        this.text = text;
        this.color = -5592406;
        this.hoverColor = -1;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setHoverColor(int hoverColor) {
        this.hoverColor = hoverColor;
    }

    public int getColor() {
        return this.color;
    }

    public int getHoverColor() {
        return this.hoverColor;
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntryWithIcon, xaero.hud.category.ui.entry.EditorListEntry
    public EditorListEntry render(GuiGraphics guiGraphics, int index, int rowWidth, int rowHeight, int relativeMouseX, int relativeMouseY, boolean isMouseOver, float partialTicks, Font font, int globalMouseX, int globalMouseY, boolean includesSelected, boolean isRoot) {
        EditorListEntry result = super.render(guiGraphics, index, rowWidth, rowHeight, relativeMouseX, relativeMouseY, isMouseOver, partialTicks, font, globalMouseX, globalMouseY, includesSelected, isRoot);
        int textColor = isMouseOver ? getHoverColor() : getColor();
        guiGraphics.drawString(font, this.text, 4, 8, textColor);
        return result;
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntryWithIcon, xaero.hud.category.ui.entry.EditorListEntry
    public Component getMessage() {
        return this.text;
    }
}
