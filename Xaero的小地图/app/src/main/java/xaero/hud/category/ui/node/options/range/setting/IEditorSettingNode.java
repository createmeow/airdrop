package xaero.hud.category.ui.node.options.range.setting;

import xaero.hud.category.setting.ObjectCategorySetting;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/range/setting/IEditorSettingNode.class */
public interface IEditorSettingNode<V> {
    ObjectCategorySetting<V> getSetting();

    V getSettingValue();

    boolean isRootSettings();
}
