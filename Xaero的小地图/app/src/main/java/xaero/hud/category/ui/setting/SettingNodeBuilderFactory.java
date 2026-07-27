package xaero.hud.category.ui.setting;

import xaero.common.misc.ListFactory;
import xaero.hud.category.ui.node.options.range.setting.IEditorSettingNodeBuilder;

@FunctionalInterface
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/setting/SettingNodeBuilderFactory.class */
public interface SettingNodeBuilderFactory {
    <V> IEditorSettingNodeBuilder<V, ?> apply(ListFactory listFactory);
}
