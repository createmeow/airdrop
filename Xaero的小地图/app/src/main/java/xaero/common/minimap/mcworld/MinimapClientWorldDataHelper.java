package xaero.common.minimap.mcworld;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/mcworld/MinimapClientWorldDataHelper.class */
public class MinimapClientWorldDataHelper {
    public static MinimapClientWorldData getCurrentWorldData() {
        return getWorldData(Minecraft.getInstance().level);
    }

    public static MinimapClientWorldData getWorldData(ClientLevel clientWorld) {
        IXaeroMinimapClientWorld inter = (IXaeroMinimapClientWorld) clientWorld;
        MinimapClientWorldData minimapWorldData = inter.getXaero_minimapData();
        if (minimapWorldData == null) {
            MinimapClientWorldData minimapClientWorldData = new MinimapClientWorldData(clientWorld);
            minimapWorldData = minimapClientWorldData;
            inter.setXaero_minimapData(minimapClientWorldData);
        }
        return minimapWorldData;
    }
}
