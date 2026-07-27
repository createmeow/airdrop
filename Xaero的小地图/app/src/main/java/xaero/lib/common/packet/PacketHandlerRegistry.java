package xaero.lib.common.packet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/PacketHandlerRegistry.class */
public final class PacketHandlerRegistry implements Iterable<IPacketHandler> {
    public static final PacketHandlerRegistry INSTANCE = Builder.begin().build();
    private final Map<ResourceLocation, IPacketHandler> packetHandlers;
    private boolean frozen;

    private PacketHandlerRegistry(Map<ResourceLocation, IPacketHandler> packetHandlers) {
        this.packetHandlers = packetHandlers;
    }

    public synchronized void freeze() {
        this.frozen = true;
    }

    public synchronized IPacketHandler register(ResourceLocation channelId, int protocolVersion, String protocolVersionString) {
        if (this.frozen) {
            throw new IllegalStateException();
        }
        if (this.packetHandlers.containsKey(channelId)) {
            throw new IllegalArgumentException("duplicate packet channel used: " + String.valueOf(channelId));
        }
        IPacketHandler packetHandler = Services.PLATFORM.createPacketHandler(channelId, protocolVersion, protocolVersionString);
        this.packetHandlers.put(channelId, packetHandler);
        return packetHandler;
    }

    @Override // java.lang.Iterable
    public Iterator<IPacketHandler> iterator() {
        if (!this.frozen) {
            throw new IllegalStateException();
        }
        return Collections.unmodifiableCollection(this.packetHandlers.values()).iterator();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/PacketHandlerRegistry$Builder.class */
    private static final class Builder {
        private Builder() {
        }

        public Builder setDefault() {
            return this;
        }

        public PacketHandlerRegistry build() {
            return new PacketHandlerRegistry(new HashMap());
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
