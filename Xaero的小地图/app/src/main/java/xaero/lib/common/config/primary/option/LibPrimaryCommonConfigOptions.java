package xaero.lib.common.config.primary.option;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import xaero.hud.minimap.radar.icon.creator.RadarIconCreator;
import xaero.lib.common.config.option.BooleanConfigOption;
import xaero.lib.common.config.option.ConfigOption;
import xaero.lib.common.config.option.ConfigOptionManager;
import xaero.lib.common.config.option.value.type.BuiltInConfigValueTypes;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/config/primary/option/LibPrimaryCommonConfigOptions.class */
public class LibPrimaryCommonConfigOptions {
    private static final List<ConfigOption<?>> ALL = new ArrayList();
    public static final BooleanConfigOption ALLOW_INTERNET = ((BooleanConfigOption.Builder) ((BooleanConfigOption.Builder) ((BooleanConfigOption.Builder) ((BooleanConfigOption.Builder) BooleanConfigOption.Builder.begin().setId("allow_internet_access")).setDefaultValue(true)).setDisplayName(Component.translatable("gui.xaero_allow_internet_access"))).setTooltip(Component.translatable("gui.xaero_box_allow_internet_access"))).build(ALL);
    public static final BooleanConfigOption EVERYONE_TRACKS_EVERYONE = ((BooleanConfigOption.Builder) ((BooleanConfigOption.Builder) BooleanConfigOption.Builder.begin().setId("everyone_tracks_everyone")).setDefaultValue(false)).build(ALL);
    public static final ConfigOption<String> EDIT_SERVER_PROFILES_PERMISSION = ConfigOption.FinalBuilder.begin().setId("edit_server_profiles_permission_node").setDefaultValue("xaero.lib.edit_server_profiles").setValueType(BuiltInConfigValueTypes.getString(RadarIconCreator.FAR_PLANE)).build(ALL);

    public static void registerAll(ConfigOptionManager manager) {
        for (ConfigOption<?> option : ALL) {
            manager.register(option);
        }
    }
}
