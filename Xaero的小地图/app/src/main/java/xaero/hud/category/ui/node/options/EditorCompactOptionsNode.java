package xaero.hud.category.ui.node.options;

import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.entry.EditorListEntryWidget;
import xaero.hud.category.ui.entry.EditorListEntryWrapper;
import xaero.hud.category.ui.entry.EditorListRootEntry;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.entry.widget.EditorButtonIteration;
import xaero.hud.category.ui.entry.widget.EditorSlider;
import xaero.hud.category.ui.node.EditorNode;
import xaero.hud.category.ui.node.options.EditorOptionsNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorCompactOptionsNode.class */
public abstract class EditorCompactOptionsNode<V> extends EditorOptionsNode<V> {
    private IntConsumer updatedIndexConsumer;
    protected int currentIndex;
    protected final int optionCount;

    protected abstract IntFunction<EditorOptionNode<V>> getIndexReader();

    protected EditorCompactOptionsNode(Component displayName, int currentIndex, int optionCount, boolean movable, EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, EditorOptionsNode.IOptionsNodeIsActiveSupplier isActiveSupplier) {
        super(displayName, movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
        this.currentIndex = currentIndex;
        this.optionCount = optionCount;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public int getOptionCount() {
        return this.optionCount;
    }

    public final IntConsumer getUpdatedIndexConsumer() {
        if (this.updatedIndexConsumer == null) {
            this.updatedIndexConsumer = i -> {
                this.currentIndex = i;
                setCurrentValue(getIndexReader().apply(i));
            };
        }
        return this.updatedIndexConsumer;
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public List<EditorNode> getSubNodes() {
        return null;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorCompactOptionsNode$Builder.class */
    public static abstract class Builder<V, B extends Builder<V, B>> extends EditorOptionsNode.Builder<V, B> {
        private boolean slider;

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public abstract EditorCompactOptionsNode<V> buildInternally();

        protected Builder() {
        }

        @Override // xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public B setDefault() {
            super.setDefault();
            setSlider(false);
            return (B) this.self;
        }

        public B setSlider(boolean slider) {
            this.slider = slider;
            return (B) this.self;
        }

        @Override // xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorOptionsNode<V> build() {
            setListEntryFactory(getMainEntryFactory(this.slider));
            return super.build();
        }

        private EditorListRootEntryFactory getMainEntryFactory(boolean slider) {
            return (data, parent, index, lineType, rowList, screenWidth, isFinalExpanded) -> {
                return new EditorListEntryWrapper(getCenteredEntryFactory(slider, data, parent, index, rowList), screenWidth, index, rowList, lineType, data);
            };
        }

        protected EditorListRootEntry.CenteredEntryFactory getCenteredEntryFactory(boolean slider, EditorNode data, EditorNode parent, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
            AbstractWidget widget = getEntryWidget(slider, data, parent, index, rowList);
            if (rowList.readOnly) {
                widget.active = false;
            }
            return (x, y, width, height, root) -> {
                return new EditorListEntryWidget(x, y, width, height, index, rowList, root, widget, data.getTooltipSupplier(parent));
            };
        }

        protected AbstractWidget getEntryWidget(boolean slider, EditorNode data, EditorNode parent, int index, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList) {
            if (slider) {
                return new EditorSlider(((EditorCompactOptionsNode) data).getUpdatedIndexConsumer(), ((EditorCompactOptionsNode) data).getMessageSupplier(), ((EditorCompactOptionsNode) data).getCurrentIndex(), ((EditorCompactOptionsNode) data).getOptionCount(), 216, 20, rowList);
            }
            return new EditorButtonIteration(parent, ((EditorCompactOptionsNode) data).getUpdatedIndexConsumer(), ((EditorCompactOptionsNode) data).getMessageSupplier(), true, ((EditorCompactOptionsNode) data).getCurrentIndex(), ((EditorCompactOptionsNode) data).getOptionCount(), 216, 20, rowList);
        }
    }
}
