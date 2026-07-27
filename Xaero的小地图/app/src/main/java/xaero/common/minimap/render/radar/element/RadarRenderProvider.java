package xaero.common.minimap.render.radar.element;

import net.minecraft.world.entity.Entity;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/render/radar/element/RadarRenderProvider.class */
public final class RadarRenderProvider extends xaero.hud.minimap.radar.render.element.RadarRenderProvider {
    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public void begin(int location, xaero.hud.minimap.radar.render.element.RadarRenderContext context) {
        begin(MinimapElementRenderLocation.fromIndex(location), context);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public boolean hasNext(int location, xaero.hud.minimap.radar.render.element.RadarRenderContext context) {
        return hasNext(MinimapElementRenderLocation.fromIndex(location), context);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public Entity setupContextAndGetNext(int location, xaero.hud.minimap.radar.render.element.RadarRenderContext context) {
        return setupContextAndGetNext(MinimapElementRenderLocation.fromIndex(location), context);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public Entity getNext(int location, xaero.hud.minimap.radar.render.element.RadarRenderContext context) {
        return getNext(MinimapElementRenderLocation.fromIndex(location), context);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public void end(int location, xaero.hud.minimap.radar.render.element.RadarRenderContext context) {
        end(MinimapElementRenderLocation.fromIndex(location), context);
    }

    @Override // xaero.hud.minimap.radar.render.element.RadarRenderProvider, xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public void begin(MinimapElementRenderLocation location, xaero.hud.minimap.radar.render.element.RadarRenderContext context) {
        super.begin(location, context);
    }

    @Override // xaero.hud.minimap.radar.render.element.RadarRenderProvider, xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public boolean hasNext(MinimapElementRenderLocation location, xaero.hud.minimap.radar.render.element.RadarRenderContext context) {
        return super.hasNext(location, context);
    }

    @Override // xaero.hud.minimap.radar.render.element.RadarRenderProvider, xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public Entity setupContextAndGetNext(MinimapElementRenderLocation location, xaero.hud.minimap.radar.render.element.RadarRenderContext context) {
        return super.setupContextAndGetNext(location, context);
    }

    @Override // xaero.hud.minimap.radar.render.element.RadarRenderProvider, xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public Entity getNext(MinimapElementRenderLocation location, xaero.hud.minimap.radar.render.element.RadarRenderContext context) {
        return super.getNext(location, context);
    }

    @Override // xaero.hud.minimap.radar.render.element.RadarRenderProvider, xaero.common.minimap.element.render.MinimapElementRenderProvider, xaero.hud.minimap.element.render.MinimapElementRenderProvider
    @Deprecated
    public void end(MinimapElementRenderLocation location, xaero.hud.minimap.radar.render.element.RadarRenderContext context) {
        super.end(location, context);
    }
}
