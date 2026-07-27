package xaero.hud.config.channel.register;

import xaero.hud.minimap.config.listener.handler.MinimapConfigOptionClientHandlers;
import xaero.hud.minimap.config.option.ui.MinimapConfigOptionUIRegister;
import xaero.hud.minimap.config.option.value.redirect.MinimapConfigOptionClientRedirectors;
import xaero.hud.minimap.config.primary.option.MinimapPrimaryClientConfigOptions;
import xaero.lib.client.config.channel.register.handler.IConfigChannelClientRegistryHandler;
import xaero.lib.client.config.listener.ClientConfigChangeListener;
import xaero.lib.client.config.option.ClientConfigOptionManager;
import xaero.lib.client.config.option.ui.ConfigOptionUITypeManager;
import xaero.lib.client.config.option.value.redirect.ClientOptionValueRedirectorManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/config/channel/register/HudModConfigClientRegistryHandler.class */
public class HudModConfigClientRegistryHandler implements IConfigChannelClientRegistryHandler {
    @Override // xaero.lib.client.config.channel.register.handler.IConfigChannelClientRegistryHandler
    public void registerPrimaryClientOptions(ClientConfigOptionManager manager) {
        MinimapPrimaryClientConfigOptions.registerAll(manager);
    }

    @Override // xaero.lib.client.config.channel.register.handler.IConfigChannelClientRegistryHandler
    public void registerConfigOptionUITypes(ConfigOptionUITypeManager manager) {
        MinimapConfigOptionUIRegister.registerAll(manager);
    }

    @Override // xaero.lib.client.config.channel.register.handler.IConfigChannelClientRegistryHandler
    public void registerClientOptionChangeHandlers(ClientConfigChangeListener registry) {
        MinimapConfigOptionClientHandlers.registerAll(registry);
    }

    @Override // xaero.lib.client.config.channel.register.handler.IConfigChannelClientRegistryHandler
    public void registerOptionClientRedirectors(ClientOptionValueRedirectorManager manager) {
        MinimapConfigOptionClientRedirectors.registerAll(manager);
    }
}
