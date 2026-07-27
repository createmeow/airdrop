package xaero.common.minimap.mcworld;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import xaero.hud.packet.basic.ClientboundRulesPacket;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/mcworld/MinimapClientWorldData.class */
public class MinimapClientWorldData {
    private int serverModNetworkVersion;
    public Integer serverLevelId;
    public float shadowR;
    public float shadowG;
    public float shadowB;
    private ClientboundRulesPacket syncedRules;

    public MinimapClientWorldData(ClientLevel world) {
        this.shadowR = 1.0f;
        this.shadowG = 1.0f;
        this.shadowB = 1.0f;
        if (world.dimensionType().effectsLocation().equals(BuiltinDimensionTypes.OVERWORLD_EFFECTS)) {
            this.shadowR = 0.518f;
            this.shadowG = 0.678f;
            this.shadowB = 1.0f;
        } else if (world.dimensionType().effectsLocation().equals(BuiltinDimensionTypes.NETHER_EFFECTS)) {
            this.shadowR = 1.0f;
            this.shadowG = 0.0f;
            this.shadowB = 0.0f;
        }
    }

    public void setServerModNetworkVersion(int serverModNetworkVersion) {
        this.serverModNetworkVersion = serverModNetworkVersion;
    }

    public int getServerModNetworkVersion() {
        return this.serverModNetworkVersion;
    }

    public void setSyncedRules(ClientboundRulesPacket syncedRules) {
        this.syncedRules = syncedRules;
    }

    public ClientboundRulesPacket getSyncedRules() {
        if (this.syncedRules == null) {
            this.syncedRules = new ClientboundRulesPacket(true, true, true);
        }
        return this.syncedRules;
    }
}
