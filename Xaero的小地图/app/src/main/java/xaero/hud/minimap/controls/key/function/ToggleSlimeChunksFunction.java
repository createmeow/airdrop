package xaero.hud.minimap.controls.key.function;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import xaero.common.HudMod;
import xaero.common.gui.GuiSlimeSeed;
import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.module.MinimapSession;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.config.context.BuiltInEditConfigScreenContexts;
import xaero.lib.common.config.profile.ConfigProfile;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/controls/key/function/ToggleSlimeChunksFunction.class */
public class ToggleSlimeChunksFunction extends KeyMappingFunction {
    protected ToggleSlimeChunksFunction() {
        super(false);
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onPress() throws InterruptedException, IOException {
        HudMod modMain = HudMod.INSTANCE;
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        ClientConfigManager configManager = modMain.getHudConfigs().getClientConfigManager();
        if (Minecraft.getInstance().getSingleplayerServer() == null && ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.OPEN_SLIME_CHUNKS_SCREEN)).booleanValue()) {
            Screen current = Minecraft.getInstance().screen;
            Screen currentEscScreen = current instanceof ScreenBase ? ((ScreenBase) current).escape : null;
            Minecraft.getInstance().setScreen(new GuiSlimeSeed(modMain, session, current, currentEscScreen, BuiltInEditConfigScreenContexts.CLIENT));
        } else {
            ConfigProfile currentProfile = configManager.getCurrentProfile();
            currentProfile.set(MinimapProfiledConfigOptions.SLIME_CHUNKS, Boolean.valueOf(!((Boolean) currentProfile.get(MinimapProfiledConfigOptions.SLIME_CHUNKS)).booleanValue()));
            modMain.getHudConfigs().getClientConfigProfileIO().save(currentProfile);
        }
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onRelease() {
    }
}
