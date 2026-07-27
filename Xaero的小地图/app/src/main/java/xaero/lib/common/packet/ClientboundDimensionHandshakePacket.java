package xaero.lib.common.packet;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import xaero.lib.XaeroLib;
import xaero.lib.client.level.ClientLevelData;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/ClientboundDimensionHandshakePacket.class */
public class ClientboundDimensionHandshakePacket {
    public static ClientboundDimensionHandshakePacket read(FriendlyByteBuf buf) {
        try {
            buf.readByte();
            return new ClientboundDimensionHandshakePacket();
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

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/ClientboundDimensionHandshakePacket$ClientHandler.class */
    public static final class ClientHandler implements Consumer<ClientboundDimensionHandshakePacket> {
        @Override // java.util.function.Consumer
        public void accept(ClientboundDimensionHandshakePacket packet) {
            try {
                ClientLevelData.get(Minecraft.getInstance().level).setServerHasMod();
            } catch (Throwable t) {
                XaeroLib.LOGGER.error("packet error", t);
            }
        }
    }
}
