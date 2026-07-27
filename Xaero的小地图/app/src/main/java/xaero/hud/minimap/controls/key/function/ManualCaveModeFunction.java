package xaero.hud.minimap.controls.key.function;

import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.module.MinimapSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/controls/key/function/ManualCaveModeFunction.class */
public class ManualCaveModeFunction extends KeyMappingFunction {
    protected ManualCaveModeFunction() {
        super(false);
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onPress() {
        ((MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession()).getProcessor().toggleManualCaveMode();
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onRelease() {
    }
}
