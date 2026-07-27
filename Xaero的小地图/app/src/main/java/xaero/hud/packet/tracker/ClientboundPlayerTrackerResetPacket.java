package xaero.hud.packet.tracker;

import java.util.function.Consumer;
import net.minecraft.network.FriendlyByteBuf;
import xaero.common.XaeroMinimapSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/packet/tracker/ClientboundPlayerTrackerResetPacket.class */
public class ClientboundPlayerTrackerResetPacket {
    public void write(FriendlyByteBuf buffer) {
    }

    public static ClientboundPlayerTrackerResetPacket read(FriendlyByteBuf buffer) {
        return new ClientboundPlayerTrackerResetPacket();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/packet/tracker/ClientboundPlayerTrackerResetPacket$Handler.class */
    public static class Handler implements Consumer<ClientboundPlayerTrackerResetPacket> {
        @Override // java.util.function.Consumer
        public void accept(ClientboundPlayerTrackerResetPacket t) {
            XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
            if (minimapSession == null) {
                return;
            }
            minimapSession.getMinimapProcessor().getSyncedTrackedPlayerManager().reset();
        }
    }
}
