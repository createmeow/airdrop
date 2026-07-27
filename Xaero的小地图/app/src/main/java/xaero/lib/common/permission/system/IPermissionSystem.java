package xaero.lib.common.permission.system;

import net.minecraft.server.level.ServerPlayer;
import xaero.lib.common.permission.PermissionNode;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/permission/system/IPermissionSystem.class */
public interface IPermissionSystem {
    boolean getBoolean(PermissionNode<Boolean> permissionNode, ServerPlayer serverPlayer);

    boolean getBooleanFast(PermissionNode<Boolean> permissionNode, ServerPlayer serverPlayer);

    <T> T get(PermissionNode<T> permissionNode, ServerPlayer serverPlayer);

    <T> T getFast(PermissionNode<T> permissionNode, ServerPlayer serverPlayer);

    void handleModPresence(String str);
}
