package xaero.lib.common.packet.type;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/type/PacketTypeManager.class */
public class PacketTypeManager {
    private final Int2ObjectOpenHashMap<PacketType<?>> index2Type;
    private final Map<Class<?>, PacketType<?>> class2Type;

    private PacketTypeManager(Int2ObjectOpenHashMap<PacketType<?>> index2Type, Map<Class<?>, PacketType<?>> class2Type) {
        this.index2Type = index2Type;
        this.class2Type = class2Type;
    }

    public <P> void register(int index, Class<P> type, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, BiConsumer<P, ServerPlayer> serverHandler, Consumer<P> clientHandler) {
        PacketType<?> packetType = new PacketType<>(index, type, encoder, decoder, serverHandler, clientHandler);
        if (this.index2Type.containsKey(index)) {
            throw new IllegalArgumentException("duplicate index!");
        }
        if (this.class2Type.containsKey(type)) {
            throw new IllegalArgumentException("duplicate packet class!");
        }
        this.index2Type.put(index, packetType);
        this.class2Type.put(type, packetType);
    }

    public PacketType<?> getByIndex(int index) {
        return (PacketType) this.index2Type.get(index);
    }

    public PacketType<?> getByClass(Class<?> clazz) {
        return this.class2Type.get(clazz);
    }

    public <P> PacketType<P> getType(P message) {
        return (PacketType) this.class2Type.get(message.getClass());
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/type/PacketTypeManager$Builder.class */
    public static final class Builder {
        private Builder() {
        }

        private Builder setDefault() {
            return this;
        }

        public PacketTypeManager build() {
            return new PacketTypeManager(new Int2ObjectOpenHashMap(), new HashMap());
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
