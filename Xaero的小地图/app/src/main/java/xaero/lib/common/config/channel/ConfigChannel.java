package xaero.lib.common.config.channel;

import java.nio.file.Path;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import xaero.lib.XaeroLib;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.client.config.channel.register.handler.IConfigChannelClientRegistryHandler;
import xaero.lib.client.config.option.ClientConfigOptionManager;
import xaero.lib.client.config.option.ui.ConfigOptionUITypeManager;
import xaero.lib.client.config.sync.ClientConfigChannelSynchronizer;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.register.handler.IConfigChannelCommonRegistryHandler;
import xaero.lib.common.config.io.serialization.cfg.AConfigCfgSerializer;
import xaero.lib.common.config.io.serialization.cfg.ConfigCfgSerializer;
import xaero.lib.common.config.option.ConfigOptionManager;
import xaero.lib.common.config.profile.io.ConfigProfileManagerIO;
import xaero.lib.common.config.server.ServerConfigManager;
import xaero.lib.common.config.server.sync.ServerConfigChannelSynchronizer;
import xaero.lib.common.config.single.SingleConfigManager;
import xaero.lib.common.config.single.io.SingleConfigManagerIO;
import xaero.lib.common.config.util.ConfigConstants;
import xaero.lib.common.permission.config.channel.BuiltInConfigChannelPermissions;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/channel/ConfigChannel.class */
public class ConfigChannel {
    public final Logger logger;
    private final ResourceLocation id;
    private final ConfigOptionManager configOptionManager;
    private final ClientConfigManager clientConfigManager;
    private final ConfigProfileManagerIO clientConfigProfileManagerIO;
    private final ClientConfigChannelSynchronizer clientConfigSynchronizer;
    private final ServerConfigManager serverConfigManager;
    private final ConfigProfileManagerIO serverConfigProfileManagerIO;
    private final ServerConfigChannelSynchronizer serverConfigSynchronizer;
    private final ClientConfigOptionManager primaryClientConfigOptionManager;
    private final SingleConfigManager<Config> primaryClientConfigManager;
    private final SingleConfigManagerIO<Config> primaryClientConfigManagerIO;
    private final ConfigOptionManager primaryCommonConfigOptionManager;
    private final SingleConfigManager<Config> primaryCommonConfigManager;
    private final SingleConfigManagerIO<Config> primaryCommonConfigManagerIO;
    private final ConfigOptionUITypeManager configOptionUITypeManager;
    private final IConfigChannelCommonRegistryHandler commonRegistryHandler;
    private final Supplier<IConfigChannelClientRegistryHandler> clientRegistryHandlerSupplier;
    private BuiltInConfigChannelPermissions builtInPermissions;

