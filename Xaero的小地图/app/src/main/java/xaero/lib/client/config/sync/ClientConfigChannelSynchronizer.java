package xaero.lib.client.config.sync;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.Logger;
import xaero.lib.XaeroLib;
import xaero.lib.client.config.sync.profile.SyncedConfigProfileInfo;
import xaero.lib.common.config.option.BuiltInProfiledConfigOptions;
import xaero.lib.common.config.option.ConfigOptionManager;
import xaero.lib.common.config.profile.ConfigProfile;
import xaero.lib.common.config.profile.IConfigProfileInfo;
import xaero.lib.common.config.sync.CommonConfigChannelSynchronizer;
import xaero.lib.common.packet.config.AbstractConfigPacket;
import xaero.lib.common.packet.config.profile.ConfigProfileCreatePacket;
import xaero.lib.common.packet.config.profile.ConfigProfileDeletePacket;
import xaero.lib.common.packet.config.profile.ConfigProfileEditPacket;
import xaero.lib.common.packet.config.profile.ConfigProfileInfoPacket;
import xaero.lib.common.packet.config.profile.ServerboundConfigProfileRequestPacket;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/sync/ClientConfigChannelSynchronizer.class */
public class ClientConfigChannelSynchronizer extends CommonConfigChannelSynchronizer {
    private final SyncedConfigManager manager;

    public ClientConfigChannelSynchronizer(ConfigOptionManager options, SyncedConfigManager manager, Logger logger) {
        super(options, logger);
        this.manager = manager;
    }

    public void requestProfile(String desiredEditedProfileId) {
        XaeroLib.INSTANCE.getClient().getConfigSynchronizer().send(this.channel, new ServerboundConfigProfileRequestPacket(desiredEditedProfileId));
    }

    public void createProfile(String id, String name, String profileToCopy) {
        XaeroLib.INSTANCE.getClient().getConfigSynchronizer().send(this.channel, new ConfigProfileCreatePacket(id, name, profileToCopy));
    }

    public void deleteProfile(String id) {
        XaeroLib.INSTANCE.getClient().getConfigSynchronizer().send(this.channel, new ConfigProfileDeletePacket(id));
    }

    public void changeDefaultEnforcedProfileId(String profileId) {
        XaeroLib.INSTANCE.getClient().getConfigSynchronizer().send(this.channel, new ConfigProfileInfoPacket((List<ConfigProfileInfoPacket.Entry>) Lists.newArrayList(), profileId, false));
    }

    public void syncDirtyOptions(ConfigProfile profile) {
        XaeroLib.INSTANCE.getClient().getConfigSynchronizer().send(this.channel, new ConfigProfileEditPacket(profile.getId(), getDirtyConfigEntries(profile)));
    }

    public void onEditProfilePacket(String profileId, Iterable<AbstractConfigPacket.OptionEntry> entries) {
        IConfigProfileInfo configProfileInfo = this.manager.getProfileInfoManager().get(profileId);
        if (configProfileInfo == null) {
            this.logger.error("Received config profile with an invalid id!");
            return;
        }
        ConfigProfile profile = ConfigProfile.Builder.begin().setId(profileId).setAllowNullValues(true).setTrackDirtyOptions(true).setOptions(this.options).build();
        profile.set(BuiltInProfiledConfigOptions.PROFILE_NAME, configProfileInfo.getName());
        setConfigValues(profile, entries);
        profile.clearDirtyOptions();
        this.manager.receiveEditedProfile(profile);
    }

    public void onEnforcedConfigPacket(boolean reset, Iterable<AbstractConfigPacket.OptionEntry> entries) {
        if (reset) {
            this.manager.getConfig().reset();
        }
        this.manager.confirmChannelPresenceOnServer();
        setConfigValues(this.manager.getConfig(), entries);
    }

    public void onConfigProfileInfoPacket(List<ConfigProfileInfoPacket.Entry> entries, String defaultEnforcedProfileId, boolean reset) {
        if (reset) {
            this.manager.getProfileInfoManager().reset();
        }
        for (ConfigProfileInfoPacket.Entry entry : entries) {
            if (BuiltInProfiledConfigOptions.PROFILE_NAME.isValidValue(entry.id) && BuiltInProfiledConfigOptions.PROFILE_NAME.isValidValue(entry.name)) {
                SyncedConfigProfileInfo profileInfo = this.manager.getProfileInfoManager().get(entry.id);
                if (profileInfo == null) {
                    this.manager.getProfileInfoManager().add(entry.id, entry.name);
                } else {
                    profileInfo.setName(entry.name);
                }
            }
        }
        if (defaultEnforcedProfileId != null) {
            this.manager.getProfileInfoManager().setDefaultEnforcedProfileId(defaultEnforcedProfileId);
        }
    }

    public void reset() {
        this.manager.reset();
    }

    public void onDeleteConfigProfilePacket(String profileId) {
        this.manager.getProfileInfoManager().remove(profileId);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/sync/ClientConfigChannelSynchronizer$Builder.class */
    public static final class Builder extends CommonConfigChannelSynchronizer.Builder<Builder> {
        private SyncedConfigManager manager;

        private Builder() {
        }

        @Override // xaero.lib.common.config.sync.CommonConfigChannelSynchronizer.Builder
        public Builder setDefault() {
            super.setDefault();
            setManager(null);
            return this;
        }

        public Builder setManager(SyncedConfigManager manager) {
            this.manager = manager;
            return this;
        }

        @Override // xaero.lib.common.config.sync.CommonConfigChannelSynchronizer.Builder
        public ClientConfigChannelSynchronizer build() {
            if (this.manager == null) {
                throw new IllegalStateException();
            }
            return (ClientConfigChannelSynchronizer) super.build();
        }

        @Override // xaero.lib.common.config.sync.CommonConfigChannelSynchronizer.Builder
        protected CommonConfigChannelSynchronizer buildInternally() {
            return new ClientConfigChannelSynchronizer(this.options, this.manager, this.manager.logger);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
