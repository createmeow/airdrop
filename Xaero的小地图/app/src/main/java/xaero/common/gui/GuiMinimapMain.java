package xaero.common.gui;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.module.MinimapSession;
import xaero.lib.client.gui.GuiConstants;
import xaero.lib.client.gui.GuiSettings;
import xaero.lib.client.gui.ISettingEntry;
import xaero.lib.client.gui.ScreenBase;
import xaero.lib.client.gui.config.context.BuiltInEditConfigScreenContexts;
import xaero.lib.client.gui.config.context.IEditConfigScreenContext;
import xaero.lib.client.gui.widget.Tooltip;
import xaero.lib.common.config.option.BuiltInProfiledConfigOptions;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiMinimapMain.class */
public class GuiMinimapMain extends GuiMinimapSettings {
    private ISettingEntry[] mainEntries;
    private ISettingEntry[] searchableEntries;

    public GuiMinimapMain(Screen current) {
        this(HudMod.INSTANCE, current, ScreenBase.tryToGetEscape(current), true, BuiltInEditConfigScreenContexts.CLIENT);
    }

    public GuiMinimapMain(IXaeroMinimap modMain, Screen par1GuiScreen, Screen escScreen, boolean profileOptions, IEditConfigScreenContext context) {
        super(Component.translatable("gui.xaero_minimap_settings"), par1GuiScreen, escScreen, context);
        ScreenSwitchSettingEntry changePositionEntry = new ScreenSwitchSettingEntry("gui.xaero_change_position", (BiFunction<Screen, Screen, Screen>) (current, escape) -> {
            if (par1GuiScreen instanceof GuiEditMode) {
                return par1GuiScreen;
            }
            return new GuiEditMode(modMain, current, escape, false, Component.translatable("gui.xaero_minimap_guide"));
        }, context.isClientSide() ? null : new Tooltip(GuiConstants.SETTING_ENTRY_WRONG_CONTEXT_COMPONENT), context.isClientSide());
        ScreenSwitchSettingEntry viewSettingsEntry = new ScreenSwitchSettingEntry("gui.xaero_minimap_view_settings", (BiFunction<Screen, Screen, Screen>) (current2, escape2) -> {
            return new GuiMinimapViewSettings(modMain, current2, escape2, context);
        }, (Tooltip) null, true);
        ScreenSwitchSettingEntry entityRadarSettingsEntry = new ScreenSwitchSettingEntry("gui.xaero_entity_radar_settings", (BiFunction<Screen, Screen, Screen>) (current3, escape3) -> {
            return new GuiEntityRadarSettings(modMain, current3, escape3, context);
        }, (Tooltip) null, true);
        ScreenSwitchSettingEntry blockMapSettingsEntry = new ScreenSwitchSettingEntry("gui.xaero_minimap_block_map_settings", (BiFunction<Screen, Screen, Screen>) (current4, escape4) -> {
            return new GuiMinimapBlockMapSettings(modMain, current4, escape4, context);
        }, (Tooltip) null, true);
        ScreenSwitchSettingEntry overlaySettingsEntry = new ScreenSwitchSettingEntry("gui.xaero_overlay_settings", (BiFunction<Screen, Screen, Screen>) (current5, escape5) -> {
            return new GuiMinimapOverlaysSettings(modMain, current5, escape5, context);
        }, (Tooltip) null, true);
        ScreenSwitchSettingEntry infoSettingsEntry = new ScreenSwitchSettingEntry("gui.xaero_minimap_info_settings", (BiFunction<Screen, Screen, Screen>) (current6, escape6) -> {
            return new GuiMinimapInfoSettings(modMain, current6, escape6, context);
        }, (Tooltip) null, true);
        ScreenSwitchSettingEntry waypointSettingsEntry = new ScreenSwitchSettingEntry("gui.xaero_waypoint_settings", (BiFunction<Screen, Screen, Screen>) (current7, escape7) -> {
            return new GuiWaypointSettings(modMain, current7, escape7, context);
        }, (Tooltip) null, true);
        ScreenSwitchSettingEntry miscSettingsEntry = new ScreenSwitchSettingEntry("gui.xaero_minimap_misc_settings", (BiFunction<Screen, Screen, Screen>) (current8, escape8) -> {
            return new GuiMinimapMiscSettings(current8, escape8, context);
        }, (Tooltip) null, true);
        ScreenSwitchSettingEntry waypointsEntry = new ScreenSwitchSettingEntry("gui.xaero_waypoints", (BiFunction<Screen, Screen, Screen>) (current9, escape9) -> {
            MinimapSession minimapSession2 = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
            if (minimapSession2 != null && modMain.getSettings().waypointsGUI(minimapSession2)) {
                return new GuiWaypoints((HudMod) modMain, minimapSession2, this, escape9);
            }
            return null;
        }, context.isClientSide() ? null : new Tooltip(GuiConstants.SETTING_ENTRY_WRONG_CONTEXT_COMPONENT), () -> {
            MinimapSession minimapSession;
            return context.isClientSide() && (minimapSession = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession()) != null && modMain.getSettings().waypointsGUI(minimapSession);
        });
        List<ISettingEntry> mainEntriesBuilder = new ArrayList<>();
        if (profileOptions) {
            mainEntriesBuilder.add(createProfileIDEntry());
            mainEntriesBuilder.add(optionEntry(BuiltInProfiledConfigOptions.PROFILE_NAME));
        }
        mainEntriesBuilder.addAll(Lists.newArrayList(new ISettingEntry[]{optionEntry(MinimapProfiledConfigOptions.DISPLAY_MINIMAP), changePositionEntry, viewSettingsEntry, blockMapSettingsEntry, entityRadarSettingsEntry, overlaySettingsEntry, infoSettingsEntry, waypointSettingsEntry, miscSettingsEntry, waypointsEntry, optionEntry(BuiltInProfiledConfigOptions.IGNORE_ENFORCEMENT_IF_EDITOR)}));
        if (modMain.isStandalone()) {
            mainEntriesBuilder.add(new ScreenSwitchSettingEntry("gui.xaero_reset_config_profile_default", (BiFunction<Screen, Screen, Screen>) (current10, escape10) -> {
                return new GuiReset(this::resetConfirmResult, par1GuiScreen, escape10);
            }, (Tooltip) null, true, false));
        }
        this.mainEntries = (ISettingEntry[]) mainEntriesBuilder.toArray(new ISettingEntry[0]);
        updateSearchableEntries();
    }

