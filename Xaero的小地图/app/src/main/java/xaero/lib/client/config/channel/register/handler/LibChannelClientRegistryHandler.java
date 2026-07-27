package xaero.lib.client.config.channel.register.handler;

import xaero.lib.client.config.listener.ClientConfigChangeListener;
import xaero.lib.client.config.option.ClientConfigOptionManager;
import xaero.lib.client.config.option.ui.ConfigOptionUITypeManager;
import xaero.lib.client.config.option.ui.LibConfigOptionUIRegister;
import xaero.lib.client.config.option.value.redirect.ClientOptionValueRedirectorManager;
import xaero.lib.client.config.primary.option.LibPrimaryClientConfigOptions;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/channel/register/handler/LibChannelClientRegistryHandler.class */
public class LibChannelClientRegistryHandler implements IConfigChannelClientRegistryHandler {
    @Override // xaero.lib.client.config.channel.register.handler.IConfigChannelClientRegistryHandler
    public void registerPrimaryClientOptions(ClientConfigOptionManager manager) {
        LibPrimaryClientConfigOptions.registerAll(manager);
    }

    @Override // xaero.lib.client.config.channel.register.handler.IConfigChannelClientRegistryHandler
    public void registerConfigOptionUITypes(ConfigOptionUITypeManager manager) {
        LibConfigOptionUIRegister.registerAll(manager);
    }

    @Override // xaero.lib.client.config.channel.register.handler.IConfigChannelClientRegistryHandler
    public void registerClientOptionChangeHandlers(ClientConfigChangeListener registry) {
    }

    @Override // xaero.lib.client.config.channel.register.handler.IConfigChannelClientRegistryHandler
    public void registerOptionClientRedirectors(ClientOptionValueRedirectorManager manager) {
    }
}
