package xaero.lib.common.compat.ftbranks;

import dev.ftb.mods.ftbranks.api.FTBRanksAPI;
import dev.ftb.mods.ftbranks.api.PermissionValue;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import xaero.lib.common.permission.PermissionNode;
import xaero.lib.common.permission.system.IPermissionSystem;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/compat/ftbranks/FTBRanksSystem.class */
public class FTBRanksSystem implements IPermissionSystem {
    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public boolean getBoolean(PermissionNode<Boolean> node, ServerPlayer player) {
        PermissionValue value = FTBRanksAPI.getPermissionValue(player, node.getActualPath());
        if (value == null) {
            return false;
        }
        return value.asBooleanOrFalse();
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public boolean getBooleanFast(PermissionNode<Boolean> node, ServerPlayer player) {
        return getBoolean(node, player);
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public <T> T get(PermissionNode<T> permissionNode, ServerPlayer serverPlayer) {
        PermissionValue permissionValue = FTBRanksAPI.getPermissionValue(serverPlayer, permissionNode.getActualPath());
        if (permissionValue == null) {
            return null;
        }
        if (permissionNode.getType() == Boolean.class) {
            return (T) Boolean.valueOf(permissionValue.asBooleanOrFalse());
        }
        if (permissionNode.getType() == String.class) {
            return (T) permissionValue.asString().orElse(null);
        }
        if (permissionNode.getType() == Component.class) {
            String str = (String) permissionValue.asString().orElse(null);
            if (str == null) {
                return null;
            }
            return (T) Component.literal(str);
        }
        if (permissionNode.getType() == Integer.class) {
            return (T) Integer.valueOf(permissionValue.asInteger().orElse(0));
        }
        if (permissionNode.getType() == Double.class) {
            return (T) Double.valueOf(permissionValue.asDouble().orElse(0.0d));
        }
        if (permissionNode.getType() == Long.class) {
            return (T) Long.valueOf(permissionValue.asLong().orElse(0L));
        }
        if (permissionNode.getType() == Byte.class) {
            return (T) Byte.valueOf((byte) permissionValue.asInteger().orElse(0));
        }
        throw new IllegalArgumentException("Unsupported permission node type!");
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public <T> T getFast(PermissionNode<T> permissionNode, ServerPlayer serverPlayer) {
        return (T) get(permissionNode, serverPlayer);
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public void handleModPresence(String modId) {
    }
}
