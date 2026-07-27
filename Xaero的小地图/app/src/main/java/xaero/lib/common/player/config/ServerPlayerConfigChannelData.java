package xaero.lib.common.player.config;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/player/config/ServerPlayerConfigChannelData.class */
public class ServerPlayerConfigChannelData {
    private String cachedEnforcedConfigProfilePermission;
    private boolean cachedUsingConfigProfilePermission;
    private boolean cachedHasServerProfileEditPermission;

    public String getEnforcedConfigProfilePermission() {
        return this.cachedEnforcedConfigProfilePermission;
    }

    public void setCachedEnforcedConfigProfilePermission(String cachedEnforcedConfigProfilePermission) {
        this.cachedEnforcedConfigProfilePermission = cachedEnforcedConfigProfilePermission;
    }

    public boolean isUsingConfigProfilePermission() {
        return this.cachedUsingConfigProfilePermission;
    }

    public void setCachedUsingConfigProfilePermission(boolean cachedUsingConfigProfilePermission) {
        this.cachedUsingConfigProfilePermission = cachedUsingConfigProfilePermission;
    }

    public boolean hasServerProfileEditPermission() {
        return this.cachedHasServerProfileEditPermission;
    }

    public void setCachedHasServerProfileEditPermission(boolean cachedHasServerProfileEditPermission) {
        this.cachedHasServerProfileEditPermission = cachedHasServerProfileEditPermission;
    }
}
