package xaero.lib.common.packet;

import xaero.lib.XaeroLib;
import xaero.lib.common.packet.ClientboundDimensionHandshakePacket;
import xaero.lib.common.packet.ClientboundServerHandshakePacket;
import xaero.lib.common.packet.config.ConfigPacketRegister;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/PacketRegister.class */
public class PacketRegister {
    public void register() {
        IPacketHandler packetHandler = XaeroLib.INSTANCE.getPacketHandler();
        int nextIndex = 0 + 1;
        packetHandler.register(0, ClientboundServerHandshakePacket.class, (v0, v1) -> {
            v0.write(v1);
        }, ClientboundServerHandshakePacket::read, null, new ClientboundServerHandshakePacket.ClientHandler());
        packetHandler.register(nextIndex, ClientboundDimensionHandshakePacket.class, (v0, v1) -> {
            v0.write(v1);
        }, ClientboundDimensionHandshakePacket::read, null, new ClientboundDimensionHandshakePacket.ClientHandler());
        new ConfigPacketRegister().register(packetHandler, nextIndex + 1);
    }
}
