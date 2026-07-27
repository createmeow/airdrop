package xaero.lib.common.compat.luckperms;

import xaero.lib.common.permission.system.IPermissionSystem;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/compat/luckperms/LuckPermsCompat.class */
public class LuckPermsCompat {
    public IPermissionSystem createPermissionSystem() {
        return new LuckPermsSystem();
    }
}
