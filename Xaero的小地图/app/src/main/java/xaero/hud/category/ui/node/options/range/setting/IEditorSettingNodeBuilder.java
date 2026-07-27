package xaero.hud.category.ui.node.options.range.setting;

import xaero.hud.category.setting.ObjectCategorySetting;
import xaero.hud.category.ui.node.options.EditorOptionsNode;
import xaero.hud.category.ui.node.options.range.setting.IEditorSettingNode;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/range/setting/IEditorSettingNodeBuilder.class */
public interface IEditorSettingNodeBuilder<V, SD extends EditorOptionsNode<Integer> & IEditorSettingNode<V>> {
    IEditorSettingNodeBuilder<V, SD> setSetting(ObjectCategorySetting<V> objectCategorySetting);

    IEditorSettingNodeBuilder<V, SD> setSettingValue(V v);

    IEditorSettingNodeBuilder<V, SD> setRootSettings(boolean z);

    /* JADX WARN: Incorrect return type in method signature: ()TSD; */
    EditorOptionsNode build();
}