    private void resetConfirmResult(boolean b) {
        if (b) {
            HudMod.INSTANCE.getEntityRadarCategoryManager().resetRootCategorySettings(this.context);
            resetProfileToDefaults();
        }
        this.minecraft.setScreen(this);
    }

    private void updateSearchableEntries() {
        if (this.mainEntries == null) {
            return;
        }
        LinkedHashSet<ISettingEntry> searchableEntriesBuilder = new LinkedHashSet<>();
        for (ISettingEntry entry : this.mainEntries) {
            if (entry instanceof ScreenSwitchSettingEntry) {
                ScreenSwitchSettingEntry screenSwitchEntry = (ScreenSwitchSettingEntry) entry;
                Screen tempScreen = screenSwitchEntry.getScreenFactory().apply(this, this);
                if (tempScreen instanceof GuiSettings) {
                    GuiSettings tempSettingsScreen = (GuiSettings) tempScreen;
                    ISettingEntry[] settingsScreenEntries = tempSettingsScreen.getEntriesCopy();
                    if (settingsScreenEntries != null) {
                        searchableEntriesBuilder.addAll(Arrays.asList(settingsScreenEntries));
                    }
                } else {
                    searchableEntriesBuilder.add(entry);
                }
            } else {
                searchableEntriesBuilder.add(entry);
            }
        }
        this.searchableEntries = (ISettingEntry[]) searchableEntriesBuilder.toArray(new ISettingEntry[0]);
    }

    @Override // xaero.lib.client.gui.config.EditConfigScreen, xaero.lib.client.gui.GuiSettings, xaero.lib.client.gui.ScreenBase
    public void init() {
        if (this.entryFilter.isEmpty()) {
            this.entries = this.mainEntries;
        } else {
            this.entries = this.searchableEntries;
        }
        super.init();
        if (ModSettings.serverSettings != ModSettings.defaultSettings) {
            this.screenTitle = Component.literal("§e" + I18n.get("gui.xaero_server_disabled", new Object[0]));
        }
    }
}
