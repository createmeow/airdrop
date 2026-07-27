package xaero.common.minimap.radar;

import java.util.List;
import net.minecraft.world.entity.Entity;
import xaero.hud.minimap.radar.category.EntityRadarCategory;
import xaero.hud.minimap.radar.state.RadarList;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/radar/MinimapRadarList.class */
public final class MinimapRadarList extends RadarList {
    @Deprecated
    public MinimapRadarList() {
    }

    @Override // xaero.hud.minimap.radar.state.RadarList
    @Deprecated
    public EntityRadarCategory getClientCategory() {
        return super.getClientCategory();
    }

    @Override // xaero.hud.minimap.radar.state.RadarList
    @Deprecated
    public MinimapRadarList setClientCategory(EntityRadarCategory clientCategory) {
        return (MinimapRadarList) super.setClientCategory(clientCategory);
    }

    @Override // xaero.hud.minimap.radar.state.RadarList
    @Deprecated
    public List<Entity> getEntities() {
        return (List) super.getEntities();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/radar/MinimapRadarList$Builder.class */
    public static final class Builder {
        private Builder() {
        }

        public Builder setDefault() {
            return this;
        }

        public MinimapRadarList build() {
            return new MinimapRadarList();
        }

        public static Builder getDefault() {
            return new Builder().setDefault();
        }
    }
}
