package xaero.hud.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.entry.EditorListRootEntry;
import xaero.hud.category.ui.node.EditorNode;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/EditorListEntryWrapper.class */
public class EditorListEntryWrapper extends EditorListRootEntry {
    public EditorListEntryWrapper(EditorListRootEntry.CenteredEntryFactory wrappedFactory, int screenWidth, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList, ConnectionLineType lineType, EditorNode node) {
        this(wrappedFactory, screenWidth, index, rowList, lineType, node, null);
    }

    public EditorListEntryWrapper(EditorListRootEntry.CenteredEntryFactory wrappedFactory, int screenWidth, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList, ConnectionLineType lineType, EditorNode node, Supplier<Tooltip> helpTooltipSupplier) {
        super(screenWidth, index, rowList, lineType, node);
        withSubEntry(wrappedFactory);
        addHelpElement(helpTooltipSupplier);
    }

    @Override // xaero.hud.category.ui.entry.EditorListEntry
    public Component getMessage() {
        return Component.literal("");
    }
}
