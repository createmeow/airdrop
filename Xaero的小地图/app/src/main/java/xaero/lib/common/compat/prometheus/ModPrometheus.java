package xaero.lib.common.compat.prometheus;

import earth.terrarium.prometheus.api.roles.options.RoleOptionSerializer;
import net.minecraft.resources.ResourceLocation;
import xaero.lib.common.compat.prometheus.PrometheusOptions;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/compat/prometheus/ModPrometheus.class */
public class ModPrometheus {
    private final String modId;
    private final RoleOptionSerializer<PrometheusOptions> serializer;

    private ModPrometheus(String modId, RoleOptionSerializer<PrometheusOptions> serializer) {
        this.modId = modId;
        this.serializer = serializer;
    }

    public String getModId() {
        return this.modId;
    }

    public RoleOptionSerializer<PrometheusOptions> getSerializer() {
        return this.serializer;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/compat/prometheus/ModPrometheus$Builder.class */
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

        public ModPrometheus build() {
            if (this.modId == null) {
                throw new IllegalStateException();
            }
            PrometheusOptionsCodec codec = new PrometheusOptionsCodec(this.modId);
            RoleOptionSerializer<PrometheusOptions> serializer = RoleOptionSerializer.of(ResourceLocation.fromNamespaceAndPath(this.modId, "options"), 1, codec, PrometheusOptions.Builder.begin().setModId(this.modId).build());
            return new ModPrometheus(this.modId, serializer);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
