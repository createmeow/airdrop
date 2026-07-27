package xaero.common.interfaces;

import java.io.IOException;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.MinimapInterface;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/interfaces/InterfaceManager.class */
public class InterfaceManager {
    private IXaeroMinimap modMain;

    public InterfaceManager(IXaeroMinimap modMain) throws IOException {
        this.modMain = modMain;
    }

    @Deprecated
    public MinimapInterface getMinimapInterface() {
        return (MinimapInterface) this.modMain.getMinimap();
    }
}
