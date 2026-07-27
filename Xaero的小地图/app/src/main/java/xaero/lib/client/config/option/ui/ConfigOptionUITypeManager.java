package xaero.lib.client.config.option.ui;

import java.util.HashMap;
import java.util.Map;
import xaero.lib.client.config.option.ui.type.ConfigOptionUIType;
import xaero.lib.common.config.option.ConfigOption;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/ui/ConfigOptionUITypeManager.class */
public class ConfigOptionUITypeManager {
    private final Map<ConfigOption<?>, ConfigOptionUIType<?>> uiTypes;

    private ConfigOptionUITypeManager(Map<ConfigOption<?>, ConfigOptionUIType<?>> uiTypes) {
        this.uiTypes = uiTypes;
    }

    public <CT extends ConfigOption<?>> void registerUIType(CT option, ConfigOptionUIType<? super CT> uiType) {
        if (this.uiTypes.containsKey(option)) {
            throw new IllegalArgumentException("Duplicate config option UI type register!");
        }
        this.uiTypes.put(option, uiType);
    }

    public <CT extends ConfigOption<?>> ConfigOptionUIType<? super CT> getUIType(CT option) {
        return (ConfigOptionUIType) this.uiTypes.get(option);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/ui/ConfigOptionUITypeManager$Builder.class */
    public static final class Builder {
        private Builder() {
        }

        public Builder setDefault() {
            return this;
        }

        public ConfigOptionUITypeManager build() {
            return new ConfigOptionUITypeManager(new HashMap());
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
