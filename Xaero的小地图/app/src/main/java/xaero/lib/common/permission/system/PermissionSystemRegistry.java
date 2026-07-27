package xaero.lib.common.permission.system;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.server.level.ServerPlayer;
import xaero.lib.common.permission.PermissionNode;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/permission/system/PermissionSystemRegistry.class */
public class PermissionSystemRegistry implements IPermissionSystem {
    public static final PermissionSystemRegistry INSTANCE = Builder.begin().build();
    private final List<IPermissionSystem> systems;
    private boolean frozen;
    private final Set<String> presentMods;

    private PermissionSystemRegistry(List<IPermissionSystem> systems, Set<String> presentMods) {
        this.systems = systems;
        this.presentMods = presentMods;
    }

    public synchronized void register(IPermissionSystem system) {
        if (this.frozen) {
            throw new IllegalStateException();
        }
        this.systems.add(system);
        for (String presentMod : this.presentMods) {
            system.handleModPresence(presentMod);
        }
    }

    public void freeze() {
        this.frozen = true;
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public boolean getBoolean(PermissionNode<Boolean> node, ServerPlayer player) {
        for (IPermissionSystem system : this.systems) {
            if (system.getBoolean(node, player)) {
                return true;
            }
        }
        return false;
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public boolean getBooleanFast(PermissionNode<Boolean> node, ServerPlayer player) {
        for (IPermissionSystem system : this.systems) {
            if (system.getBooleanFast(node, player)) {
                return true;
            }
        }
        return false;
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public <T> T get(PermissionNode<T> permissionNode, ServerPlayer serverPlayer) {
        Iterator<IPermissionSystem> it = this.systems.iterator();
        while (it.hasNext()) {
            T t = (T) it.next().get(permissionNode, serverPlayer);
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public <T> T getFast(PermissionNode<T> permissionNode, ServerPlayer serverPlayer) {
        Iterator<IPermissionSystem> it = this.systems.iterator();
        while (it.hasNext()) {
            T t = (T) it.next().getFast(permissionNode, serverPlayer);
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    @Override // xaero.lib.common.permission.system.IPermissionSystem
    public void handleModPresence(String modId) {
        if (this.frozen) {
            throw new IllegalStateException();
        }
        this.presentMods.add(modId);
        for (IPermissionSystem system : this.systems) {
            system.handleModPresence(modId);
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/permission/system/PermissionSystemRegistry$Builder.class */
    private static final class Builder {
        private Builder() {
        }

        public Builder setDefault() {
            return this;
        }

        public PermissionSystemRegistry build() {
            return new PermissionSystemRegistry(new ArrayList(), new HashSet());
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
