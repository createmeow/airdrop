package xaero.hud.minimap.controls.key.function;

import xaero.common.HudMod;
import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.module.MinimapSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/controls/key/function/HeldEnlargeMapFunction.class */
public class HeldEnlargeMapFunction extends KeyMappingFunction {
    private boolean active;

    protected HeldEnlargeMapFunction() {
        super(true);
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onPress() {
        if (((Boolean) HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.TOGGLED_ENLARGED)).booleanValue() || this.active) {
            return;
        }
        this.active = true;
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        session.getProcessor().setEnlargedMap(true);
        session.getProcessor().setToResetImage(true);
        session.getProcessor().instantZoom();
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onRelease() {
        this.active = false;
        if (((Boolean) HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.TOGGLED_ENLARGED)).booleanValue()) {
            return;
        }
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        session.getProcessor().setEnlargedMap(false);
        session.getProcessor().setToResetImage(true);
        session.getProcessor().instantZoom();
    }
}
