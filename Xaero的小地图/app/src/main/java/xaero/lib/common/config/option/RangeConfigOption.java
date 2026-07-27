package xaero.lib.common.config.option;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;
import xaero.lib.common.config.option.IndexedConfigOption;
import xaero.lib.common.config.option.value.type.BuiltInConfigValueTypes;
import xaero.lib.common.config.option.value.type.ConfigValueType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/RangeConfigOption.class */
public class RangeConfigOption extends IndexedConfigOption<Integer> {
    private RangeConfigOption(ConfigValueType<Integer> valueType, String id, Integer defaultValue, boolean shouldSaveDefaultValue, boolean overridable, boolean skipConfigReset, Predicate<Integer> validator, Component displayName, Component tooltip, BiFunction<ConfigOption<Integer>, Integer, Component> labelGetter, List<Integer> validIndices) {
        super(valueType, id, defaultValue, shouldSaveDefaultValue, overridable, skipConfigReset, validator, displayName, tooltip, labelGetter, validIndices);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/RangeConfigOption$Builder.class */
    public static final class Builder extends IndexedConfigOption.Builder<Integer, Builder> {
        private boolean rangeValidator;

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
            Builder result = (Builder) super.setDefault();
            setValueType(BuiltInConfigValueTypes.INTEGER);
            setRangeValidator(true);
            return result;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder
        public Builder setIndexValueGetter(IntFunction<Integer> indexValueGetter) {
            if (indexValueGetter != null) {
                throw new IllegalArgumentException("Cannot set a custom index value getter for range config options!");
            }
            return (Builder) super.setIndexValueGetter((IntFunction) indexValueGetter);
        }

        public Builder setRangeValidator(boolean rangeValidator) {
            this.rangeValidator = rangeValidator;
            return (Builder) this.self;
        }

        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder, xaero.lib.common.config.option.ConfigOption.Builder
        public RangeConfigOption build(List<ConfigOption<?>> destination) {
            super.setIndexValueGetter(index -> {
                return Integer.valueOf(index);
            });
            if (this.rangeValidator) {
                int finalMinIndex = this.minIndex;
                int finalMaxIndex = this.maxIndex;
                Predicate<Integer> rangeTest = d -> {
                    return d.intValue() >= finalMinIndex && d.intValue() <= finalMaxIndex;
                };
                Predicate<T> predicate = this.validator;
                if (predicate == 0) {
                    setValidator(rangeTest);
                } else {
                    setValidator(d2 -> {
                        return predicate.test(d2) && rangeTest.test(d2);
                    });
                }
            }
            return (RangeConfigOption) super.build(destination);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder
        /* renamed from: buildInternally */
        public IndexedConfigOption<Integer> buildInternally2(List<Integer> validValues) {
            return new RangeConfigOption(this.valueType, this.id, (Integer) this.defaultValue, this.shouldSaveDefaultValue, this.overridable, this.skipConfigReset, this.validator, this.displayName, this.tooltip, this.displayGetter, validValues);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
