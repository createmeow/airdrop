package xaero.hud.minimap.radar.category.ui.node;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Nonnull;
import xaero.hud.category.ui.entry.EditorListRootEntryFactory;
import xaero.hud.category.ui.node.EditorAdderNode;
import xaero.hud.category.ui.node.EditorFilterCategoryNode;
import xaero.hud.category.ui.node.tooltip.IEditorDataTooltipSupplier;
import xaero.hud.minimap.radar.category.EntityRadarCategory;
import xaero.hud.minimap.radar.category.EntityRadarCategoryConstants;
import xaero.hud.minimap.radar.category.ui.node.EditorEntityRadarCategorySettingsNode;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/ui/node/EditorEntityRadarCategoryNode.class */
public final class EditorEntityRadarCategoryNode extends EditorFilterCategoryNode<EntityRadarCategory, EditorEntityRadarCategorySettingsNode<?>, EditorEntityRadarCategoryNode> {
    private EditorEntityRadarCategoryNode(@Nonnull EditorEntityRadarCategorySettingsNode<?> settingOverrides, @Nonnull List<EditorEntityRadarCategoryNode> subCategories, @Nonnull EditorAdderNode topAdder, @Nonnull Function<EditorAdderNode, EditorEntityRadarCategoryNode> newCategorySupplier, boolean movable, int subIndex, @Nonnull EditorListRootEntryFactory listEntryFactory, IEditorDataTooltipSupplier tooltipSupplier) {
        super(settingOverrides, subCategories, topAdder, newCategorySupplier, movable, subIndex, listEntryFactory, tooltipSupplier);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/ui/node/EditorEntityRadarCategoryNode$Builder.class */
    public static final class Builder extends EditorFilterCategoryNode.Builder<EntityRadarCategory, EditorEntityRadarCategoryNode, EditorEntityRadarCategorySettingsNode<?>, EditorEntityRadarCategorySettingsNode.Builder, Builder> {
        private Builder() {
            super(EntityRadarCategoryConstants.LIST_FACTORY, EditorEntityRadarCategorySettingsNode.Builder.begin());
        }

        @Override // xaero.hud.category.ui.node.EditorFilterCategoryNode.Builder, xaero.hud.category.ui.node.EditorCategoryNode.Builder, xaero.hud.category.ui.node.EditorNode.Builder
        public Builder setDefault() {
            super.setDefault();
            setNewCategorySupplier(ad -> {
                return ((Builder) begin().setName(ad.getNameField().getResult())).build();
            });
            return this;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.node.EditorNode.Builder
        public EditorEntityRadarCategoryNode buildInternally() {
            return new EditorEntityRadarCategoryNode(((EditorEntityRadarCategorySettingsNode.Builder) this.settingsDataBuilder).build(), buildSubCategories(), this.topAdderBuilder.build(), this.newCategorySupplier, this.movable, this.subIndex, this.listEntryFactory, this.tooltipSupplier);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
