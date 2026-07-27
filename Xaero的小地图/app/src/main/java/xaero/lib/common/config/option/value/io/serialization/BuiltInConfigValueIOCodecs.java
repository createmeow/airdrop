package xaero.lib.common.config.option.value.io.serialization;

import net.minecraft.resources.ResourceLocation;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/io/serialization/BuiltInConfigValueIOCodecs.class */
public class BuiltInConfigValueIOCodecs {
    public static final ConfigValueIOCodec<Boolean> BOOLEAN = new ConfigValueIOCodec<>(b -> {
        return b;
    }, s -> {
        return Boolean.valueOf(s.equals("true"));
    }, 5);
    public static final ConfigValueIOCodec<Integer> INTEGER = new ConfigValueIOCodec<>(i -> {
        return i;
    }, Integer::parseInt, "-2147483648".length());
    public static final ConfigValueIOCodec<Double> DOUBLE = new ConfigValueIOCodec<>(d -> {
        return d;
    }, Double::parseDouble, 100);
    public static final ConfigValueIOCodec<ResourceLocation> RESOURCE_LOCATION = new ConfigValueIOCodec<>((v0) -> {
        return v0.toString();
    }, ResourceLocation::parse, 200);

    public static ConfigValueIOCodec<String> getString(int maxLength) {
        return new ConfigValueIOCodec<>(s -> {
            return s;
        }, s2 -> {
            return s2;
        }, maxLength);
    }
}
