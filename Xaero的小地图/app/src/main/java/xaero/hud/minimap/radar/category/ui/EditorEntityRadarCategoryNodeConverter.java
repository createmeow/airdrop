package xaero.hud.minimap.radar.category.ui;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import xaero.hud.category.rule.ObjectCategoryListRuleType;
import xaero.hud.category.ui.EditorFilterCategoryNodeConverter;
import xaero.hud.minimap.radar.category.EntityRadarCategory;
import xaero.hud.minimap.radar.category.rule.EntityRadarListRuleTypes;
import xaero.hud.minimap.radar.category.ui.node.EditorEntityRadarCategoryNode;
import xaero.hud.minimap.radar.category.ui.node.EditorEntityRadarCategorySettingsNode;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/ui/EditorEntityRadarCategoryNodeConverter.class */
public final class EditorEntityRadarCategoryNodeConverter extends EditorFilterCategoryNodeConverter<Entity, Player, EntityRadarCategory, EditorEntityRadarCategoryNode, EntityRadarCategory.Builder, EditorEntityRadarCategorySettingsNode<?>, EditorEntityRadarCategorySettingsNode.Builder, EditorEntityRadarCategoryNode.Builder> {
    private EditorEntityRadarCategoryNodeConverter(@Nonnull Supplier<EntityRadarCategory.Builder> categoryBuilderFactory, @Nonnull Supplier<EditorEntityRadarCategoryNode.Builder> editorDataBuilderFactory, ObjectCategoryListRuleType<Entity, Player, ?> defaultListRuleType, Function<String, ObjectCategoryListRuleType<Entity, Player, ?>> listRuleTypeGetter, String listRuleTypePrefixSeparator, Predicate<String> inputRuleTypeStringValidator) {
        super(categoryBuilderFactory, editorDataBuilderFactory, defaultListRuleType, listRuleTypeGetter, listRuleTypePrefixSeparator, inputRuleTypeStringValidator);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/ui/EditorEntityRadarCategoryNodeConverter$Builder.class */
    public static final class Builder extends EditorFilterCategoryNodeConverter.Builder<Entity, Player, EntityRadarCategory, EditorEntityRadarCategoryNode, EntityRadarCategory.Builder, EditorEntityRadarCategorySettingsNode<?>, EditorEntityRadarCategorySettingsNode.Builder, EditorEntityRadarCategoryNode.Builder, Builder> {
        private Builder() {
            super(EntityRadarCategory.Builder::begin, EditorEntityRadarCategoryNode.Builder::begin);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.EditorFilterCategoryNodeConverter.Builder, xaero.hud.category.ui.EditorCategoryNodeConverter.Builder
        public Builder setDefault() {
            super.setDefault();
            setDefaultListRuleType(EntityRadarListRuleTypes.ENTITY_TYPE);
            Map<String, ObjectCategoryListRuleType<Entity, Player, ?>> map = EntityRadarListRuleTypes.TYPE_MAP;
            Objects.requireNonNull(map);
            setListRuleTypeGetter((v1) -> {
                return r1.get(v1);
            });
            return this;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.ui.EditorFilterCategoryNodeConverter.Builder, xaero.hud.category.ui.EditorCategoryNodeConverter.Builder
        public EditorEntityRadarCategoryNodeConverter buildInternally() {
            return new EditorEntityRadarCategoryNodeConverter(this.categoryBuilderFactory, this.editorDataBuilderFactory, this.defaultListRuleType, this.listRuleTypeGetter, this.listRuleTypePrefixSeparator, this.inputRuleTypeStringValidator);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
