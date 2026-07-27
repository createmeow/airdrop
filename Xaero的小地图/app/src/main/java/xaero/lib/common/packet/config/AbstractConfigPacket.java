package xaero.lib.common.packet.config;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.value.sync.serialization.ConfigValueSyncCodec;
import xaero.lib.common.packet.config.AbstractConfigPacket;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/AbstractConfigPacket.class */
public abstract class AbstractConfigPacket<P extends AbstractConfigPacket<P>> {
    private final List<OptionEntry> entries;

    protected abstract void writeExtra(CompoundTag compoundTag);

    protected AbstractConfigPacket(List<OptionEntry> entries) {
        this.entries = entries;
    }

    public void write(FriendlyByteBuf buffer) {
        CompoundTag tag = new CompoundTag();
        CompoundTag optionsTag = new CompoundTag();
        for (OptionEntry entry : this.entries) {
            optionsTag.put(entry.optionId, entry.valueTag);
        }
        tag.put("o", optionsTag);
        writeExtra(tag);
        buffer.writeNbt(tag);
    }

    public Iterable<OptionEntry> getEntries() {
        return this.entries;
    }

    public static List<OptionEntry> readOptions(CompoundTag tag) {
        CompoundTag optionsTag = tag.getCompound("o");
        List<OptionEntry> result = new ArrayList<>();
        for (String optionId : optionsTag.getAllKeys()) {
            Tag valueTag = optionsTag.get(optionId);
            result.add(new OptionEntry(optionId, valueTag));
        }
        return result;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/packet/config/AbstractConfigPacket$OptionEntry.class */
    public static class OptionEntry {
        public static final Tag NULL_VALUE = new CompoundTag();
        public final String optionId;
        public final Tag valueTag;

        public OptionEntry(String optionId, Tag valueTag) {
            this.optionId = optionId;
            this.valueTag = valueTag == null ? NULL_VALUE : valueTag;
        }

        public static <T> OptionEntry of(ConfigOption<T> option, T value) {
            ConfigValueSyncCodec<T, ? extends Tag> syncCodec = option.getValueType().getSyncCodec();
            Tag valueTag = value == null ? null : syncCodec.encode(value);
            return new OptionEntry(option.getId(), valueTag);
        }

        public static <T> OptionEntry of(Config config, ConfigOption<T> option) {
            return of(option, config.get(option));
        }
    }
}
