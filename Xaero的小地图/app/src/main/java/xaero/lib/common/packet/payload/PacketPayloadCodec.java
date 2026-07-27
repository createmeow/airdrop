package xaero.lib.common.packet.payload;

import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import xaero.lib.common.packet.PacketHandlerFull;
import xaero.lib.common.packet.type.PacketType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/payload/PacketPayloadCodec.class */
public class PacketPayloadCodec implements StreamCodec<FriendlyByteBuf, PacketPayload<?>> {
    private final PacketHandlerFull packetHandlerFull;

    public PacketPayloadCodec(PacketHandlerFull packetHandlerFull) {
        this.packetHandlerFull = packetHandlerFull;
    }

    public <T> void encodeTyped(FriendlyByteBuf buf, PacketPayload<T> payload) {
        PacketHandlerFull.encodePacket(payload.getPacketType(), payload.getPacket(), buf);
    }

    public void encode(FriendlyByteBuf buf, PacketPayload<?> packetPayload) {
        encodeTyped(buf, packetPayload);
    }

    @Nonnull
    public PacketPayload<?> decode(FriendlyByteBuf friendlyByteBuf) {
        if (friendlyByteBuf.readableBytes() <= 0) {
            return new PacketPayload<>(null, null, this.packetHandlerFull.getType());
        }
        int index = friendlyByteBuf.readByte();
        PacketType<?> packetType = this.packetHandlerFull.getByIndex(index);
        return readPacketPayloadTyped(packetType, friendlyByteBuf);
    }

    private <P> PacketPayload<P> readPacketPayloadTyped(PacketType<P> packetType, FriendlyByteBuf friendlyByteBuf) {
        return new PacketPayload<>(packetType, packetType.getDecoder().apply(friendlyByteBuf), this.packetHandlerFull.getType());
    }
}
