package xaero.hud.minimap;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import xaero.common.HudMod;
import xaero.common.minimap.element.render.MinimapElementRenderer;
import xaero.common.minimap.render.MinimapFBORenderer;
import xaero.common.minimap.render.MinimapSafeModeRenderer;
import xaero.common.minimap.waypoints.render.WaypointsGuiRenderer;
import xaero.common.minimap.waypoints.render.WaypointsIngameRenderer;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.compass.render.CompassRenderer;
import xaero.hud.minimap.element.render.over.MinimapElementOverMapRendererHandler;
import xaero.hud.minimap.element.render.world.MinimapElementWorldRendererHandler;
import xaero.hud.minimap.info.InfoDisplays;
import xaero.hud.minimap.waypoint.render.WaypointDeleter;
import xaero.hud.minimap.waypoint.render.WaypointMapRenderer;
import xaero.hud.minimap.waypoint.render.world.WaypointWorldRenderer;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/Minimap.class */
public class Minimap {
    private final HudMod modMain;
    private final Minecraft mc = Minecraft.getInstance();
    private final WaypointMapRenderer waypointMapRenderer;
    private final WaypointWorldRenderer waypointWorldRenderer;
    private final MinimapFBORenderer minimapFBORenderer;
    private final CompassRenderer compassRenderer;
    private final MinimapElementOverMapRendererHandler overMapRendererHandler;
    private final MinimapElementWorldRendererHandler worldRendererHandler;
    private final InfoDisplays infoDisplays;
    private Throwable crashedWith;
    private MinimapSafeModeRenderer minimapSafeModeRenderer;

    public Minimap(HudMod modMain) throws IOException {
        this.modMain = modMain;
        WaypointDeleter waypointDeleter = new WaypointDeleter(modMain);
        this.waypointMapRenderer = WaypointMapRenderer.Builder.begin(modMain).setWaypointDeleter(waypointDeleter).build();
        this.waypointWorldRenderer = WaypointWorldRenderer.Builder.begin().build();
        this.compassRenderer = new CompassRenderer(modMain, this.mc);
        this.overMapRendererHandler = MinimapElementOverMapRendererHandler.Builder.begin().build();
        this.overMapRendererHandler.add((MinimapElementRenderer<?, ?>) this.waypointMapRenderer);
        this.worldRendererHandler = MinimapElementWorldRendererHandler.Builder.begin().build();
        this.worldRendererHandler.add(this.waypointWorldRenderer);
        this.minimapFBORenderer = new MinimapFBORenderer(modMain, this.mc, this.waypointMapRenderer, this, this.compassRenderer);
        this.minimapSafeModeRenderer = new MinimapSafeModeRenderer(modMain, this.mc, this.waypointMapRenderer, this, this.compassRenderer);
        this.infoDisplays = new InfoDisplays(modMain.getInfoDisplaysIO());
    }

    public Throwable getCrashedWith() {
        return this.crashedWith;
    }

    public void setCrashedWith(Throwable crashedWith) {
        if (this.crashedWith == null) {
            this.crashedWith = crashedWith;
        }
    }

    public void checkCrashes() {
        if (this.crashedWith != null) {
            Throwable crash = this.crashedWith;
            this.crashedWith = null;
            throw new RuntimeException("Xaero's Minimap (" + this.modMain.getVersionID() + ") has crashed! Please report here: bit.ly/XaeroMMIssues", crash);
        }
    }

    @Deprecated
    public WaypointsGuiRenderer getWaypointsGuiRenderer() {
        return (WaypointsGuiRenderer) this.waypointMapRenderer;
    }

    @Deprecated
    public xaero.hud.minimap.waypoint.render.WaypointsGuiRenderer getWaypointGuiRenderer() {
        return (xaero.hud.minimap.waypoint.render.WaypointsGuiRenderer) this.waypointMapRenderer;
    }

    public WaypointMapRenderer getWaypointMapRenderer() {
        return this.waypointMapRenderer;
    }

    @Deprecated
    public WaypointsIngameRenderer getWaypointsIngameRenderer() {
        return (WaypointsIngameRenderer) getWaypointWorldRenderer();
    }

    public WaypointWorldRenderer getWaypointWorldRenderer() {
        return this.waypointWorldRenderer;
    }

    public MinimapFBORenderer getMinimapFBORenderer() {
        return this.minimapFBORenderer;
    }

    public MinimapSafeModeRenderer getMinimapSafeModeRenderer() {
        return this.minimapSafeModeRenderer;
    }

    public MinimapElementOverMapRendererHandler getOverMapRendererHandler() {
        return this.overMapRendererHandler;
    }

    public MinimapElementWorldRendererHandler getWorldRendererHandler() {
        return this.worldRendererHandler;
    }

    public boolean usingFBO() {
        boolean mapSafeMode = ((Boolean) this.modMain.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.SAFE_MODE)).booleanValue();
        return getMinimapFBORenderer().isLoadedFBO() && !mapSafeMode;
    }

    public CompassRenderer getCompassRenderer() {
        return this.compassRenderer;
    }

    public InfoDisplays getInfoDisplays() {
        return this.infoDisplays;
    }

    public HudMod getModMain() {
        return this.modMain;
    }
}
