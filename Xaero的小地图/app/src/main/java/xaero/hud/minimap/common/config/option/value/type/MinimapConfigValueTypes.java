package xaero.hud.minimap.common.config.option.value.type;

import xaero.hud.minimap.common.config.info.config.InfoDisplayManagerConfigData;
import xaero.hud.minimap.common.config.option.value.type.io.serialization.MinimapValueIOCodecs;
import xaero.hud.minimap.common.config.option.value.type.sync.serialization.MinimapValueSyncCodecs;
import xaero.hud.minimap.radar.category.serialization.data.EntityRadarCategoryData;
import xaero.lib.common.config.option.value.io.serialization.LargeConfigValueIOCodec;
import xaero.lib.common.config.option.value.type.ConfigValueType;
import xaero.lib.common.config.option.value.type.LargeConfigValueType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/common/config/option/value/type/MinimapConfigValueTypes.class */
public class MinimapConfigValueTypes {
    public static final ConfigValueType<EntityRadarCategoryData> ENTITY_RADAR_CATEGORIES = LargeConfigValueType.Builder.begin().setIoCodec((LargeConfigValueIOCodec) MinimapValueIOCodecs.ENTITY_RADAR_CATEGORIES).setSyncCodec(MinimapValueSyncCodecs.ENTITY_RADAR_CATEGORIES).build();
    public static final ConfigValueType<InfoDisplayManagerConfigData> INFO_DISPLAY_CONFIG = LargeConfigValueType.Builder.begin().setIoCodec((LargeConfigValueIOCodec) MinimapValueIOCodecs.INFO_DISPLAY_CONFIG).setSyncCodec(MinimapValueSyncCodecs.INFO_DISPLAY_CONFIG).build();
}
