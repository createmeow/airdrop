package xaero.lib.common.config.option.value.io.serialization;

import java.nio.file.Path;
import java.util.function.Function;
import javax.annotation.Nullable;
import xaero.lib.common.config.option.ConfigOption;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/io/serialization/ConfigValueIOCodec.class */
public class ConfigValueIOCodec<T> {
    private final Function<T, String> encoder;
    private final Function<String, T> decoder;
    private final int maxStringLength;

    public ConfigValueIOCodec(Function<T, String> encoder, Function<String, T> decoder, int maxStringLength) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.maxStringLength = maxStringLength;
    }

    public String encode(T value, @Nullable Path file, @Nullable ConfigOption<T> option) {
        String result = this.encoder.apply(value);
        if (result.length() > this.maxStringLength) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    public T decode(String valueString, @Nullable Path file, @Nullable ConfigOption<T> option) {
        if (valueString.length() > this.maxStringLength) {
            throw new IllegalArgumentException();
        }
        return this.decoder.apply(valueString);
    }

    public int getMaxStringLength() {
        return this.maxStringLength;
    }
}
