package xaero.common.config;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import xaero.common.IXaeroMinimap;
import xaero.common.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/config/LegacyCommonConfigInit.class */
public class LegacyCommonConfigInit {
    public void init(IXaeroMinimap modMain, String configFileName) {
        Path configDestinationPath = Services.PLATFORM.getConfigDir();
        Path configPath = configDestinationPath.resolve(configFileName);
        if (Services.PLATFORM.isDedicatedServer() && !Files.exists(configPath, new LinkOption[0])) {
            Path oldConfigPath = Services.PLATFORM.getGameDir().resolve(configFileName);
            if (Files.exists(oldConfigPath, new LinkOption[0])) {
                configPath = oldConfigPath;
            }
        }
        LegacyCommonConfigIO io = new LegacyCommonConfigIO(configPath);
        modMain.setCommonConfigIO(io);
        if (Files.exists(configPath, new LinkOption[0])) {
            io.load();
        }
    }
}
