package xaero.hud.minimap.common.config.primary.option;

import java.util.ArrayList;
import java.util.List;
import xaero.lib.common.config.option.BooleanConfigOption;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.ConfigOptionManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/common/config/primary/option/MinimapPrimaryCommonConfigOptions.class */
public class MinimapPrimaryCommonConfigOptions {
    private static final List<ConfigOption<?>> ALL = new ArrayList();
    public static final BooleanConfigOption REGISTER_EFFECTS = ((BooleanConfigOption.Builder) ((BooleanConfigOption.Builder) BooleanConfigOption.Builder.begin().setId("register_minimap_status_effects")).setDefaultValue(true)).build(ALL);

    public static void registerAll(ConfigOptionManager manager) {
        for (ConfigOption<?> option : ALL) {
            manager.register(option);
        }
    }
}
