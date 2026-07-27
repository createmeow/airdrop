package xaero.hud.minimap.controls.key.function;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import xaero.common.HudMod;
import xaero.common.effect.Effects;
import xaero.common.misc.Misc;
import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.lib.client.config.ClientConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/controls/key/function/ToggleMapFunction.class */
public class ToggleMapFunction extends KeyMappingFunction {
    protected ToggleMapFunction() {
        super(false);
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onPress() throws InterruptedException, IOException {
        Minecraft mc = Minecraft.getInstance();
        if (Misc.hasEffect(mc.player, Effects.NO_MINIMAP) || Misc.hasEffect(mc.player, Effects.NO_MINIMAP_HARMFUL)) {
            return;
        }
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        boolean currentDisplayMinimap = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.DISPLAY_MINIMAP)).booleanValue();
        configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.DISPLAY_MINIMAP, Boolean.valueOf(!currentDisplayMinimap));
        HudMod.INSTANCE.getHudConfigs().getClientConfigProfileIO().save(configManager.getCurrentProfile());
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onRelease() {
    }
}
