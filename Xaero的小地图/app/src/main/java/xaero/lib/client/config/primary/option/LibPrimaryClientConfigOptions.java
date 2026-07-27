package xaero.lib.client.config.primary.option;

import java.util.ArrayList;
import java.util.List;
import xaero.lib.client.config.option.ClientConfigOptionManager;
import xaero.lib.common.config.option.ConfigOption;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/primary/option/LibPrimaryClientConfigOptions.class */
public class LibPrimaryClientConfigOptions {
    private static final List<ConfigOption<?>> ALL = new ArrayList();

    public static void registerAll(ClientConfigOptionManager manager) {
        for (ConfigOption<?> option : ALL) {
            manager.register(option);
        }
    }
}
