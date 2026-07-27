package xaero.lib.client.config.channel.register.handler;

import xaero.lib.client.config.listener.ClientConfigChangeListener;
import xaero.lib.client.config.option.ClientConfigOptionManager;
import xaero.lib.client.config.option.ui.ConfigOptionUITypeManager;
import xaero.lib.client.config.option.value.redirect.ClientOptionValueRedirectorManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/channel/register/handler/IConfigChannelClientRegistryHandler.class */
public interface IConfigChannelClientRegistryHandler {
    void registerPrimaryClientOptions(ClientConfigOptionManager clientConfigOptionManager);

    void registerConfigOptionUITypes(ConfigOptionUITypeManager configOptionUITypeManager);

    void registerClientOptionChangeHandlers(ClientConfigChangeListener clientConfigChangeListener);

    void registerOptionClientRedirectors(ClientOptionValueRedirectorManager clientOptionValueRedirectorManager);
}
