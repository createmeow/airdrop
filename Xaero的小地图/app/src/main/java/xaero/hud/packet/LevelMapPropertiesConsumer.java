package xaero.hud.packet;

import java.util.function.Consumer;
import xaero.common.server.level.LevelMapProperties;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.module.MinimapSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/packet/LevelMapPropertiesConsumer.class */
public class LevelMapPropertiesConsumer implements Consumer<LevelMapProperties> {
    @Override // java.util.function.Consumer
    public void accept(LevelMapProperties t) {
        MinimapSession minimapSession = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        minimapSession.getWorldStateUpdater().onServerLevelId(t.getId());
    }
}
