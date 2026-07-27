package xaero.hud.category.ui.entry;

import java.util.function.Supplier;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/EditorListEntryExpandingOptions.class */
public class EditorListEntryExpandingOptions extends EditorListEntryWidget {
    public EditorListEntryExpandingOptions(int entryX, int entryY, int entryW, int entryH, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList, EditorListRootEntry root, AbstractWidget widget, Supplier<Component> messageSupplier, Supplier<Tooltip> tooltipSupplier) {
        super(entryX, entryY, entryW, entryH, index, rowList, root, widget, tooltipSupplier);
        if (messageSupplier == null) {
            return;
        }
        Component optionTypeName = messageSupplier.get();
        if (!root.node.isExpanded()) {
            widget.setMessage(optionTypeName);
        } else {
            widget.setMessage(Component.translatable("gui.xaero_category_expanded_options", new Object[]{optionTypeName}));
        }
    }
}
