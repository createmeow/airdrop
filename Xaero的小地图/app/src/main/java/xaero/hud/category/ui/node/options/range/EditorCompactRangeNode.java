package xaero.hud.category.ui.node.options.range;

import java.util.function.Function;
import java.util.function.IntFunction;
import net.minecraft.network.chat.Component;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.options.EditorCompactOptionsNode;
import xaero.hud.category.ui.node.options.EditorOptionNode;
import xaero.hud.category.ui.node.options.EditorOptionsNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/range/EditorCompactRangeNode.class */
public class EditorCompactRangeNode<V> extends EditorCompactOptionsNode<Integer> {
    private V currentRangeValue;
    private final int minNumber;
    private final IntFunction<V> numberReader;
    private final Function<V, Component> valueNamer;
    private IntFunction<EditorOptionNode<Integer>> zeroIndexReader;
    private final boolean hasNullOption;

    protected EditorCompactRangeNode(Component displayName, V currentRangeValue, int currentIndex, int optionCount, int minNumber, boolean hasNullOption, IntFunction<V> numberReader, Function<V, Component> valueNamer, boolean movable, EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, EditorOptionsNode.IOptionsNodeIsActiveSupplier isActiveSupplier) {
        super(displayName, currentIndex, optionCount, movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
        this.numberReader = numberReader;
        this.valueNamer = valueNamer;
        this.currentRangeValue = currentRangeValue;
        this.hasNullOption = hasNullOption;
        this.minNumber = minNumber;
        this.currentValue = (EditorOptionNode) getIndexReader().apply(currentIndex);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.hud.category.ui.node.options.EditorOptionsNode
    public void setCurrentValue(EditorOptionNode<Integer> editorOptionNode) {
        super.setCurrentValue(editorOptionNode);
        Integer currentInteger = (Integer) editorOptionNode.getValue();
        this.currentRangeValue = currentInteger == null ? null : this.numberReader.apply(currentInteger.intValue());
    }

    public V getCurrentRangeValue() {
        return this.currentRangeValue;
    }

    @Override // xaero.hud.category.ui.node.options.EditorCompactOptionsNode
    protected IntFunction<EditorOptionNode<Integer>> getIndexReader() {
        if (this.zeroIndexReader == null) {
            this.zeroIndexReader = i -> {
                if (this.hasNullOption) {
                    i--;
                }
                Integer actualOptionNumber = i < 0 ? null : Integer.valueOf(this.minNumber + i);
                V correspondingSettingValue = actualOptionNumber == null ? null : this.numberReader.apply(actualOptionNumber.intValue());
                return EditorOptionNode.Builder.begin().setDisplayName(this.valueNamer.apply(correspondingSettingValue)).setValue(actualOptionNumber).build();
            };
        }
        return this.zeroIndexReader;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/range/EditorCompactRangeNode$Builder.class */
    public static abstract class Builder<V, B extends Builder<V, B>> extends EditorCompactOptionsNode.Builder<Integer, B> {
        protected V currentRangeValue;
        protected int minNumber;
        protected int maxNumber;
        protected IntFunction<V> numberReader;
        protected Function<V, Integer> numberWriter;
        protected Function<V, Component> valueNamer;
        protected boolean hasNullOption;

        protected abstract EditorCompactRangeNode<V> buildInternally(int i, int i2, EditorListRootEntryFactory editorListRootEntryFactory);

        @Override // xaero.hud.category.ui.node.options.EditorCompactOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
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

        @Override // xaero.hud.category.ui.node.options.EditorCompactOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorCompactRangeNode<V> build() {
            if (this.numberReader == null || this.valueNamer == null || this.numberWriter == null) {
                throw new IllegalStateException("required fields not set!");
            }
            return (EditorCompactRangeNode) super.build();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.node.options.EditorCompactOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorCompactRangeNode<V> buildInternally() {
            int currentIndex = this.currentRangeValue == null ? -1 : this.numberWriter.apply(this.currentRangeValue).intValue() - this.minNumber;
            if (this.currentRangeValue != null && currentIndex < 0) {
                currentIndex = 0;
            }
            int optionCount = (this.maxNumber - this.minNumber) + 1;
            if (this.hasNullOption) {
                optionCount++;
                currentIndex++;
            }
            return buildInternally(currentIndex, optionCount, this.listEntryFactory);
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/range/EditorCompactRangeNode$FinalBuilder.class */
    public static final class FinalBuilder<V> extends Builder<V, FinalBuilder<V>> {
        private FinalBuilder() {
        }

        @Override // xaero.hud.category.ui.node.options.range.EditorCompactRangeNode.Builder
        protected EditorCompactRangeNode<V> buildInternally(int currentIndex, int optionCount, EditorListRootEntryFactory listEntryFactory) {
            return new EditorCompactRangeNode<>(this.displayName, this.currentRangeValue, currentIndex, optionCount, this.minNumber, this.hasNullOption, this.numberReader, this.valueNamer, this.movable, listEntryFactory, this.tooltipSupplier, this.isActiveSupplier);
        }

        public static <V> FinalBuilder<V> begin() {
            return new FinalBuilder().setDefault();
        }
    }
}
