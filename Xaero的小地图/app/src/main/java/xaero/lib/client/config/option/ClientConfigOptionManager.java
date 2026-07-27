package xaero.lib.client.config.option;

import java.util.Map;
import org.apache.logging.log4j.Logger;
import xaero.lib.client.config.option.ui.ConfigOptionUITypeManager;
import xaero.lib.client.config.option.ui.type.ConfigOptionUIType;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.ConfigOptionManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/ClientConfigOptionManager.class */
public final class ClientConfigOptionManager extends ConfigOptionManager {
    private final ConfigOptionUITypeManager uiTypeManager;

    private ClientConfigOptionManager(Logger logger, Map<String, ConfigOption<?>> options, ConfigOptionUITypeManager uiTypeManager) {
        super(logger, options);
        this.uiTypeManager = uiTypeManager;
    }

    public <CT extends ConfigOption<?>> void registerUIType(CT option, ConfigOptionUIType<? super CT> uiType) {
        this.uiTypeManager.registerUIType(option, uiType);
    }

    public <CT extends ConfigOption<?>> ConfigOptionUIType<? super CT> getUIType(CT option) {
        return this.uiTypeManager.getUIType(option);
    }

    public ConfigOptionUITypeManager getUiTypeManager() {
        return this.uiTypeManager;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/ClientConfigOptionManager$Builder.class */
    public static final class Builder extends ConfigOptionManager.Builder<Builder> {
        private ConfigOptionUITypeManager uiTypeManager;

        @Override // xaero.lib.common.config.option.ConfigOptionManager.Builder
        protected /* bridge */ /* synthetic */ ConfigOptionManager buildInternally(Map map) {
            return buildInternally((Map<String, ConfigOption<?>>) map);
        }

        private Builder() {
        }

        @Override // xaero.lib.common.config.option.ConfigOptionManager.Builder
        public Builder setDefault() {
            setUiTypeManager(null);
            return this;
        }

        public Builder setUiTypeManager(ConfigOptionUITypeManager uiTypeManager) {
            this.uiTypeManager = uiTypeManager;
            return this;
        }

        @Override // xaero.lib.common.config.option.ConfigOptionManager.Builder
        public ClientConfigOptionManager build() {
            if (this.uiTypeManager == null) {
                throw new IllegalStateException();
            }
            return (ClientConfigOptionManager) super.build();
        }

        @Override // xaero.lib.common.config.option.ConfigOptionManager.Builder
        protected ClientConfigOptionManager buildInternally(Map<String, ConfigOption<?>> options) {
            return new ClientConfigOptionManager(this.logger, options, this.uiTypeManager);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
