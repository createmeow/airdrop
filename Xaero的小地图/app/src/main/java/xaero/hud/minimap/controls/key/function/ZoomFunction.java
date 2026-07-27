package xaero.hud.minimap.controls.key.function;

import java.io.IOException;
import xaero.common.HudMod;
import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.config.util.MinimapConfigClientUtils;
import xaero.hud.minimap.module.MinimapSession;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.common.config.channel.ConfigChannel;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/controls/key/function/ZoomFunction.class */
public class ZoomFunction extends KeyMappingFunction {
    private final boolean in;

    protected ZoomFunction(boolean in) {
        super(false);
        this.in = in;
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onPress() throws InterruptedException, IOException {
        MinimapSession minimapSession = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        if (minimapSession == null) {
            return;
        }
        ConfigChannel channel = HudMod.INSTANCE.getHudConfigs();
        ClientConfigManager configManager = channel.getClientConfigManager();
        if (minimapSession.getProcessor().isEnlargedMap() && ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.ZOOM_ENLARGED)).intValue() != 0) {
            return;
        }
        int zoomChange = this.in ? 1 : -1;
        MinimapConfigClientUtils.changeZoom(zoomChange);
        channel.getClientConfigProfileIO().save(configManager.getCurrentProfile());
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onRelease() {
    }
}
