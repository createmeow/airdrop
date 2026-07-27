package xaero.lib.client.config.option.ui;

import xaero.lib.client.config.option.ui.type.BuiltInConfigOptionUITypes;
import xaero.lib.common.config.option.BuiltInProfiledConfigOptions;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/option/ui/BuiltInConfigOptionUIRegister.class */
public class BuiltInConfigOptionUIRegister {
    public static void registerAll(ConfigOptionUITypeManager manager) {
        manager.registerUIType(BuiltInProfiledConfigOptions.PROFILE_NAME, BuiltInConfigOptionUITypes.STRING_STRING_EDIT);
        manager.registerUIType(BuiltInProfiledConfigOptions.IGNORE_ENFORCEMENT_IF_EDITOR, BuiltInConfigOptionUITypes.TOGGLE);
    }
}
