package xaero.lib.common.config.option;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import xaero.lib.common.config.option.BooleanConfigOption;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.value.type.BuiltInConfigValueTypes;
import xaero.lib.common.config.util.ConfigUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/option/BuiltInProfiledConfigOptions.class */
public class BuiltInProfiledConfigOptions {
    private static final List<ConfigOption<?>> ALL = new ArrayList();
    public static final ConfigOption<String> PROFILE_NAME = ConfigOption.FinalBuilder.begin().setId("profile_name").setDisplayName(Component.translatable("gui.xaero_config_option_profile_name")).setDefaultValue("Untitled").setValidator(s -> {
        return !s.isEmpty() && s.length() <= 50;
    }).setValueType(BuiltInConfigValueTypes.getString(50)).setDisplayGetter(ConfigUtils::getDisplayForString).setSkipConfigReset(true).setOverridable(false).build(ALL);
    public static final BooleanConfigOption IGNORE_ENFORCEMENT_IF_EDITOR = ((BooleanConfigOption.Builder) ((BooleanConfigOption.Builder) ((BooleanConfigOption.Builder) ((BooleanConfigOption.Builder) BooleanConfigOption.Builder.begin().setId("ignore_enforcement_if_edit_permission")).setDefaultValue(false)).setDisplayName(Component.translatable("gui.xaero_ignore_enforcement_if_edit_permission"))).setTooltip(Component.translatable("gui.xaero_box_ignore_enforcement_if_edit_permission"))).build(ALL);

    public static void registerAll(ConfigOptionManager manager) {
        for (ConfigOption<?> option : ALL) {
            manager.register(option);
        }
    }
}
