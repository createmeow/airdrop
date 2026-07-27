package xaero.hud.minimap.world.state;

import xaero.hud.path.XaeroPath;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/state/MinimapWorldState.class */
public class MinimapWorldState {
    private XaeroPath autoRootContainerPath;
    private XaeroPath autoWorldPath;
    private XaeroPath customWorldPath;
    private final XaeroPath[] outdatedAutoRootContainerPaths = new XaeroPath[4];
    private XaeroPath autoContainerPathIgnoreCaseCache;

    public void setAutoRootContainerPath(XaeroPath autoRootContainerPath) {
        if (this.autoRootContainerPath != null) {
            throw new IllegalStateException();
        }
        this.autoRootContainerPath = autoRootContainerPath;
    }

    public XaeroPath getAutoRootContainerPath() {
        return this.autoRootContainerPath;
    }

    public void setOutdatedAutoRootContainerPath(int format, XaeroPath autoRootContainerPath) {
        if (this.outdatedAutoRootContainerPaths[format] != null) {
            throw new IllegalStateException();
        }
        this.outdatedAutoRootContainerPaths[format] = autoRootContainerPath;
    }

    public XaeroPath getOutdatedAutoRootContainerPath(int format) {
        return this.outdatedAutoRootContainerPaths[format];
    }

    public XaeroPath getAutoWorldPath() {
        return this.autoWorldPath;
    }

    public void setAutoWorldPath(XaeroPath autoWorldPath) {
        this.autoWorldPath = autoWorldPath;
    }

    public XaeroPath getCustomContainerPath() {
        if (this.customWorldPath == null) {
            return null;
        }
        return this.customWorldPath.getParent();
    }

    public XaeroPath getCustomWorldPath() {
        return this.customWorldPath;
    }

    public void setCustomWorldPath(XaeroPath customWorldPath) {
        this.customWorldPath = customWorldPath;
    }

    public XaeroPath getAutoContainerPathIgnoreCaseCache() {
        return this.autoContainerPathIgnoreCaseCache;
    }

    public void setAutoContainerPathIgnoreCaseCache(XaeroPath autoContainerPathIgnoreCaseCache) {
        this.autoContainerPathIgnoreCaseCache = autoContainerPathIgnoreCaseCache;
    }

    public XaeroPath getCurrentWorldPath() {
        return getCurrentWorldPath(this.autoWorldPath);
    }

    public XaeroPath getCurrentContainerPath() {
        XaeroPath worldPath = getCurrentWorldPath();
        if (worldPath == null) {
            return null;
        }
        return worldPath.getParent();
    }

    public XaeroPath getCurrentRootContainerPath() {
        XaeroPath containerPath = getCurrentContainerPath();
        if (containerPath == null) {
            return null;
        }
        return containerPath.getRoot();
    }

    public XaeroPath getCurrentWorldPath(XaeroPath autoWorldPath) {
        if (this.customWorldPath == null) {
            return autoWorldPath;
        }
        return this.customWorldPath;
    }
}
