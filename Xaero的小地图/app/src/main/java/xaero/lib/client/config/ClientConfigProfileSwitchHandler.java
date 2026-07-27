package xaero.lib.client.config;

import java.util.Iterator;
import java.util.Objects;
import xaero.lib.client.config.listener.ClientConfigChangeListener;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.ConfigOptionManager;
import xaero.lib.common.config.profile.ConfigProfile;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/ClientConfigProfileSwitchHandler.class */
public final class ClientConfigProfileSwitchHandler {
    private final ConfigOptionManager options;

    private ClientConfigProfileSwitchHandler(ConfigOptionManager options) {
        this.options = options;
    }

    public void handleSwitch(ConfigProfile from, ConfigProfile to, ClientConfigManager manager) {
        ClientConfigChangeListener listener = manager.getChangeListener();
        Iterator<ConfigOption<?>> it = this.options.iterator();
        while (it.hasNext()) {
            ConfigOption<?> option = it.next();
            if (!Objects.equals(manager.getEffective(from, option), manager.getEffective(to, option))) {
                listener.onChange(to, option);
            }
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/ClientConfigProfileSwitchHandler$Builder.class */
    public static final class Builder {
        private ConfigOptionManager options;

        private Builder() {
        }

        public Builder setDefault() {
            setOptions(null);
            return this;
        }

        public Builder setOptions(ConfigOptionManager options) {
            this.options = options;
            return this;
        }

        public ClientConfigProfileSwitchHandler build() {
            if (this.options == null) {
                throw new IllegalStateException();
            }
            return new ClientConfigProfileSwitchHandler(this.options);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
