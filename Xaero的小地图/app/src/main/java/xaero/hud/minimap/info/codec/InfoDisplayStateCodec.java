package xaero.hud.minimap.info.codec;

import java.util.function.Function;
import xaero.lib.common.config.option.value.io.serialization.ConfigValueIOCodec;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/info/codec/InfoDisplayStateCodec.class */
public class InfoDisplayStateCodec<T> extends ConfigValueIOCodec<T> {
    public InfoDisplayStateCodec(Function<String, T> decoder, Function<T, String> encoder, int maxStringLength) {
        super(encoder, decoder, maxStringLength);
    }
}
