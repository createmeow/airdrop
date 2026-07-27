package xaero.lib.client.config.option.ui.type;

import xaero.lib.client.config.option.ui.factory.IConfigOptionWidgetFactory;
import xaero.lib.common.config.option.ConfigOption;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/ui/type/ConfigOptionUIType.class */
public final class ConfigOptionUIType<CT extends ConfigOption<?>> {
    private final IConfigOptionWidgetFactory<CT> widgetFactory;

    public ConfigOptionUIType(IConfigOptionWidgetFactory<CT> widgetFactory) {
        this.widgetFactory = widgetFactory;
    }

    public IConfigOptionWidgetFactory<CT> getWidgetFactory() {
        return this.widgetFactory;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/ui/type/ConfigOptionUIType$Builder.class */
    public static final class Builder<CT extends ConfigOption<?>> {
        private IConfigOptionWidgetFactory<CT> widgetFactory;

        private Builder() {
        }

        public Builder<CT> setDefault() {
            setWidgetFactory(null);
            return this;
        }

        public Builder<CT> setWidgetFactory(IConfigOptionWidgetFactory<CT> widgetFactory) {
            this.widgetFactory = widgetFactory;
            return this;
        }

        public ConfigOptionUIType<CT> build() {
            if (this.widgetFactory == null) {
                throw new IllegalStateException();
            }
            return new ConfigOptionUIType<>(this.widgetFactory);
        }

        public static <CT extends ConfigOption<?>> Builder<CT> begin() {
            return new Builder().setDefault();
        }
    }
}
