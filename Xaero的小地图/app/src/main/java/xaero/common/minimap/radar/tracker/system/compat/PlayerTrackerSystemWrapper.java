package xaero.common.minimap.radar.tracker.system.compat;

import java.util.Iterator;
import xaero.common.minimap.radar.tracker.system.IPlayerTrackerSystem;
import xaero.common.minimap.radar.tracker.system.ITrackedPlayerReader;
import xaero.hud.minimap.player.tracker.system.IRenderedPlayerTracker;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/radar/tracker/system/compat/PlayerTrackerSystemWrapper.class */
public class PlayerTrackerSystemWrapper<P> implements IPlayerTrackerSystem<P> {
    private final IRenderedPlayerTracker<P> original;
    private final TrackedPlayerReaderWrapper<P> readerWrapper;

    public PlayerTrackerSystemWrapper(IRenderedPlayerTracker<P> original) {
        this.original = original;
        this.readerWrapper = new TrackedPlayerReaderWrapper<>(original.getReader());
    }

    @Override // xaero.hud.minimap.player.tracker.system.IRenderedPlayerTracker
    public ITrackedPlayerReader<P> getReader() {
        return this.readerWrapper;
    }

    @Override // xaero.hud.minimap.player.tracker.system.IRenderedPlayerTracker
    public Iterator<P> getTrackedPlayerIterator() {
        return this.original.getTrackedPlayerIterator();
    }
}
