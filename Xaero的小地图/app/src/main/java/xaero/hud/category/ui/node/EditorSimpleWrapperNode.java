package xaero.hud.category.ui.node;

import com.google.common.base.Objects;
import java.lang.Comparable;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.EditorNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorSimpleWrapperNode.class */
public class EditorSimpleWrapperNode<S extends Comparable<S>> extends EditorNode implements Comparable<EditorSimpleWrapperNode<S>> {
    private S element;

    protected EditorSimpleWrapperNode(@Nonnull S element, boolean movable, EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier) {
        super(movable, listEntryFactory, tooltipSupplier);
        this.element = element;
    }

    public S getElement() {
        return this.element;
    }

    public void setElement(S element) {
        this.element = element;
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public List<EditorNode> getSubNodes() {
        return null;
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public Component getDisplayName() {
        return Component.literal(this.element.toString());
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof EditorSimpleWrapperNode)) {
            EditorSimpleWrapperNode<?> otherWrapper = (EditorSimpleWrapperNode) obj;
            return Objects.equal(this.element, otherWrapper.element);
        }
        return false;
    }

    @Override // java.lang.Comparable
    public int compareTo(EditorSimpleWrapperNode<S> o) {
        if (this.element == o.element) {
            return 0;
        }
        if (this.element == null) {
            return -1;
        }
        if (o.element == null) {
            return 1;
        }
        return this.element.compareTo(o.element);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorSimpleWrapperNode$Builder.class */
    public static abstract class Builder<S extends Comparable<S>, B extends Builder<S, B>> extends EditorNode.Builder<B> {
        protected S element;

        protected Builder() {
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public B setDefault() {
            super.setDefault();
            setElement(null);
            return (B) this.self;
        }

        public B setElement(S element) {
            this.element = element;
            return (B) this.self;
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public EditorSimpleWrapperNode<S> build() {
            if (this.element == null) {
                throw new IllegalStateException("required fields not set!");
            }
            EditorSimpleWrapperNode<S> result = (EditorSimpleWrapperNode) super.build();
            return result;
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorSimpleWrapperNode$FinalBuilder.class */
    public static final class FinalBuilder<S extends Comparable<S>> extends Builder<S, FinalBuilder<S>> {
        public static <S extends Comparable<S>> FinalBuilder<S> begin() {
            return (FinalBuilder) new FinalBuilder().setDefault();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public EditorSimpleWrapperNode<S> buildInternally() {
            return new EditorSimpleWrapperNode<>(this.element, this.movable, this.listEntryFactory, this.tooltipSupplier);
        }
    }
}
