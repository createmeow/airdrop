package xaero.lib.common.compat.prometheus;

import earth.terrarium.prometheus.api.roles.options.RoleOption;
import earth.terrarium.prometheus.api.roles.options.RoleOptionSerializer;
import java.util.HashMap;
import java.util.Map;
import xaero.lib.common.compat.ModCompatibility;
import xaero.lib.common.permission.PermissionNode;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/compat/prometheus/PrometheusOptions.class */
public class PrometheusOptions implements RoleOption<PrometheusOptions> {
    private final String modId;
    private final Map<PermissionNode<?>, Object> values;

    private PrometheusOptions(String modId, Map<PermissionNode<?>, Object> values) {
        this.modId = modId;
        this.values = values;
    }

    public RoleOptionSerializer<PrometheusOptions> serializer() {
        return ModCompatibility.getInstance().getPrometheus().getModData(this.modId).getSerializer();
    }

    public <T> T get(PermissionNode<T> permissionNode) {
        return (T) this.values.get(permissionNode);
    }

    public <T> void put(PermissionNode<T> permission, T value) {
        if (value == null) {
            this.values.remove(permission);
        } else {
            this.values.put(permission, value);
        }
    }

    public Iterable<PermissionNode<?>> getKeys() {
        return this.values.keySet();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/compat/prometheus/PrometheusOptions$Builder.class */
    public static final class Builder {
        private String modId;

        private Builder() {
        }

        public Builder setDefault() {
            setModId(null);
            return this;
        }

        public Builder setModId(String modId) {
            this.modId = modId;
            return this;
        }

        public PrometheusOptions build() {
            if (this.modId == null) {
                throw new IllegalStateException();
            }
            Map<PermissionNode<?>, Object> values = new HashMap<>();
            return new PrometheusOptions(this.modId, values);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
