package xaero.lib.common.config.io.util;

import java.nio.file.Path;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/io/util/ConfigIOUtils.class */
public class ConfigIOUtils {
    public static Path getLargeValueFilePath(Path originalConfigFile, String subFolder, String extension) {
        return originalConfigFile.resolveSibling(subFolder).resolve(String.valueOf(originalConfigFile.getFileName()) + extension);
    }
}
