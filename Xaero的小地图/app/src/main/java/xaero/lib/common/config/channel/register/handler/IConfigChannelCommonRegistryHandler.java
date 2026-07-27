package xaero.lib.common.config.channel.register.handler;

import xaero.lib.common.config.option.ConfigOptionManager;
import xaero.lib.common.config.option.value.redirect.OptionValueRedirectorManager;
import xaero.lib.common.config.server.listener.ServerConfigChangeListener;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/channel/register/handler/IConfigChannelCommonRegistryHandler.class */
public interface IConfigChannelCommonRegistryHandler {
    void registerPrimaryCommonOptions(ConfigOptionManager configOptionManager);

    void registerProfiledOptions(ConfigOptionManager configOptionManager);

    void registerServerOptionChangeHandlers(ServerConfigChangeListener serverConfigChangeListener);

    void registerOptionServerRedirectors(OptionValueRedirectorManager optionValueRedirectorManager);
}
