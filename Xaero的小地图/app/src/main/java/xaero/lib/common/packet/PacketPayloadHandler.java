package xaero.lib.common.packet;

import javax.annotation.Nonnull;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import xaero.lib.common.packet.payload.PacketPayload;
import xaero.lib.common.packet.type.PacketType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/PacketPayloadHandler.class */
public class PacketPayloadHandler implements IPayloadHandler<PacketPayload<?>> {
    public void handle(@Nonnull PacketPayload<?> payload, @Nonnull IPayloadContext context) {
        handleTyped(payload, context);
    }

    private <P> void handleTyped(PacketPayload<P> payload, IPayloadContext context) {
        P packet;
        PacketType<P> packetType = payload.getPacketType();
        if (packetType == null || (packet = payload.getPacket()) == null) {
            return;
        }
        if (context.flow() == PacketFlow.CLIENTBOUND) {
            if (packetType.getClientHandler() == null) {
                return;
            }
            context.enqueueWork(() -> {
                packetType.getClientHandler().accept(packet);
            });
        } else {
            if (packetType.getServerHandler() == null || context.flow() != PacketFlow.SERVERBOUND) {
                return;
            }
            ServerPlayer player = context.player();
            context.enqueueWork(() -> {
                packetType.getServerHandler().accept(packet, player);
            });
        }
    }
}
