package xaero.lib.common.packet;

import io.netty.buffer.Unpooled;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import xaero.lib.common.packet.payload.PacketPayload;
import xaero.lib.common.packet.type.PacketType;
import xaero.lib.common.packet.type.PacketTypeManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/PacketHandlerFull.class */
public abstract class PacketHandlerFull implements IPacketHandler {
    protected final ResourceLocation channelId;
    protected final PacketTypeManager packetTypeManager;
    protected final CustomPacketPayload.Type<PacketPayload<?>> type;

    protected PacketHandlerFull(ResourceLocation channelId, PacketTypeManager packetTypeManager, CustomPacketPayload.Type<PacketPayload<?>> type) {
        this.channelId = channelId;
        this.packetTypeManager = packetTypeManager;
        this.type = type;
    }

    @Override // xaero.lib.common.packet.IPacketHandler
    public <P> void register(int index, Class<P> type, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, BiConsumer<P, ServerPlayer> serverHandler, Consumer<P> clientHandler) {
        this.packetTypeManager.register(index, type, encoder, decoder, serverHandler, clientHandler);
    }

    public static <T> void encodePacket(PacketType<T> packetType, T packet, FriendlyByteBuf buffer) {
        if (packetType == null) {
            throw new IllegalArgumentException("unregistered packet class!");
        }
        buffer.writeByte(packetType.getIndex());
        packetType.getEncoder().accept(packet, buffer);
    }

    <T> FriendlyByteBuf getPacketBuffer(T packet) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        encodePacket(this.packetTypeManager.getType(packet), packet, buffer);
        return buffer;
    }

    public PacketType<?> getPacketTypeByIndex(int index) {
        return this.packetTypeManager.getByIndex(index);
    }

    public PacketType<?> getByIndex(int index) {
        return this.packetTypeManager.getByIndex(index);
    }

    public <T> PacketPayload<T> createPayload(T packet) {
        return new PacketPayload<>(this.packetTypeManager.getType(packet), packet, this.type);
    }

    @Override // xaero.lib.common.packet.IPacketHandler
    public ResourceLocation getChannelId() {
        return this.channelId;
    }

    public CustomPacketPayload.Type<PacketPayload<?>> getType() {
        return this.type;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/PacketHandlerFull$Builder.class */
    public static abstract class Builder<B extends Builder<B>> {
        private final B self = this;
        protected ResourceLocation channelId;

        protected abstract PacketHandlerFull buildInternal(PacketTypeManager packetTypeManager, CustomPacketPayload.Type<PacketPayload<?>> type);

        protected Builder() {
        }

        public B setDefault() {
            setChannelId(null);
            return this.self;
        }

        public B setChannelId(ResourceLocation channelId) {
            this.channelId = channelId;
            return this.self;
        }

        public PacketHandlerFull build() {
            if (this.channelId == null) {
                throw new IllegalStateException();
            }
            PacketTypeManager packetTypeManager = PacketTypeManager.Builder.begin().build();
            CustomPacketPayload.Type<PacketPayload<?>> type = new CustomPacketPayload.Type<>(this.channelId);
            return buildInternal(packetTypeManager, type);
        }
    }
}
