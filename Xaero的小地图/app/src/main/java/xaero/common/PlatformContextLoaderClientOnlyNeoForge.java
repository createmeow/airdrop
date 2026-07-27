package xaero.common;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/PlatformContextLoaderClientOnlyNeoForge.class */
public class PlatformContextLoaderClientOnlyNeoForge extends PlatformContextLoaderClientOnly {
    @Override // xaero.common.PlatformContextLoaderClientOnly
    public void preInit(String modId, IXaeroMinimap modMain) {
        ModContainer modContainer = (ModContainer) ModList.get().getModContainerById(modId).get();
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, (mod, current) -> {
            return modMain.getGuiHelper().getMainSettingsScreen(current);
        });
    }
}
