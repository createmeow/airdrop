package xaero.hud.minimap.player.tracker.system;

import java.util.Iterator;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/player/tracker/system/IRenderedPlayerTracker.class */
public interface IRenderedPlayerTracker<P> {
    ITrackedPlayerReader<P> getReader();

    Iterator<P> getTrackedPlayerIterator();
}
