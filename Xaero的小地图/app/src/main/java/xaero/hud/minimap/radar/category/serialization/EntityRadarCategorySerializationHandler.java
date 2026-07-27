package xaero.hud.minimap.radar.category.serialization;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import xaero.hud.category.rule.ObjectCategoryHardRule;
import xaero.hud.category.rule.ObjectCategoryListRuleType;
import xaero.hud.category.serialization.FilterObjectCategorySerializationHandler;
import xaero.hud.category.serialization.data.ObjectCategoryDataSerializer;
import xaero.hud.category.setting.ObjectCategorySetting;
import xaero.hud.minimap.radar.category.EntityRadarCategory;
import xaero.hud.minimap.radar.category.EntityRadarCategoryConstants;
import xaero.hud.minimap.radar.category.rule.EntityRadarCategoryHardRules;
import xaero.hud.minimap.radar.category.rule.EntityRadarListRuleTypes;
import xaero.hud.minimap.radar.category.serialization.data.EntityRadarCategoryData;
import xaero.hud.minimap.radar.category.setting.EntityRadarCategorySettings;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/serialization/EntityRadarCategorySerializationHandler.class */
public final class EntityRadarCategorySerializationHandler extends FilterObjectCategorySerializationHandler<Entity, Player, EntityRadarCategoryData, EntityRadarCategory, EntityRadarCategory.Builder, EntityRadarCategoryData.Builder> {
    private EntityRadarCategorySerializationHandler(ObjectCategoryDataSerializer<EntityRadarCategoryData, String> serializer, Supplier<EntityRadarCategoryData.Builder> dataBuilderFactory, Supplier<EntityRadarCategory.Builder> objectCategoryBuilderFactory, Function<String, ObjectCategorySetting<?>> settingTypeGetter, Function<String, ObjectCategoryHardRule<Entity, Player>> hardRuleGetter, ObjectCategoryListRuleType<Entity, Player, ?> defaultListRuleType, Function<String, ObjectCategoryListRuleType<Entity, Player, ?>> listRuleTypeGetter, String listRuleTypePrefixSeparator) {
        super(serializer, dataBuilderFactory, objectCategoryBuilderFactory, settingTypeGetter, hardRuleGetter, defaultListRuleType, listRuleTypeGetter, listRuleTypePrefixSeparator);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/category/serialization/EntityRadarCategorySerializationHandler$Builder.class */
    public static final class Builder extends FilterObjectCategorySerializationHandler.Builder<Entity, Player, EntityRadarCategoryData, EntityRadarCategory, EntityRadarCategory.Builder, EntityRadarCategoryData.Builder, EntityRadarCategorySerializationHandler, Builder> {
        private Builder(ObjectCategoryDataSerializer<EntityRadarCategoryData, String> serializer) {
            super(serializer);
        }

        @Override // xaero.hud.category.serialization.FilterObjectCategorySerializationHandler.Builder, xaero.hud.category.serialization.ObjectCategorySerializationHandler.Builder
        public Builder setDefault() {
            super.setDefault();
            Map<String, ObjectCategoryHardRule<Entity, Player>> map = EntityRadarCategoryHardRules.HARD_RULES;
            Objects.requireNonNull(map);
            setHardRuleGetter((v1) -> {
                return r1.get(v1);
            });
            setDataBuilderFactory(EntityRadarCategoryConstants.DATA_BUILDER_FACTORY);
            setObjectCategoryBuilderFactory(EntityRadarCategoryConstants.CATEGORY_BUILDER_FACTORY);
            Map<String, ObjectCategorySetting<?>> map2 = EntityRadarCategorySettings.SETTINGS;
            Objects.requireNonNull(map2);
            setSettingTypeGetter((v1) -> {
                return r1.get(v1);
            });
            setDefaultListRuleType(EntityRadarListRuleTypes.ENTITY_TYPE);
            Map<String, ObjectCategoryListRuleType<Entity, Player, ?>> map3 = EntityRadarListRuleTypes.TYPE_MAP;
            Objects.requireNonNull(map3);
            setListRuleTypeGetter((v1) -> {
                return r1.get(v1);
            });
            return this;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.category.serialization.ObjectCategorySerializationHandler.Builder
        public EntityRadarCategorySerializationHandler buildInternally() {
            return new EntityRadarCategorySerializationHandler(this.serializer, this.dataBuilderFactory, this.objectCategoryBuilderFactory, this.settingTypeGetter, this.hardRuleGetter, this.defaultListRuleType, this.listRuleTypeGetter, this.listRuleTypePrefixSeparator);
        }

        public static Builder begin(ObjectCategoryDataSerializer<EntityRadarCategoryData, String> serializer) {
            return new Builder(serializer).setDefault();
        }
    }
}
