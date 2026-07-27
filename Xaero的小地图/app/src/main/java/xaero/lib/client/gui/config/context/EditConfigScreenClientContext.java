package xaero.lib.client.gui.config.context;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import xaero.lib.client.config.primary.option.BuiltInPrimaryClientConfigOptions;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.option.BuiltInProfiledConfigOptions;
import xaero.lib.common.config.profile.ConfigProfile;
import xaero.lib.common.config.profile.IConfigProfileInfo;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/config/context/EditConfigScreenClientContext.class */
public class EditConfigScreenClientContext implements IEditConfigScreenContext {
    private static final Component DROPDOWN_NARRATION = Component.translatable("gui.xaero_client_config_profile");

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public ConfigProfile getCurrentProfile(ConfigChannel channel) {
        return channel.getClientConfigManager().getCurrentProfile();
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public Config getEnforcedConfig(ConfigChannel channel) {
        if (Minecraft.getInstance().level == null) {
            return null;
        }
        return channel.getClientConfigManager().getServerSynced().getConfig();
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public String getSelectedProfileId(ConfigChannel channel) {
        return channel.getClientConfigManager().getCurrentProfile().getId();
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public String getDefaultProfileId(ConfigChannel channel) {
        return getSelectedProfileId(channel);
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public void setDefaultProfileId(String profileId, ConfigChannel channel) {
        setCurrentProfile(profileId, channel);
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public void setCurrentProfile(String profileId, ConfigChannel channel) {
        channel.getPrimaryClientConfigManager().getConfig().set(BuiltInPrimaryClientConfigOptions.CURRENT_PROFILE, profileId);
        channel.getPrimaryClientConfigManagerIO().save();
        channel.getClientConfigManager().getCurrentProfile();
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public void confirmProfile(ConfigProfile profile, ConfigChannel channel) throws InterruptedException, IOException {
        channel.getClientConfigProfileIO().save(profile);
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public void reset(ConfigChannel channel) {
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public Iterable<IConfigProfileInfo> getProfiles(ConfigChannel channel) {
        Stream map = StreamSupport.stream(channel.getClientConfigManager().getProfileManager().spliterator(), false).map(Function.identity());
        Objects.requireNonNull(map);
        return map::iterator;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public boolean profileExists(String profileId, ConfigChannel channel) {
        return channel.getClientConfigManager().getProfileManager().get(profileId) != null;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public void createProfile(String profileId, String name, ConfigChannel channel, String profileToCopy) throws InterruptedException, IOException {
        ConfigProfile profile = ConfigProfile.Builder.begin().setOptions(channel.getConfigOptionManager()).setId(profileId).build();
        if (channel.getConfigOptionManager().get(BuiltInProfiledConfigOptions.PROFILE_NAME.getId()) == null) {
            return;
        }
        ConfigProfile baseProfile = channel.getClientConfigManager().getProfileManager().get(profileToCopy);
        if (baseProfile != null) {
            profile.copyOptionsFrom(baseProfile);
        }
        profile.set(BuiltInProfiledConfigOptions.PROFILE_NAME, name);
        channel.getClientConfigManager().getProfileManager().add(profile);
        channel.getClientConfigProfileIO().save(profile);
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public void deleteProfile(ConfigProfile editedProfile, ConfigChannel channel) throws InterruptedException {
        channel.getClientConfigManager().getProfileManager().remove(editedProfile.getId());
        channel.getClientConfigProfileIO().delete(editedProfile);
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public Component getDropdownNarration() {
        return DROPDOWN_NARRATION;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public boolean isClientSide() {
        return true;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public boolean isAutoConfirm() {
        return true;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public boolean isAutoDefaultProfile() {
        return true;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public boolean getSyncStatus(ConfigChannel channel) {
        return true;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public Component getSyncMessage() {
        return null;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public boolean hasPermission(ConfigChannel channel) {
        return true;
    }

    @Override // xaero.lib.client.gui.config.context.IEditConfigScreenContext
    public String getScreenTitleFormat() {
        return "gui.xaero_client_config_title_format";
    }
}
