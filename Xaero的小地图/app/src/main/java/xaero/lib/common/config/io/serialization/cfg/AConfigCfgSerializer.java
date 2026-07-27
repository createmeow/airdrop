package xaero.lib.common.config.io.serialization.cfg;

import java.nio.file.Path;
import java.util.Iterator;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.io.serialization.IConfigSerializer;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.ConfigOptionManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/io/serialization/cfg/AConfigCfgSerializer.class */
public abstract class AConfigCfgSerializer<C extends Config> implements IConfigSerializer<C> {
    public static final String RECOMMENDED_EXTENSION = ".cfg";
    protected final ConfigOptionManager options;
    protected final boolean configsTrackDirtyOptions;

    protected abstract Config.Builder<C, ?> createConfigBuilder(String str);

    protected AConfigCfgSerializer(ConfigOptionManager options, boolean configsTrackDirtyOptions) {
        this.options = options;
        this.configsTrackDirtyOptions = configsTrackDirtyOptions;
    }

    @Override // xaero.lib.common.config.io.serialization.IConfigSerializer
    public String serialize(C config, Path file) {
        StringBuilder builder = new StringBuilder();
        Iterator<ConfigOption<?>> it = config.usedOptions().iterator();
        while (it.hasNext()) {
            addOptionLine((ConfigOption) it.next(), config, builder, file);
        }
        return builder.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> void addOptionLine(ConfigOption<T> configOption, C c, StringBuilder sb, Path path) {
        String failedSerializedValue = c.getFailedSerializedValue(configOption);
        if (failedSerializedValue == null) {
            Object obj = c.get(configOption);
            if (!configOption.shouldSaveDefaultValue() && obj == configOption.getDefaultValue()) {
                return;
            } else {
                failedSerializedValue = configOption.getValueType().getIoCodec().encode(obj, path, configOption);
            }
        }
        sb.append(configOption.getId()).append(" = ").append(failedSerializedValue).append('\n');
    }

    @Override // xaero.lib.common.config.io.serialization.IConfigSerializer
    public C deserialize(String str, boolean z, String str2, Path path) {
        C c = (C) createConfigBuilder(str2).setOptions(this.options).setAllowNullValues(z).setTrackDirtyOptions(this.configsTrackDirtyOptions).build();
        str.lines().forEach(line -> {
            int equalsIndex;
            String trimmedLine = line.trim();
            if (trimmedLine.startsWith("#") || (equalsIndex = trimmedLine.indexOf("=")) == -1) {
                return;
            }
            String optionId = trimmedLine.substring(0, equalsIndex).trim();
            ConfigOption configOption = this.options.get(optionId);
            if (configOption == null) {
                this.options.logger.warn("Unknown option id {} in config {}! Skipping.", optionId, str2);
            } else {
                String serializedValue = trimmedLine.substring(equalsIndex + 1).trim();
                setOptionValue(configOption, serializedValue, c, str2, path);
            }
        });
        return c;
    }

    private <T> void setOptionValue(ConfigOption<T> option, String serializedValue, Config config, String debugId, Path file) {
        try {
            T value = option.getValueType().getIoCodec().decode(serializedValue, file, option);
            if (value == null) {
                this.options.logger.warn("Unknown value in config {} for option {}! Skipping.", debugId, option.getId());
                config.addFailedSerializedValue(option, serializedValue);
            } else {
                config.set(option, value);
            }
        } catch (Throwable t) {
            this.options.logger.warn("Error parsing config option value in config {}! {} = {} Message: {}", debugId, option.getId(), serializedValue, t.getMessage());
            config.addFailedSerializedValue(option, serializedValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/io/serialization/cfg/AConfigCfgSerializer$Builder.class */
    public static abstract class Builder<C extends Config, B extends Builder<C, B>> {
        protected final B self = this;
        protected ConfigOptionManager options;
        protected boolean configsTrackDirtyOptions;

        protected abstract AConfigCfgSerializer<C> buildInternally();

        protected Builder() {
        }

        public B setDefault() {
            setOptions(null);
            setConfigsTrackDirtyOptions(false);
            return this.self;
        }

        public B setOptions(ConfigOptionManager options) {
            this.options = options;
            return this.self;
        }

        public B setConfigsTrackDirtyOptions(boolean configsTrackDirtyOptions) {
            this.configsTrackDirtyOptions = configsTrackDirtyOptions;
            return this.self;
        }

        public AConfigCfgSerializer<C> build() {
            if (this.options == null) {
                throw new IllegalStateException();
            }
            return buildInternally();
        }
    }
}
