package xaero.lib.client.gui.config.context;

import net.minecraft.network.chat.Component;
import xaero.lib.client.config.sync.SyncedConfigManager;
import xaero.lib.client.config.sync.profile.SyncedConfigProfileInfoManager;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.profile.ConfigProfile;
import xaero.lib.common.config.profile.IConfigProfileInfo;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/config/context/EditConfigScreenServerContext.class */
public class EditConfigScreenServerContext implements IEditConfigScreenContext {
    private static final Component DROPDOWN_NARRATION = Component.translatable("gui.xaero_server_config_profile");
    private static final Component SYNC_MESSAGE = Component.translatable("gui.xaero_server_config_profile_syncing");

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public ConfigProfile getCurrentProfile(ConfigChannel channel) {
        return channel.getClientConfigManager().getServerSynced().getEditedProfile();
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public Config getEnforcedConfig(ConfigChannel channel) {
        return null;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public String getSelectedProfileId(ConfigChannel channel) {
        return channel.getClientConfigManager().getServerSynced().getDesiredEditedProfileId();
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public String getDefaultProfileId(ConfigChannel channel) {
        SyncedConfigProfileInfoManager profileInfoManager = channel.getClientConfigManager().getServerSynced().getProfileInfoManager();
        return profileInfoManager.getDefaultEnforcedProfileId();
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public void setDefaultProfileId(String profileId, ConfigChannel channel) {
        SyncedConfigProfileInfoManager profileInfoManager = channel.getClientConfigManager().getServerSynced().getProfileInfoManager();
        profileInfoManager.setDefaultEnforcedProfileId(profileId);
        channel.getClientConfigSynchronizer().changeDefaultEnforcedProfileId(profileId);
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public void setCurrentProfile(String profileId, ConfigChannel channel) {
        channel.getClientConfigManager().getServerSynced().setDesiredEditedProfileId(profileId);
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public void confirmProfile(ConfigProfile profile, ConfigChannel channel) {
        channel.getClientConfigManager().getServerSynced().confirmEdit(profile);
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public void reset(ConfigChannel channel) {
        channel.getClientConfigManager().getServerSynced().resetEdit();
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public Iterable<IConfigProfileInfo> getProfiles(ConfigChannel channel) {
        return channel.getClientConfigManager().getServerSynced().getProfileInfoManager();
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public boolean profileExists(String profileId, ConfigChannel channel) {
        return channel.getClientConfigManager().getServerSynced().getProfileInfoManager().get(profileId) != null;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public void createProfile(String profileId, String name, ConfigChannel channel, String profileToCopy) {
        SyncedConfigManager syncedConfigManager = channel.getClientConfigManager().getServerSynced();
        syncedConfigManager.getProfileInfoManager().add(profileId, name);
        channel.getClientConfigSynchronizer().createProfile(profileId, name, profileToCopy);
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public void deleteProfile(ConfigProfile editedProfile, ConfigChannel channel) {
        channel.getClientConfigSynchronizer().deleteProfile(editedProfile.getId());
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public Component getDropdownNarration() {
        return DROPDOWN_NARRATION;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public boolean isClientSide() {
        return false;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public boolean isAutoConfirm() {
        return false;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public boolean isAutoDefaultProfile() {
        return false;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public boolean getSyncStatus(ConfigChannel channel) {
        return !channel.getClientConfigManager().getServerSynced().isSyncingEditedProfile();
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public Component getSyncMessage() {
        return SYNC_MESSAGE;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public boolean hasPermission(ConfigChannel channel) {
        return getDefaultProfileId(channel) != null;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public String getScreenTitleFormat() {
        return "gui.xaero_server_config_title_format";
    }
}
