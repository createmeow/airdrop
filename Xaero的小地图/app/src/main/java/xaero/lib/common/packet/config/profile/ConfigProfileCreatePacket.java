package xaero.lib.common.packet.config.profile;

import java.util.function.BiConsumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import xaero.lib.XaeroLib;
import xaero.lib.common.player.ServerPlayerData;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/profile/ConfigProfileCreatePacket.class */
public final class ConfigProfileCreatePacket {
    private final String id;
    private final String name;
    private final String profileToCopy;

    public ConfigProfileCreatePacket(String id, String name, String profileToCopy) {
        this.id = id;
        this.name = name;
        this.profileToCopy = profileToCopy;
    }

    public void write(FriendlyByteBuf buffer) {
        CompoundTag tag = new CompoundTag();
        tag.putString("i", this.id);
        tag.putString("n", this.name);
        tag.putString("c", this.profileToCopy);
        buffer.writeNbt(tag);
    }

    public static ConfigProfileCreatePacket read(FriendlyByteBuf buffer) {
        try {
            CompoundTag tag = buffer.readNbt(NbtAccounter.unlimitedHeap());
            if (tag == null) {
                return null;
            }
            String id = tag.getString("i");
            String name = tag.getString("n");
            String profileToCopy = tag.getString("c");
            return new ConfigProfileCreatePacket(id, name, profileToCopy);
        } catch (Throwable t) {
            if (Services.PLATFORM.isDevelopmentEnvironment() || !Services.PLATFORM.isDedicatedServer()) {
                XaeroLib.LOGGER.error("packet error", t);
                return null;
            }
            return null;
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/profile/ConfigProfileCreatePacket$ServerHandler.class */
    public static final class ServerHandler implements BiConsumer<ConfigProfileCreatePacket, ServerPlayer> {
        @Override // java.util.function.BiConsumer
        public void accept(ConfigProfileCreatePacket packet, ServerPlayer player) {
            try {
                ServerPlayerData.get(player).getConfigSynchronizer().onCreateConfigProfilePacket(packet.id, packet.name, packet.profileToCopy);
            } catch (Throwable t) {
                if (Services.PLATFORM.isDevelopmentEnvironment()) {
                    XaeroLib.LOGGER.error("packet error", t);
                }
            }
        }
    }
}
