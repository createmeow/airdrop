package xaero.lib.client.config.option.ui.factory;

import net.minecraft.client.gui.screens.Screen;
import xaero.lib.client.gui.config.EditConfigScreen;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.option.ConfigOption;

@FunctionalInterface
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/ui/factory/ICustomOptionEditScreenFactory.class */
public interface ICustomOptionEditScreenFactory<CT extends ConfigOption<?>> {
    Screen get(EditConfigScreen editConfigScreen, Screen screen, Config config, Config config2, CT ct, Runnable runnable, boolean z, boolean z2);
}
