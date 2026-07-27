package xaero.lib.common.packet;

import net.minecraft.util.thread.ReentrantBlockableEventLoop;
import xaero.lib.common.packet.payload.PacketPayload;
import xaero.lib.common.packet.type.PacketType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/PacketReceiver.class */
public abstract class PacketReceiver<C> {
    private final PacketHandlerFull packetHandlerFull;

    protected abstract <T> boolean isCorrectSide(PacketType<T> packetType);

    protected abstract <T> Runnable getTask(PacketType<T> packetType, T t, C c);

    public PacketReceiver(PacketHandlerFull packetHandlerFull) {
        this.packetHandlerFull = packetHandlerFull;
    }

    protected <T> void receive(ReentrantBlockableEventLoop<?> executor, PacketPayload<T> payload, C context) {
        PacketType<T> packetType = payload.getPacketType();
        if (packetType == null || !isCorrectSide(packetType)) {
            return;
        }
        T packet = payload.getPacket();
        if (executor.isSameThread()) {
            getTask(packetType, packet, context).run();
        } else {
            executor.execute(getTask(packetType, packet, context));
        }
    }
}
