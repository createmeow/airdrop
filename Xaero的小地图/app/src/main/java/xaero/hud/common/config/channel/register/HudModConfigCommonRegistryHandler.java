package xaero.hud.common.config.channel.register;

import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.common.config.option.value.redirect.MinimapConfigOptionServerRedirectors;
import xaero.hud.minimap.common.config.primary.option.MinimapPrimaryCommonConfigOptions;
import xaero.hud.minimap.common.config.server.listener.handler.MinimapConfigOptionServerHandlers;
import xaero.lib.common.config.channel.register.handler.IConfigChannelCommonRegistryHandler;
import xaero.lib.common.config.option.ConfigOptionManager;
import xaero.lib.common.config.option.value.redirect.OptionValueRedirectorManager;
import xaero.lib.common.config.server.listener.ServerConfigChangeListener;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/common/config/channel/register/HudModConfigCommonRegistryHandler.class */
public class HudModConfigCommonRegistryHandler implements IConfigChannelCommonRegistryHandler {
    @Override // xaero.lib.common.config.channel.register.handler.IConfigChannelCommonRegistryHandler
    public void registerPrimaryCommonOptions(ConfigOptionManager manager) {
        MinimapPrimaryCommonConfigOptions.registerAll(manager);
    }

    @Override // xaero.lib.common.config.channel.register.handler.IConfigChannelCommonRegistryHandler
    public void registerProfiledOptions(ConfigOptionManager manager) {
        MinimapProfiledConfigOptions.registerAll(manager);
    }

    @Override // xaero.lib.common.config.channel.register.handler.IConfigChannelCommonRegistryHandler
    public void registerServerOptionChangeHandlers(ServerConfigChangeListener registry) {
        MinimapConfigOptionServerHandlers.registerAll(registry);
    }

    @Override // xaero.lib.common.config.channel.register.handler.IConfigChannelCommonRegistryHandler
    public void registerOptionServerRedirectors(OptionValueRedirectorManager manager) {
        MinimapConfigOptionServerRedirectors.registerAll(manager);
    }
}
