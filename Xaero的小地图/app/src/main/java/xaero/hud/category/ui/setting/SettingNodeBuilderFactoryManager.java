package xaero.hud.category.ui.setting;

import java.util.Map;
import xaero.common.misc.ListFactory;
import xaero.common.misc.MapFactory;
import xaero.hud.category.setting.ObjectCategorySetting;
import xaero.hud.category.ui.node.options.range.setting.EditorCompactSettingNode;
import xaero.hud.category.ui.node.options.range.setting.EditorExpandingSettingNode;
import xaero.hud.category.ui.node.options.range.setting.IEditorSettingNodeBuilder;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/setting/SettingNodeBuilderFactoryManager.class */
public final class SettingNodeBuilderFactoryManager {
    private final Map<EditorSettingType, SettingNodeBuilderFactory> factoryMap;

    private SettingNodeBuilderFactoryManager(Map<EditorSettingType, SettingNodeBuilderFactory> factoryMap) {
        this.factoryMap = factoryMap;
    }

    public void register(EditorSettingType type, SettingNodeBuilderFactory factory) {
        if (this.factoryMap.containsKey(type)) {
            throw new IllegalArgumentException("Attempting to register duplicate editor setting type: " + String.valueOf(type));
        }
        this.factoryMap.put(type, factory);
    }

    public SettingNodeBuilderFactory get(ObjectCategorySetting<?> setting) {
        return this.factoryMap.get(setting.getSettingUIType());
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/setting/SettingNodeBuilderFactoryManager$Builder.class */
    public static final class Builder {
        private final MapFactory mapFactory;

        private Builder(MapFactory mapFactory) {
            this.mapFactory = mapFactory;
        }

        public Builder setDefault() {
            return this;
        }

        public SettingNodeBuilderFactoryManager build() {
            SettingNodeBuilderFactoryManager manager = new SettingNodeBuilderFactoryManager(this.mapFactory.get());
            manager.register(EditorSettingType.ITERATION_BUTTON, new SettingNodeBuilderFactory(this) { // from class: xaero.hud.category.ui.setting.SettingNodeBuilderFactoryManager.Builder.1
                @Override // xaero.hud.category.ui.setting.SettingNodeBuilderFactory
                public <V> IEditorSettingNodeBuilder<V, ?> apply(ListFactory listFactory) {
                    return EditorCompactSettingNode.Builder.begin();
                }
            });
            manager.register(EditorSettingType.SLIDER, new SettingNodeBuilderFactory(this) { // from class: xaero.hud.category.ui.setting.SettingNodeBuilderFactoryManager.Builder.2
                @Override // xaero.hud.category.ui.setting.SettingNodeBuilderFactory
                public <V> IEditorSettingNodeBuilder<V, ?> apply(ListFactory listFactory) {
                    return EditorCompactSettingNode.Builder.begin().setSlider(true);
                }
            });
            manager.register(EditorSettingType.EXPANDING, EditorExpandingSettingNode.Builder::begin);
            return manager;
        }

        public static Builder begin(MapFactory mapFactory) {
            return new Builder(mapFactory).setDefault();
        }
    }
}
