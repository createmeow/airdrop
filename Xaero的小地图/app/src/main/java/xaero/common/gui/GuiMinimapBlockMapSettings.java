package xaero.common.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import xaero.common.IXaeroMinimap;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.lib.client.gui.CustomSettingEntry;
import xaero.lib.client.gui.GuiConstants;
import xaero.lib.client.gui.ISettingEntry;
import xaero.lib.client.gui.config.context.IEditConfigScreenContext;
import xaero.lib.common.config.util.ConfigConstants;
import xaero.lib.common.gui.widget.TooltipInfo;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiMinimapBlockMapSettings.class */
public class GuiMinimapBlockMapSettings extends GuiMinimapSettings {
    public GuiMinimapBlockMapSettings(IXaeroMinimap modMain, Screen backScreen, Screen escScreen, IEditConfigScreenContext context) {
        super(Component.translatable("gui.xaero_minimap_block_map_settings"), backScreen, escScreen, context);
        ISettingEntry ignoreHeightmapsEntry = new CustomSettingEntry(() -> {
            return false;
        }, Component.translatable("gui.xaero_ignore_heightmaps"), context.isClientSide() ? new TooltipInfo("gui.xaero_box_ignore_heightmaps") : new TooltipInfo(GuiConstants.SETTING_ENTRY_WRONG_CONTEXT_COMPONENT, false, true), false, () -> {
            MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
            if (session == null) {
                return null;
            }
            MinimapWorldRootContainer currentRootContainer = session.getWorldManager().getAutoRootContainer();
            return Boolean.valueOf(currentRootContainer.getConfig().isIgnoreHeightmaps());
        }, 0, 1, i -> {
            return Boolean.valueOf(i == 1);
        }, v -> {
            if (modMain.getSupportMods().shouldUseWorldMapChunks()) {
                return Component.translatable("gui.xaero_world_map").withStyle(ChatFormatting.YELLOW);
            }
            return v.booleanValue() ? ConfigConstants.ON : ConfigConstants.OFF;
        }, (oldValue, newValue) -> {
            if (modMain.getSupportMods().shouldUseWorldMapChunks()) {
                modMain.getSupportMods().worldmapSupport.openSettings();
                return;
            }
            MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
            if (session == null) {
                return;
            }
            MinimapWorldRootContainer currentRootContainer = session.getWorldManager().getAutoRootContainer();
            currentRootContainer.getConfig().setIgnoreHeightmaps(newValue.booleanValue());
            currentRootContainer.getSession().getWorldManagerIO().getRootConfigIO().save(currentRootContainer);
        }, () -> {
            return context.isClientSide() && BuiltInHudModules.MINIMAP.getCurrentSession() != null;
        });
        this.entries = new ISettingEntry[]{optionEntry(MinimapProfiledConfigOptions.BLOCK_COLORS), optionEntry(MinimapProfiledConfigOptions.BIOMES_IN_VANILLA_COLORS), optionEntry(MinimapProfiledConfigOptions.CAVE_MODE_ALLOWED), optionEntry(MinimapProfiledConfigOptions.BIOME_BLENDING), optionEntry(MinimapProfiledConfigOptions.CAVE_MODE_ALLOWED_DIMENSIONS), optionEntry(MinimapProfiledConfigOptions.AUTO_CAVE_MODE), optionEntry(MinimapProfiledConfigOptions.CAVE_MODE_DEPTH), optionEntry(MinimapProfiledConfigOptions.MANUAL_CAVE_MODE_START), optionEntry(MinimapProfiledConfigOptions.LEGIBLE_CAVE_MAPS), optionEntry(MinimapProfiledConfigOptions.CAVE_MODE_TOGGLE_TIMER), optionEntry(MinimapProfiledConfigOptions.DISPLAY_WORLD_MAP_CHUNKS), optionEntry(MinimapProfiledConfigOptions.TERRAIN_DEPTH), optionEntry(MinimapProfiledConfigOptions.DISPLAY_REDSTONE), optionEntry(MinimapProfiledConfigOptions.TERRAIN_SLOPES), optionEntry(MinimapProfiledConfigOptions.DISPLAY_FLOWERS), optionEntry(MinimapProfiledConfigOptions.BLOCK_TRANSPARENCY), optionEntry(MinimapProfiledConfigOptions.DISPLAY_STAINED_GLASS), optionEntry(MinimapProfiledConfigOptions.ADJUST_HEIGHT_FOR_SHORT_BLOCKS), ignoreHeightmapsEntry, optionEntry(MinimapProfiledConfigOptions.ANTI_ALIASING)};
    }
}
