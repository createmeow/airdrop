package xaero.hud.packet;

import xaero.common.server.level.LevelMapProperties;
import xaero.hud.packet.basic.ClientboundRulesPacket;
import xaero.hud.packet.basic.HandshakePacket;
import xaero.hud.packet.tracker.ClientboundPlayerTrackerResetPacket;
import xaero.hud.packet.tracker.ClientboundTrackedPlayerPacket;
import xaero.lib.common.packet.IPacketHandler;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/packet/MinimapPacketRegister.class */
public class MinimapPacketRegister {
    public void register(IPacketHandler messageHandler) {
        messageHandler.register(0, LevelMapProperties.class, (v0, v1) -> {
            v0.write(v1);
        }, LevelMapProperties::read, null, new LevelMapPropertiesConsumer());
        messageHandler.register(1, HandshakePacket.class, (v0, v1) -> {
            v0.write(v1);
        }, HandshakePacket::read, new HandshakePacket.ServerHandler(), new HandshakePacket.ClientHandler());
        messageHandler.register(2, ClientboundTrackedPlayerPacket.class, (v0, v1) -> {
            v0.write(v1);
        }, ClientboundTrackedPlayerPacket::read, null, new ClientboundTrackedPlayerPacket.Handler());
        messageHandler.register(3, ClientboundPlayerTrackerResetPacket.class, (v0, v1) -> {
            v0.write(v1);
        }, ClientboundPlayerTrackerResetPacket::read, null, new ClientboundPlayerTrackerResetPacket.Handler());
        messageHandler.register(4, ClientboundRulesPacket.class, (v0, v1) -> {
            v0.write(v1);
        }, ClientboundRulesPacket::read, null, new ClientboundRulesPacket.ClientHandler());
    }
}
