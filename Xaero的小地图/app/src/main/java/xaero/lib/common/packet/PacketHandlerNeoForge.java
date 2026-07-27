package xaero.lib.common.packet;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import xaero.lib.common.packet.payload.PacketPayloadCodec;
import xaero.lib.common.packet.type.PacketTypeManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/PacketHandlerNeoForge.class */
public class PacketHandlerNeoForge extends PacketHandlerFull {
    private final String protocolVersion;

    private PacketHandlerNeoForge(ResourceLocation channelId, String protocolVersion) {
        super(channelId, PacketTypeManager.Builder.begin().build(), new CustomPacketPayload.Type(channelId));
        this.protocolVersion = protocolVersion;
    }

    public void registerPayloadHandler(RegisterPayloadHandlersEvent event) {
        event.registrar(this.channelId.getNamespace()).versioned(this.protocolVersion).optional().playBidirectional(this.type, new PacketPayloadCodec(this), new PacketPayloadHandler());
    }

    @Override // xaero.lib.common.packet.IPacketHandler
    public <P> void sendToServer(P packet) {
        PacketDistributor.sendToServer(createPayload(packet), new CustomPacketPayload[0]);
    }

    @Override // xaero.lib.common.packet.IPacketHandler
    public <P> void sendToPlayer(ServerPlayer player, P packet) {
        if (!player.connection.hasChannel(this.channelId)) {
            return;
        }
        PacketDistributor.sendToPlayer(player, createPayload(packet), new CustomPacketPayload[0]);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/PacketHandlerNeoForge$Builder.class */
    public static final class Builder {
        private ResourceLocation channelId;
        private String protocolVersion;

        private Builder() {
        }

        public Builder setDefault() {
            setChannelId(null);
            setProtocolVersion(null);
            return this;
        }

        public Builder setChannelId(ResourceLocation channelId) {
            this.channelId = channelId;
            return this;
        }

        public Builder setProtocolVersion(String protocolVersion) {
            this.protocolVersion = protocolVersion;
            return this;
        }

        public PacketHandlerNeoForge build() {
            if (this.channelId == null || this.protocolVersion == null) {
                throw new IllegalStateException();
            }
            return new PacketHandlerNeoForge(this.channelId, this.protocolVersion);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
