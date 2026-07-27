package xaero.hud.minimap.waypoint.render.world;

import net.minecraft.client.Minecraft;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.entity.EntityUtils;
import xaero.hud.minimap.element.render.MinimapElementReader;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/render/world/WaypointWorldRenderReader.class */
public class WaypointWorldRenderReader extends MinimapElementReader<Waypoint, WaypointWorldRenderContext> {
    private final WaypointWorldRenderContext context;

    public WaypointWorldRenderReader(WaypointWorldRenderContext context) {
        this.context = context;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public boolean isHidden(Waypoint element, WaypointWorldRenderContext context) {
        return false;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public double getRenderX(Waypoint element, WaypointWorldRenderContext context, float partialTicks) {
        return element.getX() + 0.5d;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public double getRenderY(Waypoint element, WaypointWorldRenderContext context, float partialTicks) {
        if (element.isYIncluded()) {
            return element.getY() + 1;
        }
        if (context.renderEntityPos == null) {
            return EntityUtils.getEntityY(Minecraft.getInstance().getCameraEntity(), partialTicks) + 1.0d;
        }
        return context.renderEntityPos.y + 1.0d;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public double getRenderZ(Waypoint element, WaypointWorldRenderContext context, float partialTicks) {
        return element.getZ() + 0.5d;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public double getCoordinateScale(Waypoint element, WaypointWorldRenderContext context, MinimapElementRenderInfo renderInfo) {
        return context.dimCoordinateScale;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public boolean shouldScalePartialCoordinates(Waypoint element, WaypointWorldRenderContext context, MinimapElementRenderInfo renderInfo) {
        return false;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxLeft(Waypoint element, WaypointWorldRenderContext context, float partialTicks) {
        return context.interactionBoxLeft;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxRight(Waypoint element, WaypointWorldRenderContext context, float partialTicks) {
        return -context.interactionBoxLeft;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxTop(Waypoint element, WaypointWorldRenderContext context, float partialTicks) {
        return context.interactionBoxTop;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxBottom(Waypoint element, WaypointWorldRenderContext context, float partialTicks) {
        return -context.interactionBoxTop;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxLeft(Waypoint element, WaypointWorldRenderContext context, float partialTicks) {
        return -32;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxRight(Waypoint element, WaypointWorldRenderContext context, float partialTicks) {
        return 32;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxTop(Waypoint element, WaypointWorldRenderContext context, float partialTicks) {
        return -32;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxBottom(Waypoint element, WaypointWorldRenderContext context, float partialTicks) {
        return 32;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getLeftSideLength(Waypoint element, Minecraft mc) {
        return 0;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public String getMenuName(Waypoint element) {
        return "n/a";
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public String getFilterName(Waypoint element) {
        return getMenuName(element);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getMenuTextFillLeftPadding(Waypoint element) {
        return 0;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public int getRightClickTitleBackgroundColor(Waypoint element) {
        return 0;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public boolean shouldScaleBoxWithOptionalScale() {
        return false;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public boolean isInteractable(MinimapElementRenderLocation location, Waypoint element) {
        return true;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public boolean isAlwaysHighlightedWhenHovered(Waypoint element, WaypointWorldRenderContext context) {
        return !context.onlyMainInfo;
    }
}
