package xaero.lib.common.config.server.listener.handler;

import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.listener.handler.HandlerBasedConfigChangeListener;
import xaero.lib.common.config.option.BuiltInProfiledConfigOptions;
import xaero.lib.common.config.primary.option.BuiltInPrimaryCommonConfigOptions;
import xaero.lib.common.config.profile.ConfigProfile;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/server/listener/handler/BuiltInConfigOptionServerHandlers.class */
public class BuiltInConfigOptionServerHandlers {
    public static void registerAll(HandlerBasedConfigChangeListener listener, ConfigChannel channel) {
        listener.register(BuiltInProfiledConfigOptions.PROFILE_NAME, config -> {
            if (!(config instanceof ConfigProfile) || channel.getServerConfigManager().getServer() == null) {
                return;
            }
            channel.getServerConfigSynchronizer().syncProfileInfoChange((ConfigProfile) config);
        });
        listener.register(BuiltInPrimaryCommonConfigOptions.DEFAULT_ENFORCED_PROFILE, config2 -> {
            if ((config2 instanceof ConfigProfile) || channel.getServerConfigManager().getServer() == null) {
                return;
            }
            ConfigProfile enforcedProfile = channel.getServerConfigManager().getDefaultEnforcedProfile();
            channel.getServerConfigSynchronizer().syncDefaultEnforcedConfigProfileSwitch(enforcedProfile);
        });
    }
}
