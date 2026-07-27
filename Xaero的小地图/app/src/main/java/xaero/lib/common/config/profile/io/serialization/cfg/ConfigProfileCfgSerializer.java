package xaero.lib.common.config.profile.io.serialization.cfg;

import java.nio.file.Path;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer;
import xaero.lib.common.config.option.ConfigOptionManager;
import xaero.lib.common.config.profile.ConfigProfile;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/profile/io/serialization/cfg/ConfigProfileCfgSerializer.class */
public final class ConfigProfileCfgSerializer extends AConfigCfgSerializer<ConfigProfile> {
    private final String configType;

    private ConfigProfileCfgSerializer(ConfigOptionManager options, boolean configsTrackDirtyOptions, String configType) {
        super(options, configsTrackDirtyOptions);
        this.configType = configType;
    }

    @Override // xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer, xaero.lib.common.config.io.serialization.IConfigSerializer
    public String serialize(ConfigProfile profile, Path file) {
        StringBuilder builder = new StringBuilder();
        builder.append("############").append('\n');
        builder.append("## ").append(this.configType).append(" Profile \"").append(profile.getId()).append('\"').append('\n');
        builder.append("##").append('\n');
        builder.append('\n');
        builder.append(super.serialize((ConfigProfileCfgSerializer) profile, file));
        return builder.toString();
    }

    @Override // xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer
    protected Config.Builder<ConfigProfile, ?> createConfigBuilder(String debugId) {
        return ConfigProfile.Builder.begin().setId(debugId);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/profile/io/serialization/cfg/ConfigProfileCfgSerializer$Builder.class */
    public static final class Builder extends AConfigCfgSerializer.Builder<ConfigProfile, Builder> {
        private String configType;

        @Override // xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer.Builder
        public /* bridge */ /* synthetic */ AConfigCfgSerializer.Builder setConfigsTrackDirtyOptions(boolean z) {
            return super.setConfigsTrackDirtyOptions(z);
        }

        @Override // xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer.Builder
        public /* bridge */ /* synthetic */ AConfigCfgSerializer.Builder setOptions(ConfigOptionManager configOptionManager) {
            return super.setOptions(configOptionManager);
        }

        private Builder() {
        }

        @Override // xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer.Builder
        public Builder setDefault() {
            setConfigType(null);
            return (Builder) super.setDefault();
        }

        public Builder setConfigType(String configType) {
            this.configType = configType;
            return this;
        }

        @Override // xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer.Builder
        public ConfigProfileCfgSerializer build() {
            if (this.configType == null) {
                throw new IllegalStateException();
            }
            return (ConfigProfileCfgSerializer) super.build();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer.Builder
        public ConfigProfileCfgSerializer buildInternally() {
            return new ConfigProfileCfgSerializer(this.options, this.configsTrackDirtyOptions, this.configType);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
