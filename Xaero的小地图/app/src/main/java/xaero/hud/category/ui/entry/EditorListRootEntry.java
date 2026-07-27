package xaero.hud.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.node.EditorNode;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/EditorListRootEntry.class */
public abstract class EditorListRootEntry extends EditorListEntry {
    private final ConnectionLineType lineType;
    protected final EditorNode node;

    @FunctionalInterface
    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/EditorListRootEntry$CenteredEntryFactory.class */
    public interface CenteredEntryFactory {
        EditorListEntry get(int i, int i2, int i3, int i4, EditorListRootEntry editorListRootEntry);
    }

    public EditorListRootEntry(int screenWidth, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList, ConnectionLineType lineType, EditorNode node) {
        super(0, 0, screenWidth, 24, index, rowList, () -> {
            return null;
        });
        this.lineType = lineType;
        this.node = node;
    }

    protected void addHelpElement(Supplier<Tooltip> helpTooltipSupplier) {
        if (helpTooltipSupplier == null) {
            return;
        }
        withSubEntry((x, y, w, h, root) -> {
            return new EditorListTextButtonEntry(x - 24, y + 2, this.index, this.rowList, Component.literal("?"), -5592406, -1, 5, () -> {
                return false;
            }, this, helpTooltipSupplier);
        });
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public EditorListEntry render(GuiGraphics guiGraphics, int index, int rowWidth, int rowHeight, int relativeMouseX, int relativeMouseY, boolean isMouseOver, float partialTicks, Font font, int globalMouseX, int globalMouseY, boolean includesSelected, boolean isRoot) {
        EditorListEntry result = super.render(guiGraphics, index, rowWidth, rowHeight, relativeMouseX, relativeMouseY, isMouseOver, partialTicks, font, globalMouseX, globalMouseY, includesSelected, isRoot);
        int xOffset = (rowWidth / 2) - 110;
        if (this.lineType == ConnectionLineType.TAIL_LEAF || this.lineType == ConnectionLineType.HEAD_LEAF) {
            int leftX = xOffset - 14;
            int rightX = xOffset - 2;
            int bottomY = 8 + 4;
            int topY = (8 - 24) + 4;
            guiGraphics.hLine(leftX, rightX, bottomY, -5592406);
            guiGraphics.vLine(leftX, topY, bottomY, -5592406);
            guiGraphics.vLine(rightX - 1, bottomY - 2, bottomY + 2, -5592406);
            guiGraphics.vLine(rightX - 2, bottomY - 3, bottomY + 3, -5592406);
            if (this.lineType == ConnectionLineType.HEAD_LEAF) {
                guiGraphics.hLine(leftX, rightX, topY, -5592406);
            }
            return result;
        }
        if (this.lineType != ConnectionLineType.PATH) {
            return result;
        }
        int bottomY2 = 8 - 2;
        int lineX = xOffset + 12;
        guiGraphics.hLine(lineX - 2, lineX + 2, bottomY2 - 3, -5592406);
        guiGraphics.hLine(lineX - 1, lineX + 1, bottomY2 - 2, -5592406);
        guiGraphics.vLine(lineX, (8 - 24) + 9, bottomY2, -5592406);
        return result;
    }

    public EditorListRootEntry withSubEntry(CenteredEntryFactory entryFactory) {
        super.withSubEntry(entryFactory.get(((this.rowList.getRowWidth() / 2) - 110) - 1, 0, GuiCategoryEditor.ROW_WIDTH, 24, this));
        return this;
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    protected boolean selectAction() {
        return false;
    }
}
