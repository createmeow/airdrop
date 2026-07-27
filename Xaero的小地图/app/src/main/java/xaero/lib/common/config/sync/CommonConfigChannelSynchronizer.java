package xaero.lib.common.config.sync;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.apache.logging.log4j.Logger;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.ConfigOptionManager;
import xaero.lib.common.config.option.value.sync.serialization.ConfigValueSyncCodec;
import xaero.lib.common.config.profile.ConfigProfile;
import xaero.lib.common.packet.config.AbstractConfigPacket;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/sync/CommonConfigChannelSynchronizer.class */
public class CommonConfigChannelSynchronizer {
    protected final ConfigOptionManager options;
    protected ConfigChannel channel;
    public final Logger logger;

    public CommonConfigChannelSynchronizer(ConfigOptionManager options, Logger logger) {
        this.options = options;
        this.logger = logger;
    }

    public void setChannel(ConfigChannel channel) {
        if (this.channel != null) {
            throw new IllegalStateException();
        }
        this.channel = channel;
    }

    protected List<AbstractConfigPacket.OptionEntry> getConfigDifferenceEntries(ConfigProfile from, ConfigProfile to) {
        List<AbstractConfigPacket.OptionEntry> packetEntries = new ArrayList<>();
        Iterator<ConfigOption<?>> it = this.options.iterator();
        while (it.hasNext()) {
            ConfigOption<?> option = it.next();
            addDifferenceEntryFor(option, from, to, packetEntries);
        }
        return packetEntries;
    }

    private <T> void addDifferenceEntryFor(ConfigOption<T> option, ConfigProfile from, ConfigProfile to, List<AbstractConfigPacket.OptionEntry> packetEntries) {
        Object obj = to.get(option);
        if (!Objects.equals(from.get(option), obj)) {
            packetEntries.add(AbstractConfigPacket.OptionEntry.of(option, obj));
        }
    }

    protected List<AbstractConfigPacket.OptionEntry> getAllConfigEntries(ConfigProfile config) {
        List<AbstractConfigPacket.OptionEntry> packetEntries = new ArrayList<>();
        for (ConfigOption<?> option : config.usedOptions()) {
            packetEntries.add(AbstractConfigPacket.OptionEntry.of(config, option));
        }
        return packetEntries;
    }

    protected List<AbstractConfigPacket.OptionEntry> getDirtyConfigEntries(ConfigProfile config) {
        List<AbstractConfigPacket.OptionEntry> packetEntries = new ArrayList<>();
        for (ConfigOption<?> option : config.getDirtyOptions()) {
            packetEntries.add(AbstractConfigPacket.OptionEntry.of(config, option));
        }
        return packetEntries;
    }

    protected void setConfigValues(Config config, Iterable<AbstractConfigPacket.OptionEntry> entries) {
        for (AbstractConfigPacket.OptionEntry entry : entries) {
            ConfigOption<?> option = this.options.get(entry.optionId);
            if (option == null) {
                this.logger.warn("Received invalid config option ID {} for channel {}. Skipping.", entry.optionId, this.channel.getId());
            } else {
                setConfigOptionValue(config, (ConfigOption) option, entry.valueTag);
            }
        }
    }

    protected <T> void setConfigOptionValue(Config config, ConfigOption<T> option, Tag valueTag) {
        if (valueTag instanceof CompoundTag) {
            CompoundTag compoundTag = (CompoundTag) valueTag;
            if (compoundTag.isEmpty()) {
                setConfigOptionValue(config, option, (Object) null);
                return;
            }
        }
        Object objDecodeOptionValue = decodeOptionValue(option, valueTag);
        if (objDecodeOptionValue == null) {
            this.logger.warn("Failed to decode received value for option {} of channel {}. Skipping.", option.getId(), this.channel.getId());
        } else {
            setConfigOptionValue(config, option, objDecodeOptionValue);
        }
    }

    protected static <T> void setConfigOptionValue(Config config, ConfigOption<T> option, T value) {
        config.set(option, value);
    }

    protected static <T> T decodeOptionValue(ConfigOption<T> configOption, Tag tag) {
        return (T) decodeOptionValue(configOption.getValueType().getSyncCodec(), tag);
    }

    protected static <T, N extends Tag> T decodeOptionValue(ConfigValueSyncCodec<T, N> syncCodec, Tag tag) {
        try {
            return syncCodec.decode(tag);
        } catch (Throwable th) {
            return null;
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/sync/CommonConfigChannelSynchronizer$Builder.class */
    public static abstract class Builder<B extends Builder<B>> {
        protected final B self = this;
        protected ConfigOptionManager options;
        protected Logger logger;

        protected abstract CommonConfigChannelSynchronizer buildInternally();

        protected Builder() {
        }

        public B setDefault() {
            setOptions(null);
            setLogger(null);
            return this.self;
        }

        public B setOptions(ConfigOptionManager options) {
            this.options = options;
            return this.self;
        }

        public B setLogger(Logger logger) {
            this.logger = logger;
            return this.self;
        }

        public CommonConfigChannelSynchronizer build() {
            if (this.options == null) {
                throw new IllegalStateException();
            }
            return buildInternally();
        }
    }
}
