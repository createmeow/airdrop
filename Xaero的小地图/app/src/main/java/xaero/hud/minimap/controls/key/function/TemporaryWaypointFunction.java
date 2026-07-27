package xaero.hud.minimap.controls.key.function;

import net.minecraft.client.Minecraft;
import xaero.common.effect.Effects;
import xaero.common.misc.Misc;
import xaero.common.misc.OptimizedMath;
import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.module.MinimapSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/controls/key/function/TemporaryWaypointFunction.class */
public class TemporaryWaypointFunction extends KeyMappingFunction {
    protected TemporaryWaypointFunction() {
        super(false);
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onPress() {
        Minecraft mc = Minecraft.getInstance();
        if (Misc.hasEffect(mc.player, Effects.NO_WAYPOINTS) || Misc.hasEffect(mc.player, Effects.NO_WAYPOINTS_HARMFUL)) {
            return;
        }
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        session.getWaypointSession().getTemporaryHandler().createTemporaryWaypoint(session.getWorldManager().getCurrentWorld(), OptimizedMath.myFloor(mc.cameraEntity.getX()), OptimizedMath.myFloor(mc.cameraEntity.getY() + 0.0625d), OptimizedMath.myFloor(mc.cameraEntity.getZ()));
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onRelease() {
    }
}
