package xaero.hud.category.ui.node.options;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.EditorNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorOptionsNode.class */
public abstract class EditorOptionsNode<V> extends EditorNode {
    protected EditorOptionNode<V> currentValue;
    protected Supplier<Component> messageSupplier;
    protected final Component displayName;
    private final IOptionsNodeIsActiveSupplier isActiveSupplier;

    @FunctionalInterface
    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorOptionsNode$IOptionsNodeIsActiveSupplier.class */
    public interface IOptionsNodeIsActiveSupplier {
        boolean get(EditorNode editorNode, EditorOptionsNode<?> editorOptionsNode);
    }

    protected EditorOptionsNode(@Nonnull Component displayName, boolean movable, EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, IOptionsNodeIsActiveSupplier isActiveSupplier) {
        super(movable, listEntryFactory, tooltipSupplier);
        this.displayName = displayName;
        this.isActiveSupplier = isActiveSupplier;
    }

    public EditorOptionNode<V> getCurrentValue() {
        return this.currentValue;
    }

    public void setCurrentValue(EditorOptionNode<V> currentValue) {
        this.currentValue = currentValue;
    }

    public final Supplier<Component> getMessageSupplier() {
        if (this.messageSupplier == null) {
            this.messageSupplier = () -> {
                return isExpanded() ? this.displayName : CommonComponents.optionNameValue(this.displayName, this.currentValue.getDisplayName());
            };
        }
        return this.messageSupplier;
    }

    public IOptionsNodeIsActiveSupplier getIsActiveSupplier() {
        return this.isActiveSupplier;
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public Component getDisplayName() {
        return this.displayName;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/options/EditorOptionsNode$Builder.class */
    public static abstract class Builder<V, B extends Builder<V, B>> extends EditorNode.Builder<B> {
        protected B self = this;
        protected V currentValue;
        protected Component displayName;
        protected IOptionsNodeIsActiveSupplier isActiveSupplier;

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public abstract EditorOptionsNode<V> buildInternally();

        protected Builder() {
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public B setDefault() {
            super.setDefault();
            setCurrentValue(null);
            setDisplayName(null);
            setIsActiveSupplier((p, d) -> {
                return true;
            });
            return this.self;
        }

        public B setCurrentValue(V currentValue) {
            this.currentValue = currentValue;
            return this.self;
        }

        public B setDisplayName(Component displayName) {
            this.displayName = displayName;
            return this.self;
        }

        public B setIsActiveSupplier(IOptionsNodeIsActiveSupplier isActiveSupplier) {
            this.isActiveSupplier = isActiveSupplier;
            return this.self;
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public EditorOptionsNode<V> build() {
            if (this.displayName == null) {
                throw new IllegalStateException("required fields not set!");
            }
            EditorOptionsNode<V> result = (EditorOptionsNode) super.build();
            return result;
        }
    }
}
