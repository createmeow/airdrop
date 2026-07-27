package xaero.hud.module;

import xaero.common.HudMod;
import xaero.hud.module.ModuleSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/module/ModuleSession.class */
public abstract class ModuleSession<MS extends ModuleSession<MS>> {
    protected final HudMod modMain;
    protected final HudModule<MS> module;

    public abstract int getWidth(double d);

    public abstract int getHeight(double d);

    public abstract void close();

    public ModuleSession(HudMod modMain, HudModule<MS> module) {
        this.modMain = modMain;
        this.module = module;
    }

    public HudModule<MS> getModule() {
        return this.module;
    }

    public HudMod getModMain() {
        return this.modMain;
    }

    public boolean isActive() {
        return this.module.isActive(this.modMain.getHudConfigs().getClientConfigManager());
    }

    public int getEffectiveX(int screenWidth, double screenScale) {
        ModuleTransform transform = this.module.getUsedTransform();
        if (!transform.centered && !transform.fromRight) {
            return transform.x;
        }
        int width = getWidth(screenScale);
        if (transform.centered) {
            return (screenWidth / 2) - (width / 2);
        }
        return (screenWidth - transform.x) - width;
    }

    public int getEffectiveY(int screenHeight, double screenScale) {
        ModuleTransform transform = this.module.getUsedTransform();
        if (!transform.fromBottom) {
            return transform.y;
        }
        int height = getHeight(screenScale);
        return (screenHeight - transform.y) - height;
    }

    public boolean isFlippedHor() {
        return this.module.getUsedTransform().flippedHor;
    }

    public boolean isFlippedVer() {
        return this.module.getUsedTransform().flippedVer;
    }

    public boolean isCentered() {
        return this.module.getUsedTransform().centered;
    }

    public boolean shouldFlipHorizontally(int screenWidth, double screenScale) {
        boolean flipped = isFlippedHor();
        int x = getEffectiveX(screenWidth, screenScale);
        int w = getWidth(screenScale);
        if (isCentered()) {
            return flipped;
        }
        return flipped ? x + (w / 2) < screenWidth / 2 : x + (w / 2) > screenWidth / 2;
    }

    public boolean shouldFlipVertically(int screenHeight, double screenScale) {
        boolean flipped = isFlippedVer();
        int y = getEffectiveY(screenHeight, screenScale);
        int h = getHeight(screenScale);
        return flipped ? y + (h / 2) < screenHeight / 2 : y + (h / 2) > screenHeight / 2;
    }

    public void prePotentialRender() {
    }

    public void onPostGameOverlay() {
    }
}
