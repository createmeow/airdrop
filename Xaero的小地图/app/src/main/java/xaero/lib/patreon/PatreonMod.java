package xaero.lib.patreon;

import java.io.File;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/patreon/PatreonMod.class */
public class PatreonMod {
    public String fileLayoutID;
    public String latestVersionLayout;
    public String changelogLink;
    public String modName;
    public File modJar;
    public String currentVersion;
    public String latestVersion;
    public String md5;
    public Runnable onVersionIgnore;

    public PatreonMod(String fileLayoutID, String latestVersionLayout, String changelogLink, String modName) {
        this.fileLayoutID = fileLayoutID;
        this.latestVersionLayout = latestVersionLayout;
        this.changelogLink = changelogLink;
        this.modName = modName;
    }
}
