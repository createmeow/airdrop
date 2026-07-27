package xaero.lib.common.config.option;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import net.minecraft.network.chat.Component;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.value.type.ConfigValueType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/IndexedConfigOption.class */
public class IndexedConfigOption<T> extends ConfigOption<T> {
    private final List<T> validValues;

    protected IndexedConfigOption(ConfigValueType<T> valueType, String id, T defaultValue, boolean shouldSaveDefaultValue, boolean overridable, boolean skipConfigReset, Predicate<T> validator, Component displayName, Component tooltip, BiFunction<ConfigOption<T>, T, Component> labelGetter, List<T> validValues) {
        super(valueType, id, defaultValue, shouldSaveDefaultValue, overridable, skipConfigReset, validator, displayName, tooltip, labelGetter);
        this.validValues = validValues;
    }

    public List<T> getValidValues() {
        return this.validValues;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/IndexedConfigOption$Builder.class */
    public static abstract class Builder<T, B extends Builder<T, B>> extends ConfigOption.Builder<T, B> {
        protected int minIndex;
        protected int maxIndex;
        protected IntFunction<T> indexValueGetter;

        protected abstract IndexedConfigOption<T> buildInternally(List<T> list);

        @Override // xaero.lib.common.config.option.ConfigOption.Builder
        public /* bridge */ /* synthetic */ ConfigOption build(List list) {
            return build((List<ConfigOption<?>>) list);
        }

        protected Builder() {
        }

        @Override // xaero.lib.common.config.option.ConfigOption.Builder
        public B setDefault() {
            setMaxIndex(0);
            setMinIndex(0);
            setIndexValueGetter(null);
            return (B) super.setDefault();
        }

        public B setMinIndex(int minIndex) {
            this.minIndex = minIndex;
            return (B) this.self;
        }

        public B setMaxIndex(int maxIndex) {
            this.maxIndex = maxIndex;
            return (B) this.self;
        }

        public B setIndexValueGetter(IntFunction<T> indexValueGetter) {
            this.indexValueGetter = indexValueGetter;
            return (B) this.self;
        }

        @Override // xaero.lib.common.config.option.ConfigOption.Builder
        public IndexedConfigOption<T> build(List<ConfigOption<?>> destination) {
            if (this.indexValueGetter == null) {
                throw new IllegalStateException();
            }
            return (IndexedConfigOption) super.build(destination);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.lib.common.config.option.ConfigOption.Builder
        public final IndexedConfigOption<T> buildInternally() {
            List<T> validValues = IntStream.rangeClosed(this.minIndex, this.maxIndex).mapToObj(this.indexValueGetter).toList();
            if (this.validator == null) {
                Set<T> validValueSet = new HashSet<>(validValues);
                Objects.requireNonNull(validValueSet);
                setValidator(validValueSet::contains);
            }
            return buildInternally(validValues);
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/IndexedConfigOption$FinalBuilder.class */
    public static final class FinalBuilder<T> extends Builder<T, FinalBuilder<T>> {
        private FinalBuilder() {
        }

        public static <T> FinalBuilder<T> begin() {
            return new FinalBuilder().setDefault();
        }

        @Override // xaero.lib.common.config.option.IndexedConfigOption.Builder
        protected IndexedConfigOption<T> buildInternally(List<T> validValues) {
            return new IndexedConfigOption<>(this.valueType, this.id, this.defaultValue, this.shouldSaveDefaultValue, this.overridable, this.skipConfigReset, this.validator, this.displayName, this.tooltip, this.displayGetter, validValues);
        }
    }
}
