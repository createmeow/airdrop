package xaero.lib.common.config.option;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;
import xaero.lib.common.config.option.value.type.ConfigValueType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/ConfigOption.class */
public class ConfigOption<T> {
    private final ConfigValueType<T> valueType;
    private final String id;
    private final T defaultValue;
    private final boolean shouldSaveDefaultValue;
    private final boolean overridable;
    private final boolean skipConfigReset;
    private final Predicate<T> validator;
    private final Component displayName;
    private final Component tooltip;
    private final BiFunction<ConfigOption<T>, T, Component> displayGetter;

    protected ConfigOption(ConfigValueType<T> valueType, String id, T defaultValue, boolean shouldSaveDefaultValue, boolean overridable, boolean skipConfigReset, Predicate<T> validator, Component displayName, Component tooltip, BiFunction<ConfigOption<T>, T, Component> displayGetter) {
        this.valueType = valueType;
        this.id = id;
        this.defaultValue = defaultValue;
        this.shouldSaveDefaultValue = shouldSaveDefaultValue;
        this.overridable = overridable;
        this.skipConfigReset = skipConfigReset;
        this.validator = validator;
        this.displayName = displayName;
        this.tooltip = tooltip;
        this.displayGetter = displayGetter;
    }

    public ConfigValueType<T> getValueType() {
        return this.valueType;
    }

    public String getId() {
        return this.id;
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    public Component getTooltip() {
        return this.tooltip;
    }

    public BiFunction<ConfigOption<T>, T, Component> getDisplayGetter() {
        return this.displayGetter;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public boolean shouldSaveDefaultValue() {
        return this.shouldSaveDefaultValue;
    }

    public boolean isOverridable() {
        return this.overridable;
    }

    public boolean skipConfigReset() {
        return this.skipConfigReset;
    }

    public boolean isValidValue(T value) {
        return this.validator == null || this.validator.test(value);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/ConfigOption$Builder.class */
    public static abstract class Builder<T, B extends Builder<T, B>> {
        protected final B self = this;
        protected ConfigValueType<T> valueType;
        protected String id;
        protected T defaultValue;
        protected boolean shouldSaveDefaultValue;
        protected boolean overridable;
        protected boolean skipConfigReset;
        protected Predicate<T> validator;
        protected Component displayName;
        protected Component tooltip;
        protected BiFunction<ConfigOption<T>, T, Component> displayGetter;

        protected abstract ConfigOption<T> buildInternally();

        protected Builder() {
        }

        public B setDefault() {
            setValueType(null);
            setId(null);
            setDefaultValue(null);
            setShouldSaveDefaultValue(true);
            setOverridable(true);
            setSkipConfigReset(false);
            setValidator(null);
            setDisplayName(null);
            setTooltip(null);
            setDisplayGetter(null);
            return this.self;
        }

        public B setValueType(ConfigValueType<T> valueType) {
            this.valueType = valueType;
            return this.self;
        }

        public B setId(String id) {
            this.id = id;
            return this.self;
        }

        public B setDefaultValue(T defaultValue) {
            this.defaultValue = defaultValue;
            return this.self;
        }

        public B setShouldSaveDefaultValue(boolean shouldSaveDefaultValue) {
            this.shouldSaveDefaultValue = shouldSaveDefaultValue;
            return this.self;
        }

        public B setOverridable(boolean overridable) {
            this.overridable = overridable;
            return this.self;
        }

        public B setSkipConfigReset(boolean skipConfigReset) {
            this.skipConfigReset = skipConfigReset;
            return this.self;
        }

        public B setValidator(Predicate<T> validator) {
            this.validator = validator;
            return this.self;
        }

        public B setDisplayName(Component displayName) {
            this.displayName = displayName;
            return this.self;
        }

        public B setTooltip(Component tooltip) {
            this.tooltip = tooltip;
            return this.self;
        }

        public B setDisplayGetter(BiFunction<ConfigOption<T>, T, Component> displayGetter) {
            this.displayGetter = displayGetter;
            return this.self;
        }

        public ConfigOption<T> build(List<ConfigOption<?>> destination) {
            if (this.valueType == null || this.id == null || this.defaultValue == null) {
                throw new IllegalStateException();
            }
            if (this.displayGetter == null) {
                setDisplayGetter((c, v) -> {
                    return Component.literal(v.toString());
                });
            }
            ConfigOption<T> result = buildInternally();
            if (destination != null) {
                destination.add(result);
            }
            return result;
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/ConfigOption$FinalBuilder.class */
    public static final class FinalBuilder<T> extends Builder<T, FinalBuilder<T>> {
        private FinalBuilder() {
        }

        @Override // xaero.lib.common.config.option.ConfigOption.Builder
        protected ConfigOption<T> buildInternally() {
            return new ConfigOption<>(this.valueType, this.id, this.defaultValue, this.shouldSaveDefaultValue, this.overridable, this.skipConfigReset, this.validator, this.displayName, this.tooltip, this.displayGetter);
        }

        public static <T> FinalBuilder<T> begin() {
            return new FinalBuilder().setDefault();
        }
    }
}
