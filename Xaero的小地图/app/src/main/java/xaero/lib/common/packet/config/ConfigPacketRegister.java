package xaero.lib.common.packet.config;

import xaero.lib.common.packet.IPacketHandler;
import xaero.lib.common.packet.config.ClientboundEnforcedConfigPacket;
import xaero.lib.common.packet.config.ConfigChannelPacket;
import xaero.lib.common.packet.config.profile.ConfigProfileCreatePacket;
import xaero.lib.common.packet.config.profile.ConfigProfileDeletePacket;
import xaero.lib.common.packet.config.profile.ConfigProfileEditPacket;
import xaero.lib.common.packet.config.profile.ConfigProfileInfoPacket;
import xaero.lib.common.packet.config.profile.ServerboundConfigProfileRequestPacket;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/ConfigPacketRegister.class */
public class ConfigPacketRegister {
    public int register(IPacketHandler packetHandler, int nextIndex) {
        int nextIndex2 = nextIndex + 1;
        packetHandler.register(nextIndex, ConfigChannelPacket.class, (v0, v1) -> {
            v0.write(v1);
        }, ConfigChannelPacket::read, new ConfigChannelPacket.ServerHandler(), new ConfigChannelPacket.ClientHandler());
        int nextIndex3 = nextIndex2 + 1;
        packetHandler.register(nextIndex2, ClientboundEnforcedConfigPacket.class, (v0, v1) -> {
            v0.write(v1);
        }, ClientboundEnforcedConfigPacket::read, null, new ClientboundEnforcedConfigPacket.ClientHandler());
        int nextIndex4 = nextIndex3 + 1;
        packetHandler.register(nextIndex3, ConfigProfileEditPacket.class, (v0, v1) -> {
            v0.write(v1);
        }, ConfigProfileEditPacket::read, new ConfigProfileEditPacket.ServerHandler(), new ConfigProfileEditPacket.ClientHandler());
        int nextIndex5 = nextIndex4 + 1;
        packetHandler.register(nextIndex4, ConfigProfileInfoPacket.class, (v0, v1) -> {
            v0.write(v1);
        }, ConfigProfileInfoPacket::read, new ConfigProfileInfoPacket.ServerHandler(), new ConfigProfileInfoPacket.ClientHandler());
        int nextIndex6 = nextIndex5 + 1;
        packetHandler.register(nextIndex5, ServerboundConfigProfileRequestPacket.class, (v0, v1) -> {
            v0.write(v1);
        }, ServerboundConfigProfileRequestPacket::read, new ServerboundConfigProfileRequestPacket.ServerHandler(), null);
        int nextIndex7 = nextIndex6 + 1;
        packetHandler.register(nextIndex6, ConfigProfileDeletePacket.class, (v0, v1) -> {
            v0.write(v1);
        }, ConfigProfileDeletePacket::read, new ConfigProfileDeletePacket.ServerHandler(), new ConfigProfileDeletePacket.ClientHandler());
        int nextIndex8 = nextIndex7 + 1;
        packetHandler.register(nextIndex7, ConfigProfileCreatePacket.class, (v0, v1) -> {
            v0.write(v1);
        }, ConfigProfileCreatePacket::read, new ConfigProfileCreatePacket.ServerHandler(), null);
        return nextIndex8;
    }
}
