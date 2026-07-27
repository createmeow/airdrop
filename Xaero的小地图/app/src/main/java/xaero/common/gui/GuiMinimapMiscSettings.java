package xaero.common.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.config.primary.option.MinimapPrimaryClientConfigOptions;
import xaero.lib.client.gui.ISettingEntry;
import xaero.lib.client.gui.config.context.IEditConfigScreenContext;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiMinimapMiscSettings.class */
public class GuiMinimapMiscSettings extends GuiMinimapSettings {
    public GuiMinimapMiscSettings(Screen backScreen, Screen escScreen, IEditConfigScreenContext context) {
        super(Component.translatable("gui.xaero_minimap_misc_settings"), backScreen, escScreen, context);
        this.entries = new ISettingEntry[]{optionEntry(MinimapProfiledConfigOptions.SAFE_MODE), primaryOptionEntry(MinimapPrimaryClientConfigOptions.UPDATE_NOTIFICATIONS), optionEntry(MinimapProfiledConfigOptions.UI_SCALE), optionEntry(MinimapProfiledConfigOptions.MINIMAP_ITEM)};
    }
}
