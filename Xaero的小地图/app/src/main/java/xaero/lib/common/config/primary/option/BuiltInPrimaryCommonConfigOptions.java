package xaero.lib.common.config.primary.option;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.ConfigOptionManager;
import xaero.lib.common.config.option.value.type.BuiltInConfigValueTypes;
import xaero.lib.common.config.util.ConfigUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/primary/option/BuiltInPrimaryCommonConfigOptions.class */
public class BuiltInPrimaryCommonConfigOptions {
    private static final List<ConfigOption<?>> ALL = new ArrayList();
    public static final ConfigOption<String> DEFAULT_ENFORCED_PROFILE = ConfigOption.FinalBuilder.begin().setId("default_enforced_profile").setDefaultValue("default").setValidator(s -> {
        return !s.isEmpty() && s.length() <= 50;
    }).setDisplayName(Component.translatable("gui.xaero_default_enforced_profile")).setTooltip(Component.translatable("gui.xaero_box_default_enforced_profile")).setValueType(BuiltInConfigValueTypes.getString(50)).setDisplayGetter(ConfigUtils::getDisplayForString).build(ALL);

    public static void registerAll(ConfigOptionManager manager) {
        for (ConfigOption<?> option : ALL) {
            manager.register(option);
        }
    }
}
