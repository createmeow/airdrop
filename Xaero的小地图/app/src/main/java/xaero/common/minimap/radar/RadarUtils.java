package xaero.common.minimap.radar;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/radar/RadarUtils.class */
public class RadarUtils {
    @Deprecated
    public static boolean hostileException(Entity e) {
        return xaero.hud.minimap.radar.util.RadarUtils.isHostileException(e);
    }

    @Deprecated
    public static boolean isTamed(Entity e, Player p) {
        return xaero.hud.minimap.radar.util.RadarUtils.isTamed(e, p);
    }

    @Deprecated
    public static boolean isHostile(Entity e) {
        return xaero.hud.minimap.radar.util.RadarUtils.isHostile(e);
    }
}
