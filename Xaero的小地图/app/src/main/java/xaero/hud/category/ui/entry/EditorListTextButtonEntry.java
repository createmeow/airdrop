package xaero.hud.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/EditorListTextButtonEntry.class */
public class EditorListTextButtonEntry extends EditorListEntryWithRootReference {
    private final Component text;
    private final int color;
    private final int hoverColor;
    private final int frameSize;
    private final Supplier<Boolean> action;

    public EditorListTextButtonEntry(int entryX, int entryY, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList, Component text, int color, int hoverColor, int frameSize, Supplier<Boolean> action, EditorListRootEntry root, Supplier<Tooltip> tooltipSupplier) {
        super((entryX - (Minecraft.getInstance().font.width(text) / 2)) - frameSize, entryY, Minecraft.getInstance().font.width(text) + (frameSize * 2), 9 + (frameSize * 2), index, rowList, root, tooltipSupplier);
        this.text = text;
        this.color = color;
        this.hoverColor = hoverColor;
        this.frameSize = frameSize;
        this.action = action;
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public EditorListEntry render(GuiGraphics guiGraphics, int index, int rowWidth, int rowHeight, int relativeMouseX, int relativeMouseY, boolean isMouseOver, float partialTicks, Font font, int globalMouseX, int globalMouseY, boolean includesSelected, boolean isRoot) {
        EditorListEntry result = super.render(guiGraphics, index, rowWidth, rowHeight, relativeMouseX, relativeMouseY, isMouseOver, partialTicks, font, globalMouseX, globalMouseY, includesSelected, isRoot);
        int textX = this.frameSize + (Minecraft.getInstance().font.width(this.text) / 2);
        guiGraphics.drawCenteredString(font, this.text, textX, this.frameSize + 1, isMouseOver ? this.hoverColor : this.color);
        return result;
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    protected boolean selectAction() {
        return this.action.get().booleanValue();
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public Component getMessage() {
        return this.text;
    }
}
