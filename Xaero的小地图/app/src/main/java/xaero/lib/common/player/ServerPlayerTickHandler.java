package xaero.lib.common.player;

import net.minecraft.server.level.ServerPlayer;
import xaero.lib.common.player.config.permission.PlayerConfigChannelPermissionUpdater;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/player/ServerPlayerTickHandler.class */
public class ServerPlayerTickHandler {
    public void handle(ServerPlayer player) {
        ServerPlayerData playerData = ServerPlayerData.get(player);
        if (playerData.shouldUpdateConfigPermissions()) {
            new PlayerConfigChannelPermissionUpdater().handle(player, true);
            playerData.setShouldUpdateConfigPermissions(false);
        }
    }
}
