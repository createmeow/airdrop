package xaero.hud.minimap.common.config.option.value.type.sync.serialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import xaero.common.HudMod;
import xaero.hud.category.serialization.data.ObjectCategoryDataNbtSerializer;
import xaero.hud.minimap.common.config.info.config.InfoDisplayConfigData;
import xaero.hud.minimap.common.config.info.config.InfoDisplayManagerConfigData;
import xaero.hud.minimap.radar.category.serialization.data.EntityRadarCategoryData;
import xaero.lib.common.config.option.value.sync.serialization.ConfigValueSyncCodec;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/common/config/option/value/type/sync/serialization/MinimapValueSyncCodecs.class */
public class MinimapValueSyncCodecs {
    public static final ConfigValueSyncCodec<EntityRadarCategoryData, CompoundTag> ENTITY_RADAR_CATEGORIES = new ConfigValueSyncCodec<>(rootCategory -> {
        return HudMod.INSTANCE.getEntityRadarCategorySerializers().getNbt().serialize((ObjectCategoryDataNbtSerializer<EntityRadarCategoryData, EntityRadarCategoryData.Builder>) rootCategory);
    }, serializedData -> {
        return (EntityRadarCategoryData) HudMod.INSTANCE.getEntityRadarCategorySerializers().getNbt().deserialize(serializedData);
    });
    public static final ConfigValueSyncCodec<InfoDisplayManagerConfigData, CompoundTag> INFO_DISPLAY_CONFIG = new ConfigValueSyncCodec<>(config -> {
        CompoundTag encodedData = new CompoundTag();
        ListTag orderTag = new ListTag();
        config.getOrderStream().forEach(s -> {
            orderTag.add(StringTag.valueOf(s));
        });
        encodedData.put("o", orderTag);
        CompoundTag configsTag = new CompoundTag();
        config.getOrderStream().forEach(id -> {
            InfoDisplayConfigData infoDisplayConfig = config.get(id);
            if (infoDisplayConfig == null) {
                return;
            }
            CompoundTag infoDisplayTag = new CompoundTag();
            if (infoDisplayConfig.getState() != null) {
                infoDisplayTag.putString("s", infoDisplayConfig.getState());
            }
            if (infoDisplayConfig.getBackgroundColor() != null) {
                infoDisplayTag.putInt("b", infoDisplayConfig.getBackgroundColor().intValue());
            }
            if (infoDisplayConfig.getTextColor() != null) {
                infoDisplayTag.putInt("t", infoDisplayConfig.getTextColor().intValue());
            }
            if (infoDisplayTag.isEmpty()) {
                return;
            }
            configsTag.put(id, infoDisplayTag);
        });
        encodedData.put("c", configsTag);
        return encodedData;
    }, encodedData -> {
        ListTag orderTag = encodedData.getList("o", 8);
        List<String> order = new ArrayList<>();
        if (!orderTag.isEmpty()) {
            for (int i = 0; i < orderTag.size(); i++) {
                order.add(orderTag.getString(i));
            }
        }
        CompoundTag configsTag = encodedData.getCompound("c");
        Map<String, InfoDisplayConfigData> configs = new HashMap<>();
        order.forEach(id -> {
            CompoundTag infoDisplayTag = configsTag.getCompound(id);
            if (infoDisplayTag.isEmpty()) {
                configs.put(id, new InfoDisplayConfigData(null, null, null));
                return;
            }
            String state = infoDisplayTag.contains("s", 8) ? infoDisplayTag.getString("s") : null;
            Integer backgroundColor = infoDisplayTag.contains("b", 3) ? Integer.valueOf(infoDisplayTag.getInt("b")) : null;
            Integer textColor = infoDisplayTag.contains("t", 3) ? Integer.valueOf(infoDisplayTag.getInt("t")) : null;
            configs.put(id, new InfoDisplayConfigData(backgroundColor, textColor, state));
        });
        return new InfoDisplayManagerConfigData(order, configs);
    });
}
