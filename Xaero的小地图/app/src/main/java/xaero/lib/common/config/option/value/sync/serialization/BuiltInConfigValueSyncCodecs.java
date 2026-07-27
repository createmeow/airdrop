package xaero.lib.common.config.option.value.sync.serialization;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/sync/serialization/BuiltInConfigValueSyncCodecs.class */
public class BuiltInConfigValueSyncCodecs {
    public static final ConfigValueSyncCodec<Boolean, ByteTag> BOOLEAN = new ConfigValueSyncCodec<>((v0) -> {
        return ByteTag.valueOf(v0);
    }, tag -> {
        return Boolean.valueOf(tag.getAsByte() == 1);
    });
    public static final ConfigValueSyncCodec<Integer, IntTag> INTEGER = new ConfigValueSyncCodec<>((v0) -> {
        return IntTag.valueOf(v0);
    }, (v0) -> {
        return v0.getAsInt();
    });
    public static final ConfigValueSyncCodec<Double, DoubleTag> DOUBLE = new ConfigValueSyncCodec<>((v0) -> {
        return DoubleTag.valueOf(v0);
    }, (v0) -> {
        return v0.getAsDouble();
    });
    public static final ConfigValueSyncCodec<String, StringTag> STRING = new ConfigValueSyncCodec<>(StringTag::valueOf, (v0) -> {
        return v0.getAsString();
    });
    public static final ConfigValueSyncCodec<ResourceLocation, StringTag> RESOURCE_LOCATION = new ConfigValueSyncCodec<>(rl -> {
        return STRING.encode(rl.toString());
    }, tag -> {
        return ResourceLocation.parse(STRING.decode(tag));
    });
}
