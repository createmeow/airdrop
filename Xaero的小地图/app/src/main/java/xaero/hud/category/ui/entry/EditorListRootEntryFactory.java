package xaero.hud.category.ui.entry;

import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.node.EditorNode;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/entry/EditorListRootEntryFactory.class */
public interface EditorListRootEntryFactory {
    EditorListRootEntry get(EditorNode editorNode, EditorNode editorNode2, int i, ConnectionLineType connectionLineType, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList settingRowList, int i2, boolean z);
}
