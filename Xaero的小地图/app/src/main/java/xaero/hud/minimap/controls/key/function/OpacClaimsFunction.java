package xaero.hud.minimap.controls.key.function;

import java.io.IOException;
import xaero.common.HudMod;
import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.common.config.profile.ConfigProfile;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/controls/key/function/OpacClaimsFunction.class */
public class OpacClaimsFunction extends KeyMappingFunction {
    protected OpacClaimsFunction() {
        super(false);
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onPress() throws InterruptedException, IOException {
        HudMod modMain = HudMod.INSTANCE;
        if (modMain.getSupportMods().worldmap() && modMain.getSupportMods().shouldUseWorldMapChunks()) {
            modMain.getSupportMods().worldmapSupport.toggleChunkClaims();
            return;
        }
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        ConfigProfile currentProfile = configManager.getCurrentProfile();
        currentProfile.set(MinimapProfiledConfigOptions.OPAC_CLAIMS, Boolean.valueOf(!((Boolean) currentProfile.get(MinimapProfiledConfigOptions.OPAC_CLAIMS)).booleanValue()));
        HudMod.INSTANCE.getHudConfigs().getClientConfigProfileIO().save(currentProfile);
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onRelease() {
    }
}
