package xaero.common.platform.services;

import java.nio.file.Path;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.fml.loading.LoadingModList;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/platform/services/NeoForgePlatformHelper.class */
public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override // xaero.common.platform.services.IPlatformHelper
    public String getPlatformName() {
        return "NeoForge";
    }

    @Override // xaero.common.platform.services.IPlatformHelper
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override // xaero.common.platform.services.IPlatformHelper
    public boolean checkModForMixin(String modId) {
        return LoadingModList.get().getModFileById(modId) != null;
    }

    @Override // xaero.common.platform.services.IPlatformHelper
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override // xaero.common.platform.services.IPlatformHelper
    public boolean isDedicatedServer() {
        return FMLLoader.getDist() == Dist.DEDICATED_SERVER;
    }

    @Override // xaero.common.platform.services.IPlatformHelper
    public Path getGameDir() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override // xaero.common.platform.services.IPlatformHelper
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override // xaero.common.platform.services.IPlatformHelper
    public Path getModFile(String modId) {
        return FMLLoader.getLoadingModList().getModFileById(modId).getFile().getFilePath();
    }
}
