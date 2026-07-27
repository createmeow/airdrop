package xaero.hud.minimap.controls.key.function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import xaero.common.HudMod;
import xaero.common.gui.GuiWaypoints;
import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.module.MinimapSession;
import xaero.lib.client.gui.ScreenBase;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/controls/key/function/WaypointMenuFunction.class */
public class WaypointMenuFunction extends KeyMappingFunction {
    protected WaypointMenuFunction() {
        super(false);
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onPress() {
        Minecraft mc = Minecraft.getInstance();
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        if (!HudMod.INSTANCE.getSettings().waypointsGUI(session)) {
            return;
        }
        Screen current = mc.screen;
        Screen currentEscScreen = current instanceof ScreenBase ? ((ScreenBase) current).escape : null;
        mc.setScreen(new GuiWaypoints(HudMod.INSTANCE, session, current, currentEscScreen));
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onRelease() {
    }
}