    private ConfigChannel(Logger logger, ResourceLocation id, ConfigOptionManager configOptionManager, ClientConfigManager clientConfigManager, ConfigProfileManagerIO clientConfigProfileManagerIO, ClientConfigChannelSynchronizer clientConfigSynchronizer, ServerConfigManager serverConfigManager, ConfigProfileManagerIO serverConfigProfileManagerIO, ServerConfigChannelSynchronizer serverConfigSynchronizer, ClientConfigOptionManager primaryClientConfigOptionManager, SingleConfigManager<Config> primaryClientConfigManager, SingleConfigManagerIO<Config> primaryClientConfigManagerIO, ConfigOptionManager primaryCommonConfigOptionManager, SingleConfigManager<Config> primaryCommonConfigManager, SingleConfigManagerIO<Config> primaryCommonConfigManagerIO, ConfigOptionUITypeManager configOptionUITypeManager, IConfigChannelCommonRegistryHandler commonRegistryHandler, Supplier<IConfigChannelClientRegistryHandler> clientRegistryHandlerSupplier) {
        this.logger = logger;
        this.id = id;
        this.configOptionManager = configOptionManager;
        this.clientConfigManager = clientConfigManager;
        this.clientConfigProfileManagerIO = clientConfigProfileManagerIO;
        this.clientConfigSynchronizer = clientConfigSynchronizer;
        this.serverConfigManager = serverConfigManager;
        this.serverConfigProfileManagerIO = serverConfigProfileManagerIO;
        this.serverConfigSynchronizer = serverConfigSynchronizer;
        this.primaryClientConfigOptionManager = primaryClientConfigOptionManager;
        this.primaryClientConfigManager = primaryClientConfigManager;
        this.primaryClientConfigManagerIO = primaryClientConfigManagerIO;
        this.primaryCommonConfigOptionManager = primaryCommonConfigOptionManager;
        this.primaryCommonConfigManager = primaryCommonConfigManager;
        this.primaryCommonConfigManagerIO = primaryCommonConfigManagerIO;
        this.configOptionUITypeManager = configOptionUITypeManager;
        this.commonRegistryHandler = commonRegistryHandler;
        this.clientRegistryHandlerSupplier = clientRegistryHandlerSupplier;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public ConfigOptionManager getConfigOptionManager() {
        return this.configOptionManager;
    }

    public ClientConfigManager getClientConfigManager() {
        return this.clientConfigManager;
    }

    public ConfigProfileManagerIO getClientConfigProfileIO() {
        return this.clientConfigProfileManagerIO;
    }

    public ClientConfigChannelSynchronizer getClientConfigSynchronizer() {
        return this.clientConfigSynchronizer;
    }

    public ServerConfigManager getServerConfigManager() {
        return this.serverConfigManager;
    }

    public ConfigProfileManagerIO getServerConfigProfileIO() {
        return this.serverConfigProfileManagerIO;
    }

    public ClientConfigOptionManager getPrimaryClientConfigOptionManager() {
        return this.primaryClientConfigOptionManager;
    }

    public SingleConfigManagerIO<Config> getPrimaryClientConfigManagerIO() {
        return this.primaryClientConfigManagerIO;
    }

    public SingleConfigManager<Config> getPrimaryClientConfigManager() {
        return this.primaryClientConfigManager;
    }

    public ConfigOptionManager getPrimaryCommonConfigOptionManager() {
        return this.primaryCommonConfigOptionManager;
    }

    public SingleConfigManager<Config> getPrimaryCommonConfigManager() {
        return this.primaryCommonConfigManager;
    }

    public SingleConfigManagerIO<Config> getPrimaryCommonConfigManagerIO() {
        return this.primaryCommonConfigManagerIO;
    }

    public ConfigOptionUITypeManager getConfigOptionUITypeManager() {
        return this.configOptionUITypeManager;
    }

    public ServerConfigChannelSynchronizer getServerConfigSynchronizer() {
        return this.serverConfigSynchronizer;
    }

    public IConfigChannelCommonRegistryHandler getCommonRegistryHandler() {
        return this.commonRegistryHandler;
    }

    public IConfigChannelClientRegistryHandler getClientRegistryHandler() {
        return this.clientRegistryHandlerSupplier.get();
    }

    public void freezeOptionManagers() {
        this.configOptionManager.freeze();
        this.primaryCommonConfigOptionManager.freeze();
        if (this.primaryClientConfigOptionManager == null) {
            return;
        }
        this.primaryClientConfigOptionManager.freeze();
    }

    public void postLoad() {
        this.serverConfigManager.postLoad();
        if (this.clientConfigManager == null) {
            return;
        }
        this.clientConfigManager.postLoad();
    }

    public void setBuiltInPermissions(BuiltInConfigChannelPermissions builtInPermissions) {
        if (this.builtInPermissions != null) {
            throw new IllegalStateException();
        }
        this.builtInPermissions = builtInPermissions;
    }

    public BuiltInConfigChannelPermissions getBuiltInPermissions() {
        return this.builtInPermissions;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/channel/ConfigChannel$Builder.class */
    public static final class Builder {
        private Logger logger;
        private ResourceLocation id;
        private Path configPath;
        private Path defaultConfigsPath;
        private IConfigChannelCommonRegistryHandler commonRegistryHandler;
        private Supplier<IConfigChannelClientRegistryHandler> clientRegistryHandlerSupplier;
        private final BuiltInConfigChannelPermissions.Builder builtInPermissionsBuilder = BuiltInConfigChannelPermissions.Builder.begin();

        private Builder() {
        }

        public Builder setDefault() {
            setLogger(XaeroLib.LOGGER);
            setId(null);
            setConfigPath(null);
            setDefaultConfigsPath(null);
            setCommonRegistryHandler(null);
            this.builtInPermissionsBuilder.setDefault();
            return this;
        }

        public Builder setLogger(Logger logger) {
            this.logger = logger;
            return this;
        }

        public Builder setId(ResourceLocation id) {
            this.id = id;
            return this;
        }

        public Builder setConfigPath(Path configPath) {
            this.configPath = configPath;
            return this;
        }

        public Builder setDefaultConfigsPath(Path defaultConfigsPath) {
            this.defaultConfigsPath = defaultConfigsPath;
            return this;
        }

        public Builder setCommonRegistryHandler(IConfigChannelCommonRegistryHandler commonRegistryHandler) {
            this.commonRegistryHandler = commonRegistryHandler;
            return this;
        }

        public Builder setClientRegistryHandlerSupplier(Supplier<IConfigChannelClientRegistryHandler> clientRegistryHandlerSupplier) {
            this.clientRegistryHandlerSupplier = clientRegistryHandlerSupplier;
            return this;
        }

        public Builder setDefaultEnforcedServerProfileNodePath(String nodePath) {
            this.builtInPermissionsBuilder.setDefaultEnforcedServerProfileNodePath(nodePath);
            return this;
        }

        /* JADX WARN: Multi-variable type inference failed */
        public ConfigChannel build() {
            ConfigOptionManager configOptionManager;
            if (this.logger == null || this.id == null || this.configPath == null || this.commonRegistryHandler == null || this.clientRegistryHandlerSupplier == null || this.builtInPermissionsBuilder == null) {
                throw new IllegalStateException();
            }
            ClientConfigOptionManager primaryClientConfigOptionManager = null;
            ConfigOptionUITypeManager configOptionUITypeManager = null;
            SingleConfigManager singleConfigManagerBuild = null;
            SingleConfigManagerIO<Config> primaryClientConfigManagerIO = null;
            ClientConfigManager clientConfigManager = null;
            ConfigProfileManagerIO clientConfigProfileManagerIO = null;
            ClientConfigChannelSynchronizer clientConfigChannelSynchronizer = null;
            if (!Services.PLATFORM.isDedicatedServer()) {
                configOptionUITypeManager = ConfigOptionUITypeManager.Builder.begin().build();
                configOptionManager = ClientConfigOptionManager.Builder.begin().setLogger(this.logger).setUiTypeManager(configOptionUITypeManager).build();
                primaryClientConfigOptionManager = ClientConfigOptionManager.Builder.begin().setLogger(this.logger).setUiTypeManager(configOptionUITypeManager).build();
                singleConfigManagerBuild = SingleConfigManager.FinalBuilder.begin().setConfigId(ConfigConstants.PRIMARY_CLIENT_CONFIG).setLogger(this.logger).build();
                primaryClientConfigManagerIO = SingleConfigManagerIO.Builder.begin().setManager(singleConfigManagerBuild).setDestination(this.configPath).setDefaultConfigsSource(this.defaultConfigsPath).setExtension(AConfigCfgSerializer.RECOMMENDED_EXTENSION).setSerializer(((ConfigCfgSerializer.Builder) ConfigCfgSerializer.Builder.begin().setOptions(primaryClientConfigOptionManager)).build()).setAllowNullValues(false).build();
                clientConfigManager = ClientConfigManager.Builder.begin().setPrimaryConfigManager(singleConfigManagerBuild).setProfiledConfigOptions(configOptionManager).build();
                clientConfigProfileManagerIO = ConfigProfileManagerIO.Builder.begin().setManager(clientConfigManager.getProfileManager()).setPath(this.configPath.resolve("profiles")).setDefaultConfigsPath(this.defaultConfigsPath == null ? null : this.defaultConfigsPath.resolve("profiles")).setExtension(AConfigCfgSerializer.RECOMMENDED_EXTENSION).setOptionsDefault(configOptionManager).build();
                singleConfigManagerBuild.setRedirectorManager(clientConfigManager.getRedirectorManager());
                clientConfigChannelSynchronizer = ClientConfigChannelSynchronizer.Builder.begin().setOptions(configOptionManager).setManager(clientConfigManager.getServerSynced()).build();
                clientConfigManager.getServerSynced().setSynchronizer(clientConfigChannelSynchronizer);
            } else {
                configOptionManager = ConfigOptionManager.FinalBuilder.begin().setLogger(this.logger).build();
            }
            ConfigOptionManager primaryCommonConfigOptionManager = ConfigOptionManager.FinalBuilder.begin().setLogger(this.logger).build();
            SingleConfigManager<C> singleConfigManagerBuild2 = SingleConfigManager.FinalBuilder.begin().setConfigId(ConfigConstants.PRIMARY_COMMON_CONFIG).setLogger(this.logger).build();
            SingleConfigManagerIO<Config> primaryCommonConfigManagerIO = SingleConfigManagerIO.Builder.begin().setManager(singleConfigManagerBuild2).setDestination(this.configPath).setDefaultConfigsSource(this.defaultConfigsPath).setExtension(AConfigCfgSerializer.RECOMMENDED_EXTENSION).setSerializer(((ConfigCfgSerializer.Builder) ConfigCfgSerializer.Builder.begin().setOptions(primaryCommonConfigOptionManager)).build()).setAllowNullValues(false).build();
            ServerConfigManager serverConfigManager = ServerConfigManager.Builder.begin().setPrimaryConfigManager(singleConfigManagerBuild2).setProfiledConfigOptions(configOptionManager).build();
            ConfigProfileManagerIO serverConfigProfileManagerIO = ConfigProfileManagerIO.Builder.begin().setManager(serverConfigManager.getProfileManager()).setPath(this.configPath.resolve("server_profiles")).setDefaultConfigsPath(this.defaultConfigsPath == null ? null : this.defaultConfigsPath.resolve("server_profiles")).setExtension(AConfigCfgSerializer.RECOMMENDED_EXTENSION).setOptionsDefault(configOptionManager).setAllowNullValues(true).build();
            singleConfigManagerBuild2.setRedirectorManager(serverConfigManager.getRedirectorManager());
            ServerConfigChannelSynchronizer serverConfigChannelSynchronizer = ServerConfigChannelSynchronizer.Builder.begin().setManager(serverConfigManager).setOptions(configOptionManager).build();
            serverConfigManager.getChangeListener().setSynchronizer(serverConfigChannelSynchronizer);
            ConfigChannel channel = new ConfigChannel(this.logger, this.id, configOptionManager, clientConfigManager, clientConfigProfileManagerIO, clientConfigChannelSynchronizer, serverConfigManager, serverConfigProfileManagerIO, serverConfigChannelSynchronizer, primaryClientConfigOptionManager, singleConfigManagerBuild, primaryClientConfigManagerIO, primaryCommonConfigOptionManager, singleConfigManagerBuild2, primaryCommonConfigManagerIO, configOptionUITypeManager, this.commonRegistryHandler, this.clientRegistryHandlerSupplier);
            BuiltInConfigChannelPermissions builtInPermissions = this.builtInPermissionsBuilder.setChannel(channel).build();
            channel.setBuiltInPermissions(builtInPermissions);
            if (clientConfigManager != null) {
                clientConfigManager.setChannel(channel);
            }
            serverConfigChannelSynchronizer.setChannel(channel);
            serverConfigManager.setChannel(channel);
            return channel;
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
