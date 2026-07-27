package xaero.hud.minimap.radar.category.serialization.data;

import java.util.List;
import java.util.Map;
import xaero.hud.category.rule.ExcludeListMode;
import xaero.hud.category.serialization.data.FilterObjectCategoryData;
import xaero.hud.category.serialization.data.ObjectCategoryData;
import xaero.hud.minimap.radar.category.EntityRadarCategoryConstants;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/serialization/data/EntityRadarCategoryData.class */
public final class EntityRadarCategoryData extends FilterObjectCategoryData<EntityRadarCategoryData> {
    private EntityRadarCategoryData(String name, String hardInclude, List<String> includeList, List<String> excludeList, ExcludeListMode excludeMode, Map<String, Object> settingOverrides, List<EntityRadarCategoryData> subCategories, boolean protection, boolean includeInSuperCategory) {
        super(name, hardInclude, includeList, excludeList, excludeMode, settingOverrides, subCategories, protection, includeInSuperCategory);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/serialization/data/EntityRadarCategoryData$Builder.class */
    public static final class Builder extends FilterObjectCategoryData.Builder<EntityRadarCategoryData, Builder> {
        @Override // xaero.hud.category.serialization.data.ObjectCategoryData.Builder
        protected /* bridge */ /* synthetic */ ObjectCategoryData buildInternally(List list) {
            return buildInternally((List<EntityRadarCategoryData>) list);
        }

        private Builder() {
            super(EntityRadarCategoryConstants.LIST_FACTORY, EntityRadarCategoryConstants.MAP_FACTORY);
        }

        @Override // xaero.hud.category.serialization.data.ObjectCategoryData.Builder
        protected EntityRadarCategoryData buildInternally(List<EntityRadarCategoryData> builtSubCategories) {
            return new EntityRadarCategoryData(this.name, this.hardInclude, this.includeList, this.excludeList, this.excludeMode, this.settingOverrides, builtSubCategories, this.protection, this.includeListInSuperCategory);
        }

        public static Builder begin() {
            return new Builder();
        }
    }
}
