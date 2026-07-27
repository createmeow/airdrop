package xaero.lib.platform;

import java.util.ServiceLoader;
import xaero.lib.XaeroLib;
import xaero.lib.platform.services.IPlatformHelper;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/platform/Services.class */
public class Services {
    public static final IPlatformHelper PLATFORM = (IPlatformHelper) load(IPlatformHelper.class);

    public static <T> T load(Class<T> cls) {
        T t = (T) ServiceLoader.load(cls).findFirst().orElseThrow(() -> {
            return new NullPointerException("Failed to load service for " + cls.getName());
        });
        XaeroLib.LOGGER.debug("Loaded {} for service {}", t, cls);
        return t;
    }
}
