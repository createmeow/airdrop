package xaero.common.platform;

import java.util.ServiceLoader;
import xaero.common.platform.services.IPlatformHelper;
import xaero.hud.minimap.MinimapLogs;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/platform/Services.class */
public class Services {
    public static final IPlatformHelper PLATFORM = (IPlatformHelper) load(IPlatformHelper.class);

    public static <T> T load(Class<T> cls) {
        T t = (T) ServiceLoader.load(cls).findFirst().orElseThrow(() -> {
            return new NullPointerException("Failed to load service for " + cls.getName());
        });
        MinimapLogs.LOGGER.debug("Loaded {} for service {}", t, cls);
        return t;
    }
}
