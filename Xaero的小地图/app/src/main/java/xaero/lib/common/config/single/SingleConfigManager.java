package xaero.lib.common.config.single;

import org.apache.logging.log4j.Logger;
import xaero.lib.XaeroLib;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.listener.IConfigChangeListener;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.value.redirect.OptionValueRedirectorManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/single/SingleConfigManager.class */
public class SingleConfigManager<C extends Config> {
    public final Logger logger;
    private C config;
    private final String configId;
    private OptionValueRedirectorManager redirectorManager;
    private ConfigChannel channel;
    private IConfigChangeListener changeListener;

    public SingleConfigManager(Logger logger, String configId) {
        this.logger = logger;
        this.configId = configId;
    }

    public C getConfig() {
        return this.config;
    }

    public void setConfig(C config) {
        this.config = config;
        config.setChangeListener(this.changeListener);
    }

    public <T> T getEffective(ConfigOption<T> configOption) {
        if (this.redirectorManager != null && this.redirectorManager.shouldRedirect(configOption)) {
            return (T) this.redirectorManager.getValue(configOption);
        }
        return (T) getConfig().get(configOption);
    }

    public String getConfigId() {
        return this.configId;
    }

    public void setChangeListener(IConfigChangeListener changeListener) {
        this.changeListener = changeListener;
        if (this.config == null) {
            return;
        }
        this.config.setChangeListener(changeListener);
    }

    public void setChannel(ConfigChannel channel) {
        if (this.channel != null) {
            throw new IllegalStateException();
        }
        this.channel = channel;
    }

    public ConfigChannel getChannel() {
        return this.channel;
    }

    public void setRedirectorManager(OptionValueRedirectorManager redirectorManager) {
        if (this.redirectorManager != null) {
            throw new IllegalStateException();
        }
        this.redirectorManager = redirectorManager;
    }

    public OptionValueRedirectorManager getRedirectorManager() {
        return this.redirectorManager;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/single/SingleConfigManager$Builder.class */
    public static abstract class Builder<C extends Config, B extends Builder<C, B>> {
        protected String configId;
        protected Logger logger;
        protected final B self = this;

        protected abstract SingleConfigManager<C> buildInternally();

        protected Builder() {
        }

        public B setDefault() {
            setConfigId(null);
            setLogger(XaeroLib.LOGGER);
            return this.self;
        }

        public B setConfigId(String configId) {
            this.configId = configId;
            return this.self;
        }

        public B setLogger(Logger logger) {
            this.logger = logger;
            return this.self;
        }

        public SingleConfigManager<C> build() {
            if (this.configId == null || this.logger == null) {
                throw new IllegalStateException();
            }
            return buildInternally();
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/single/SingleConfigManager$FinalBuilder.class */
    public static final class FinalBuilder<C extends Config> extends Builder<C, FinalBuilder<C>> {
        private FinalBuilder() {
        }

        @Override // xaero.lib.common.config.single.SingleConfigManager.Builder
        protected SingleConfigManager<C> buildInternally() {
            return new SingleConfigManager<>(this.logger, this.configId);
        }

        public static <C extends Config> FinalBuilder<C> begin() {
            return (FinalBuilder) new FinalBuilder().setDefault();
        }
    }
}
