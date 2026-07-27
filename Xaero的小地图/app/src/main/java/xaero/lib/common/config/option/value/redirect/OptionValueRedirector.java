package xaero.lib.common.config.option.value.redirect;

import java.util.function.Predicate;
import java.util.function.Supplier;
import xaero.lib.common.config.channel.ConfigChannel;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/redirect/OptionValueRedirector.class */
public class OptionValueRedirector<T> {
    private final Supplier<T> supplier;
    private final Predicate<ConfigChannel> condition;

    protected OptionValueRedirector(Supplier<T> supplier, Predicate<ConfigChannel> condition) {
        this.supplier = supplier;
        this.condition = condition;
    }

    public T getValue() {
        return this.supplier.get();
    }

    public boolean shouldRedirect(ConfigChannel channel) {
        return this.condition.test(channel);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/redirect/OptionValueRedirector$Builder.class */
    public static abstract class Builder<T, B extends Builder<T, B>> {
        protected final B self = this;
        protected Supplier<T> supplier;
        protected Predicate<ConfigChannel> condition;

        protected abstract OptionValueRedirector<T> buildInternally();

        protected Builder() {
        }

        public B setDefault() {
            setSupplier(null);
            setCondition(null);
            return this.self;
        }

        public B setSupplier(Supplier<T> supplier) {
            this.supplier = supplier;
            return this.self;
        }

        public B setCondition(Predicate<ConfigChannel> condition) {
            this.condition = condition;
            return this.self;
        }

        public OptionValueRedirector<T> build() {
            if (this.supplier == null || this.condition == null) {
                throw new IllegalStateException();
            }
            return buildInternally();
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/redirect/OptionValueRedirector$FinalBuilder.class */
    public static final class FinalBuilder<T> extends Builder<T, FinalBuilder<T>> {
        private FinalBuilder() {
        }

        @Override // xaero.lib.common.config.option.value.redirect.OptionValueRedirector.Builder
        protected OptionValueRedirector<T> buildInternally() {
            return new OptionValueRedirector<>(this.supplier, this.condition);
        }

        public static <T> FinalBuilder<T> begin() {
            return new FinalBuilder().setDefault();
        }
    }
}
