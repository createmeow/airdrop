package xaero.hud.minimap.radar;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import xaero.common.minimap.radar.MinimapRadar;
import xaero.hud.minimap.radar.category.EntityRadarCategoryManager;
import xaero.hud.minimap.radar.color.RadarColorHelper;
import xaero.hud.minimap.radar.state.RadarState;
import xaero.hud.minimap.radar.state.RadarStateUpdater;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/RadarSession.class */
public class RadarSession {
    private final EntityRadarCategoryManager categoryManager;
    private final RadarStateUpdater stateUpdater;
    private final RadarState state = new RadarState();
    private final RadarColorHelper colorHelper = new RadarColorHelper();

    public RadarSession(EntityRadarCategoryManager categoryManager) {
        this.categoryManager = categoryManager;
        this.stateUpdater = new RadarStateUpdater(categoryManager, this.state);
    }

    public void update(ClientLevel world, Entity renderEntity, Player player) {
        this.stateUpdater.update(world, renderEntity, player);
    }

    public EntityRadarCategoryManager getCategoryManager() {
        return this.categoryManager;
    }

    public RadarState getState() {
        return this.state;
    }

    public RadarStateUpdater getStateUpdater() {
        return this.stateUpdater;
    }

    public RadarColorHelper getColorHelper() {
        return this.colorHelper;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/RadarSession$Builder.class */
    public static class Builder {
        private EntityRadarCategoryManager categoryManager;

        private Builder() {
        }

        public Builder setDefault() {
            setCategoryManager(null);
            return this;
        }

        public Builder setCategoryManager(EntityRadarCategoryManager categoryManager) {
            this.categoryManager = categoryManager;
            return this;
        }

        public RadarSession build() {
            if (this.categoryManager == null) {
                throw new IllegalStateException();
            }
            return new MinimapRadar(this.categoryManager);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
