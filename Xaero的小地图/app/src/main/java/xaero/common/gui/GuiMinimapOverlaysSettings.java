package xaero.common.gui;

import java.util.function.BiFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.common.IXaeroMinimap;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.lib.client.gui.ISettingEntry;
import xaero.lib.client.gui.config.context.IEditConfigScreenContext;
import xaero.lib.client.gui.widget.Tooltip;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiMinimapOverlaysSettings.class */
public class GuiMinimapOverlaysSettings extends GuiMinimapSettings {
    public GuiMinimapOverlaysSettings(IXaeroMinimap modMain, Screen backScreen, Screen escScreen, IEditConfigScreenContext context) {
        super(Component.translatable("gui.xaero_overlay_settings"), backScreen, escScreen, context);
        ScreenSwitchSettingEntry lightOverlayEntry = new ScreenSwitchSettingEntry("gui.xaero_light_overlay", (BiFunction<Screen, Screen, Screen>) (current, escape) -> {
            return new GuiLightOverlay(modMain, current, escape, context);
        }, (Tooltip) null, true);
        ScreenSwitchSettingEntry slimeChunksMultiplayerEntry = null;
        MinimapSession minimapSession = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        if (minimapSession != null && context.isClientSide() && Minecraft.getInstance().getSingleplayerServer() == null) {
            MinimapWorld minimapWorld = minimapSession.getWorldManager().getAutoWorld();
            if (minimapWorld != null) {
                slimeChunksMultiplayerEntry = new ScreenSwitchSettingEntry("gui.xaero_slime_chunks", (BiFunction<Screen, Screen, Screen>) (current2, escape2) -> {
                    return new GuiSlimeSeed(modMain, minimapSession, current2, escape2, context);
                }, (Tooltip) null, true);
            }
        }
        ISettingEntry[] iSettingEntryArr = new ISettingEntry[7];
        iSettingEntryArr[0] = optionEntry(MinimapProfiledConfigOptions.CHUNK_GRID);
        iSettingEntryArr[1] = optionEntry(MinimapProfiledConfigOptions.CHUNK_GRID_LINE_WIDTH);
        iSettingEntryArr[2] = lightOverlayEntry;
        iSettingEntryArr[3] = slimeChunksMultiplayerEntry == null ? optionEntry(MinimapProfiledConfigOptions.SLIME_CHUNKS) : slimeChunksMultiplayerEntry;
        iSettingEntryArr[4] = optionEntry(MinimapProfiledConfigOptions.OPAC_CLAIMS);
        iSettingEntryArr[5] = optionEntry(MinimapProfiledConfigOptions.OPAC_CLAIMS_BORDER_OPACITY);
        iSettingEntryArr[6] = optionEntry(MinimapProfiledConfigOptions.OPAC_CLAIMS_FILL_OPACITY);
        this.entries = iSettingEntryArr;
    }
}
