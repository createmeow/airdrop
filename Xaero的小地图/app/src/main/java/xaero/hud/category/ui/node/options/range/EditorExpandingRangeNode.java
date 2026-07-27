package xaero.hud.category.ui.node.options.range;

import com.google.common.base.Objects;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import xaero.common.misc.ListFactory;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.options.EditorExpandingOptionsNode;
import xaero.hud.category.ui.node.options.EditorOptionNode;
import xaero.hud.category.ui.node.options.EditorOptionsNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/range/EditorExpandingRangeNode.class */
public class EditorExpandingRangeNode<V> extends EditorExpandingOptionsNode<Integer> {
    private V currentRangeValue;
    private final IntFunction<V> numberReader;

    protected EditorExpandingRangeNode(@Nonnull Component displayName, V currentRangeValue, @Nonnull IntFunction<V> numberReader, @Nonnull EditorOptionNode<Integer> currentValue, @Nonnull List<EditorOptionNode<Integer>> options, boolean movable, @Nonnull EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, EditorOptionsNode.IOptionsNodeIsActiveSupplier isActiveSupplier) {
        super(displayName, currentValue, options, movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
        this.currentRangeValue = currentRangeValue;
        this.numberReader = numberReader;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.hud.category.ui.node.options.EditorExpandingOptionsNode
    public boolean onSelected(EditorOptionNode<Integer> editorOptionNode) {
        V selectedValue = editorOptionNode.getValue() == null ? null : this.numberReader.apply(((Integer) editorOptionNode.getValue()).intValue());
        if (this.currentRangeValue != selectedValue && !Objects.equal(this.currentRangeValue, selectedValue)) {
            this.currentRangeValue = selectedValue;
        }
        return super.onSelected(editorOptionNode);
    }

    public V getCurrentRangeValue() {
        return this.currentRangeValue;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/range/EditorExpandingRangeNode$Builder.class */
    public static abstract class Builder<V, B extends Builder<V, B>> extends EditorExpandingOptionsNode.Builder<Integer, B> {
        protected V currentRangeValue;
        protected int minNumber;
        protected int maxNumber;
        protected IntFunction<V> numberReader;
        protected Function<V, Integer> numberWriter;
        protected Function<V, Component> valueNamer;
        protected boolean hasNullOption;

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.node.options.EditorExpandingOptionsNode.Builder
        /* renamed from: buildInternally */
        public abstract EditorOptionsNode<Integer> buildInternally2(EditorOptionNode<Integer> editorOptionNode, List<EditorOptionNode<Integer>> list);

        protected Builder(ListFactory listFactory) {
            super(listFactory);
        }

        @Override // xaero.hud.category.ui.node.options.EditorExpandingOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public B setDefault() {
            setCurrentRangeValue(null);
            setMinNumber(0);
            setMaxNumber(0);
            setNumberReader(null);
            setNumberWriter(null);
            setValueNamer(null);
            setHasNullOption(false);
            return (B) super.setDefault();
        }

        public B setCurrentRangeValue(V currentRangeValue) {
            this.currentRangeValue = currentRangeValue;
            return (B) this.self;
        }

        public B setMinNumber(int minNumber) {
            this.minNumber = minNumber;
            return (B) this.self;
        }

        public B setMaxNumber(int maxNumber) {
            this.maxNumber = maxNumber;
            return (B) this.self;
        }

        public B setNumberReader(IntFunction<V> numberReader) {
            this.numberReader = numberReader;
            return (B) this.self;
        }

        public B setNumberWriter(Function<V, Integer> numberWriter) {
            this.numberWriter = numberWriter;
            return (B) this.self;
        }

        public B setValueNamer(Function<V, Component> valueNamer) {
            this.valueNamer = valueNamer;
            return (B) this.self;
        }

        public B setHasNullOption(boolean hasNullOption) {
            this.hasNullOption = hasNullOption;
            return (B) this.self;
        }

        /* JADX WARN: Type inference failed for: r1v19, types: [java.lang.Integer, java.lang.Object] */
        @Override // xaero.hud.category.ui.node.options.EditorExpandingOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorExpandingRangeNode<V> build() {
            if (this.numberReader == null || this.valueNamer == null || this.numberWriter == null) {
                throw new IllegalStateException("required fields not set!");
            }
            this.optionBuilders.clear();
            if (this.currentRangeValue != null) {
                setCurrentValue(this.numberWriter.apply(this.currentRangeValue));
            }
            if (this.hasNullOption) {
                EditorOptionNode.Builder<V> builderBegin = EditorOptionNode.Builder.begin();
                builderBegin.setValue(null);
                builderBegin.setDisplayName(this.valueNamer.apply(null));
                addOptionBuilder(builderBegin);
            }
            for (int i = this.minNumber; i <= this.maxNumber; i++) {
                EditorOptionNode.Builder<V> builderBegin2 = EditorOptionNode.Builder.begin();
                builderBegin2.setValue(Integer.valueOf(i));
                builderBegin2.setDisplayName((Component) this.valueNamer.apply(this.numberReader.apply(i)));
                addOptionBuilder(builderBegin2);
            }
            return (EditorExpandingRangeNode) super.build();
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/range/EditorExpandingRangeNode$FinalBuilder.class */
    public static final class FinalBuilder<V> extends Builder<V, FinalBuilder<V>> {
        private FinalBuilder(ListFactory listFactory) {
            super(listFactory);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.node.options.range.EditorExpandingRangeNode.Builder, xaero.hud.category.ui.node.options.EditorExpandingOptionsNode.Builder
        /* renamed from: buildInternally */
        public EditorOptionsNode<Integer> buildInternally2(EditorOptionNode<Integer> currentValueData, List<EditorOptionNode<Integer>> options) {
            return new EditorExpandingRangeNode(this.displayName, this.currentRangeValue, this.numberReader, currentValueData, options, this.movable, this.listEntryFactory, this.tooltipSupplier, this.isActiveSupplier);
        }

        public static <V> FinalBuilder<V> begin(ListFactory listFactory) {
            return new FinalBuilder(listFactory).setDefault();
        }
    }
}
