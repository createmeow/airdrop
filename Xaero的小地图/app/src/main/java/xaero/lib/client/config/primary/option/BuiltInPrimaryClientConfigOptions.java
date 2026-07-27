package xaero.lib.client.config.primary.option;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import xaero.lib.client.config.option.ClientConfigOptionManager;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.value.type.BuiltInConfigValueTypes;
import xaero.lib.common.config.util.ConfigUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/primary/option/BuiltInPrimaryClientConfigOptions.class */
public class BuiltInPrimaryClientConfigOptions {
    private static final List<ConfigOption<?>> ALL = new ArrayList();
    public static final ConfigOption<String> CURRENT_PROFILE = ConfigOption.FinalBuilder.begin().setId("current_profile").setDefaultValue("default").setValidator(s -> {
        return !s.isEmpty() && s.length() <= 50;
    }).setDisplayName(Component.translatable("gui.xaero_current_profile")).setTooltip(Component.translatable("gui.xaero_box_current_profile")).setValueType(BuiltInConfigValueTypes.getString(50)).setDisplayGetter(ConfigUtils::getDisplayForString).build(ALL);

    public static void registerAll(ClientConfigOptionManager manager) {
        for (ConfigOption<?> option : ALL) {
            manager.register(option);
        }
    }
}
