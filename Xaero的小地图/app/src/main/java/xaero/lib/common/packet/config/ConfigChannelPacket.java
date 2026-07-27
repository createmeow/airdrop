package xaero.lib.common.packet.config;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import xaero.lib.XaeroLib;
import xaero.lib.common.player.ServerPlayerData;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/ConfigChannelPacket.class */
public class ConfigChannelPacket {
    private final ResourceLocation channelId;

    public ConfigChannelPacket(ResourceLocation channelId) {
        this.channelId = channelId;
    }

    public void write(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        tag.putString("c", this.channelId.toString());
        buf.writeNbt(tag);
    }

    public static ConfigChannelPacket read(FriendlyByteBuf buf) {
        try {
            CompoundTag tag = buf.readNbt(NbtAccounter.create(65536L));
            String channelIdString = tag.getString("c");
            return new ConfigChannelPacket(ResourceLocation.parse(channelIdString));
        } catch (Throwable t) {
            if (Services.PLATFORM.isDevelopmentEnvironment() || !Services.PLATFORM.isDedicatedServer()) {
                XaeroLib.LOGGER.error("packet error", t);
                return null;
            }
            return null;
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/ConfigChannelPacket$ClientHandler.class */
    public static final class ClientHandler implements Consumer<ConfigChannelPacket> {
        @Override // java.util.function.Consumer
        public void accept(ConfigChannelPacket packet) {
            try {
                XaeroLib.INSTANCE.getClient().getConfigSynchronizer().receiveChannelId(packet.channelId);
            } catch (Throwable t) {
                XaeroLib.LOGGER.error("packet error", t);
            }
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/ConfigChannelPacket$ServerHandler.class */
    public static final class ServerHandler implements BiConsumer<ConfigChannelPacket, ServerPlayer> {
        @Override // java.util.function.BiConsumer
        public void accept(ConfigChannelPacket packet, ServerPlayer player) {
            try {
                ServerPlayerData.get(player).getConfigSynchronizer().receiveChannelId(packet.channelId);
            } catch (Throwable t) {
                ServerPlayerData.get(player).getConfigSynchronizer().receiveChannelId(null);
                if (Services.PLATFORM.isDevelopmentEnvironment()) {
                    XaeroLib.LOGGER.error("packet error", t);
                }
            }
        }
    }
}
