package xaero.hud.minimap.waypoint.render;

import net.minecraft.client.Minecraft;
import xaero.common.minimap.element.render.MinimapElementReader;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.waypoint.WaypointPurpose;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/render/WaypointMapRenderReader.class */
public class WaypointMapRenderReader extends MinimapElementReader<Waypoint, WaypointMapRenderContext> {
    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public double getRenderX(Waypoint element, WaypointMapRenderContext context, float partialTicks) {
        return element.getX() + 0.5d;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public double getRenderY(Waypoint element, WaypointMapRenderContext context, float partialTicks) {
        return element.getY() + 1;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public double getRenderZ(Waypoint element, WaypointMapRenderContext context, float partialTicks) {
        return element.getZ() + 0.5d;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public double getCoordinateScale(Waypoint element, WaypointMapRenderContext context, MinimapElementRenderInfo renderInfo) {
        return context.dimCoordinateScale;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public boolean shouldScalePartialCoordinates(Waypoint element, WaypointMapRenderContext context, MinimapElementRenderInfo renderInfo) {
        return false;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public boolean isHidden(Waypoint element, WaypointMapRenderContext context) {
        return false;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxLeft(Waypoint element, WaypointMapRenderContext context, float partialTicks) {
        return 0;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxRight(Waypoint element, WaypointMapRenderContext context, float partialTicks) {
        return 0;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxTop(Waypoint element, WaypointMapRenderContext context, float partialTicks) {
        return 0;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxBottom(Waypoint element, WaypointMapRenderContext context, float partialTicks) {
        return 0;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxLeft(Waypoint element, WaypointMapRenderContext context, float partialTicks) {
        return -getRenderBoxRight(element, context, partialTicks);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxRight(Waypoint element, WaypointMapRenderContext context, float partialTicks) {
        int j = element.getPurpose() == WaypointPurpose.DEATH ? 4 : Minecraft.getInstance().font.width(element.getInitials()) / 2;
        int addedFrame = j > 4 ? j - 4 : 0;
        return 5 + addedFrame;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxTop(Waypoint element, WaypointMapRenderContext context, float partialTicks) {
        return getRenderBoxLeft(element, context, partialTicks);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxBottom(Waypoint element, WaypointMapRenderContext context, float partialTicks) {
        return getRenderBoxRight(element, context, partialTicks);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getLeftSideLength(Waypoint element, Minecraft mc) {
        return 0;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public String getMenuName(Waypoint element) {
        return "n/a";
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public String getFilterName(Waypoint element) {
        return getMenuName(element);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getMenuTextFillLeftPadding(Waypoint element) {
        return 0;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getRightClickTitleBackgroundColor(Waypoint element) {
        return 0;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public boolean shouldScaleBoxWithOptionalScale() {
        return false;
    }
}
