package xaero.hud.category.ui.node.rule;

import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import xaero.common.misc.ListFactory;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.EditorNode;
import xaero.hud.category.ui.node.EditorSimpleDeletableWrapperNode;
import xaero.hud.category.ui.node.options.EditorCompactBooleanOptionsNode;
import xaero.hud.category.ui.node.options.text.EditorTextFieldOptionsNode;
import xaero.hud.category.ui.node.rule.EditorListNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/rule/EditorIncludeListNode.class */
public final class EditorIncludeListNode extends EditorListNode {
    private final EditorCompactBooleanOptionsNode includeInSuperToggleData;

    @Override // xaero.hud.category.ui.node.rule.EditorListNode
    public /* bridge */ /* synthetic */ EditorSimpleDeletableWrapperNode.DeletionCallback getDeletionCallback() {
        return super.getDeletionCallback();
    }

    @Override // xaero.hud.category.ui.node.rule.EditorListNode
    public /* bridge */ /* synthetic */ List getList() {
        return super.getList();
    }

    private EditorIncludeListNode(@Nonnull List<EditorSimpleDeletableWrapperNode<String>> list, ListFactory listFactory, @Nonnull EditorTextFieldOptionsNode topAdder, @Nonnull EditorTextFieldOptionsNode bottomAdder, boolean movable, @Nonnull EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, @Nonnull EditorSimpleDeletableWrapperNode.DeletionCallback deletionCallback, @Nonnull IEditorDataTooltipSupplier helpTooltipSupplier, EditorCompactBooleanOptionsNode includeInSuperToggleData) {
        super(list, listFactory, topAdder, bottomAdder, movable, listEntryFactory, tooltipSupplier, deletionCallback, helpTooltipSupplier);
        this.includeInSuperToggleData = includeInSuperToggleData;
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public Component getDisplayName() {
        return Component.translatable("gui.xaero_category_include_list");
    }

    @Override // xaero.hud.category.ui.node.rule.EditorListNode, xaero.hud.category.ui.node.EditorNode
    public List<EditorNode> getSubNodes() {
        List<EditorNode> result = super.getSubNodes();
        result.add(0, this.includeInSuperToggleData);
        return result;
    }

    public boolean getIncludeInSuper() {
        return this.includeInSuperToggleData.getCurrentValue().getValue().booleanValue();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/rule/EditorIncludeListNode$Builder.class */
    public static final class Builder<E, P> extends EditorListNode.Builder<E, P, EditorIncludeListNode, Builder<E, P>> {
        private final EditorCompactBooleanOptionsNode.Builder includeInSuperToggleDataBuilder;

        private Builder(ListFactory listFactory) {
            super(listFactory);
            this.includeInSuperToggleDataBuilder = EditorCompactBooleanOptionsNode.Builder.begin();
        }

        @Override // xaero.hud.category.ui.node.rule.EditorListNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public Builder<E, P> setDefault() {
            super.setDefault();
            this.includeInSuperToggleDataBuilder.setDefault().setDisplayName(Component.translatable("gui.xaero_category_include_list_include_in_super"));
            return this;
        }

        public EditorCompactBooleanOptionsNode.Builder getIncludeInSuperToggleDataBuilder() {
            return this.includeInSuperToggleDataBuilder;
        }

        @Override // xaero.hud.category.ui.node.rule.EditorListNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorIncludeListNode build() {
            return (EditorIncludeListNode) super.build();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.node.rule.EditorListNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorIncludeListNode buildInternally() {
            return new EditorIncludeListNode(buildList(), this.listFactory, this.adderBuilder.build(), this.adderBuilder.build(), this.movable, this.listEntryFactory, this.tooltipSupplier, this.deletionCallback, this.helpTooltipSupplier, this.includeInSuperToggleDataBuilder.build());
        }

        public static <E, P> Builder<E, P> begin(ListFactory listFactory) {
            return new Builder(listFactory).setDefault();
        }
    }
}
