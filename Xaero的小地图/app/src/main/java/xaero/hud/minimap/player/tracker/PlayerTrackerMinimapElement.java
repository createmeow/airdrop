package xaero.hud.minimap.player.tracker;

import java.util.UUID;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.hud.minimap.player.tracker.system.IRenderedPlayerTracker;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/player/tracker/PlayerTrackerMinimapElement.class */
public class PlayerTrackerMinimapElement<P> {
    private P player;
    private IRenderedPlayerTracker<P> system;
    private boolean renderedOnRadar;

    public PlayerTrackerMinimapElement(P player, IRenderedPlayerTracker<P> system) {
        this.player = player;
        this.system = system;
    }

    public UUID getPlayerId() {
        return this.system.getReader().getId(this.player);
    }

    public double getX() {
        return this.system.getReader().getX(this.player);
    }

    public double getY() {
        return this.system.getReader().getY(this.player);
    }

    public double getZ() {
        return this.system.getReader().getZ(this.player);
    }

    public ResourceKey<Level> getDimension() {
        return this.system.getReader().getDimension(this.player);
    }

    public P getPlayer() {
        return this.player;
    }

    public void setRenderedOnRadar(boolean renderedOnRadar) {
        this.renderedOnRadar = renderedOnRadar;
    }

    public boolean wasRenderedOnRadar() {
        return this.renderedOnRadar;
    }

    public IRenderedPlayerTracker<P> getSystem() {
        return this.system;
    }
}
