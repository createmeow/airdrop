package xaero.lib.client.config.option.ui.factory;

import net.minecraft.client.gui.components.AbstractWidget;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.option.ConfigOption;

@FunctionalInterface
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/ui/factory/IConfigOptionWidgetFactory.class */
public interface IConfigOptionWidgetFactory<CT extends ConfigOption<?>> {
    AbstractWidget create(CT ct, Config config, Config config2, int i, int i2, int i3, boolean z, Runnable runnable, ConfigChannel configChannel, boolean z2);
}
