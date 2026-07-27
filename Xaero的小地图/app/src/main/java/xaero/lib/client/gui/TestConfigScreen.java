package xaero.lib.client.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.lib.client.gui.config.EditConfigScreen;
import xaero.lib.client.gui.config.context.IEditConfigScreenContext;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.option.BuiltInProfiledConfigOptions;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/TestConfigScreen.class */
public class TestConfigScreen extends EditConfigScreen {
    public TestConfigScreen(Screen backScreen, Screen escScreen, IEditConfigScreenContext context, ConfigChannel channel) {
        super(Component.literal("test"), backScreen, escScreen, context, channel);
        this.entries = new ISettingEntry[]{createProfileIDEntry(), optionEntry(BuiltInProfiledConfigOptions.PROFILE_NAME), optionEntry(BuiltInProfiledConfigOptions.IGNORE_ENFORCEMENT_IF_EDITOR)};
    }
}
