package xaero.lib.client.config.sync.profile;

import xaero.lib.common.config.profile.IConfigProfileInfo;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/sync/profile/SyncedConfigProfileInfo.class */
public class SyncedConfigProfileInfo implements IConfigProfileInfo {
    private final String id;
    private String name;

    public SyncedConfigProfileInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override // xaero.lib.common.config.profile.IConfigProfileInfo
    public String getId() {
        return this.id;
    }

    @Override // xaero.lib.common.config.profile.IConfigProfileInfo
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
