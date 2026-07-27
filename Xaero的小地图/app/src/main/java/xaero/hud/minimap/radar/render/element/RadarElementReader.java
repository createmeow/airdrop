package xaero.hud.minimap.radar.render.element;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import xaero.common.minimap.element.render.MinimapElementReader;
import xaero.hud.entity.EntityUtils;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/render/element/RadarElementReader.class */
public abstract class RadarElementReader extends MinimapElementReader<Entity, RadarRenderContext> {
    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public double getRenderX(Entity element, RadarRenderContext context, float partialTicks) {
        return EntityUtils.getEntityX(element, partialTicks);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public double getRenderY(Entity element, RadarRenderContext context, float partialTicks) {
        return EntityUtils.getEntityY(element, partialTicks);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public double getRenderZ(Entity element, RadarRenderContext context, float partialTicks) {
        return EntityUtils.getEntityZ(element, partialTicks);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public boolean isHidden(Entity element, RadarRenderContext context) {
        return false;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxLeft(Entity element, RadarRenderContext context, float partialTicks) {
        return context.icon ? -16 : -6;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxRight(Entity element, RadarRenderContext context, float partialTicks) {
        return context.icon ? 16 : 6;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxTop(Entity element, RadarRenderContext context, float partialTicks) {
        return context.icon ? -16 : -6;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getInteractionBoxBottom(Entity element, RadarRenderContext context, float partialTicks) {
        return context.icon ? 16 : 6;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxLeft(Entity element, RadarRenderContext context, float partialTicks) {
        return -64;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxRight(Entity element, RadarRenderContext context, float partialTicks) {
        return 64;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxTop(Entity element, RadarRenderContext context, float partialTicks) {
        return -32;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getRenderBoxBottom(Entity element, RadarRenderContext context, float partialTicks) {
        return 32;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getLeftSideLength(Entity element, Minecraft mc) {
        return 0;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public String getMenuName(Entity element) {
        return "n/a";
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public String getFilterName(Entity element) {
        return getMenuName(element);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getMenuTextFillLeftPadding(Entity element) {
        return 0;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public int getRightClickTitleBackgroundColor(Entity element) {
        return 0;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public float getBoxScale(MinimapElementRenderLocation location, Entity element, RadarRenderContext context) {
        return (location == MinimapElementRenderLocation.OVER_MINIMAP ? 0.5f : 1.0f) * ((float) (context.icon ? context.iconScale : context.dotScale));
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public boolean isInteractable(MinimapElementRenderLocation location, Entity element) {
        return true;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementReader, xaero.hud.minimap.element.render.MinimapElementReader
    public boolean shouldScaleBoxWithOptionalScale() {
        return true;
    }
}
