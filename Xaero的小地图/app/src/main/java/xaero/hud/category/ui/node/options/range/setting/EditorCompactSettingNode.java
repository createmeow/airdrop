package xaero.hud.category.ui.node.options.range.setting;

import java.util.function.Function;
import java.util.function.IntFunction;
import net.minecraft.network.chat.Component;
import xaero.hud.category.setting.ObjectCategorySetting;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.options.EditorOptionsNode;
import xaero.hud.category.ui.node.options.range.EditorCompactRangeNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;
import xaero.hud.category.util.CategoryConstants;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/range/setting/EditorCompactSettingNode.class */
public final class EditorCompactSettingNode<V> extends EditorCompactRangeNode<V> implements IEditorSettingNode<V> {
    private final ObjectCategorySetting<V> setting;
    private final boolean rootSettings;

    private EditorCompactSettingNode(ObjectCategorySetting<V> setting, Component displayName, V settingValue, boolean rootSettings, boolean hasNullOption, int currentIndex, int optionCount, int minNumber, IntFunction<V> numberReader, Function<V, Component> valueNamer, boolean movable, EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, EditorOptionsNode.IOptionsNodeIsActiveSupplier isActiveSupplier) {
        super(displayName, settingValue, currentIndex, optionCount, minNumber, hasNullOption, numberReader, valueNamer, movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
        this.setting = setting;
        this.rootSettings = rootSettings;
    }

    @Override // xaero.hud.category.ui.node.options.range.setting.IEditorSettingNode
    public ObjectCategorySetting<V> getSetting() {
        return this.setting;
    }

    @Override // xaero.hud.category.ui.node.options.range.setting.IEditorSettingNode
    public V getSettingValue() {
        return getCurrentRangeValue();
    }

    @Override // xaero.hud.category.ui.node.options.range.setting.IEditorSettingNode
    public boolean isRootSettings() {
        return this.rootSettings;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T> Component getValueName(ObjectCategorySetting<T> setting, Object obj) {
        if (obj == 0) {
            return CategoryConstants.INHERIT;
        }
        return setting.getWidgetValueNameProvider().apply(obj);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/range/setting/EditorCompactSettingNode$Builder.class */
    public static final class Builder<V> extends EditorCompactRangeNode.Builder<V, Builder<V>> implements IEditorSettingNodeBuilder<V, EditorCompactSettingNode<V>> {
        private ObjectCategorySetting<V> setting;
        private boolean rootSettings;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // xaero.hud.category.ui.node.options.range.setting.IEditorSettingNodeBuilder
        public /* bridge */ /* synthetic */ IEditorSettingNodeBuilder setSettingValue(Object obj) {
            return setSettingValue((Builder<V>) obj);
        }

        private Builder() {
        }

        @Override // xaero.hud.category.ui.node.options.range.EditorCompactRangeNode.Builder, xaero.hud.category.ui.node.options.EditorCompactOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public Builder<V> setDefault() {
            setSetting((ObjectCategorySetting) null);
            setRootSettings(false);
            return (Builder) super.setDefault();
        }

        @Override // xaero.hud.category.ui.node.options.range.setting.IEditorSettingNodeBuilder
        public Builder<V> setSetting(ObjectCategorySetting<V> setting) {
            this.setting = setting;
            if (setting == null) {
                setValueNamer(null);
                setNumberReader(null);
                setNumberWriter(null);
                setMinNumber(0);
                setMaxNumber(0);
                setTooltipSupplier(null);
            } else {
                setValueNamer(v -> {
                    return EditorCompactSettingNode.getValueName(setting, v);
                });
                setNumberReader(setting.getIndexReader());
                setNumberWriter(setting.getIndexWriter());
                setMinNumber(setting.getUiFirstOption());
                setMaxNumber(setting.getUiLastOption());
                setTooltipInfoSupplier((parent, data) -> {
                    return setting.getTooltip();
                });
            }
            return this;
        }

        @Override // xaero.hud.category.ui.node.options.range.setting.IEditorSettingNodeBuilder
        public Builder<V> setSettingValue(V settingValue) {
            setCurrentRangeValue(settingValue);
            return this;
        }

        @Override // xaero.hud.category.ui.node.options.range.setting.IEditorSettingNodeBuilder
        public Builder<V> setRootSettings(boolean rootSettings) {
            this.rootSettings = rootSettings;
            setHasNullOption(!rootSettings);
            return this;
        }

        @Override // xaero.hud.category.ui.node.options.EditorCompactOptionsNode.Builder
        public Builder<V> setSlider(boolean slider) {
            return (Builder) super.setSlider(slider);
        }

        @Override // xaero.hud.category.ui.node.options.range.EditorCompactRangeNode.Builder, xaero.hud.category.ui.node.options.EditorCompactOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorCompactSettingNode<V> build() {
            if (this.setting == null) {
                throw new IllegalStateException("required fields not set!");
            }
            if (this.displayName == null) {
                setDisplayName(this.setting.getDisplayName());
            }
            return (EditorCompactSettingNode) super.build();
        }

        public static <V> Builder<V> begin() {
            return new Builder().setDefault();
        }

        @Override // xaero.hud.category.ui.node.options.range.EditorCompactRangeNode.Builder
        protected EditorCompactRangeNode<V> buildInternally(int currentIndex, int optionCount, EditorListRootEntryFactory listEntryFactory) {
            return new EditorCompactSettingNode(this.setting, this.displayName, this.currentRangeValue, this.rootSettings, this.hasNullOption, currentIndex, optionCount, this.minNumber, this.numberReader, this.valueNamer, this.movable, listEntryFactory, this.tooltipSupplier, this.isActiveSupplier);
        }
    }
}
