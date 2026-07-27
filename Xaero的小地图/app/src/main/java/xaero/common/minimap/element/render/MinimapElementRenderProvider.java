package xaero.common.minimap.element.render;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/element/render/MinimapElementRenderProvider.class */
public abstract class MinimapElementRenderProvider<E, RC> extends xaero.hud.minimap.element.render.MinimapElementRenderProvider<E, RC> {
    @Deprecated
    public abstract void begin(int i, RC rc);

    @Deprecated
    public abstract boolean hasNext(int i, RC rc);

    @Deprecated
    public abstract E getNext(int i, RC rc);

    @Deprecated
    public abstract void end(int i, RC rc);

    @Deprecated
    public E setupContextAndGetNext(int i, RC rc) {
        return (E) super.setupContextAndGetNext(xaero.hud.minimap.element.render.MinimapElementRenderLocation.fromIndex(i), (xaero.hud.minimap.element.render.MinimapElementRenderLocation) rc);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public void begin(xaero.hud.minimap.element.render.MinimapElementRenderLocation location, RC context) {
        begin(location.getIndex(), (int) context);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public boolean hasNext(xaero.hud.minimap.element.render.MinimapElementRenderLocation location, RC context) {
        return hasNext(location.getIndex(), (int) context);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public E getNext(xaero.hud.minimap.element.render.MinimapElementRenderLocation location, RC context) {
        return getNext(location.getIndex(), (int) context);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public E setupContextAndGetNext(xaero.hud.minimap.element.render.MinimapElementRenderLocation location, RC context) {
        return setupContextAndGetNext(location.getIndex(), (int) context);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public void end(xaero.hud.minimap.element.render.MinimapElementRenderLocation location, RC context) {
        end(location.getIndex(), (int) context);
    }
}
