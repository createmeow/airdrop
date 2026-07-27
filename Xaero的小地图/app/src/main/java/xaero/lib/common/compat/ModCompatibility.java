package xaero.lib.common.compat;

import java.util.HashMap;
import xaero.lib.client.compat.ClientModCompatibility;
import xaero.lib.common.compat.ftbranks.FTBRanksCompat;
import xaero.lib.common.compat.luckperms.LuckPermsCompat;
import xaero.lib.common.compat.prometheus.PrometheusCompat;
import xaero.lib.common.permission.system.PermissionSystemRegistry;
import xaero.lib.platform.Services;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/compat/ModCompatibility.class */
public class ModCompatibility {
    private static ModCompatibility instance;
    private FTBRanksCompat ftbRanks;
    private PrometheusCompat prometheus;
    private LuckPermsCompat luckPerms;
    private PlatformModCompatibility platform;

    public static ModCompatibility getInstance() {
        if (instance == null) {
            instance = Services.PLATFORM.isDedicatedServer() ? new ModCompatibility() : new ClientModCompatibility();
        }
        return instance;
    }

    protected ModCompatibility() throws ClassNotFoundException {
        try {
            Class.forName("dev.ftb.mods.ftbranks.api.FTBRanksAPI");
            this.ftbRanks = new FTBRanksCompat();
        } catch (ClassNotFoundException e) {
        }
        try {
            Class.forName("earth.terrarium.prometheus.api.permissions.PermissionApi");
            this.prometheus = createPrometheus();
        } catch (ClassNotFoundException e2) {
        }
        if (Services.PLATFORM.isDedicatedServer()) {
            try {
                Class.forName("net.luckperms.api.LuckPerms");
                this.luckPerms = new LuckPermsCompat();
            } catch (ClassNotFoundException e3) {
            }
        }
        this.platform = Services.PLATFORM.createPlatformModCompatibility();
    }

    public boolean hasFTBRanks() {
        return this.ftbRanks != null;
    }

    public boolean hasPrometheus() {
        return this.prometheus != null;
    }

    public boolean hasLuckPerms() {
        return this.luckPerms != null;
    }

    public FTBRanksCompat getFtbRanks() {
        return this.ftbRanks;
    }

    public PrometheusCompat getPrometheus() {
        return this.prometheus;
    }

    protected PrometheusCompat createPrometheus() {
        return new PrometheusCompat(new HashMap());
    }

    public LuckPermsCompat getLuckPerms() {
        return this.luckPerms;
    }

    public void registerPermissionSystems() {
        if (hasFTBRanks()) {
            PermissionSystemRegistry.INSTANCE.register(this.ftbRanks.createPermissionSystem());
        }
        if (hasLuckPerms()) {
            PermissionSystemRegistry.INSTANCE.register(this.luckPerms.createPermissionSystem());
        }
        if (hasPrometheus()) {
            PermissionSystemRegistry.INSTANCE.register(this.prometheus.createPrometheusSystem());
        }
        this.platform.registerPermissionSystems();
    }

    public void freeze() {
        if (hasPrometheus()) {
            this.prometheus.freeze();
        }
        this.platform.freeze();
    }
}
