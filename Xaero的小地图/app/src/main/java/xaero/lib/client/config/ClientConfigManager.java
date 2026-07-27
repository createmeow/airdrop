package xaero.lib.client.config;

import net.minecraft.client.Minecraft;
import xaero.lib.client.config.ClientConfigProfileSwitchHandler;
import xaero.lib.client.config.listener.ClientConfigChangeListener;
import xaero.lib.client.config.option.value.redirect.ClientOptionValueRedirectorManager;
import xaero.lib.client.config.primary.option.BuiltInPrimaryClientConfigOptions;
import xaero.lib.client.config.sync.SyncedConfigManager;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.option.BuiltInProfiledConfigOptions;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.ConfigOptionManager;
import xaero.lib.common.config.profile.ConfigProfile;
import xaero.lib.common.config.profile.ConfigProfileManager;
import xaero.lib.common.config.single.SingleConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/ClientConfigManager.class */
public final class ClientConfigManager {
    private final ConfigProfileManager profileManager;
    private final SingleConfigManager<Config> primaryConfigManager;
    private final SyncedConfigManager serverSynced;
    private final ClientConfigProfileSwitchHandler profileSwitchHandler;
    private final ClientOptionValueRedirectorManager redirectorManager;
    private ConfigProfile lastFetchedProfile;
    private ClientConfigChangeListener changeListener;
    private ConfigChannel channel;

    private ClientConfigManager(ConfigProfileManager profileManager, SingleConfigManager<Config> primaryConfigManager, SyncedConfigManager serverSynced, ClientConfigProfileSwitchHandler profileSwitchHandler, ClientOptionValueRedirectorManager redirectorManager) {
        this.profileManager = profileManager;
        this.primaryConfigManager = primaryConfigManager;
        this.serverSynced = serverSynced;
        this.profileSwitchHandler = profileSwitchHandler;
        this.redirectorManager = redirectorManager;
    }

    public ConfigProfileManager getProfileManager() {
        return this.profileManager;
    }

    public ConfigProfile getCurrentProfile() {
        ConfigProfile result;
        String currentProfileId = (String) this.primaryConfigManager.getEffective(BuiltInPrimaryClientConfigOptions.CURRENT_PROFILE);
        if (currentProfileId == null || (result = this.profileManager.get(currentProfileId)) == null) {
            return null;
        }
        if (result != this.lastFetchedProfile) {
            ConfigProfile changingFrom = this.lastFetchedProfile;
            this.lastFetchedProfile = result;
            if (changingFrom == null) {
                return result;
            }
            this.profileSwitchHandler.handleSwitch(changingFrom, result, this);
        }
        return result;
    }

    public SyncedConfigManager getServerSynced() {
        return this.serverSynced;
    }

    private void setChangeListener(ClientConfigChangeListener changeListener) {
        if (this.changeListener != null) {
            throw new IllegalStateException();
        }
        this.changeListener = changeListener;
        this.profileManager.setChangeListener(changeListener);
        this.serverSynced.setChangeListener(changeListener);
        this.primaryConfigManager.setChangeListener(changeListener);
        this.redirectorManager.setChangeListener(changeListener);
    }

    public ClientConfigChangeListener getChangeListener() {
        return this.changeListener;
    }

    public void setChannel(ConfigChannel channel) {
        if (this.channel != null) {
            throw new IllegalStateException();
        }
        this.channel = channel;
        this.serverSynced.setChannel(channel);
        this.primaryConfigManager.setChannel(channel);
        this.redirectorManager.setChannel(channel);
    }

    public ConfigChannel getChannel() {
        return this.channel;
    }

    public <T> T getEffective(ConfigOption<T> configOption) {
        return (T) getEffective(getCurrentProfile(), configOption);
    }

    public <T> T getEffective(ConfigProfile configProfile, ConfigOption<T> configOption) {
        Object effective;
        if (this.redirectorManager.shouldRedirect(configOption)) {
            return (T) this.redirectorManager.getValue(configOption);
        }
        if (Minecraft.getInstance().level == null || shouldIgnoreServerEnforcement(configProfile, configOption) || !configOption.isOverridable()) {
            effective = null;
        } else {
            effective = this.serverSynced.getEffective(configOption);
        }
        T t = (T) effective;
        if (t != null) {
            return t;
        }
        return (T) getRaw(configProfile, configOption);
    }

    public <T> boolean shouldIgnoreServerEnforcement(ConfigProfile config, ConfigOption<T> option) {
        return (option == BuiltInProfiledConfigOptions.IGNORE_ENFORCEMENT_IF_EDITOR || Minecraft.getInstance().player == null || getServerSynced().getProfileInfoManager().getDefaultEnforcedProfileId() == null || !((Boolean) getEffective(config, BuiltInProfiledConfigOptions.IGNORE_ENFORCEMENT_IF_EDITOR)).booleanValue()) ? false : true;
    }

    public <T> boolean shouldIgnoreServerEnforcement(ConfigOption<T> option) {
        return shouldIgnoreServerEnforcement(getCurrentProfile(), option);
    }

    public <T> T getRaw(ConfigOption<T> configOption) {
        return (T) getRaw(getCurrentProfile(), configOption);
    }

    public <T> T getRaw(ConfigProfile configProfile, ConfigOption<T> configOption) {
        if (configProfile == null) {
            return null;
        }
        return (T) configProfile.get(configOption);
    }

    public SingleConfigManager<Config> getPrimaryConfigManager() {
        return this.primaryConfigManager;
    }

    public ClientOptionValueRedirectorManager getRedirectorManager() {
        return this.redirectorManager;
    }

    public void postLoad() {
        getChangeListener().postLoad();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/ClientConfigManager$Builder.class */
    public static final class Builder {
        private SingleConfigManager<Config> primaryConfigManager;
        private ConfigOptionManager profiledConfigOptions;

        private Builder() {
        }

        public Builder setDefault() {
            setPrimaryConfigManager(null);
            setProfiledConfigOptions(null);
            return this;
        }

        public Builder setPrimaryConfigManager(SingleConfigManager<Config> primaryConfigManager) {
            this.primaryConfigManager = primaryConfigManager;
            return this;
        }

        public Builder setProfiledConfigOptions(ConfigOptionManager profiledConfigOptions) {
            this.profiledConfigOptions = profiledConfigOptions;
            return this;
        }

        public ClientConfigManager build() {
            if (this.primaryConfigManager == null || this.profiledConfigOptions == null) {
                throw new IllegalStateException();
            }
            ConfigProfileManager profileManager = ConfigProfileManager.Builder.begin().setConfigType("Client Config").setLogger(this.primaryConfigManager.logger).build();
            ClientConfigProfileSwitchHandler profileSwitchHandler = ClientConfigProfileSwitchHandler.Builder.begin().setOptions(this.profiledConfigOptions).build();
            SyncedConfigManager serverSynced = SyncedConfigManager.Builder.begin().setLogger(this.primaryConfigManager.logger).build();
            ClientOptionValueRedirectorManager redirectorManager = ClientOptionValueRedirectorManager.Builder.begin().build();
            ClientConfigManager result = new ClientConfigManager(profileManager, this.primaryConfigManager, serverSynced, profileSwitchHandler, redirectorManager);
            ClientConfigChangeListener clientChangeListener = ClientConfigChangeListener.Builder.begin().setManager(result).setOptions(this.profiledConfigOptions).build();
            result.setChangeListener(clientChangeListener);
            return result;
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
