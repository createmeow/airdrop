package xaero.hud.minimap.element.render;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/element/render/MinimapElementRenderProvider.class */
public abstract class MinimapElementRenderProvider<E, RC> {
    public abstract void begin(MinimapElementRenderLocation minimapElementRenderLocation, RC rc);

    public abstract boolean hasNext(MinimapElementRenderLocation minimapElementRenderLocation, RC rc);

    public abstract E getNext(MinimapElementRenderLocation minimapElementRenderLocation, RC rc);

    public abstract void end(MinimapElementRenderLocation minimapElementRenderLocation, RC rc);

    public E setupContextAndGetNext(MinimapElementRenderLocation location, RC context) {
        return getNext(location, context);
    }
}
