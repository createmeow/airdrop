package xaero.hud.category.ui.node;

import java.lang.Comparable;
import javax.annotation.Nonnull;
import xaero.hud.category.ui.GuiCategoryEditor;
import xaero.hud.category.ui.entry.ConnectionLineType;
import xaero.hud.category.ui.entry.EditorListEntryDeletableListElement;
import xaero.hud.category.ui.entry.EditorListRootEntry;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.EditorSimpleWrapperNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorSimpleDeletableWrapperNode.class */
public class EditorSimpleDeletableWrapperNode<S extends Comparable<S>> extends EditorSimpleWrapperNode<S> {
    private final DeletionCallback deletionCallback;

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorSimpleDeletableWrapperNode$DeletionCallback.class */
    public interface DeletionCallback {
        boolean delete(EditorNode editorNode, EditorSimpleDeletableWrapperNode<?> editorSimpleDeletableWrapperNode, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList settingRowList);
    }

    protected EditorSimpleDeletableWrapperNode(@Nonnull S element, boolean movable, @Nonnull EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, @Nonnull DeletionCallback deletionCallback) {
        super(element, movable, listEntryFactory, tooltipSupplier);
        this.deletionCallback = deletionCallback;
    }

    public DeletionCallback getDeletionCallback() {
        return this.deletionCallback;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/EditorSimpleDeletableWrapperNode$Builder.class */
    public static final class Builder<S extends Comparable<S>> extends EditorSimpleWrapperNode.Builder<S, Builder<S>> {
        private DeletionCallback deletionCallback;

        private Builder() {
        }

        @Override // xaero.hud.category.ui.node.EditorSimpleWrapperNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public Builder<S> setDefault() {
            super.setDefault();
            setDeletionCallback(null);
            return (Builder) this.self;
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        protected EditorListRootEntry mainEntryFactory(EditorNode data, EditorNode parent, int index, ConnectionLineType lineType, GuiCategoryEditor<?, ?, ?, ?, ?, ?>.SettingRowList rowList, int screenWidth, boolean isFinalExpanded) {
            EditorSimpleDeletableWrapperNode<?> sdwData = (EditorSimpleDeletableWrapperNode) data;
            return new EditorListEntryDeletableListElement(screenWidth, index, rowList, lineType, sdwData, parent, sdwData.getDeletionCallback(), data.getTooltipSupplier(parent), rowList.readOnly);
        }

        public Builder<S> setDeletionCallback(DeletionCallback deletionCallback) {
            this.deletionCallback = deletionCallback;
            return (Builder) this.self;
        }

        @Override // xaero.hud.category.ui.node.EditorSimpleWrapperNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorSimpleDeletableWrapperNode<S> build() {
            if (this.deletionCallback == null) {
                throw new IllegalStateException("required fields not set!");
            }
            return (EditorSimpleDeletableWrapperNode) super.build();
        }

        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        protected EditorNode buildInternally() {
            return new EditorSimpleDeletableWrapperNode(this.element, this.movable, this.listEntryFactory, this.tooltipSupplier, this.deletionCallback);
        }

        public static <S extends Comparable<S>> Builder<S> begin() {
            return new Builder().setDefault();
        }
    }
}
