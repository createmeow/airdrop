package xaero.lib.common.packet;

import java.util.function.Consumer;
import net.minecraft.network.FriendlyByteBuf;
import xaero.lib.XaeroLib;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/ClientboundServerHandshakePacket.class */
public class ClientboundServerHandshakePacket {
    public static ClientboundServerHandshakePacket read(FriendlyByteBuf buf) {
        try {
            buf.readByte();
            return new ClientboundServerHandshakePacket();
        } catch (Throwable t) {
            if (Services.PLATFORM.isDevelopmentEnvironment() || !Services.PLATFORM.isDedicatedServer()) {
                XaeroLib.LOGGER.error("packet error", t);
                return null;
            }
            return null;
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeByte(1);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/ClientboundServerHandshakePacket$ClientHandler.class */
    public static final class ClientHandler implements Consumer<ClientboundServerHandshakePacket> {
        @Override // java.util.function.Consumer
        public void accept(ClientboundServerHandshakePacket packet) {
            try {
                XaeroLib.INSTANCE.getClient().getConfigSynchronizer().reset();
            } catch (Throwable t) {
                XaeroLib.LOGGER.error("packet error", t);
            }
        }
    }
}
