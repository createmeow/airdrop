package xaero.hud.minimap.player.tracker.system;

import java.util.UUID;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/player/tracker/system/ITrackedPlayerReader.class */
public interface ITrackedPlayerReader<P> {
    UUID getId(P p);

    double getX(P p);

    double getY(P p);

    double getZ(P p);

    ResourceKey<Level> getDimension(P p);
}
