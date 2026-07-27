package xaero.lib.common.player.config.permission;

import java.util.Iterator;
import net.minecraft.server.level.ServerPlayer;
import xaero.lib.XaeroLib;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.channel.register.ConfigChannelRegistry;
import xaero.lib.common.permission.LibPermissionNodes;
import xaero.lib.common.permission.system.PermissionSystemRegistry;
import xaero.lib.common.player.ServerPlayerData;
import xaero.lib.common.player.config.ServerPlayerConfigChannelData;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/player/config/permission/PlayerConfigChannelPermissionUpdater.class */
public class PlayerConfigChannelPermissionUpdater {
    public void handle(ServerPlayer player, boolean resync) {
        ServerPlayerData playerData = ServerPlayerData.get(player);
        boolean isOpped = playerData.getPlayer().hasPermissions(2);
        PermissionSystemRegistry permissions = PermissionSystemRegistry.INSTANCE;
        boolean hasServerProfileEditPerm = isOpped || permissions.getBoolean(LibPermissionNodes.EDIT_SERVER_PROFILES, player);
        Iterator<ConfigChannel> it = ConfigChannelRegistry.INSTANCE.iterator();
        while (it.hasNext()) {
            ConfigChannel configChannel = it.next();
            String enforcedProfilePermission = (String) permissions.get(configChannel.getBuiltInPermissions().getEnforcedServerProfileNode(), player);
            if (enforcedProfilePermission != null && configChannel.getServerConfigManager().getProfileManager().get(enforcedProfilePermission) == null) {
                XaeroLib.LOGGER.error("Player {} has an invalid (permission-based) enforced profile ID: {}. Using default.", player.getGameProfile().getName(), enforcedProfilePermission);
                enforcedProfilePermission = null;
            }
            ServerPlayerConfigChannelData configChannelData = playerData.getConfigChannelData(configChannel);
            configChannelData.setCachedUsingConfigProfilePermission(enforcedProfilePermission != null);
            configChannelData.setCachedHasServerProfileEditPermission(hasServerProfileEditPerm);
            configChannelData.setCachedEnforcedConfigProfilePermission(enforcedProfilePermission);
            if (resync) {
                configChannel.getServerConfigSynchronizer().syncServerProfileEditingInfo(playerData.getConfigSynchronizer());
            }
        }
    }
}
