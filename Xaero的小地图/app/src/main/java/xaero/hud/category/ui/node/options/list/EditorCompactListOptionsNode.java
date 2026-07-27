package xaero.hud.category.ui.node.options.list;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import xaero.common.misc.ListFactory;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.options.EditorCompactOptionsNode;
import xaero.hud.category.ui.node.options.EditorOptionNode;
import xaero.hud.category.ui.node.options.EditorOptionsNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/list/EditorCompactListOptionsNode.class */
public final class EditorCompactListOptionsNode<V> extends EditorCompactOptionsNode<V> {
    private IntFunction<EditorOptionNode<V>> indexReader;
    private List<EditorOptionNode<V>> options;

    protected EditorCompactListOptionsNode(Component displayName, @Nonnull EditorOptionNode<V> currentValue, @Nonnull List<EditorOptionNode<V>> options, boolean movable, EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, EditorOptionsNode.IOptionsNodeIsActiveSupplier isActiveSupplier) {
        super(displayName, options.indexOf(currentValue), options.size(), movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
        this.currentValue = currentValue;
        this.options = options;
    }

    @Override // xaero.hud.category.ui.node.options.EditorCompactOptionsNode
    protected IntFunction<EditorOptionNode<V>> getIndexReader() {
        if (this.indexReader == null) {
            List<EditorOptionNode<V>> list = this.options;
            Objects.requireNonNull(list);
            this.indexReader = list::get;
        }
        return this.indexReader;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/list/EditorCompactListOptionsNode$Builder.class */
    public static final class Builder<V> extends EditorCompactOptionsNode.Builder<V, Builder<V>> {
        protected final List<EditorOptionNode.Builder<V>> optionBuilders;
        protected final ListFactory listFactory;

        private Builder(ListFactory listFactory) {
            this.optionBuilders = listFactory.get();
            this.listFactory = listFactory;
        }

        @Override // xaero.hud.category.ui.node.options.EditorCompactOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public Builder<V> setDefault() {
            this.optionBuilders.clear();
            return (Builder) super.setDefault();
        }

        public Builder<V> addOptionBuilder(EditorOptionNode.Builder<V> optionBuilder) {
            this.optionBuilders.add(optionBuilder);
            return this;
        }

        @Override // xaero.hud.category.ui.node.options.EditorCompactOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorCompactListOptionsNode<V> build() {
            if (this.listFactory == null) {
                throw new IllegalStateException("required fields not set!");
            }
            return (EditorCompactListOptionsNode) super.build();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.node.options.EditorCompactOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorCompactListOptionsNode<V> buildInternally() {
            Stream<R> map = this.optionBuilders.stream().map((v0) -> {
                return v0.build();
            });
            ListFactory listFactory = this.listFactory;
            Objects.requireNonNull(listFactory);
            List<EditorOptionNode<V>> options = (List) map.collect(listFactory::get, (v0, v1) -> {
                v0.add(v1);
            }, (v0, v1) -> {
                v0.addAll(v1);
            });
            EditorOptionNode<V> currentValueNode = null;
            Iterator<EditorOptionNode<V>> it = options.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                EditorOptionNode<V> optionData = it.next();
                if (optionData.getValue() == this.currentValue) {
                    currentValueNode = optionData;
                    break;
                }
            }
            if (currentValueNode == null) {
                throw new IllegalStateException("current value is not one of the options! " + String.valueOf(this.currentValue));
            }
            return new EditorCompactListOptionsNode<>(this.displayName, currentValueNode, options, this.movable, this.listEntryFactory, this.tooltipSupplier, this.isActiveSupplier);
        }

        public static <V> Builder<V> begin(ListFactory listFactory) {
            return new Builder(listFactory).setDefault();
        }
    }
}
