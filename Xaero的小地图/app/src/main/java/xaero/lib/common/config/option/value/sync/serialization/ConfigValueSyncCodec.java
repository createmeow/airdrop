package xaero.lib.common.config.option.value.sync.serialization;

import java.util.function.Function;
import net.minecraft.nbt.Tag;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/sync/serialization/ConfigValueSyncCodec.class */
public class ConfigValueSyncCodec<T, N extends Tag> {
    private final Function<T, N> encoder;
    private final Function<N, T> decoder;

    public ConfigValueSyncCodec(Function<T, N> encoder, Function<N, T> decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public N encode(T value) {
        return this.encoder.apply(value);
    }

    public T decode(N tag) {
        return this.decoder.apply(tag);
    }
}
