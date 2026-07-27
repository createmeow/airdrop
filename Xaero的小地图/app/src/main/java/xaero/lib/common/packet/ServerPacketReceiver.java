package xaero.lib.common.packet;

import net.minecraft.server.level.ServerPlayer;
import xaero.lib.common.packet.type.PacketType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/ServerPacketReceiver.class */
public class ServerPacketReceiver extends PacketReceiver<ServerPlayer> {
    @Override // xaero.lib.common.packet.PacketReceiver
    protected /* bridge */ /* synthetic */ Runnable getTask(PacketType packetType, Object obj, ServerPlayer serverPlayer) {
        return getTask2((PacketType<PacketType>) packetType, (PacketType) obj, serverPlayer);
    }

    public ServerPacketReceiver(PacketHandlerFull packetHandlerFull) {
        super(packetHandlerFull);
    }

    @Override // xaero.lib.common.packet.PacketReceiver
    protected <T> boolean isCorrectSide(PacketType<T> packetType) {
        return packetType.getServerHandler() != null;
    }

    /* renamed from: getTask, reason: avoid collision after fix types in other method */
    protected <T> Runnable getTask2(PacketType<T> packetType, T packet, ServerPlayer player) {
        return () -> {
            packetType.getServerHandler().accept(packet, player);
        };
    }
}
