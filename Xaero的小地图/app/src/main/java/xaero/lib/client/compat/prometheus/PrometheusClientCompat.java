package xaero.lib.client.compat.prometheus;

import earth.terrarium.prometheus.api.roles.client.OptionDisplayApi;
import java.util.Map;
import xaero.lib.common.compat.prometheus.ModPrometheus;
import xaero.lib.common.compat.prometheus.PrometheusCompat;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/compat/prometheus/PrometheusClientCompat.class */
public class PrometheusClientCompat extends PrometheusCompat {
    public PrometheusClientCompat(Map<String, ModPrometheus> modPrometheusMap) {
        super(modPrometheusMap);
    }

    @Override // xaero.lib.common.compat.prometheus.PrometheusCompat
    public ModPrometheus registerMod(String modId) {
        ModPrometheus result = super.registerMod(modId);
        OptionDisplayApi.API.register(result.getSerializer().id(), (role, ignored) -> {
            return PrometheusOptionDisplay.create(result, role, ignored);
        });
        return result;
    }
}
