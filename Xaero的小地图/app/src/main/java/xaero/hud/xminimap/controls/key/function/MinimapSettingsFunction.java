package xaero.hud.xminimap.controls.key.function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import xaero.common.HudMod;
import xaero.common.gui.GuiMinimapMain;
import xaero.hud.controls.key.function.KeyMappingFunction;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.config.context.BuiltInEditConfigScreenContexts;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/xminimap/controls/key/function/MinimapSettingsFunction.class */
public class MinimapSettingsFunction extends KeyMappingFunction {
    private final boolean server;

    protected MinimapSettingsFunction(boolean server) {
        super(false);
        this.server = server;
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onPress() {
        Screen current = Minecraft.getInstance().screen;
        Screen currentEscScreen = current instanceof ScreenBase ? ((ScreenBase) current).escape : null;
        Minecraft.getInstance().setScreen(new GuiMinimapMain(HudMod.INSTANCE, current, currentEscScreen, true, this.server ? BuiltInEditConfigScreenContexts.SERVER : BuiltInEditConfigScreenContexts.CLIENT));
    }

    @Override // xaero.hud.controls.key.function.KeyMappingFunction
    public void onRelease() {
    }
}
