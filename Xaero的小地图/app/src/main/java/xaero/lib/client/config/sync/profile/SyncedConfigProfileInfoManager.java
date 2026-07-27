package xaero.lib.client.config.sync.profile;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nonnull;
import xaero.lib.common.config.profile.IConfigProfileInfo;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/sync/profile/SyncedConfigProfileInfoManager.class */
public final class SyncedConfigProfileInfoManager implements Iterable<IConfigProfileInfo> {
    private final Map<String, SyncedConfigProfileInfo> profileInfoMap;
    private String defaultEnforcedProfileId;

    private SyncedConfigProfileInfoManager(Map<String, SyncedConfigProfileInfo> profileInfoMap) {
        this.profileInfoMap = profileInfoMap;
    }

    public void add(String id, String name) {
        this.profileInfoMap.put(id, new SyncedConfigProfileInfo(id, name));
    }

    public void remove(String id) {
        this.profileInfoMap.remove(id);
    }

    public SyncedConfigProfileInfo get(String id) {
        return this.profileInfoMap.get(id);
    }

    public void setDefaultEnforcedProfileId(String defaultEnforcedProfileId) {
        this.defaultEnforcedProfileId = defaultEnforcedProfileId;
    }

    public String getDefaultEnforcedProfileId() {
        return this.defaultEnforcedProfileId;
    }

    public void reset() {
        this.profileInfoMap.clear();
        this.defaultEnforcedProfileId = null;
    }

    @Override // java.lang.Iterable
    @Nonnull
    public Iterator<IConfigProfileInfo> iterator() {
        return this.profileInfoMap.values().stream().map(Function.identity()).iterator();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/config/sync/profile/SyncedConfigProfileInfoManager$Builder.class */
    public static final class Builder {
        private Builder() {
        }

        public Builder setDefault() {
            return this;
        }

        public SyncedConfigProfileInfoManager build() {
            return new SyncedConfigProfileInfoManager(new LinkedHashMap());
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
