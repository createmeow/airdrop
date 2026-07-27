package xaero.lib.common.config.option;

import java.util.ArrayList;
import java.util.List;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/LibProfiledConfigOptions.class */
public class LibProfiledConfigOptions {
    private static final List<ConfigOption<?>> ALL = new ArrayList();

    public static void registerAll(ConfigOptionManager manager) {
        for (ConfigOption<?> option : ALL) {
            manager.register(option);
        }
    }
}
