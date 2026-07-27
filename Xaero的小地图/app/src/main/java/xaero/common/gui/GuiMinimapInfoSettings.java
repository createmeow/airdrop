package xaero.common.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.common.IXaeroMinimap;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.lib.client.gui.ISettingEntry;
import xaero.lib.client.gui.config.context.IEditConfigScreenContext;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiMinimapInfoSettings.class */
public class GuiMinimapInfoSettings extends GuiMinimapSettings {
    public GuiMinimapInfoSettings(IXaeroMinimap modMain, Screen backScreen, Screen escScreen, IEditConfigScreenContext context) {
        super(Component.translatable("gui.xaero_minimap_info_settings"), backScreen, escScreen, context);
        new Tooltip("gui.xaero_box_minimap_info_display_manager");
        this.entries = new ISettingEntry[]{optionEntry(MinimapProfiledConfigOptions.INFO_DISPLAY_CONFIG), optionEntry(MinimapProfiledConfigOptions.INFO_DISPLAY_BG_OPACITY), optionEntry(MinimapProfiledConfigOptions.INFO_DISPLAY_ALIGNMENT), optionEntry(MinimapProfiledConfigOptions.OPAC_CURRENT_CLAIM)};
    }
}
