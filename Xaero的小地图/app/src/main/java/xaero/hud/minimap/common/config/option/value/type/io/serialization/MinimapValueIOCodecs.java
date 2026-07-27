package xaero.hud.minimap.common.config.option.value.type.io.serialization;

import xaero.common.HudMod;
import xaero.common.minimap.write.MinimapWriter;
import xaero.hud.category.serialization.data.ObjectCategoryDataGsonSerializer;
import xaero.hud.minimap.common.config.info.config.InfoDisplayManagerConfigData;
import xaero.hud.minimap.radar.category.serialization.data.EntityRadarCategoryData;
import xaero.lib.common.config.option.value.io.serialization.LargeConfigValueIOCodec;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/common/config/option/value/type/io/serialization/MinimapValueIOCodecs.class */
public class MinimapValueIOCodecs {
    public static final LargeConfigValueIOCodec<EntityRadarCategoryData> ENTITY_RADAR_CATEGORIES = new LargeConfigValueIOCodec<>(rootCategory -> {
        return HudMod.INSTANCE.getEntityRadarCategorySerializers().getGson().serialize((ObjectCategoryDataGsonSerializer<EntityRadarCategoryData>) rootCategory);
    }, serializedData -> {
        if (serializedData == null) {
            return null;
        }
        return (EntityRadarCategoryData) HudMod.INSTANCE.getEntityRadarCategorySerializers().getGson().deserialize(serializedData);
    }, MinimapWriter.NO_Y_VALUE, ".json");
    public static final LargeConfigValueIOCodec<InfoDisplayManagerConfigData> INFO_DISPLAY_CONFIG = new LargeConfigValueIOCodec<>(managerConfigData -> {
        return HudMod.INSTANCE.getInfoDisplaysIO().encode(managerConfigData);
    }, encodedData -> {
        if (encodedData == null) {
            return null;
        }
        return HudMod.INSTANCE.getInfoDisplaysIO().decode(encodedData);
    }, MinimapWriter.NO_Y_VALUE, ".txt");
}
