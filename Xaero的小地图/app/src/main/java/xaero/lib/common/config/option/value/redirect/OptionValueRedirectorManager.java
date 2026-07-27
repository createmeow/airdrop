package xaero.lib.common.config.option.value.redirect;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.listener.IConfigChangeListener;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.value.redirect.OptionValueRedirector;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/redirect/OptionValueRedirectorManager.class */
public class OptionValueRedirectorManager {
    protected final Map<ConfigOption<?>, OptionValueRedirector<?>> redirectors;
    private final Config cacheConfig;
    protected ConfigChannel channel;
    protected IConfigChangeListener changeListener;
    private boolean frozen;

    protected OptionValueRedirectorManager(Map<ConfigOption<?>, OptionValueRedirector<?>> redirectors, Config cacheConfig) {
        this.redirectors = redirectors;
        this.cacheConfig = cacheConfig;
    }

    public void freeze() {
        this.frozen = true;
    }

    protected <T> void checkDuplicates(ConfigOption<T> option) {
        if (this.redirectors.containsKey(option)) {
            throw new IllegalArgumentException("Tried to register duplicate config option redirector for " + option.getId());
        }
    }

    public <T> void register(ConfigOption<T> option, Supplier<T> redirectSupplier, Predicate<ConfigChannel> condition) {
        if (this.frozen) {
            throw new IllegalStateException("The redirector manager is already frozen!");
        }
        checkDuplicates(option);
        this.redirectors.put(option, OptionValueRedirector.FinalBuilder.begin().setSupplier(redirectSupplier).setCondition(condition).build());
    }

    public void setChannel(ConfigChannel channel) {
        if (this.channel != null) {
            throw new IllegalStateException();
        }
        this.channel = channel;
    }

    public void setChangeListener(IConfigChangeListener changeListener) {
        if (this.changeListener != null) {
            throw new IllegalStateException();
        }
        this.changeListener = changeListener;
        this.cacheConfig.setChangeListener(changeListener);
    }

    protected <T> OptionValueRedirector<T> get(ConfigOption<T> option) {
        return (OptionValueRedirector) this.redirectors.get(option);
    }

    public <T> T getValue(ConfigOption<T> option) {
        OptionValueRedirector<T> redirector = get(option);
        if (redirector == null) {
            return null;
        }
        T value = redirector.getValue();
        if (value == null) {
            throw new IllegalArgumentException("Redirectors aren't allowed to return null!");
        }
        this.cacheConfig.set(option, value);
        return value;
    }

    public boolean shouldRedirect(ConfigOption<?> option) {
        OptionValueRedirector<?> redirector;
        if (!option.isOverridable() || (redirector = get(option)) == null) {
            return false;
        }
        boolean result = redirector.shouldRedirect(this.channel);
        if (!result) {
            this.cacheConfig.set(option, null);
        }
        return result;
    }

    public Config getCacheConfig() {
        return this.cacheConfig;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/redirect/OptionValueRedirectorManager$Builder.class */
    public static abstract class Builder<B extends Builder<B>> {
        protected final B self = this;

        protected abstract OptionValueRedirectorManager buildInternally(Map<ConfigOption<?>, OptionValueRedirector<?>> map, Config config);

        protected Builder() {
        }

        public B setDefault() {
            return this.self;
        }

        public OptionValueRedirectorManager build() {
            Config cacheConfig = Config.FinalBuilder.begin().setAllowNullValues(true).build();
            return buildInternally(new HashMap(), cacheConfig);
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/redirect/OptionValueRedirectorManager$FinalBuilder.class */
    public static final class FinalBuilder extends Builder<FinalBuilder> {
        private FinalBuilder() {
        }

        @Override // xaero.lib.common.config.option.value.redirect.OptionValueRedirectorManager.Builder
        protected OptionValueRedirectorManager buildInternally(Map<ConfigOption<?>, OptionValueRedirector<?>> redirectors, Config cacheConfig) {
            return new OptionValueRedirectorManager(redirectors, cacheConfig);
        }

        public static FinalBuilder begin() {
            return new FinalBuilder().setDefault();
        }
    }
}
