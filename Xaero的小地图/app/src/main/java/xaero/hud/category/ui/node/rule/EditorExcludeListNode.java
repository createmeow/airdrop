package xaero.hud.category.ui.node.rule;

import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import xaero.common.misc.ListFactory;
import xaero.hud.category.rule.ExcludeListMode;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.EditorNode;
import xaero.hud.category.ui.node.EditorSimpleDeletableWrapperNode;
import xaero.hud.category.ui.node.options.EditorOptionNode;
import xaero.hud.category.ui.node.options.list.EditorCompactListOptionsNode;
import xaero.hud.category.ui.node.options.text.EditorTextFieldOptionsNode;
import xaero.hud.category.ui.node.rule.EditorListNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/rule/EditorExcludeListNode.class */
public final class EditorExcludeListNode extends EditorListNode {
    private EditorCompactListOptionsNode<ExcludeListMode> excludeMode;

    @Override // xaero.hud.category.ui.node.rule.EditorListNode
    public /* bridge */ /* synthetic */ EditorSimpleDeletableWrapperNode.DeletionCallback getDeletionCallback() {
        return super.getDeletionCallback();
    }

    @Override // xaero.hud.category.ui.node.rule.EditorListNode
    public /* bridge */ /* synthetic */ List getList() {
        return super.getList();
    }

    private EditorExcludeListNode(@Nonnull List<EditorSimpleDeletableWrapperNode<String>> list, ListFactory listFactory, @Nonnull EditorCompactListOptionsNode<ExcludeListMode> excludeMode, @Nonnull EditorTextFieldOptionsNode topAdder, @Nonnull EditorTextFieldOptionsNode bottomAdder, boolean movable, @Nonnull EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier, @Nonnull EditorSimpleDeletableWrapperNode.DeletionCallback deletionCallback, @Nonnull IEditorDataTooltipSupplier helpTooltipSupplier) {
        super(list, listFactory, topAdder, bottomAdder, movable, listEntryFactory, tooltipSupplier, deletionCallback, helpTooltipSupplier);
        this.excludeMode = excludeMode;
    }

    public ExcludeListMode getExcludeMode() {
        return this.excludeMode.getCurrentValue().getValue();
    }

    @Override // xaero.hud.category.ui.node.EditorNode
    public Component getDisplayName() {
        return Component.translatable("gui.xaero_category_exclude_list");
    }

    @Override // xaero.hud.category.ui.node.rule.EditorListNode, xaero.hud.category.ui.node.EditorNode
    public List<EditorNode> getSubNodes() {
        List<EditorNode> result = super.getSubNodes();
        result.add(0, this.excludeMode);
        return result;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/category/ui/node/rule/EditorExcludeListNode$Builder.class */
    public static final class Builder<E, P> extends EditorListNode.Builder<E, P, EditorExcludeListNode, Builder<E, P>> {
        private final EditorCompactListOptionsNode.Builder<ExcludeListMode> excludeModeBuilder;

        private Builder(ListFactory listFactory) {
            super(listFactory);
            this.excludeModeBuilder = EditorCompactListOptionsNode.Builder.begin(listFactory);
        }

        @Override // xaero.hud.category.ui.node.rule.EditorListNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public Builder<E, P> setDefault() {
            this.excludeModeBuilder.setDefault().setDisplayName(Component.translatable("gui.xaero_category_exclude_list_mode"));
            for (ExcludeListMode mode : ExcludeListMode.values()) {
                this.excludeModeBuilder.addOptionBuilder(EditorOptionNode.Builder.begin().setValue(mode));
            }
            setExcludeMode(ExcludeListMode.ONLY);
            return (Builder) super.setDefault();
        }

        public Builder<E, P> setExcludeMode(ExcludeListMode excludeMode) {
            this.excludeModeBuilder.setCurrentValue(excludeMode);
            return this;
        }

        @Override // xaero.hud.category.ui.node.rule.EditorListNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorExcludeListNode build() {
            return (EditorExcludeListNode) super.build();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.node.rule.EditorListNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public EditorExcludeListNode buildInternally() {
            return new EditorExcludeListNode(buildList(), this.listFactory, this.excludeModeBuilder.build(), this.adderBuilder.build(), this.adderBuilder.build(), this.movable, this.listEntryFactory, this.tooltipSupplier, this.deletionCallback, this.helpTooltipSupplier);
        }

        public static <E, P> Builder<E, P> begin(ListFactory listFactory) {
            return new Builder(listFactory).setDefault();
        }
    }
}
