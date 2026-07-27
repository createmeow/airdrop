package xaero.lib.common.permission;

import java.util.HashMap;
import java.util.Map;
import xaero.lib.common.permission.ModPermissionRegistry;
import xaero.lib.common.permission.system.PermissionSystemRegistry;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/permission/PermissionRegistry.class */
public class PermissionRegistry {
    public static final PermissionRegistry INSTANCE = Builder.begin().build();
    private final Map<String, ModPermissionRegistry> mods;
    private boolean frozen;

    private PermissionRegistry(Map<String, ModPermissionRegistry> mods) {
        this.mods = mods;
    }

    private ModPermissionRegistry computeMod(String modId) {
        return this.mods.computeIfAbsent(modId, m -> {
            ModPermissionRegistry mod = ModPermissionRegistry.Builder.begin().build();
            PermissionSystemRegistry.INSTANCE.handleModPresence(m);
            return mod;
        });
    }

    public PermissionNode<?> getNode(String modId, String defaultNodePath) {
        if (!this.frozen) {
            throw new IllegalStateException();
        }
        ModPermissionRegistry mod = this.mods.get(modId);
        if (mod == null) {
            return null;
        }
        return mod.get(defaultNodePath);
    }

    public synchronized void register(PermissionNode<?> node) {
        if (this.frozen) {
            throw new IllegalStateException();
        }
        ModPermissionRegistry mod = computeMod(node.getModId());
        mod.register(node);
    }

    public Iterable<PermissionNode<?>> getModNodes(String modId) {
        if (!this.frozen) {
            throw new IllegalStateException();
        }
        return this.mods.get(modId);
    }

    public Iterable<String> getModIds() {
        if (!this.frozen) {
            throw new IllegalStateException();
        }
        return this.mods.keySet();
    }

    public synchronized void freeze() {
        this.frozen = true;
        for (ModPermissionRegistry mod : this.mods.values()) {
            mod.freeze();
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/permission/PermissionRegistry$Builder.class */
    private static final class Builder {
        private Builder() {
        }

        public Builder setDefault() {
            return this;
        }

        public PermissionRegistry build() {
            return new PermissionRegistry(new HashMap());
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
