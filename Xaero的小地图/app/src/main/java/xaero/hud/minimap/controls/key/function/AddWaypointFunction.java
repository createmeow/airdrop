package xaero.hud.minimap.controls.key.function;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import xaero.common.HudMod;
import xaero.common.gui.GuiAddWaypoint;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.module.MinimapSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/controls/key/function/AddWaypointFunction.class */
public class AddWaypointFunction extends KeyMappingFunction {
    protected AddWaypointFunction() {
        super(false);
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onPress() {
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        if (!HudMod.INSTANCE.getSettings().waypointsGUI(session)) {
            return;
        }
        Minecraft.getInstance().setScreen(new GuiAddWaypoint(HudMod.INSTANCE, session, (Screen) null, (ArrayList<Waypoint>) Lists.newArrayList(), session.getWorldState().getCurrentWorldPath().getRoot(), session.getWorldManager().getCurrentWorld(), true));
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onRelease() {
    }
}
