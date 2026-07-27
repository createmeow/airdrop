package xaero.lib.common.permission.system;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.events.PermissionGatherEvent;
import net.neoforged.neoforge.server.permission.nodes.PermissionDynamicContext;
import net.neoforged.neoforge.server.permission.nodes.PermissionDynamicContextKey;
import net.neoforged.neoforge.server.permission.nodes.PermissionType;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;
import xaero.lib.XaeroLib;
import xaero.lib.common.permission.PermissionNode;
import xaero.lib.common.permission.PermissionRegistry;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/permission/system/NeoForgePermissionSystem.class */
public class NeoForgePermissionSystem implements IPermissionSystem {
    private static final Map<PermissionNode<?>, net.neoforged.neoforge.server.permission.nodes.PermissionNode<?>> NODE_MAP = new HashMap();

    /* JADX WARN: Multi-variable type inference failed */
    private <T> net.neoforged.neoforge.server.permission.nodes.PermissionNode<T> getNeo(PermissionNode<T> node) {
        return NODE_MAP.get(node);
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public boolean getBoolean(PermissionNode<Boolean> node, ServerPlayer player) {
        Boolean boxed = (Boolean) get(node, player);
        return boxed != null && boxed.booleanValue();
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public boolean getBooleanFast(PermissionNode<Boolean> node, ServerPlayer player) {
        return getBoolean(node, player);
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public <T> T get(PermissionNode<T> permissionNode, ServerPlayer serverPlayer) {
        net.neoforged.neoforge.server.permission.nodes.PermissionNode<T> neo = getNeo(permissionNode);
        if (neo == null) {
            return null;
        }
        return (T) PermissionAPI.getPermission(serverPlayer, neo, new PermissionDynamicContext[0]);
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public <T> T getFast(PermissionNode<T> permissionNode, ServerPlayer serverPlayer) {
        return (T) get(permissionNode, serverPlayer);
    }

    public static void registerNodes(PermissionGatherEvent.Nodes event) {
        for (String modId : PermissionRegistry.INSTANCE.getModIds()) {
            for (PermissionNode<?> node : PermissionRegistry.INSTANCE.getModNodes(modId)) {
                net.neoforged.neoforge.server.permission.nodes.PermissionNode<?> converted = convert(node);
                event.addNodes(new net.neoforged.neoforge.server.permission.nodes.PermissionNode[]{converted});
                NODE_MAP.put(node, converted);
            }
        }
    }

    private static <T> net.neoforged.neoforge.server.permission.nodes.PermissionNode<T> convert(PermissionNode<T> node) {
        PermissionType<T> permissionType;
        if (node.getType() == Integer.class) {
            permissionType = PermissionTypes.INTEGER;
        } else if (node.getType() == Boolean.class) {
            permissionType = PermissionTypes.BOOLEAN;
        } else if (node.getType() == String.class) {
            permissionType = PermissionTypes.STRING;
        } else if (node.getType() == Component.class) {
            permissionType = PermissionTypes.COMPONENT;
        } else {
            permissionType = null;
        }
        PermissionType<T> type = permissionType;
        if (type == null) {
            XaeroLib.LOGGER.warn("Unimplemented node type for the Forge Permissions API: " + String.valueOf(node.getType()));
            return null;
        }
        return new net.neoforged.neoforge.server.permission.nodes.PermissionNode<>(node.getModId(), node.getActualPath(), type, (player, uuid, context) -> {
            return null;
        }, new PermissionDynamicContextKey[0]);
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public void handleModPresence(String modId) {
    }
}
