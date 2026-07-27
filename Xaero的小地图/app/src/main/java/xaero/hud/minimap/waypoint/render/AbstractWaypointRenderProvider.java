package xaero.hud.minimap.waypoint.render;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import xaero.common.HudMod;
import xaero.common.minimap.element.render.MinimapElementRenderProvider;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointVisibilityType;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.DestinationHandler;
import xaero.hud.minimap.waypoint.render.AbstractWaypointRenderContext;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.lib.client.config.ClientConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/render/AbstractWaypointRenderProvider.class */
public abstract class AbstractWaypointRenderProvider<C extends AbstractWaypointRenderContext> extends MinimapElementRenderProvider<Waypoint, C> {
    private Iterator<Waypoint> iterator;
    private boolean deathpoints;
    private DestinationHandler destinationHandler;
    private final List<Waypoint> collectingList = new ArrayList();
    public final Predicate<Waypoint> filter = w -> {
        if (w.isDisabled() || w.getVisibility() == WaypointVisibilityType.WORLD_MAP_LOCAL || w.getVisibility() == WaypointVisibilityType.WORLD_MAP_GLOBAL) {
            return false;
        }
        return this.deathpoints || !w.getPurpose().isDeath();
    };

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public void begin(MinimapElementRenderLocation location, C context) {
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        this.collectingList.clear();
        session.getWaypointSession().getCollector().collect(this.collectingList);
        HudMod.INSTANCE.getSettings();
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        boolean renderAllSets = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINTS_ALL_SETS)).booleanValue();
        MinimapWorld currentWorld = session.getWorldManager().getCurrentWorld();
        this.destinationHandler = session.getWaypointSession().getDestinationHandler();
        Entity renderEntity = Minecraft.getInstance().getCameraEntity();
        boolean deleteReachedDeathpoints = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.DELETE_REACHED_DEATHPOINTS)).booleanValue();
        this.destinationHandler.begin(renderEntity, currentWorld, renderAllSets, deleteReachedDeathpoints);
        this.deathpoints = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.DEATHPOINTS)).booleanValue();
        Camera activeRender = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 cameraPos = activeRender.getPosition();
        context.dimCoordinateScale = session.getDimensionHelper().getDimCoordinateScale(currentWorld);
        double cameraPosMultiplier = Minecraft.getInstance().level.dimensionType().coordinateScale() / context.dimCoordinateScale;
        Waypoint.RENDER_SORTING_POS = new Vec3(cameraPos.x * cameraPosMultiplier, cameraPos.y, cameraPos.z * cameraPosMultiplier);
        this.iterator = this.collectingList.stream().filter(this.filter).sorted().iterator();
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public boolean hasNext(MinimapElementRenderLocation location, C context) {
        return this.iterator.hasNext();
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public Waypoint getNext(MinimapElementRenderLocation location, C context) {
        Waypoint result = this.iterator.next();
        this.destinationHandler.handle(result);
        return result;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public void end(MinimapElementRenderLocation location, C context) {
        this.iterator = null;
        this.deathpoints = false;
        this.destinationHandler.end();
        this.destinationHandler = null;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public Waypoint setupContextAndGetNext(MinimapElementRenderLocation location, C context) {
        return getNext(location, (MinimapElementRenderLocation) context);
    }
}
