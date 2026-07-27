package xaero.common.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import xaero.common.IXaeroMinimap;
import xaero.lib.client.gui.GuiSettings;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.config.EditConfigScreen;
import xaero.lib.client.gui.config.context.BuiltInEditConfigScreenContexts;
import xaero.lib.client.gui.config.context.IEditConfigScreenContext;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiHelper.class */
public abstract class GuiHelper {
    protected IXaeroMinimap modMain;

    public abstract GuiSettings getMainSettingsScreen(Screen screen);

    public abstract void openMainSettingsFromScreen(Screen screen);

    public GuiHelper(IXaeroMinimap modMain) {
        this.modMain = modMain;
    }

    public ScreenBase getMinimapSettingsFromScreen(Screen currentScreen) {
        IEditConfigScreenContext context;
        Screen escScreen = ScreenBase.tryToGetEscape(currentScreen);
        if (currentScreen instanceof EditConfigScreen) {
            EditConfigScreen ecs = (EditConfigScreen) currentScreen;
            context = ecs.getContext();
        } else {
            context = BuiltInEditConfigScreenContexts.CLIENT;
        }
        IEditConfigScreenContext context2 = context;
        return new GuiMinimapMain(this.modMain, currentScreen, escScreen, true, context2);
    }

    @Deprecated
    public void openMinimapSettingsFromScreen(Screen parent, Screen escScreen) {
        Minecraft.getInstance().setScreen(new GuiMinimapMain(this.modMain, parent, escScreen, true, BuiltInEditConfigScreenContexts.CLIENT));
    }
}
