package xaero.lib.common.config.option.value.type;

import net.minecraft.resources.ResourceLocation;
import xaero.common.minimap.write.MinimapWriter;
import xaero.lib.common.config.option.value.io.serialization.BuiltInConfigValueIOCodecs;
import xaero.lib.common.config.option.value.sync.serialization.BuiltInConfigValueSyncCodecs;
import xaero.lib.common.config.option.value.type.ConfigValueType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/value/type/BuiltInConfigValueTypes.class */
public class BuiltInConfigValueTypes {
    public static ConfigValueType<Boolean> BOOLEAN = ConfigValueType.FinalBuilder.begin().setIoCodec(BuiltInConfigValueIOCodecs.BOOLEAN).setSyncCodec(BuiltInConfigValueSyncCodecs.BOOLEAN).build();
    public static ConfigValueType<Integer> INTEGER = ConfigValueType.FinalBuilder.begin().setIoCodec(BuiltInConfigValueIOCodecs.INTEGER).setSyncCodec(BuiltInConfigValueSyncCodecs.INTEGER).build();
    public static ConfigValueType<Double> DOUBLE = ConfigValueType.FinalBuilder.begin().setIoCodec(BuiltInConfigValueIOCodecs.DOUBLE).setSyncCodec(BuiltInConfigValueSyncCodecs.DOUBLE).build();
    public static ConfigValueType<ResourceLocation> RESOURCE_LOCATION = ConfigValueType.FinalBuilder.begin().setIoCodec(BuiltInConfigValueIOCodecs.RESOURCE_LOCATION).setSyncCodec(BuiltInConfigValueSyncCodecs.RESOURCE_LOCATION).build();
    public static ConfigValueType<String> UNLIMITED_STRING = getString(MinimapWriter.NO_Y_VALUE);

    public static ConfigValueType<String> getString(int maxLength) {
        return ConfigValueType.FinalBuilder.begin().setIoCodec(BuiltInConfigValueIOCodecs.getString(maxLength)).setSyncCodec(BuiltInConfigValueSyncCodecs.STRING).build();
    }
}
