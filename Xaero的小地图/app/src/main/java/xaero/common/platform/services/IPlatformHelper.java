package xaero.common.platform.services;

import java.nio.file.Path;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/platform/services/IPlatformHelper.class */
public interface IPlatformHelper {
    String getPlatformName();

    boolean isModLoaded(String str);

    boolean isDevelopmentEnvironment();

    boolean isDedicatedServer();

    Path getGameDir();

    Path getConfigDir();

    Path getModFile(String str);

    default boolean checkModForMixin(String modId) {
        return isModLoaded(modId);
    }

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
