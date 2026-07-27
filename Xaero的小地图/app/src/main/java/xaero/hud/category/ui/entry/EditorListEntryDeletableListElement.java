package xaero.hud.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.node.EditorNode;
import xaero.hud.category.ui.node.EditorSimpleDeletableWrapperNode;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/EditorListEntryDeletableListElement.class */
public class EditorListEntryDeletableListElement extends EditorListRootEntry {
    private final EditorSimpleDeletableWrapperNode.DeletionCallback deletionCallback;
    private final EditorNode parent;
    private static final Tooltip DELETE_TOOLTIP = new Tooltip("gui.xaero_category_delete_list_element", Style.EMPTY, true);

    public EditorListEntryDeletableListElement(int screenWidth, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList, ConnectionLineType lineType, EditorSimpleDeletableWrapperNode<?> node, EditorNode parent, EditorSimpleDeletableWrapperNode.DeletionCallback deletionCallback, Supplier<Tooltip> tooltipSupplier, boolean readOnly) {
        super(screenWidth, index, rowList, lineType, node);
        this.deletionCallback = deletionCallback;
        this.parent = parent;
        withSubEntry((x, y, w, h, root) -> {
            return new EditorListEntryTextWithAction(x, y, w, h, index, rowList, this, node.getExpandAction(rowList), tooltipSupplier);
        });
        if (readOnly) {
            return;
        }
        withSubEntry((x2, y2, w2, h2, root2) -> {
            return new EditorListTextButtonEntry(x2 - 24, y2 + 2, index, rowList, Component.literal("x"), -5636096, -43691, 5, () -> {
                return Boolean.valueOf(deletionCallback.delete(parent, node, rowList));
            }, this, DELETE_TOOLTIP);
        });
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public boolean keyPressed(int i, int j, int k, boolean isRoot) {
        if (i == 261) {
            if (!this.rowList.readOnly && this.deletionCallback.delete(this.parent, (EditorSimpleDeletableWrapperNode) this.node, this.rowList)) {
                this.rowList.restoreScrollAfterUpdate();
                this.rowList.updateEntries();
                return false;
            }
            return false;
        }
        return super.keyPressed(i, j, k, isRoot);
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public Component getMessage() {
        return this.node.getDisplayName();
    }
}
