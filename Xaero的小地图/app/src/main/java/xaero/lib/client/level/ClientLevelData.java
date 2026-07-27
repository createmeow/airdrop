package xaero.lib.client.level;

import net.minecraft.client.multiplayer.ClientLevel;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/level/ClientLevelData.class */
public class ClientLevelData {
    private boolean serverHasMod;

    public boolean serverHasMod() {
        return this.serverHasMod;
    }

    public void setServerHasMod() {
        this.serverHasMod = true;
    }

    public static ClientLevelData get(ClientLevel level) {
        ClientLevelData result = ((IClientLevel) level).xaerolib_getData();
        if (result == null) {
            ClientLevelData clientLevelData = new ClientLevelData();
            result = clientLevelData;
            ((IClientLevel) level).xaerolib_setData(clientLevelData);
        }
        return result;
    }
}
