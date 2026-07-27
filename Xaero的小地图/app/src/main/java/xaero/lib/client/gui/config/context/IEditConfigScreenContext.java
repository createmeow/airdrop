package xaero.lib.client.gui.config.context;

import net.minecraft.network.chat.Component;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.profile.ConfigProfile;
import xaero.lib.common.config.profile.IConfigProfileInfo;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/config/context/IEditConfigScreenContext.class */
public interface IEditConfigScreenContext {
    ConfigProfile getCurrentProfile(ConfigChannel configChannel);

    Config getEnforcedConfig(ConfigChannel configChannel);

    String getSelectedProfileId(ConfigChannel configChannel);

    String getDefaultProfileId(ConfigChannel configChannel);

    void setDefaultProfileId(String str, ConfigChannel configChannel);

    void setCurrentProfile(String str, ConfigChannel configChannel);

    void confirmProfile(ConfigProfile configProfile, ConfigChannel configChannel);

    void reset(ConfigChannel configChannel);

    Iterable<IConfigProfileInfo> getProfiles(ConfigChannel configChannel);

    boolean profileExists(String str, ConfigChannel configChannel);

    void createProfile(String str, String str2, ConfigChannel configChannel, String str3);

    void deleteProfile(ConfigProfile configProfile, ConfigChannel configChannel);

    Component getDropdownNarration();

    boolean isClientSide();

    boolean isAutoConfirm();

    boolean isAutoDefaultProfile();

    boolean getSyncStatus(ConfigChannel configChannel);

    Component getSyncMessage();

    boolean hasPermission(ConfigChannel configChannel);

    String getScreenTitleFormat();
}
