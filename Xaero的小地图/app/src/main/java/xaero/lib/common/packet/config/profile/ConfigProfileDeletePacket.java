package xaero.lib.common.packet.config.profile;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import xaero.lib.XaeroLib;
import xaero.lib.common.player.ServerPlayerData;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/profile/ConfigProfileDeletePacket.class */
public class ConfigProfileDeletePacket {
    private final String profileId;

    public ConfigProfileDeletePacket(String profileId) {
        this.profileId = profileId;
    }

    public void write(FriendlyByteBuf buffer) {
        CompoundTag tag = new CompoundTag();
        tag.putString("i", this.profileId);
        buffer.writeNbt(tag);
    }

    public static ConfigProfileDeletePacket read(FriendlyByteBuf buffer) {
        try {
            CompoundTag tag = buffer.readNbt(NbtAccounter.create(65536L));
            if (tag == null) {
                return null;
            }
            String profileId = tag.getString("i");
            return new ConfigProfileDeletePacket(profileId);
        } catch (Throwable t) {
            if (Services.PLATFORM.isDevelopmentEnvironment()) {
                XaeroLib.LOGGER.error("packet error", t);
                return null;
            }
            return null;
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/profile/ConfigProfileDeletePacket$ServerHandler.class */
    public static final class ServerHandler implements BiConsumer<ConfigProfileDeletePacket, ServerPlayer> {
        @Override // java.util.function.BiConsumer
        public void accept(ConfigProfileDeletePacket packet, ServerPlayer player) {
            ServerPlayerData.get(player).getConfigSynchronizer().onDeleteConfigProfilePacket(packet.profileId);
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/profile/ConfigProfileDeletePacket$ClientHandler.class */
    public static final class ClientHandler implements Consumer<ConfigProfileDeletePacket> {
        @Override // java.util.function.Consumer
        public void accept(ConfigProfileDeletePacket packet) {
            XaeroLib.INSTANCE.getClient().getConfigSynchronizer().onDeleteConfigProfilePacket(packet.profileId);
        }
    }
}
