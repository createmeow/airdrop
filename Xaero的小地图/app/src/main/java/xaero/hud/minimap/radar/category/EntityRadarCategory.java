package xaero.hud.minimap.radar.category;

import java.util.List;
import java.util.Map;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import xaero.hud.category.FilterObjectCategory;
import xaero.hud.category.rule.ExcludeListMode;
import xaero.hud.category.rule.ObjectCategoryExcludeList;
import xaero.hud.category.rule.ObjectCategoryIncludeList;
import xaero.hud.category.rule.ObjectCategoryListRuleType;
import xaero.hud.category.rule.ObjectCategoryRule;
import xaero.hud.category.setting.ObjectCategorySetting;
import xaero.hud.minimap.radar.category.rule.EntityRadarCategoryHardRules;
import xaero.hud.minimap.radar.category.rule.EntityRadarListRuleTypes;
import xaero.hud.minimap.radar.category.serialization.data.EntityRadarCategoryData;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/EntityRadarCategory.class */
public final class EntityRadarCategory extends FilterObjectCategory<Entity, Player, EntityRadarCategoryData, EntityRadarCategory> {
    private EntityRadarCategory(String name, EntityRadarCategory parent, ObjectCategoryRule<Entity, Player> baseRule, Map<ObjectCategoryListRuleType<Entity, Player, ?>, ObjectCategoryIncludeList<Entity, Player, ?>> includeLists, Map<ObjectCategoryListRuleType<Entity, Player, ?>, ObjectCategoryExcludeList<Entity, Player, ?>> excludeLists, List<ObjectCategoryIncludeList<Entity, Player, ?>> includeListsIndexed, List<ObjectCategoryExcludeList<Entity, Player, ?>> excludeListsIndexed, Map<ObjectCategorySetting<?>, Object> settingOverrides, List<EntityRadarCategory> subCategories, boolean protection, ExcludeListMode excludeMode, boolean includeInSuperCategory) {
        super(name, parent, baseRule, includeLists, excludeLists, includeListsIndexed, excludeListsIndexed, settingOverrides, subCategories, protection, excludeMode, includeInSuperCategory);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/EntityRadarCategory$Builder.class */
    public static final class Builder extends FilterObjectCategory.Builder<Entity, Player, EntityRadarCategory, Builder> {
        @Override // xaero.hud.category.FilterObjectCategory.Builder
        protected /* bridge */ /* synthetic */ FilterObjectCategory buildUncheckedFilter(List list, Map<ObjectCategoryListRuleType<Entity, Player, ?>, ObjectCategoryIncludeList<Entity, Player, ?>> map, Map<ObjectCategoryListRuleType<Entity, Player, ?>, ObjectCategoryExcludeList<Entity, Player, ?>> map2, List<ObjectCategoryIncludeList<Entity, Player, ?>> list2, List<ObjectCategoryExcludeList<Entity, Player, ?>> list3) {
            return buildUncheckedFilter((List<EntityRadarCategory>) list, map, map2, list2, list3);
        }

        private Builder() {
            super(EntityRadarCategoryConstants.LIST_FACTORY, EntityRadarCategoryConstants.MAP_FACTORY, EntityRadarListRuleTypes.TYPE_LIST);
        }

        @Override // xaero.hud.category.FilterObjectCategory.Builder, xaero.hud.category.ObjectCategory.Builder
        public Builder setDefault() {
            super.setDefault();
            setBaseRule(EntityRadarCategoryHardRules.IS_NOTHING);
            return this;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // xaero.hud.category.FilterObjectCategory.Builder
        protected EntityRadarCategory buildUncheckedFilter(List<EntityRadarCategory> subCategories, Map<ObjectCategoryListRuleType<Entity, Player, ?>, ObjectCategoryIncludeList<Entity, Player, ?>> includeLists, Map<ObjectCategoryListRuleType<Entity, Player, ?>, ObjectCategoryExcludeList<Entity, Player, ?>> excludeLists, List<ObjectCategoryIncludeList<Entity, Player, ?>> includeListsIndexed, List<ObjectCategoryExcludeList<Entity, Player, ?>> excludeListsIndexed) {
            return new EntityRadarCategory(this.name, (EntityRadarCategory) this.superCategory, this.baseRule, includeLists, excludeLists, includeListsIndexed, excludeListsIndexed, this.settingOverrides, subCategories, this.protection, this.excludeMode, this.includeInSuperCategory);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
