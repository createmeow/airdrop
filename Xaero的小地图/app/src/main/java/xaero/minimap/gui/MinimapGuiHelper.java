package xaero.minimap.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import xaero.common.IXaeroMinimap;
import xaero.common.gui.GuiHelper;
import xaero.common.gui.GuiMinimapMain;
import xaero.lib.client.gui.GuiSettings;
import xaero.lib.client.gui.config.context.BuiltInEditConfigScreenContexts;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/minimap/gui/MinimapGuiHelper.class */
public class MinimapGuiHelper extends GuiHelper {
    public MinimapGuiHelper(IXaeroMinimap modMain) {
        super(modMain);
    }

    @Override // xaero.common.gui.GuiHelper
    public GuiSettings getMainSettingsScreen(Screen parent) {
        return new GuiMinimapMain(this.modMain, parent, null, true, BuiltInEditConfigScreenContexts.CLIENT);
    }

    @Override // xaero.common.gui.GuiHelper
    public void openMainSettingsFromScreen(Screen screen) {
        Minecraft.getInstance().setScreen(getMinimapSettingsFromScreen(screen));
    }
}
