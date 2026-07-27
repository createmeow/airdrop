package xaero.lib.common.config.channel.register.handler;

import xaero.lib.common.config.option.ConfigOptionManager;
import xaero.lib.common.config.option.LibProfiledConfigOptions;
import xaero.lib.common.config.option.value.redirect.OptionValueRedirectorManager;
import xaero.lib.common.config.primary.option.LibPrimaryCommonConfigOptions;
import xaero.lib.common.config.server.listener.ServerConfigChangeListener;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/channel/register/handler/LibChannelCommonRegistryHandler.class */
public class LibChannelCommonRegistryHandler implements IConfigChannelCommonRegistryHandler {
    @Override // xaero.lib.common.config.channel.register.handler.IConfigChannelCommonRegistryHandler
    public void registerPrimaryCommonOptions(ConfigOptionManager manager) {
        LibPrimaryCommonConfigOptions.registerAll(manager);
    }

    @Override // xaero.lib.common.config.channel.register.handler.IConfigChannelCommonRegistryHandler
    public void registerProfiledOptions(ConfigOptionManager manager) {
        LibProfiledConfigOptions.registerAll(manager);
    }

    @Override // xaero.lib.common.config.channel.register.handler.IConfigChannelCommonRegistryHandler
    public void registerServerOptionChangeHandlers(ServerConfigChangeListener registry) {
    }

    @Override // xaero.lib.common.config.channel.register.handler.IConfigChannelCommonRegistryHandler
    public void registerOptionServerRedirectors(OptionValueRedirectorManager manager) {
    }
}
