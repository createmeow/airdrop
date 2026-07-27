package xaero.lib.common.compat.prometheus;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import xaero.lib.common.compat.prometheus.PrometheusOptions;
import xaero.lib.common.permission.PermissionNode;
import xaero.lib.common.permission.PermissionRegistry;
import xaero.lib.common.util.JsonUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/compat/prometheus/PrometheusOptionsCodec.class */
public class PrometheusOptionsCodec implements Codec<PrometheusOptions> {
    private final String modId;

    public /* bridge */ /* synthetic */ DataResult encode(Object obj, DynamicOps dynamicOps, Object obj2) {
        return encode((PrometheusOptions) obj, (DynamicOps<DynamicOps>) dynamicOps, (DynamicOps) obj2);
    }

    public PrometheusOptionsCodec(String modId) {
        this.modId = modId;
    }

    public <T> DataResult<T> encode(PrometheusOptions input, DynamicOps<T> ops, T prefix) {
        Object objCreateMap = ops.createMap(new LinkedHashMap());
        for (PermissionNode<?> node : input.getKeys()) {
            Object objOrElse = ops.mergeToMap(objCreateMap, ops.createString(node.getDefaultPath()), encodeValue(input, ops, node)).result().orElse(null);
            if (objOrElse != null) {
                objCreateMap = objOrElse;
            }
        }
        return DataResult.success(objCreateMap);
    }

    private <T, V> T encodeValue(PrometheusOptions prometheusOptions, DynamicOps<T> dynamicOps, PermissionNode<V> permissionNode) {
        Object obj = prometheusOptions.get(permissionNode);
        if (obj == null) {
            return null;
        }
        if (permissionNode.getType() == Boolean.class) {
            return (T) dynamicOps.createBoolean(((Boolean) obj).booleanValue());
        }
        if (permissionNode.getType() == String.class) {
            return (T) dynamicOps.createString((String) obj);
        }
        if (permissionNode.getType() == Component.class) {
            String json = JsonUtils.toJson((Component) obj);
            if (json == null) {
                return null;
            }
            return (T) dynamicOps.createString(json);
        }
        if (permissionNode.getType() == Integer.class) {
            return (T) dynamicOps.createInt(((Integer) obj).intValue());
        }
        if (permissionNode.getType() == Byte.class) {
            return (T) dynamicOps.createByte(((Byte) obj).byteValue());
        }
        if (permissionNode.getType() == Double.class) {
            return (T) dynamicOps.createDouble(((Double) obj).doubleValue());
        }
        if (permissionNode.getType() == Long.class) {
            return (T) dynamicOps.createLong(((Long) obj).longValue());
        }
        throw new IllegalArgumentException("Unsupported permission node type: " + String.valueOf(permissionNode.getType()));
    }

    public <T> DataResult<Pair<PrometheusOptions, T>> decode(DynamicOps<T> ops, T input) {
        PrometheusOptions result = PrometheusOptions.Builder.begin().setModId(this.modId).build();
        Consumer<BiConsumer<T, T>> mapEntriesResult = (Consumer) ops.getMapEntries(input).result().orElse(null);
        if (mapEntriesResult == null) {
            return DataResult.success(Pair.of(result, input));
        }
        mapEntriesResult.accept((key, value) -> {
            PermissionNode<?> node;
            String keyString = (String) ops.getStringValue(key).result().orElse(null);
            if (keyString == null || (node = PermissionRegistry.INSTANCE.getNode(this.modId, keyString)) == null) {
                return;
            }
            addValue(result, ops, value, node);
        });
        return DataResult.success(Pair.of(result, input));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T, V> void addValue(PrometheusOptions prometheusOptions, DynamicOps<T> ops, T valueTag, PermissionNode<V> node) {
        prometheusOptions.put(node, decodeValue(ops, valueTag, node));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v20, types: [net.minecraft.network.chat.Component] */
    private <T, V> V decodeValue(DynamicOps<T> dynamicOps, T t, PermissionNode<V> permissionNode) {
        if (permissionNode.getType() == Boolean.class) {
            return (V) dynamicOps.getBooleanValue(t).result().orElse(Boolean.FALSE);
        }
        if (permissionNode.getType() == String.class) {
            return (V) dynamicOps.getStringValue(t).result().orElse(null);
        }
        if (permissionNode.getType() == Component.class) {
            String str = (String) dynamicOps.getStringValue(t).result().orElse(null);
            V vFromJson = null;
            if (str != null) {
                vFromJson = JsonUtils.fromJson(str);
            }
            return vFromJson;
        }
        if (Number.class.isAssignableFrom(permissionNode.getType())) {
            return (V) decodeNumber(dynamicOps, t, permissionNode);
        }
        throw new IllegalArgumentException("Unsupported permission node type: " + String.valueOf(permissionNode.getType()));
    }

    private <T, V> V decodeNumber(DynamicOps<T> dynamicOps, T t, PermissionNode<V> permissionNode) {
        Number number = (Number) dynamicOps.getNumberValue(t).result().orElse(null);
        if (number == null) {
            return null;
        }
        if (permissionNode.getType() == Integer.class) {
            return (V) Integer.valueOf(number.intValue());
        }
        if (permissionNode.getType() == Double.class) {
            return (V) Double.valueOf(number.doubleValue());
        }
        if (permissionNode.getType() == Long.class) {
            return (V) Long.valueOf(number.longValue());
        }
        if (permissionNode.getType() == Byte.class) {
            return (V) Byte.valueOf(number.byteValue());
        }
        return null;
    }
}
