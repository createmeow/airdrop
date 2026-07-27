package xaero.hud.category.ui.node.options;

import java.util.function.IntFunction;
import net.minecraft.network.chat.Component;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.options.EditorCompactOptionsNode;
import xaero.hud.category.ui.node.options.EditorOptionNode;
import xaero.hud.category.ui.node.options.EditorOptionsNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorCompactBooleanOptionsNode.class */
public class EditorCompactBooleanOptionsNode extends EditorCompactOptionsNode<Boolean> {
    private final EditorOptionNode<Boolean> trueOption;
    private final EditorOptionNode<Boolean> falseOption;
    private IntFunction<EditorOptionNode<Boolean>> indexReader;

    protected EditorCompactBooleanOptionsNode(Component displayName, int currentIndex, int optionCount, boolean movable, EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, EditorOptionsNode.IOptionsNodeIsActiveSupplier isActiveSupplier, EditorOptionNode<Boolean> trueOption, EditorOptionNode<Boolean> falseOption) {
        super(displayName, currentIndex, optionCount, movable, listEntryFactory, tooltipSupplier, isActiveSupplier);
        this.trueOption = trueOption;
        this.falseOption = falseOption;
        this.currentValue = (EditorOptionNode) getIndexReader().apply(currentIndex);
    }

    @Override // xaero.hud.category.ui.node.options.EditorCompactOptionsNode
    protected IntFunction<EditorOptionNode<Boolean>> getIndexReader() {
        if (this.indexReader == null) {
            this.indexReader = i -> {
                return i != 0 ? this.trueOption : this.falseOption;
            };
        }
        return this.indexReader;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorCompactBooleanOptionsNode$Builder.class */
    public static final class Builder extends EditorCompactOptionsNode.Builder<Boolean, Builder> {
        private final EditorOptionNode.Builder<Boolean> trueOptionBuilder = EditorOptionNode.Builder.begin();
        private final EditorOptionNode.Builder<Boolean> falseOptionBuilder = EditorOptionNode.Builder.begin();

        private Builder() {
        }

        @Override // xaero.hud.category.ui.node.options.EditorCompactOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public Builder setDefault() {
            super.setDefault();
            this.trueOptionBuilder.setDefault().setDisplayName(Component.translatable("gui.xaero_on")).setValue(true);
            this.falseOptionBuilder.setDefault().setDisplayName(Component.translatable("gui.xaero_off")).setValue(false);
            setCurrentValue(false);
            return (Builder) this.self;
        }

        public EditorOptionNode.Builder<Boolean> getTrueOptionBuilder() {
            return this.trueOptionBuilder;
        }

        public EditorOptionNode.Builder<Boolean> getFalseOptionBuilder() {
            return this.falseOptionBuilder;
        }

        @Override // xaero.hud.category.ui.node.options.EditorCompactOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorCompactBooleanOptionsNode build() {
            if (this.currentValue == 0) {
                throw new IllegalStateException();
            }
            if (this.movable) {
                throw new IllegalStateException("toggles can't be movable!");
            }
            return (EditorCompactBooleanOptionsNode) super.build();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // xaero.hud.category.ui.node.options.EditorCompactOptionsNode.Builder, xaero.hud.category.ui.node.options.EditorOptionsNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorCompactBooleanOptionsNode buildInternally() {
            EditorOptionNode<Boolean> trueOption = this.trueOptionBuilder.build();
            EditorOptionNode<Boolean> falseOption = this.falseOptionBuilder.build();
            return new EditorCompactBooleanOptionsNode(this.displayName, ((Boolean) this.currentValue).booleanValue() ? 1 : 0, 2, this.movable, this.listEntryFactory, this.tooltipSupplier, this.isActiveSupplier, trueOption, falseOption);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
