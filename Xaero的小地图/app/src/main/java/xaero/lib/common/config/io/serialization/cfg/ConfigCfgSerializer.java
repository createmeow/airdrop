package xaero.lib.common.config.io.serialization.cfg;

import xaero.lib.common.config.Config;
import xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer;
import xaero.lib.common.config.option.ConfigOptionManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/io/serialization/cfg/ConfigCfgSerializer.class */
public final class ConfigCfgSerializer extends AConfigCfgSerializer<Config> {
    private ConfigCfgSerializer(ConfigOptionManager options, boolean configsTrackDirtyOptions) {
        super(options, configsTrackDirtyOptions);
    }

    @Override // xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer
    protected Config.Builder<Config, ?> createConfigBuilder(String debugId) {
        return Config.FinalBuilder.begin();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/io/serialization/cfg/ConfigCfgSerializer$Builder.class */
    public static final class Builder extends AConfigCfgSerializer.Builder<Config, Builder> {
        @Override // xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer.Builder
        public /* bridge */ /* synthetic */ AConfigCfgSerializer build() {
            return super.build();
        }

        @Override // xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer.Builder
        public /* bridge */ /* synthetic */ AConfigCfgSerializer.Builder setConfigsTrackDirtyOptions(boolean z) {
            return super.setConfigsTrackDirtyOptions(z);
        }

        @Override // xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer.Builder
        public /* bridge */ /* synthetic */ AConfigCfgSerializer.Builder setOptions(ConfigOptionManager configOptionManager) {
            return super.setOptions(configOptionManager);
        }

        @Override // xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer.Builder
        public /* bridge */ /* synthetic */ AConfigCfgSerializer.Builder setDefault() {
            return super.setDefault();
        }

        private Builder() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer.Builder
        public ConfigCfgSerializer buildInternally() {
            return new ConfigCfgSerializer(this.options, this.configsTrackDirtyOptions);
        }

        public static Builder begin() {
            return (Builder) new Builder().setDefault();
        }
    }
}
