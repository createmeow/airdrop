package xaero.lib.common.config.listener.handler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.listener.IConfigChangeListener;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.ConfigOptionManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/listener/handler/HandlerBasedConfigChangeListener.class */
public class HandlerBasedConfigChangeListener implements IConfigChangeListener {
    protected final Map<ConfigOption<?>, Set<Consumer<Config>>> changeHandlers;
    protected final ConfigOptionManager options;
    protected boolean enabled;

    protected HandlerBasedConfigChangeListener(Map<ConfigOption<?>, Set<Consumer<Config>>> changeHandlers, ConfigOptionManager options) {
        this.changeHandlers = changeHandlers;
        this.options = options;
    }

    public void register(ConfigOption<?> option, Consumer<Config> handler) {
        Set<Consumer<Config>> handlers = this.changeHandlers.computeIfAbsent(option, k -> {
            return new HashSet();
        });
        handlers.add(handler);
    }

    @Override // xaero.lib.common.config.listener.IConfigChangeListener
    public void onChange(Config config, ConfigOption<?> option) {
        if (!this.enabled) {
            return;
        }
        this.changeHandlers.computeIfAbsent(option, k -> {
            return new HashSet();
        }).forEach(c -> {
            c.accept(config);
        });
    }

    @Override // xaero.lib.common.config.listener.IConfigChangeListener
    public void onFullChange(Config config) {
        if (!this.enabled) {
            return;
        }
        Iterator<ConfigOption<?>> it = this.options.iterator();
        while (it.hasNext()) {
            ConfigOption<?> option = it.next();
            onChange(config, option);
        }
    }

    @Override // xaero.lib.common.config.listener.IConfigChangeListener
    public void onRemoved(Config config) {
    }

    public void postLoad() {
        this.enabled = true;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/listener/handler/HandlerBasedConfigChangeListener$Builder.class */
    public static abstract class Builder<B extends Builder<B>> {
        protected final B self = this;
        protected ConfigOptionManager options;

        protected abstract HandlerBasedConfigChangeListener buildInternally(Map<ConfigOption<?>, Set<Consumer<Config>>> map);

        protected Builder() {
        }

        public B setDefault() {
            setOptions(null);
            return this.self;
        }

        public B setOptions(ConfigOptionManager options) {
            this.options = options;
            return this.self;
        }

        public HandlerBasedConfigChangeListener build() {
            if (this.options == null) {
                throw new IllegalStateException();
            }
            return buildInternally(new HashMap());
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/listener/handler/HandlerBasedConfigChangeListener$FinalBuilder.class */
    public static final class FinalBuilder extends Builder<FinalBuilder> {
        private FinalBuilder() {
        }

        @Override // xaero.lib.common.config.listener.handler.HandlerBasedConfigChangeListener.Builder
        protected HandlerBasedConfigChangeListener buildInternally(Map<ConfigOption<?>, Set<Consumer<Config>>> changeHandlers) {
            return new HandlerBasedConfigChangeListener(changeHandlers, this.options);
        }

        public static FinalBuilder begin() {
            return new FinalBuilder().setDefault();
        }
    }
}
