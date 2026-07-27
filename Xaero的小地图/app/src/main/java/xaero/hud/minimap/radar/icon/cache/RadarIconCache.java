package xaero.hud.minimap.radar.icon.cache;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.entity.EntityType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/cache/RadarIconCache.class */
public class RadarIconCache {
    private final Map<EntityType<?>, RadarIconEntityCache> iconCacheMap = new HashMap();

    public RadarIconEntityCache getEntityCache(EntityType<?> entityType) {
        RadarIconEntityCache result = this.iconCacheMap.get(entityType);
        if (result == null) {
            Map<EntityType<?>, RadarIconEntityCache> map = this.iconCacheMap;
            RadarIconEntityCache radarIconEntityCache = new RadarIconEntityCache(entityType);
            result = radarIconEntityCache;
            map.put(entityType, radarIconEntityCache);
        }
        return result;
    }

    public void clear() {
        this.iconCacheMap.clear();
    }
}
