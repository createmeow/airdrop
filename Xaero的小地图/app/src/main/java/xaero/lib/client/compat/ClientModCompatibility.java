package xaero.lib.client.compat;

import java.util.HashMap;
import xaero.lib.client.compat.prometheus.PrometheusClientCompat;
import xaero.lib.common.compat.ModCompatibility;
import xaero.lib.common.compat.prometheus.PrometheusCompat;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/compat/ClientModCompatibility.class */
public class ClientModCompatibility extends ModCompatibility {
    @Override // xaero.lib.common.compat.ModCompatibility
    protected PrometheusCompat createPrometheus() {
        return new PrometheusClientCompat(new HashMap());
    }

    public static ClientModCompatibility getInstance() {
        return (ClientModCompatibility) ModCompatibility.getInstance();
    }
}
