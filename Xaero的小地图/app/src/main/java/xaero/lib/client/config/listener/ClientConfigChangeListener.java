package xaero.lib.client.config.listener;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.listener.handler.HandlerBasedConfigChangeListener;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.ConfigOptionManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/listener/ClientConfigChangeListener.class */
public final class ClientConfigChangeListener extends HandlerBasedConfigChangeListener {
    private final ClientConfigManager manager;

    private ClientConfigChangeListener(ClientConfigManager manager, Map<ConfigOption<?>, Set<Consumer<Config>>> changeHandlers, ConfigOptionManager options) {
        super(changeHandlers, options);
        this.manager = manager;
    }

    public ClientConfigManager getManager() {
        return this.manager;
    }

    @Override // xaero.lib.common.config.listener.handler.HandlerBasedConfigChangeListener, xaero.lib.common.config.listener.IConfigChangeListener
    public void onChange(Config config, ConfigOption<?> option) {
        if (!this.enabled) {
            return;
        }
        Config primaryClientConfig = this.manager.getPrimaryConfigManager().getConfig();
        if (config == primaryClientConfig) {
            super.onChange(config, option);
            return;
        }
        if (config == this.manager.getRedirectorManager().getCacheConfig()) {
            super.onChange(config, option);
            return;
        }
        if (this.manager.getRedirectorManager().shouldRedirect(option)) {
            return;
        }
        Config serverSyncedConfig = this.manager.getServerSynced().getConfig();
        if (config == serverSyncedConfig) {
            super.onChange(config, option);
        } else {
            if ((option.isOverridable() && serverSyncedConfig.get(option) != null) || config != this.manager.getCurrentProfile()) {
                return;
            }
            super.onChange(config, option);
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/listener/ClientConfigChangeListener$Builder.class */
    public static final class Builder extends HandlerBasedConfigChangeListener.Builder<Builder> {
        private ClientConfigManager manager;

        private Builder() {
        }

        @Override // xaero.lib.common.config.listener.handler.HandlerBasedConfigChangeListener.Builder
        public Builder setDefault() {
            setManager(null);
            return (Builder) super.setDefault();
        }

        public Builder setManager(ClientConfigManager manager) {
            this.manager = manager;
            return this;
        }

        @Override // xaero.lib.common.config.listener.handler.HandlerBasedConfigChangeListener.Builder
        public ClientConfigChangeListener build() {
            if (this.manager == null) {
                throw new IllegalStateException();
            }
            return (ClientConfigChangeListener) super.build();
        }

        @Override // xaero.lib.common.config.listener.handler.HandlerBasedConfigChangeListener.Builder
        protected HandlerBasedConfigChangeListener buildInternally(Map<ConfigOption<?>, Set<Consumer<Config>>> changeHandlers) {
            return new ClientConfigChangeListener(this.manager, changeHandlers, this.options);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
