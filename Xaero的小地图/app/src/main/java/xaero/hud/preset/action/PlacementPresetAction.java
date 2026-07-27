package xaero.hud.preset.action;

import xaero.hud.module.HudModule;
import xaero.hud.module.ModuleSession;
import xaero.hud.module.ModuleTransform;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/preset/action/PlacementPresetAction.class */
public class PlacementPresetAction<MS extends ModuleSession<MS>> implements IPresetAction<HudModule<MS>> {
    private final int x;
    private final int y;
    private final boolean centered;
    private final boolean fromRight;
    private final boolean fromBottom;
    private final boolean flippedHor;
    private final boolean flippedVer;

    public PlacementPresetAction(int x, int y, boolean centered, boolean fromRight, boolean fromBottom, boolean flippedHor, boolean flippedVer) {
        this.x = x;
        this.y = y;
        this.centered = centered;
        this.fromRight = fromRight;
        this.fromBottom = fromBottom;
        this.flippedHor = flippedHor;
        this.flippedVer = flippedVer;
    }

    @Override // xaero.hud.preset.action.IPresetAction
    public void apply(HudModule<MS> m) {
        ModuleTransform transform = m.getUnconfirmedTransform();
        transform.x = this.x;
        transform.y = this.y;
        transform.centered = this.centered;
        transform.fromRight = this.fromRight;
        transform.fromBottom = this.fromBottom;
        transform.flippedHor = this.flippedHor;
        transform.flippedVer = this.flippedVer;
    }

    @Override // xaero.hud.preset.action.IPresetAction
    public void confirm(HudModule<MS> module) {
    }

    @Override // xaero.hud.preset.action.IPresetAction
    public void cancel(HudModule<MS> module) {
    }
}
