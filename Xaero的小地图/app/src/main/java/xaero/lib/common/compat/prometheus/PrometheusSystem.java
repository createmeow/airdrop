package xaero.lib.common.compat.prometheus;

import com.teamresourceful.resourcefullib.common.utils.TriState;
import earth.terrarium.prometheus.api.permissions.PermissionApi;
import earth.terrarium.prometheus.api.roles.RoleApi;
import net.minecraft.server.level.ServerPlayer;
import xaero.lib.common.compat.ModCompatibility;
import xaero.lib.common.permission.PermissionNode;
import xaero.lib.common.permission.system.IPermissionSystem;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/compat/prometheus/PrometheusSystem.class */
public class PrometheusSystem implements IPermissionSystem {
    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public boolean getBoolean(PermissionNode<Boolean> node, ServerPlayer player) {
        Boolean boxedValue = (Boolean) get(node, player);
        return boxedValue != null && boxedValue.booleanValue();
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public boolean getBooleanFast(PermissionNode<Boolean> node, ServerPlayer player) {
        return getBoolean(node, player);
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public <T> T get(PermissionNode<T> permissionNode, ServerPlayer serverPlayer) {
        ModPrometheus modData = ModCompatibility.getInstance().getPrometheus().getModData(permissionNode.getModId());
        if (modData == null) {
            return (T) getPermission(permissionNode, serverPlayer);
        }
        PrometheusOptions prometheusOptions = (PrometheusOptions) RoleApi.API.getOption(serverPlayer, modData.getSerializer());
        if (prometheusOptions == null) {
            return (T) getPermission(permissionNode, serverPlayer);
        }
        T t = (T) prometheusOptions.get(permissionNode);
        if (t == null) {
            return (T) getPermission(permissionNode, serverPlayer);
        }
        return t;
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public <T> T getFast(PermissionNode<T> permissionNode, ServerPlayer serverPlayer) {
        return (T) get(permissionNode, serverPlayer);
    }

    private <T> T getPermission(PermissionNode<T> permissionNode, ServerPlayer serverPlayer) {
        if (permissionNode.getType() != Boolean.class) {
            return null;
        }
        TriState permission = PermissionApi.API.getPermission(serverPlayer, permissionNode.getActualPath());
        return (T) (permission.isDefined() ? Boolean.valueOf(permission.isTrue()) : null);
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public void handleModPresence(String modId) {
        ModCompatibility.getInstance().getPrometheus().registerMod(modId);
    }
}
