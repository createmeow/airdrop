package xaero.lib.common.config.option;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/ConfigOptionManager.class */
public class ConfigOptionManager implements Iterable<ConfigOption<?>> {
    public final Logger logger;
    private final Map<String, ConfigOption<?>> options;
    private boolean frozen;

    protected ConfigOptionManager(Logger logger, Map<String, ConfigOption<?>> options) {
        this.logger = logger;
        this.options = options;
    }

    public ConfigOption<?> get(String id) {
        return this.options.get(id);
    }

    public void register(ConfigOption<?> option) {
        if (this.frozen) {
            throw new IllegalStateException("The config option manager is read-only!");
        }
        if (this.options.containsKey(option.getId())) {
            throw new IllegalArgumentException("Duplicate config option id!");
        }
        this.options.put(option.getId(), option);
    }

    public void freeze() {
        this.frozen = true;
    }

    @Override // java.lang.Iterable
    public Iterator<ConfigOption<?>> iterator() {
        return this.options.values().iterator();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/ConfigOptionManager$Builder.class */
    public static abstract class Builder<B extends Builder<B>> {
        protected final B self = this;
        protected Logger logger;

        protected abstract ConfigOptionManager buildInternally(Map<String, ConfigOption<?>> map);

        protected Builder() {
        }

        public B setDefault() {
            setLogger(null);
            return this.self;
        }

        public B setLogger(Logger logger) {
            this.logger = logger;
            return this.self;
        }

        public ConfigOptionManager build() {
            if (this.logger == null) {
                throw new IllegalStateException();
            }
            Map<String, ConfigOption<?>> options = new LinkedHashMap<>();
            return buildInternally(options);
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/ConfigOptionManager$FinalBuilder.class */
    public static final class FinalBuilder extends Builder<FinalBuilder> {
        private FinalBuilder() {
        }

        @Override // xaero.lib.common.config.option.ConfigOptionManager.Builder
        protected ConfigOptionManager buildInternally(Map<String, ConfigOption<?>> options) {
            return new ConfigOptionManager(this.logger, options);
        }

        public static FinalBuilder begin() {
            return new FinalBuilder().setDefault();
        }
    }
}
