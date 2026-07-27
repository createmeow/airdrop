package xaero.hud.category.ui.node.tooltip;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import xaero.hud.category.ui.node.EditorNode;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/tooltip/IEditorDataTooltipSupplier.class */
public interface IEditorDataTooltipSupplier extends BiFunction<EditorNode, EditorNode, Supplier<Tooltip>> {
}
