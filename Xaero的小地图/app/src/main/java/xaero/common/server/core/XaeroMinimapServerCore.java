package xaero.common.server.core;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import xaero.common.IXaeroMinimap;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/server/core/XaeroMinimapServerCore.class */
public class XaeroMinimapServerCore {
    public static IXaeroMinimap modMain;

    public static void onServerWorldInfo(Player player) {
        if (!isModLoaded()) {
            return;
        }
        modMain.getCommonEvents().onPlayerWorldJoin((ServerPlayer) player);
    }

    public static boolean isModLoaded() {
        return modMain != null && (modMain.isLoadedServer() || modMain.isLoadedClient());
    }
}
