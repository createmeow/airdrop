package xaero.lib.common.config.server;

import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.ConfigOptionManager;
import xaero.lib.common.config.option.value.redirect.OptionValueRedirectorManager;
import xaero.lib.common.config.primary.option.BuiltInPrimaryCommonConfigOptions;
import xaero.lib.common.config.profile.ConfigProfile;
import xaero.lib.common.config.profile.ConfigProfileManager;
import xaero.lib.common.config.server.listener.ServerConfigChangeListener;
import xaero.lib.common.config.single.SingleConfigManager;
import xaero.lib.common.player.ServerPlayerData;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/server/ServerConfigManager.class */
public final class ServerConfigManager {
    private final ConfigProfileManager profileManager;
    private final SingleConfigManager<Config> primaryConfigManager;
    private final OptionValueRedirectorManager redirectorManager;
    private ServerConfigChangeListener changeListener;
    private MinecraftServer server;
    private ConfigChannel channel;
    private boolean loaded;

    private ServerConfigManager(ConfigProfileManager profileManager, SingleConfigManager<Config> primaryConfigManager, OptionValueRedirectorManager redirectorManager) {
        this.profileManager = profileManager;
        this.primaryConfigManager = primaryConfigManager;
        this.redirectorManager = redirectorManager;
    }

    public ConfigProfileManager getProfileManager() {
        return this.profileManager;
    }

    public ConfigProfile getDefaultEnforcedProfile() {
        String enforcedProfileId = (String) this.primaryConfigManager.getEffective(BuiltInPrimaryCommonConfigOptions.DEFAULT_ENFORCED_PROFILE);
        if (enforcedProfileId == null) {
            return null;
        }
        return this.profileManager.get(enforcedProfileId);
    }

    public void setDefaultEnforcedProfileId(String enforcedProfileId) {
        this.primaryConfigManager.getConfig().set(BuiltInPrimaryCommonConfigOptions.DEFAULT_ENFORCED_PROFILE, enforcedProfileId);
        this.channel.getPrimaryCommonConfigManagerIO().save();
    }

    public void setChangeListener(ServerConfigChangeListener changeListener) {
        if (this.changeListener != null) {
            throw new IllegalStateException();
        }
        this.changeListener = changeListener;
        this.profileManager.setChangeListener(changeListener);
        this.primaryConfigManager.setChangeListener(changeListener);
        this.redirectorManager.setChangeListener(changeListener);
    }

    public void setServer(MinecraftServer server) {
        if (this.server != null && this.server.isRunning()) {
            throw new IllegalStateException("Multiple servers running at once is not supported!");
        }
        this.server = server;
    }

    public void setChannel(ConfigChannel channel) {
        if (this.channel != null) {
            throw new IllegalStateException();
        }
        this.channel = channel;
        this.primaryConfigManager.setChannel(channel);
        this.redirectorManager.setChannel(channel);
    }

    public ConfigChannel getChannel() {
        return this.channel;
    }

    public ConfigProfile getEnforcedProfileForPlayer(ServerPlayerData playerData) {
        if (usesDefaultEnforcedProfile(playerData)) {
            return getDefaultEnforcedProfile();
        }
        ConfigProfile enforcedConfig = getPermissionBasedProfile(playerData);
        if (enforcedConfig == null) {
            return getDefaultEnforcedProfile();
        }
        return enforcedConfig;
    }

    public boolean usesDefaultEnforcedProfile(ServerPlayerData playerData) {
        return !playerData.getConfigChannelData(this.channel).isUsingConfigProfilePermission();
    }

    public boolean usesDefaultEnforcedProfile(ServerPlayer player) {
        return usesDefaultEnforcedProfile(ServerPlayerData.get(player));
    }

    public String getPermissionBasedProfileId(ServerPlayerData playerData) {
        return playerData.getConfigChannelData(this.channel).getEnforcedConfigProfilePermission();
    }

    @Nullable
    public ConfigProfile getPermissionBasedProfile(ServerPlayerData playerData) {
        if (usesDefaultEnforcedProfile(playerData)) {
            return getDefaultEnforcedProfile();
        }
        String profileId = getPermissionBasedProfileId(playerData);
        return this.profileManager.get(profileId);
    }

    public boolean hasServerProfileEditPermission(ServerPlayerData playerData) {
        return playerData.getConfigChannelData(this.channel).hasServerProfileEditPermission();
    }

    public <T> T getEffective(ServerPlayer serverPlayer, ConfigOption<T> configOption) {
        if (this.redirectorManager.shouldRedirect(configOption)) {
            return (T) this.redirectorManager.getValue(configOption);
        }
        return (T) getRaw(serverPlayer, configOption);
    }

    public <T> T getEffective(ServerPlayerData serverPlayerData, ConfigOption<T> configOption) {
        if (this.redirectorManager.shouldRedirect(configOption)) {
            return (T) this.redirectorManager.getValue(configOption);
        }
        return (T) getRaw(serverPlayerData, configOption);
    }

    public <T> T getRaw(ServerPlayer serverPlayer, ConfigOption<T> configOption) {
        return (T) getRaw(ServerPlayerData.get(serverPlayer), configOption);
    }

    public <T> T getRaw(ServerPlayerData serverPlayerData, ConfigOption<T> configOption) {
        return (T) getRaw(getEnforcedProfileForPlayer(serverPlayerData), configOption);
    }

    public <T> T getRaw(ConfigProfile configProfile, ConfigOption<T> configOption) {
        if (configProfile == null) {
            return null;
        }
        return (T) configProfile.get(configOption);
    }

    public void postLoad() {
        if (this.loaded) {
            throw new IllegalStateException();
        }
        this.loaded = true;
        this.channel.getServerConfigSynchronizer().postLoad();
        getChangeListener().postLoad();
    }

    public MinecraftServer getServer() {
        if (this.server != null && this.server.isStopped()) {
            this.server = null;
        }
        return this.server;
    }

    public ServerConfigChangeListener getChangeListener() {
        return this.changeListener;
    }

    public OptionValueRedirectorManager getRedirectorManager() {
        return this.redirectorManager;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/server/ServerConfigManager$Builder.class */
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

        public ServerConfigManager build() {
            if (this.primaryConfigManager == null || this.profiledConfigOptions == null) {
                throw new IllegalStateException();
            }
            ConfigProfileManager profileManager = ConfigProfileManager.Builder.begin().setConfigType("Server Config").setLogger(this.primaryConfigManager.logger).build();
            OptionValueRedirectorManager redirectorManager = OptionValueRedirectorManager.FinalBuilder.begin().build();
            ServerConfigManager result = new ServerConfigManager(profileManager, this.primaryConfigManager, redirectorManager);
            ServerConfigChangeListener changeListener = ServerConfigChangeListener.Builder.begin().setOptions(this.profiledConfigOptions).setManager(result).build();
            result.setChangeListener(changeListener);
            return result;
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
