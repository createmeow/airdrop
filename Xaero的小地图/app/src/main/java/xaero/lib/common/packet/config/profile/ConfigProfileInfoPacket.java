package xaero.lib.common.packet.config.profile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import xaero.lib.XaeroLib;
import xaero.lib.client.config.sync.ClientConfigSynchronizer;
import xaero.lib.common.player.ServerPlayerData;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/profile/ConfigProfileInfoPacket.class */
public final class ConfigProfileInfoPacket {
    private final List<Entry> entries;
    private final String defaultEnforcedProfileId;
    private final boolean reset;

    public ConfigProfileInfoPacket(List<Entry> entries, String defaultEnforcedProfileId, boolean reset) {
        this.entries = entries;
        this.defaultEnforcedProfileId = defaultEnforcedProfileId;
        this.reset = reset;
    }

    public ConfigProfileInfoPacket(String id, String name, String defaultEnforcedProfileId) {
        this((List<Entry>) List.of(new Entry(id, name)), defaultEnforcedProfileId, false);
    }

    public ConfigProfileInfoPacket(List<Entry> entries) {
        this(entries, (String) null, false);
    }

    public ConfigProfileInfoPacket(String id, String name) {
        this((List<Entry>) List.of(new Entry(id, name)), (String) null, false);
    }

    public void write(FriendlyByteBuf buffer) {
        CompoundTag tag = new CompoundTag();
        ListTag entriesTag = new ListTag();
        for (Entry entry : this.entries) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putString("i", entry.id);
            entryTag.putString("n", entry.name);
            entriesTag.add(entryTag);
        }
        tag.put("e", entriesTag);
        if (this.defaultEnforcedProfileId != null) {
            tag.putString("d", this.defaultEnforcedProfileId);
        }
        tag.putBoolean("r", this.reset);
        buffer.writeNbt(tag);
    }

    public static ConfigProfileInfoPacket read(FriendlyByteBuf buffer) {
        try {
            CompoundTag tag = buffer.readNbt(NbtAccounter.unlimitedHeap());
            if (tag == null) {
                return null;
            }
            List<Entry> entries = new ArrayList<>();
            ListTag entriesTag = tag.getList("e", 10);
            Iterator it = entriesTag.iterator();
            while (it.hasNext()) {
                CompoundTag entryTagCast = (Tag) it.next();
                String id = entryTagCast.getString("i");
                String name = entryTagCast.getString("n");
                entries.add(new Entry(id, name));
            }
            String defaultEnforcedProfileId = tag.contains("d", 8) ? tag.getString("d") : null;
            boolean reset = tag.getBoolean("r");
            return new ConfigProfileInfoPacket(entries, defaultEnforcedProfileId, reset);
        } catch (Throwable t) {
            if (Services.PLATFORM.isDevelopmentEnvironment() || !Services.PLATFORM.isDedicatedServer()) {
                XaeroLib.LOGGER.error("packet error", t);
                return null;
            }
            return null;
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/profile/ConfigProfileInfoPacket$Entry.class */
    public static final class Entry {
        public final String id;
        public final String name;

        public Entry(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/profile/ConfigProfileInfoPacket$ServerHandler.class */
    public static final class ServerHandler implements BiConsumer<ConfigProfileInfoPacket, ServerPlayer> {
        @Override // java.util.function.BiConsumer
        public void accept(ConfigProfileInfoPacket packet, ServerPlayer player) {
            try {
                ServerPlayerData.get(player).getConfigSynchronizer().onConfigProfileInfoPacket(packet.entries, packet.defaultEnforcedProfileId);
            } catch (Throwable t) {
                if (Services.PLATFORM.isDevelopmentEnvironment()) {
                    XaeroLib.LOGGER.error("packet error", t);
                }
            }
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/profile/ConfigProfileInfoPacket$ClientHandler.class */
    public static final class ClientHandler implements Consumer<ConfigProfileInfoPacket> {
        @Override // java.util.function.Consumer
        public void accept(ConfigProfileInfoPacket packet) {
            try {
                ClientConfigSynchronizer synchronizer = XaeroLib.INSTANCE.getClient().getConfigSynchronizer();
                synchronizer.onConfigProfileInfoPacket(packet.entries, packet.defaultEnforcedProfileId, packet.reset);
            } catch (Throwable t) {
                XaeroLib.LOGGER.error("packet error", t);
            }
        }
    }
}
