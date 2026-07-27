package xaero.minimap;

import xaero.common.HudClientOnlyBase;
import xaero.common.HudMod;
import xaero.hud.xminimap.preset.BuiltInHudPresets;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/minimap/MinimapClientOnly.class */
public class MinimapClientOnly extends HudClientOnlyBase {
    @Override // xaero.common.HudClientOnlyBase
    public void preInit(String modId, HudMod modMain) {
        super.preInit(modId, modMain);
    }

    @Override // xaero.common.HudClientOnlyBase
    public void preLoadLater(HudMod modMain) {
        super.preLoadLater(modMain);
        BuiltInHudPresets.addAll(modMain.getHud().getPresetManager());
        BuiltInHudPresets.TOP_LEFT.apply();
    }
}
