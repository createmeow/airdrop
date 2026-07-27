package xaero.lib.client.config.channel;

import java.io.IOException;
import xaero.lib.client.config.channel.register.handler.IConfigChannelClientRegistryHandler;
import xaero.lib.client.config.listener.handler.BuiltInConfigOptionClientHandlers;
import xaero.lib.client.config.option.ui.BuiltInConfigOptionUIRegister;
import xaero.lib.client.config.primary.option.BuiltInPrimaryClientConfigOptions;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.channel.ConfigChannelCommonInit;
import xaero.lib.common.config.profile.ConfigProfile;
import xaero.lib.common.config.single.SingleConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/channel/ConfigChannelClientInit.class */
public class ConfigChannelClientInit extends ConfigChannelCommonInit {
    @Override // xaero.lib.common.config.channel.ConfigChannelCommonInit
    protected void registerOptions(ConfigChannel channel) {
        super.registerOptions(channel);
        channel.logger.info("Registering client data for channel {}!", channel.getId());
        IConfigChannelClientRegistryHandler clientRegistryHandler = channel.getClientRegistryHandler();
        BuiltInPrimaryClientConfigOptions.registerAll(channel.getPrimaryClientConfigOptionManager());
        clientRegistryHandler.registerPrimaryClientOptions(channel.getPrimaryClientConfigOptionManager());
        BuiltInConfigOptionUIRegister.registerAll(channel.getConfigOptionUITypeManager());
        clientRegistryHandler.registerConfigOptionUITypes(channel.getConfigOptionUITypeManager());
        BuiltInConfigOptionClientHandlers.registerAll(channel.getClientConfigManager().getChangeListener());
        clientRegistryHandler.registerClientOptionChangeHandlers(channel.getClientConfigManager().getChangeListener());
        clientRegistryHandler.registerOptionClientRedirectors(channel.getClientConfigManager().getRedirectorManager());
        channel.getClientConfigManager().getRedirectorManager().freeze();
    }

    @Override // xaero.lib.common.config.channel.ConfigChannelCommonInit
    protected void load(ConfigChannel channel) throws InterruptedException, IOException {
        super.load(channel);
        channel.logger.info("Loading primary client config for channel {}!", channel.getId());
        channel.getPrimaryClientConfigManagerIO().load();
        SingleConfigManager<Config> primaryClientConfigManager = channel.getPrimaryClientConfigManager();
        if (primaryClientConfigManager.getConfig() == null) {
            primaryClientConfigManager.setConfig(Config.FinalBuilder.begin().setOptions(channel.getPrimaryClientConfigOptionManager()).setAllowNullValues(false).build());
        }
        channel.getPrimaryClientConfigManagerIO().save();
        channel.logger.info("Loading client config profiles for channel {}!", channel.getId());
        channel.getClientConfigProfileIO().load();
        ConfigProfile currentProfile = channel.getClientConfigManager().getCurrentProfile();
        if (currentProfile == null) {
            setDefaultClientConfigProfile(channel);
        }
        channel.getClientConfigProfileIO().saveAll();
    }
}
