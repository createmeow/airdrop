package xaero.lib.common.config.option;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;
import xaero.lib.common.config.option.IndexedConfigOption;
import xaero.lib.common.config.option.value.type.BuiltInConfigValueTypes;
import xaero.lib.common.config.option.value.type.ConfigValueType;
import xaero.lib.common.config.util.ConfigUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/BooleanConfigOption.class */
public class BooleanConfigOption extends IndexedConfigOption<Boolean> {
    private BooleanConfigOption(ConfigValueType<Boolean> valueType, String id, Boolean defaultValue, boolean shouldSaveDefaultValue, boolean overridable, boolean skipConfigReset, Predicate<Boolean> validator, Component displayName, Component tooltip, BiFunction<ConfigOption<Boolean>, Boolean, Component> labelGetter, List<Boolean> validValues) {
        super(valueType, id, defaultValue, shouldSaveDefaultValue, overridable, skipConfigReset, validator, displayName, tooltip, labelGetter, validValues);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/BooleanConfigOption$Builder.class */
    public static final class Builder extends IndexedConfigOption.Builder<Boolean, Builder> {
        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder, xaero.lib.common.config.option.ConfigOption.Builder
        public /* bridge */ /* synthetic */ IndexedConfigOption build(List list) {
            return build((List<ConfigOption<?>>) list);
        }

        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder, xaero.lib.common.config.option.ConfigOption.Builder
        public /* bridge */ /* synthetic */ ConfigOption build(List list) {
            return build((List<ConfigOption<?>>) list);
        }

        private Builder() {
        }

        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder, xaero.lib.common.config.option.ConfigOption.Builder
        public Builder setDefault() {
            super.setDefault();
            setDisplayGetter(ConfigUtils::getDisplayForBoolean);
            setValueType(BuiltInConfigValueTypes.BOOLEAN);
            return (Builder) this.self;
        }

        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder
        public Builder setMinIndex(int minIndex) {
            if (minIndex != 0) {
                throw new IllegalArgumentException("Cannot set a custom min index for boolean config options!");
            }
            return (Builder) super.setMinIndex(minIndex);
        }

        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder
        public Builder setMaxIndex(int maxIndex) {
            if (maxIndex != 0) {
                throw new IllegalArgumentException("Cannot set a custom max index for boolean config options!");
            }
            return (Builder) super.setMaxIndex(maxIndex);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder
        public Builder setIndexValueGetter(IntFunction<Boolean> indexValueGetter) {
            if (indexValueGetter != null) {
                throw new IllegalArgumentException("Cannot set a custom index value getter for boolean config options!");
            }
            return (Builder) super.setIndexValueGetter((IntFunction) indexValueGetter);
        }

        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder, xaero.lib.common.config.option.ConfigOption.Builder
        public BooleanConfigOption build(List<ConfigOption<?>> destination) {
            super.setMinIndex(0);
            super.setMaxIndex(1);
            super.setIndexValueGetter(index -> {
                return Boolean.valueOf(index > 0);
            });
            return (BooleanConfigOption) super.build(destination);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder
        /* renamed from: buildInternally, reason: merged with bridge method [inline-methods] */
        public IndexedConfigOption<Boolean> buildInternally2(List<Boolean> validValues) {
            return new BooleanConfigOption(this.valueType, this.id, (Boolean) this.defaultValue, this.shouldSaveDefaultValue, this.overridable, this.skipConfigReset, this.validator, this.displayName, this.tooltip, this.displayGetter, validValues);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
