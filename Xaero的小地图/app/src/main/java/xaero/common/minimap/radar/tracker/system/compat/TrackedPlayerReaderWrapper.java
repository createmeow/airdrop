package xaero.common.minimap.radar.tracker.system.compat;

import java.util.UUID;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.common.minimap.radar.tracker.system.ITrackedPlayerReader;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/radar/tracker/system/compat/TrackedPlayerReaderWrapper.class */
public class TrackedPlayerReaderWrapper<P> implements ITrackedPlayerReader<P> {
    private final xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader<P> original;

    public TrackedPlayerReaderWrapper(xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader<P> original) {
        this.original = original;
    }

    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public UUID getId(P player) {
        return this.original.getId(player);
    }

    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public double getX(P player) {
        return this.original.getX(player);
    }

    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public double getY(P player) {
        return this.original.getY(player);
    }

    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public double getZ(P player) {
        return this.original.getZ(player);
    }

    @Override // xaero.hud.minimap.player.tracker.system.ITrackedPlayerReader
    public ResourceKey<Level> getDimension(P player) {
        return this.original.getDimension(player);
    }
}
