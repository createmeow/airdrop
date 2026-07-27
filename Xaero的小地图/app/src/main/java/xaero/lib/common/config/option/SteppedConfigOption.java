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
import xaero.lib.common.util.MathUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/SteppedConfigOption.class */
public final class SteppedConfigOption extends IndexedConfigOption<Double> {
    private final int precision;

    private SteppedConfigOption(ConfigValueType<Double> valueType, String id, Double defaultValue, boolean shouldSaveDefaultValue, boolean overridable, boolean skipConfigReset, Predicate<Double> validator, Component displayName, Component tooltip, BiFunction<ConfigOption<Double>, Double, Component> labelGetter, List<Double> validValues, int precision) {
        super(valueType, id, defaultValue, shouldSaveDefaultValue, overridable, skipConfigReset, validator, displayName, tooltip, labelGetter, validValues);
        this.precision = precision;
    }

    public int getPrecision() {
        return this.precision;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/SteppedConfigOption$Builder.class */
    public static final class Builder extends IndexedConfigOption.Builder<Double, Builder> {
        private double minValue;
        private double maxValue;
        private double step;
        private int precision;
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
            super.setDefault();
            setMinValue(0.0d);
            setMaxValue(0.0d);
            setStep(0.0d);
            setRangeValidator(false);
            setValueType(BuiltInConfigValueTypes.DOUBLE);
            setDisplayGetter((v0, v1) -> {
                return ConfigUtils.getDisplayForSimpleNumber(v0, v1);
            });
            return (Builder) this.self;
        }

        public Builder setMinValue(double minValue) {
            this.minValue = minValue;
            return (Builder) this.self;
        }

        public Builder setMaxValue(double maxValue) {
            this.maxValue = maxValue;
            return (Builder) this.self;
        }

        public Builder setStep(double step) {
            this.step = step;
            return (Builder) this.self;
        }

        public Builder setRangeValidator(boolean rangeValidator) {
            this.rangeValidator = rangeValidator;
            return (Builder) this.self;
        }

        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder
        public Builder setMinIndex(int minIndex) {
            if (minIndex != 0) {
                throw new IllegalArgumentException("Cannot set a custom min index for stepped config options!");
            }
            return (Builder) super.setMinIndex(minIndex);
        }

        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder
        public Builder setMaxIndex(int maxIndex) {
            if (maxIndex != 0) {
                throw new IllegalArgumentException("Cannot set a custom max index for stepped config options!");
            }
            return (Builder) super.setMaxIndex(maxIndex);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder
        public Builder setIndexValueGetter(IntFunction<Double> indexValueGetter) {
            if (indexValueGetter != null) {
                throw new IllegalArgumentException("Cannot set a custom index value getter for stepped config options!");
            }
            return (Builder) super.setIndexValueGetter((IntFunction) indexValueGetter);
        }

        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder, xaero.lib.common.config.option.ConfigOption.Builder
        public SteppedConfigOption build(List<ConfigOption<?>> destination) {
            double finalMaxValue = this.maxValue;
            double finalMinValue = this.minValue;
            double finalStep = this.step;
            int stepPrecision = MathUtils.getDecimalCountAfterDot(finalStep, 5);
            int minValuePrecision = MathUtils.getDecimalCountAfterDot(finalMinValue, 5);
            int maxValuePrecision = MathUtils.getDecimalCountAfterDot(finalMaxValue, 5);
            this.precision = Math.max(Math.max(stepPrecision, minValuePrecision), maxValuePrecision);
            int finalPrecision = this.precision;
            super.setMinIndex(0);
            super.setMaxIndex((int) Math.ceil((finalMaxValue - finalMinValue) / finalStep));
            super.setIndexValueGetter(index -> {
                return Double.valueOf(MathUtils.floor(Math.min(finalMinValue + (index * finalStep), finalMaxValue), finalPrecision));
            });
            if (this.rangeValidator) {
                Predicate<Double> rangeTest = d -> {
                    return d.doubleValue() >= finalMinValue && d.doubleValue() <= finalMaxValue;
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
            return (SteppedConfigOption) super.build(destination);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder
        /* renamed from: buildInternally */
        public IndexedConfigOption<Double> buildInternally2(List<Double> validValues) {
            return new SteppedConfigOption(this.valueType, this.id, (Double) this.defaultValue, this.shouldSaveDefaultValue, this.overridable, this.skipConfigReset, this.validator, this.displayName, this.tooltip, this.displayGetter, validValues, this.precision);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
