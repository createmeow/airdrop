package xaero.common.minimap;

import java.io.IOException;
import xaero.common.HudMod;
import xaero.common.minimap.element.render.over.MinimapElementOverMapRendererHandler;
import xaero.common.minimap.render.MinimapFBORenderer;
import xaero.common.minimap.render.MinimapSafeModeRenderer;
import xaero.common.minimap.waypoints.render.WaypointsGuiRenderer;
import xaero.common.minimap.waypoints.render.WaypointsIngameRenderer;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.compass.render.CompassRenderer;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/MinimapInterface.class */
public class MinimapInterface extends Minimap {
    public MinimapInterface(HudMod modMain) throws IOException {
        super(modMain);
    }

    @Override // xaero.hud.minimap.Minimap
    @Deprecated
    public WaypointsGuiRenderer getWaypointsGuiRenderer() {
        return super.getWaypointsGuiRenderer();
    }

    @Override // xaero.hud.minimap.Minimap
    @Deprecated
    public WaypointsIngameRenderer getWaypointsIngameRenderer() {
        return super.getWaypointsIngameRenderer();
    }

    @Override // xaero.hud.minimap.Minimap
    @Deprecated
    public MinimapFBORenderer getMinimapFBORenderer() {
        return super.getMinimapFBORenderer();
    }

    @Override // xaero.hud.minimap.Minimap
    @Deprecated
    public MinimapSafeModeRenderer getMinimapSafeModeRenderer() {
        return super.getMinimapSafeModeRenderer();
    }

    @Override // xaero.hud.minimap.Minimap
    @Deprecated
    public MinimapElementOverMapRendererHandler getOverMapRendererHandler() {
        return (MinimapElementOverMapRendererHandler) super.getOverMapRendererHandler();
    }

    @Override // xaero.hud.minimap.Minimap
    @Deprecated
    public boolean usingFBO() {
        return super.usingFBO();
    }

    @Override // xaero.hud.minimap.Minimap
    @Deprecated
    public CompassRenderer getCompassRenderer() {
        return super.getCompassRenderer();
    }
}
