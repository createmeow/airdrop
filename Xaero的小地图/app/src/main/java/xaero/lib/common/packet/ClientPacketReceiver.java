package xaero.lib.common.packet;

import xaero.lib.common.packet.type.PacketType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/ClientPacketReceiver.class */
public abstract class ClientPacketReceiver extends PacketReceiver<Object> {
    public ClientPacketReceiver(PacketHandlerFull packetHandlerFull) {
        super(packetHandlerFull);
    }

    @Override // xaero.lib.common.packet.PacketReceiver
    protected <T> boolean isCorrectSide(PacketType<T> packetType) {
        return packetType.getClientHandler() != null;
    }

    @Override // xaero.lib.common.packet.PacketReceiver
    protected <T> Runnable getTask(PacketType<T> packetType, T packet, Object context) {
        return () -> {
            packetType.getClientHandler().accept(packet);
        };
    }
}
