package xaero.common.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.common.IXaeroMinimap;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.lib.client.gui.ISettingEntry;
import xaero.lib.client.gui.config.context.IEditConfigScreenContext;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiLightOverlay.class */
public class GuiLightOverlay extends GuiMinimapSettings {
    public GuiLightOverlay(IXaeroMinimap modMain, Screen par1Screen, Screen escScreen, IEditConfigScreenContext context) {
        super(Component.translatable("gui.xaero_light_overlay"), par1Screen, escScreen, context);
        this.entries = new ISettingEntry[]{optionEntry(MinimapProfiledConfigOptions.LIGHT_OVERLAY_TYPE), optionEntry(MinimapProfiledConfigOptions.LIGHT_OVERLAY_MAX_LIGHT), optionEntry(MinimapProfiledConfigOptions.LIGHT_OVERLAY_COLOR), optionEntry(MinimapProfiledConfigOptions.LIGHT_OVERLAY_MIN_LIGHT)};
    }
}
