package xaero.common.server.radar.tracker;

import java.util.UUID;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/server/radar/tracker/SyncedTrackedPlayer.class */
public class SyncedTrackedPlayer {
    private final UUID id;
    private double x;
    private double y;
    private double z;
    private ResourceKey<Level> dimension;

    public SyncedTrackedPlayer(UUID id, double x, double y, double z, ResourceKey<Level> dimension) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
    }

    public SyncedTrackedPlayer setPos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public SyncedTrackedPlayer setDimension(ResourceKey<Level> dimension) {
        this.dimension = dimension;
        return this;
    }

    public UUID getId() {
        return this.id;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public ResourceKey<Level> getDimensionKey() {
        return this.dimension;
    }

    public boolean matchesEnough(Player player, double maxAxisDistance) {
        return Math.abs(player.getX() - this.x) <= maxAxisDistance && Math.abs(player.getY() - this.y) <= maxAxisDistance && Math.abs(player.getZ() - this.z) <= maxAxisDistance && player.level().dimension().location().equals(this.dimension);
    }

    public void update(Player player) {
        setPos(player.getX(), player.getY(), player.getZ()).setDimension(player.level().dimension());
    }

    public void copyFrom(SyncedTrackedPlayer trackedPlayer) {
        setPos(trackedPlayer.getX(), trackedPlayer.getY(), trackedPlayer.getZ()).setDimension(trackedPlayer.getDimensionKey());
    }
}
