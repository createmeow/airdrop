package xaero.common.minimap.radar.tracker.system;

import java.util.HashMap;
import java.util.Map;
import xaero.common.minimap.radar.tracker.system.compat.PlayerTrackerSystemWrapper;
import xaero.hud.minimap.player.tracker.system.IRenderedPlayerTracker;
import xaero.hud.minimap.player.tracker.system.RenderedPlayerTrackerManager;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/radar/tracker/system/PlayerTrackerSystemManager.class */
public class PlayerTrackerSystemManager extends RenderedPlayerTrackerManager {
    private final Map<String, IPlayerTrackerSystem<?>> compatibleSystems = new HashMap();

    @Deprecated
    public void register(String name, IPlayerTrackerSystem<?> system) {
        register(name, (IRenderedPlayerTracker<?>) system);
    }

    @Override // xaero.hud.minimap.player.tracker.system.RenderedPlayerTrackerManager
    public void register(String name, IRenderedPlayerTracker<?> system) {
        IPlayerTrackerSystem<?> compatibleSystem;
        super.register(name, system);
        if (this.compatibleSystems.containsKey(name)) {
            return;
        }
        if (system instanceof IPlayerTrackerSystem) {
            compatibleSystem = (IPlayerTrackerSystem) system;
        } else {
            compatibleSystem = new PlayerTrackerSystemWrapper<>(system);
        }
        this.compatibleSystems.put(name, compatibleSystem);
    }

    @Deprecated
    public Iterable<IPlayerTrackerSystem<?>> getSystems() {
        return this.compatibleSystems.values();
    }
}
