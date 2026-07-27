package xaero.hud.category.ui.node.options.range.setting;

import java.util.List;
import java.util.function.IntFunction;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import xaero.common.misc.ListFactory;
import xaero.hud.category.setting.ObjectCategorySetting;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.options.EditorOptionNode;
import xaero.hud.category.ui.node.options.EditorOptionsNode;
import xaero.hud.category.ui.node.options.range.EditorExpandingRangeNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/range/setting/EditorExpandingSettingNode.class */
public final class EditorExpandingSettingNode<V> extends EditorExpandingRangeNode<V> implements IEditorSettingNode<V> {
    private final ObjectCategorySetting<V> setting;
    private final boolean rootSettings;

    private EditorExpandingSettingNode(ObjectCategorySetting<V> setting, Component displayName, V settingValue, boolean rootSettings, IntFunction<V> numberReader, EditorOptionNode<Integer> currentValue, List<EditorOptionNode<Integer>> options, boolean movable, @Nonnull EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, EditorOptionsNode.IOptionsNodeIsActiveSupplier isActiveSupplier) {
        super(displayName, settingValue, numberReader, currentValue, options, movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
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

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/range/setting/EditorExpandingSettingNode$Builder.class */
    public static final class Builder<V> extends EditorExpandingRangeNode.Builder<V, Builder<V>> implements IEditorSettingNodeBuilder<V, EditorExpandingSettingNode<V>> {
        private ObjectCategorySetting<V> setting;
        private boolean rootSettings;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // xaero.hud.category.ui.node.options.range.setting.IEditorSettingNodeBuilder
        public /* bridge */ /* synthetic */ IEditorSettingNodeBuilder setSettingValue(Object obj) {
            return setSettingValue((Builder<V>) obj);
        }

        private Builder(ListFactory listFactory) {
            super(listFactory);
        }

        @Override // xaero.hud.category.ui.node.options.range.EditorExpandingRangeNode.Builder, xaero.hud.category.ui.node.options.EditorExpandingOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
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
                return this;
            }
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

        @Override // xaero.hud.category.ui.node.options.range.EditorExpandingRangeNode.Builder, xaero.hud.category.ui.node.options.EditorExpandingOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorExpandingSettingNode<V> build() {
            if (this.setting == null) {
                throw new IllegalStateException("required fields not set!");
            }
            if (this.displayName == null) {
                setDisplayName(this.setting.getDisplayName());
            }
            this.optionBuilders.clear();
            return (EditorExpandingSettingNode) super.build();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // xaero.hud.category.ui.node.options.range.EditorExpandingRangeNode.Builder, xaero.hud.category.ui.node.options.EditorExpandingOptionsNode.Builder
        /* renamed from: buildInternally, reason: merged with bridge method [inline-methods] */
        public EditorOptionsNode<Integer> buildInternally2(EditorOptionNode<Integer> currentValueData, List<EditorOptionNode<Integer>> options) {
            return new EditorExpandingSettingNode(this.setting, this.displayName, this.currentRangeValue, this.rootSettings, this.numberReader, currentValueData, options, this.movable, this.listEntryFactory, this.tooltipSupplier, this.isActiveSupplier);
        }

        public static <V> Builder<V> begin(ListFactory listFactory) {
            return new Builder(listFactory).setDefault();
        }
    }
}
