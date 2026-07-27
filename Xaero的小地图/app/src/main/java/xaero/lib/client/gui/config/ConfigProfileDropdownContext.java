package xaero.lib.client.gui.config;

import java.util.stream.StreamSupport;
import net.minecraft.client.resources.language.I18n;
import xaero.lib.client.gui.config.context.IEditConfigScreenContext;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.profile.ConfigProfile;
import xaero.lib.common.config.profile.IConfigProfileInfo;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/config/ConfigProfileDropdownContext.class */
public class ConfigProfileDropdownContext {
    private final IConfigProfileInfo[] dropdownProfiles;
    private final String[] options;
    private int initialSelection;

    public ConfigProfileDropdownContext(IEditConfigScreenContext context, ConfigChannel channel, ConfigProfile lastProfileInit, String currentSeletion, boolean includeCreation) {
        this.dropdownProfiles = (IConfigProfileInfo[]) StreamSupport.stream(context.getProfiles(channel).spliterator(), false).toList().toArray(new IConfigProfileInfo[0]);
        this.options = new String[this.dropdownProfiles.length + (includeCreation ? 1 : 0)];
        for (int i = 0; i < this.dropdownProfiles.length; i++) {
            IConfigProfileInfo profile = this.dropdownProfiles[i];
            String profileName = profile.getName();
            if (profile.getId().equals(currentSeletion)) {
                this.initialSelection = i;
                if (lastProfileInit != null && lastProfileInit.getId().equals(currentSeletion)) {
                    profileName = lastProfileInit.getName();
                }
            }
            this.options[i] = profileName.replaceAll("%", "%%");
        }
        if (includeCreation) {
            this.options[this.options.length - 1] = "§8" + I18n.get("gui.xaero_create_config_profile", new Object[0]);
        }
    }

    public IConfigProfileInfo[] getProfiles() {
        return this.dropdownProfiles;
    }

    public String[] getOptions() {
        return this.options;
    }

    public int getInitialSelection() {
        return this.initialSelection;
    }
}
