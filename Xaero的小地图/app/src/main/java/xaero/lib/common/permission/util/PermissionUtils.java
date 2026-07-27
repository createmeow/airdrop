package xaero.lib.common.permission.util;

import net.minecraft.network.chat.Component;
import xaero.lib.common.permission.PermissionNode;
import xaero.lib.common.util.JsonUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/permission/util/PermissionUtils.class */
public class PermissionUtils {
    /* JADX WARN: Multi-variable type inference failed */
    public static <T> T parseString(PermissionNode<T> permissionNode, String str) {
        if (permissionNode.getType() == Boolean.class) {
            return (T) Boolean.valueOf(str.equals("true"));
        }
        if (permissionNode.getType() == String.class) {
            return str;
        }
        if (permissionNode.getType() == Component.class) {
            return (T) JsonUtils.fromJson(str);
        }
        if (permissionNode.getType() == Integer.class) {
            return (T) Integer.valueOf(str);
        }
        if (permissionNode.getType() == Double.class) {
            return (T) Double.valueOf(str);
        }
        if (permissionNode.getType() == Long.class) {
            return (T) Long.valueOf(str);
        }
        if (permissionNode.getType() == Byte.class) {
            return (T) Byte.valueOf(str);
        }
        throw new IllegalArgumentException("Unsupported permission node type: " + String.valueOf(permissionNode.getType()));
    }
}
