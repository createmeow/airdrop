package xaero.common.server.mods;

import xaero.map.WorldMap;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/server/mods/SupportWorldMapServer.class */
public class SupportWorldMapServer {
    private final int compatibilityVersion = WorldMap.MINIMAP_COMPATIBILITY_VERSION;

    public boolean supportsTrackedPlayers() {
        return this.compatibilityVersion >= 22;
    }
}
