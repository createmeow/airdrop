package xaero.hud.minimap.player.tracker;

import java.util.Iterator;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.element.render.MinimapElementRenderProvider;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/player/tracker/PlayerTrackerMinimapElementRenderProvider.class */
public class PlayerTrackerMinimapElementRenderProvider<C> extends MinimapElementRenderProvider<PlayerTrackerMinimapElement<?>, C> {
    private PlayerTrackerMinimapElementCollector collector;
    private Iterator<PlayerTrackerMinimapElement<?>> iterator;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public /* bridge */ /* synthetic */ PlayerTrackerMinimapElement<?> getNext(MinimapElementRenderLocation minimapElementRenderLocation, Object obj) {
        return getNext(minimapElementRenderLocation, (MinimapElementRenderLocation) obj);
    }

    public PlayerTrackerMinimapElementRenderProvider(PlayerTrackerMinimapElementCollector collector) {
        this.collector = collector;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public void begin(MinimapElementRenderLocation location, C context) {
        this.iterator = this.collector.getElements().iterator();
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public boolean hasNext(MinimapElementRenderLocation location, C context) {
        return this.iterator != null && this.iterator.hasNext();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public PlayerTrackerMinimapElement<?> getNext(MinimapElementRenderLocation location, C context) {
        return this.iterator.next();
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderProvider
    public void end(MinimapElementRenderLocation location, C context) {
        this.iterator = null;
    }
}
