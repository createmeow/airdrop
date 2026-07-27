package xaero.lib.common.packet;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import xaero.lib.common.player.ServerPlayerData;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/IPacketHandler.class */
public interface IPacketHandler {
    ResourceLocation getChannelId();

    <P> void register(int i, Class<P> cls, BiConsumer<P, FriendlyByteBuf> biConsumer, Function<FriendlyByteBuf, P> function, BiConsumer<P, ServerPlayer> biConsumer2, Consumer<P> consumer);

    <T> void sendToServer(T t);

    <T> void sendToPlayer(ServerPlayer serverPlayer, T t);

    default <T> void sendToPlayer(ServerPlayerData playerData, T packet) {
        sendToPlayer(playerData.getPlayer(), (ServerPlayer) packet);
    }
}
