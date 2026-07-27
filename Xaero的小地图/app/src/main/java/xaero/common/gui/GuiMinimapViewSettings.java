package xaero.common.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.common.IXaeroMinimap;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.lib.client.gui.ISettingEntry;
import xaero.lib.client.gui.config.context.IEditConfigScreenContext;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiMinimapViewSettings.class */
public class GuiMinimapViewSettings extends GuiMinimapSettings {
    public GuiMinimapViewSettings(IXaeroMinimap modMain, Screen backScreen, Screen escScreen, IEditConfigScreenContext context) {
        super(Component.translatable("gui.xaero_minimap_view_settings"), backScreen, escScreen, context);
        this.entries = new ISettingEntry[]{optionEntry(MinimapProfiledConfigOptions.SIZE), optionEntry(MinimapProfiledConfigOptions.SHAPE), optionEntry(MinimapProfiledConfigOptions.NORTH_LOCKED), optionEntry(MinimapProfiledConfigOptions.LIGHTING), optionEntry(MinimapProfiledConfigOptions.ZOOM), optionEntry(MinimapProfiledConfigOptions.CAVE_ZOOM), optionEntry(MinimapProfiledConfigOptions.OPACITY), optionEntry(MinimapProfiledConfigOptions.FRAME), optionEntry(MinimapProfiledConfigOptions.ZOOM_ENLARGED), optionEntry(MinimapProfiledConfigOptions.FRAME_COLOR), optionEntry(MinimapProfiledConfigOptions.CENTERED_ENLARGED), optionEntry(MinimapProfiledConfigOptions.KEEP_ENLARGED_UNLOCKED), optionEntry(MinimapProfiledConfigOptions.TOGGLED_ENLARGED), optionEntry(MinimapProfiledConfigOptions.HIDE_UNDER_SCREEN), optionEntry(MinimapProfiledConfigOptions.HIDE_UNDER_F3), optionEntry(MinimapProfiledConfigOptions.BOSS_HEALTH_PUSH_BOX), optionEntry(MinimapProfiledConfigOptions.POTION_EFFECT_PUSH_BOX)};
    }
}
