package xaero.hud.packet.tracker;

import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import xaero.common.XaeroMinimapSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/packet/tracker/ClientboundTrackedPlayerPacket.class */
public class ClientboundTrackedPlayerPacket {
    private final boolean remove;
    private final UUID id;
    private final double x;
    private final double y;
    private final double z;
    private final ResourceLocation dimension;

    public ClientboundTrackedPlayerPacket(boolean remove, UUID id, double x, double y, double z, ResourceLocation dimension) {
        this.remove = remove;
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
    }

    public void write(FriendlyByteBuf buffer) {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("r", this.remove);
        nbt.putUUID("i", this.id);
        if (!this.remove) {
            nbt.putDouble("x", this.x);
            nbt.putDouble("y", this.y);
            nbt.putDouble("z", this.z);
            nbt.putString("d", this.dimension.toString());
        }
        buffer.writeNbt(nbt);
    }

    public static ClientboundTrackedPlayerPacket read(FriendlyByteBuf buffer) {
        CompoundTag nbt = buffer.readNbt(NbtAccounter.unlimitedHeap());
        boolean remove = nbt.getBoolean("r");
        UUID id = nbt.getUUID("i");
        double x = remove ? 0.0d : nbt.getDouble("x");
        double y = remove ? 0.0d : nbt.getDouble("y");
        double z = remove ? 0.0d : nbt.getDouble("z");
        String dimensionString = remove ? null : nbt.getString("d");
        ResourceLocation dimension = dimensionString == null ? null : ResourceLocation.parse(dimensionString);
        return new ClientboundTrackedPlayerPacket(remove, id, x, y, z, dimension);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/packet/tracker/ClientboundTrackedPlayerPacket$Handler.class */
    public static class Handler implements Consumer<ClientboundTrackedPlayerPacket> {
        @Override // java.util.function.Consumer
        public void accept(ClientboundTrackedPlayerPacket t) {
            XaeroMinimapSession minimapSession = XaeroMinimapSession.getCurrentSession();
            if (minimapSession == null) {
                return;
            }
            if (t.remove) {
                minimapSession.getMinimapProcessor().getSyncedTrackedPlayerManager().remove(t.id);
            } else {
                minimapSession.getMinimapProcessor().getSyncedTrackedPlayerManager().update(t.id, t.x, t.y, t.z, ResourceKey.create(Registries.DIMENSION, t.dimension));
            }
        }
    }
}
