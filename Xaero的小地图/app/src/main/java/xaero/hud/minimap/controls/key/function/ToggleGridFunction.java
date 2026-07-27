package xaero.hud.minimap.controls.key.function;

import java.io.IOException;
import xaero.common.HudMod;
import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.common.config.profile.ConfigProfile;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/controls/key/function/ToggleGridFunction.class */
public class ToggleGridFunction extends KeyMappingFunction {
    protected ToggleGridFunction() {
        super(false);
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onPress() throws InterruptedException, IOException {
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        ConfigProfile currentProfile = configManager.getCurrentProfile();
        currentProfile.set(MinimapProfiledConfigOptions.CHUNK_GRID, Integer.valueOf((-((Integer) currentProfile.get(MinimapProfiledConfigOptions.CHUNK_GRID)).intValue()) - 1));
        HudMod.INSTANCE.getHudConfigs().getClientConfigProfileIO().save(currentProfile);
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onRelease() {
    }
}
