package xaero.lib.common.permission;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.network.chat.Component;
import xaero.lib.XaeroLib;
import xaero.lib.common.config.primary.option.LibPrimaryCommonConfigOptions;
import xaero.lib.common.permission.config.channel.ConfigChannelPermissionNode;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/permission/LibPermissionNodes.class */
public class LibPermissionNodes {
    private static final Set<PermissionNode<?>> ALL = new HashSet();
    public static final PermissionNode<Boolean> EDIT_SERVER_PROFILES = ConfigChannelPermissionNode.Builder.begin(Boolean.class).setModId(XaeroLib.MOD_ID).setDisplayName(Component.translatable("gui.xaero_permission_edit_server_profiles")).setActualPathOption(LibPrimaryCommonConfigOptions.EDIT_SERVER_PROFILES_PERMISSION).setConfigChannelSupplier(() -> {
        return XaeroLib.INSTANCE.getLibConfigChannel();
    }).build(ALL);

    public static void registerAll() {
        for (PermissionNode<?> node : ALL) {
            PermissionRegistry.INSTANCE.register(node);
        }
    }
}
