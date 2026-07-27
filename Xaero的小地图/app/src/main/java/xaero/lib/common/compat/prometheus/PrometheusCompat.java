package xaero.lib.common.compat.prometheus;

import earth.terrarium.prometheus.api.roles.options.RoleOptionsApi;
import java.util.Map;
import xaero.lib.common.compat.prometheus.ModPrometheus;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/compat/prometheus/PrometheusCompat.class */
public class PrometheusCompat {
    protected final Map<String, ModPrometheus> modPrometheusMap;
    private boolean frozen;

    public PrometheusCompat(Map<String, ModPrometheus> modPrometheusMap) {
        this.modPrometheusMap = modPrometheusMap;
    }

    public PrometheusSystem createPrometheusSystem() {
        return new PrometheusSystem();
    }

    public ModPrometheus getModData(String modId) {
        if (!this.frozen) {
            throw new IllegalStateException();
        }
        return this.modPrometheusMap.get(modId);
    }

    public synchronized ModPrometheus registerMod(String modId) {
        if (this.frozen) {
            throw new IllegalStateException("Mod registry is frozen!");
        }
        if (this.modPrometheusMap.containsKey(modId)) {
            throw new IllegalArgumentException("Duplicate mod id!");
        }
        ModPrometheus result = ModPrometheus.Builder.begin().setModId(modId).build();
        this.modPrometheusMap.put(modId, result);
        RoleOptionsApi.API.register(result.getSerializer());
        return result;
    }

    public void freeze() {
        this.frozen = true;
    }
}
