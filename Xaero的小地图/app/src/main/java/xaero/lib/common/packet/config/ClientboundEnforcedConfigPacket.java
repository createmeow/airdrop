package xaero.lib.common.packet.config;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import xaero.lib.XaeroLib;
import xaero.lib.common.packet.config.AbstractConfigPacket;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/ClientboundEnforcedConfigPacket.class */
public final class ClientboundEnforcedConfigPacket extends AbstractConfigPacket<ClientboundEnforcedConfigPacket> {
    private final boolean reset;

    public ClientboundEnforcedConfigPacket(List<AbstractConfigPacket.OptionEntry> entries, boolean reset) {
        super(entries);
        this.reset = reset;
    }

    @Override // xaero.lib.common.packet.config.AbstractConfigPacket
    protected void writeExtra(CompoundTag tag) {
        tag.putBoolean("r", this.reset);
    }

    public static ClientboundEnforcedConfigPacket read(FriendlyByteBuf buffer) {
        try {
            CompoundTag tag = buffer.readNbt(NbtAccounter.unlimitedHeap());
            if (tag == null) {
                return null;
            }
            List<AbstractConfigPacket.OptionEntry> entries = readOptions(tag);
            boolean reset = tag.getBoolean("r");
            return new ClientboundEnforcedConfigPacket(entries, reset);
        } catch (Throwable t) {
            XaeroLib.LOGGER.error("packet error", t);
            return null;
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/ClientboundEnforcedConfigPacket$ClientHandler.class */
    public static final class ClientHandler implements Consumer<ClientboundEnforcedConfigPacket> {
        @Override // java.util.function.Consumer
        public void accept(ClientboundEnforcedConfigPacket packet) {
            try {
                XaeroLib.INSTANCE.getClient().getConfigSynchronizer().onEnforcedConfigPacket(packet.reset, packet.getEntries());
            } catch (Throwable t) {
                XaeroLib.LOGGER.error("packet error", t);
            }
        }
    }
}
