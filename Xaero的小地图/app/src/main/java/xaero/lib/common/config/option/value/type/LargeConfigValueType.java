package xaero.lib.common.config.option.value.type;

import net.minecraft.nbt.Tag;
import xaero.lib.common.config.option.value.io.serialization.ConfigValueIOCodec;
import xaero.lib.common.config.option.value.io.serialization.LargeConfigValueIOCodec;
import xaero.lib.common.config.option.value.sync.serialization.ConfigValueSyncCodec;
import xaero.lib.common.config.option.value.type.ConfigValueType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/type/LargeConfigValueType.class */
public class LargeConfigValueType<T> extends ConfigValueType<T> {
    protected LargeConfigValueType(ConfigValueIOCodec<T> ioCodec, ConfigValueSyncCodec<T, ? extends Tag> syncCodec) {
        super(ioCodec, syncCodec);
    }

    @Override // xaero.lib.common.config.option.value.type.ConfigValueType
    public LargeConfigValueIOCodec<T> getIoCodec() {
        return (LargeConfigValueIOCodec) super.getIoCodec();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/type/LargeConfigValueType$Builder.class */
    public static final class Builder<T> extends ConfigValueType.Builder<T, Builder<T>> {
        private Builder() {
        }

        @Override // xaero.lib.common.config.option.value.type.ConfigValueType.Builder
        public Builder<T> setDefault() {
            super.setDefault();
            return (Builder) this.self;
        }

        public Builder<T> setIoCodec(LargeConfigValueIOCodec<T> ioCodec) {
            this.ioCodec = ioCodec;
            return (Builder) this.self;
        }

        @Override // xaero.lib.common.config.option.value.type.ConfigValueType.Builder
        public ConfigValueType<T> build() {
            return super.build();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.lib.common.config.option.value.type.ConfigValueType.Builder
        public LargeConfigValueType<T> buildInternally() {
            return new LargeConfigValueType<>(this.ioCodec, this.syncCodec);
        }

        public static <T> Builder<T> begin() {
            return new Builder().setDefault();
        }
    }
}
