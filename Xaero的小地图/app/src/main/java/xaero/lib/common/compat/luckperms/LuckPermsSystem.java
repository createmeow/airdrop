package xaero.lib.common.compat.luckperms;

import java.util.Collection;
import java.util.SortedSet;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.query.QueryOptions;
import net.minecraft.server.level.ServerPlayer;
import xaero.lib.common.permission.PermissionNode;
import xaero.lib.common.permission.system.IPermissionSystem;
import xaero.lib.common.permission.util.PermissionUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/compat/luckperms/LuckPermsSystem.class */
public class LuckPermsSystem implements IPermissionSystem {
    private User getUser(ServerPlayer player) {
        return LuckPermsProvider.get().getPlayerAdapter(ServerPlayer.class).getUser(player);
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public boolean getBoolean(PermissionNode<Boolean> node, ServerPlayer player) {
        User user = getUser(player);
        Collection<Node> nodes = user.resolveDistinctInheritedNodes(QueryOptions.defaultContextualOptions());
        String actualNodePath = node.getActualPath();
        for (Node nodeObject : nodes) {
            if (nodeObject.getKey().equals(actualNodePath)) {
                return nodeObject.getValue();
            }
        }
        return false;
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public boolean getBooleanFast(PermissionNode<Boolean> node, ServerPlayer player) {
        User user = getUser(player);
        return user.getCachedData().getPermissionData().checkPermission(node.getActualPath()).asBoolean();
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public <T> T get(PermissionNode<T> permissionNode, ServerPlayer serverPlayer) {
        SortedSet<MetaNode> sortedSetResolveDistinctInheritedNodes = getUser(serverPlayer).resolveDistinctInheritedNodes(QueryOptions.defaultContextualOptions());
        String actualPath = permissionNode.getActualPath();
        for (MetaNode metaNode : sortedSetResolveDistinctInheritedNodes) {
            if (metaNode.getType() == NodeType.META && metaNode.getKey().equals(actualPath)) {
                String metaValue = metaNode.getMetaValue();
                if (metaValue == null) {
                    return null;
                }
                return (T) PermissionUtils.parseString(permissionNode, metaValue);
            }
        }
        return null;
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public <T> T getFast(PermissionNode<T> permissionNode, ServerPlayer serverPlayer) {
        String metaValue = getUser(serverPlayer).getCachedData().getMetaData().getMetaValue(permissionNode.getActualPath());
        if (metaValue == null) {
            return null;
        }
        return (T) PermissionUtils.parseString(permissionNode, metaValue);
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public void handleModPresence(String modId) {
    }
}
