package xaero.lib.common.packet.config.profile;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import xaero.lib.XaeroLib;
import xaero.lib.common.packet.config.AbstractConfigPacket;
import xaero.lib.common.player.ServerPlayerData;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/profile/ConfigProfileEditPacket.class */
public final class ConfigProfileEditPacket extends AbstractConfigPacket<ConfigProfileEditPacket> {
    private final String profileId;

    public ConfigProfileEditPacket(String profileId, List<AbstractConfigPacket.OptionEntry> entries) {
        super(entries);
        this.profileId = profileId;
    }

    @Override // xaero.lib.common.packet.config.AbstractConfigPacket
    protected void writeExtra(CompoundTag tag) {
        tag.putString("i", this.profileId);
    }

    public static ConfigProfileEditPacket read(FriendlyByteBuf buffer) {
        try {
            CompoundTag tag = buffer.readNbt(NbtAccounter.create(8388608L));
            if (tag == null) {
                return null;
            }
            List<AbstractConfigPacket.OptionEntry> entries = readOptions(tag);
            String profileId = tag.getString("i");
            return new ConfigProfileEditPacket(profileId, entries);
        } catch (Throwable t) {
            if (Services.PLATFORM.isDevelopmentEnvironment() || !Services.PLATFORM.isDedicatedServer()) {
                XaeroLib.LOGGER.error("packet error", t);
                return null;
            }
            return null;
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/profile/ConfigProfileEditPacket$ClientHandler.class */
    public static final class ClientHandler implements Consumer<ConfigProfileEditPacket> {
        @Override // java.util.function.Consumer
        public void accept(ConfigProfileEditPacket message) {
            try {
                XaeroLib.INSTANCE.getClient().getConfigSynchronizer().onEditProfilePacket(message.profileId, message.getEntries());
            } catch (Throwable t) {
                XaeroLib.LOGGER.error("packet error", t);
            }
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/profile/ConfigProfileEditPacket$ServerHandler.class */
    public static final class ServerHandler implements BiConsumer<ConfigProfileEditPacket, ServerPlayer> {
        @Override // java.util.function.BiConsumer
        public void accept(ConfigProfileEditPacket message, ServerPlayer player) {
            try {
                ServerPlayerData.get(player).getConfigSynchronizer().onEditProfilePacket(message.profileId, message.getEntries());
            } catch (Throwable t) {
                if (Services.PLATFORM.isDevelopmentEnvironment()) {
                    XaeroLib.LOGGER.error("packet error", t);
                }
            }
        }
    }
}
