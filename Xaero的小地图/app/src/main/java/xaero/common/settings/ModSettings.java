package xaero.common.settings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.HashMap;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.level.Level;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.mcworld.MinimapClientWorldDataHelper;
import xaero.common.minimap.waypoints.WaypointWorld;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.minimap.write.MinimapWriter;
import xaero.common.mods.SupportXaeroWorldmap;
import xaero.hud.gui.util.GuiUtils;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.config.primary.option.MinimapPrimaryClientConfigOptions;
import xaero.hud.minimap.config.util.MinimapConfigClientUtils;
import xaero.hud.minimap.controls.key.MinimapKeyMappings;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.radar.category.EntityRadarBackwardsCompatibilityConfig;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.path.XaeroPath;
import xaero.hud.path.XaeroPathReader;
import xaero.lib.XaeroLib;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.common.config.primary.option.LibPrimaryCommonConfigOptions;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/settings/ModSettings.class */
public class ModSettings {
    public static int defaultSettings;
    public static final String format = "§";
    protected IXaeroMinimap modMain;
    private EntityRadarBackwardsCompatibilityConfig entityRadarBackwardsCompatibilityConfig = new EntityRadarBackwardsCompatibilityConfig();
    private boolean foundOldRadarSettings;
    public boolean needsLegacySlimeSeedResave;
    private String loadedWaypointLines;
    public static int serverSettings;

    @Deprecated
    public static KeyMapping keyBindZoom = MinimapKeyMappings.ZOOM_IN;

    @Deprecated
    public static KeyMapping keyBindZoom1 = MinimapKeyMappings.ZOOM_OUT;

    @Deprecated
    public static KeyMapping newWaypoint = MinimapKeyMappings.ADD_WAYPOINT;

    @Deprecated
    public static KeyMapping keyWaypoints = MinimapKeyMappings.WAYPOINT_MENU;

    @Deprecated
    public static KeyMapping keyLargeMap = MinimapKeyMappings.ENLARGE_MAP;

    @Deprecated
    public static KeyMapping keyToggleMap = MinimapKeyMappings.TOGGLE_MAP;

    @Deprecated
    public static KeyMapping keyToggleWaypoints = MinimapKeyMappings.TOGGLE_WORLD_WAYPOINTS;

    @Deprecated
    public static KeyMapping keyToggleMapWaypoints = MinimapKeyMappings.TOGGLE_MAP_WAYPOINTS;

    @Deprecated
    public static KeyMapping keyToggleSlimes = MinimapKeyMappings.TOGGLE_SLIME_CHUNKS;

    @Deprecated
    public static KeyMapping keyToggleGrid = MinimapKeyMappings.TOGGLE_GRID;

    @Deprecated
    public static KeyMapping keyInstantWaypoint = MinimapKeyMappings.TEMPORARY_WAYPOINT;

    @Deprecated
    public static KeyMapping keySwitchSet = MinimapKeyMappings.SWITCH_WAYPOINT_SET;

    @Deprecated
    public static KeyMapping keyAllSets = MinimapKeyMappings.RENDER_ALL_SETS;

    @Deprecated
    public static KeyMapping keyLightOverlay = MinimapKeyMappings.LIGHT_OVERLAY;

    @Deprecated
    public static KeyMapping keyToggleRadar = MinimapKeyMappings.TOGGLE_RADAR;

    @Deprecated
    public static KeyMapping keyReverseEntityRadar = MinimapKeyMappings.REVERSE_ENTITY_RADAR;

    @Deprecated
    public static KeyMapping keyManualCaveMode = MinimapKeyMappings.MANUAL_CAVE_MODE;

    @Deprecated
    public static KeyMapping keyAlternativeListPlayers = MinimapKeyMappings.ALTERNATIVE_LIST_PLAYERS;

    @Deprecated
    public static KeyMapping keyToggleTrackedPlayersOnMap = MinimapKeyMappings.TOGGLE_TRACKED_PLAYERS_MAP;

    @Deprecated
    public static KeyMapping keyToggleTrackedPlayersInWorld = MinimapKeyMappings.TOGGLE_TRACKED_PLAYERS_WORLD;

    @Deprecated
    public static KeyMapping keyToggleTrackedPlayers = MinimapKeyMappings.TOGGLE_TRACKED_PLAYERS_MAP;

    @Deprecated
    public static KeyMapping keyTogglePacChunkClaims = MinimapKeyMappings.TOGGLE_OPAC_CLAIMS;
    private static HashMap<XaeroPath, Long> legacyServerSlimeSeeds = new HashMap<>();
    private static final String[] SHOW_LIGHT_LEVEL_NAMES = {"gui.xaero_off", "gui.xaero_light_block", "gui.xaero_light_sky", "gui.xaero_light_all", "gui.xaero_light_both2"};
    private static int[] OLD_MINIMAP_SIZES = {57, 85, 113, 169};
    public Boolean showCoordsLegacy;
    public Boolean showBiomeLegacy;
    public Integer showLightLevelLegacy;
    public Integer showTimeLegacy;
    public Boolean showAnglesLegacy;
    public Boolean showDimensionNameLegacy;
    public Boolean displayWeatherInfoLegacy;

    public ModSettings(IXaeroMinimap modMain) {
        this.modMain = modMain;
        defaultSettings = modMain.getVersionID().endsWith("fair") ? 16188159 : MinimapWriter.NO_Y_VALUE;
        if (serverSettings == 0) {
            serverSettings = defaultSettings;
        }
    }

    @Deprecated
    public boolean isKeyRepeat(KeyMapping kb) {
        if (kb == this.modMain.getSettingsKey() || kb == keyWaypoints || kb == newWaypoint || kb == keyLargeMap || kb == keyToggleMap || kb == keyToggleWaypoints || kb == keyToggleMapWaypoints || kb == keyToggleSlimes || kb == keyToggleGrid || kb == keyInstantWaypoint || kb == keySwitchSet || kb == keyAllSets || kb == keyLightOverlay || kb == keyBindZoom || kb == keyBindZoom1 || kb == keyToggleRadar || kb == keyReverseEntityRadar || kb == keyManualCaveMode || kb == keyToggleTrackedPlayersOnMap || kb == keyToggleTrackedPlayersInWorld || kb == keyTogglePacChunkClaims) {
            return false;
        }
        return true;
    }

    @Deprecated
    public boolean getMinimap() {
        return ((Boolean) HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getEffective(MinimapProfiledConfigOptions.DISPLAY_MINIMAP)).booleanValue();
    }

    @Deprecated
    public void setSlimeChunksSeed(long seed, XaeroPath fullWorldID) {
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        if (session == null) {
            legacyServerSlimeSeeds.put(fullWorldID, Long.valueOf(seed));
            return;
        }
        MinimapWorld minimapWorld = session.getWorldManager().getWorld(fullWorldID);
        if (minimapWorld == null) {
            legacyServerSlimeSeeds.put(fullWorldID, Long.valueOf(seed));
        } else {
            minimapWorld.setSlimeChunkSeed(Long.valueOf(seed));
        }
    }

    @Deprecated
    public Long getSlimeChunksSeed(XaeroPath fullWorldID) {
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        if (session == null) {
            return getLegacySlimeChunksSeed(fullWorldID);
        }
        MinimapWorld minimapWorld = session.getWorldManager().getWorld(fullWorldID);
        if (minimapWorld == null) {
            return getLegacySlimeChunksSeed(fullWorldID);
        }
        return minimapWorld.getSlimeChunkSeed();
    }

    @Deprecated
    public Long getLegacySlimeChunksSeed(XaeroPath fullWorldID) {
        return legacyServerSlimeSeeds.get(fullWorldID);
    }

    public Long removeLegacySlimeChunksSeed(XaeroPath fullWorldID) {
        this.needsLegacySlimeSeedResave = true;
        return legacyServerSlimeSeeds.remove(fullWorldID);
    }

    @Deprecated
    public boolean getSlimeChunks(WaypointsManager waypointsManager) {
        return getSlimeChunks((MinimapSession) waypointsManager);
    }

    @Deprecated
    public boolean getSlimeChunks(MinimapSession session) {
        return MinimapConfigClientUtils.getEffectiveSlimeChunks(session);
    }

    @Deprecated
    public boolean waypointsGUI(WaypointsManager waypointsManager) {
        return waypointsGUI((MinimapSession) waypointsManager);
    }

    public boolean waypointsGUI(MinimapSession waypointSession) {
        MinimapProcessor processor = waypointSession.getProcessor();
        return (Minecraft.getInstance().player == null || waypointSession.getWorldState().getAutoWorldPath() == null || (processor.getMinimapItem() != null && Minecraft.getInstance().player != null && !MinimapProcessor.hasMinimapItem(Minecraft.getInstance().player))) ? false : true;
    }

    public float getMinimapScale() {
        return GuiUtils.getMinimapScale(HudMod.INSTANCE.getHudConfigs().getClientConfigManager());
    }

    public float getUIScale(int optionValue, int min, int max) {
        return GuiUtils.getUIScale(optionValue, min, max);
    }

    public int getAutoUIScale() {
        return GuiUtils.getAutoUIScale();
    }

    private int getMaxWaypointsDistance(int exp) {
        if (exp <= 0) {
            return 0;
        }
        return (int) Math.pow(2.0d, 2 + exp);
    }

    private boolean assumeUsingFBO() {
        return this.modMain.getInterfaces().getMinimapInterface().getMinimapFBORenderer().assumeUsingFBO();
    }

    public boolean isIgnoreHeightmaps() {
        if (this.modMain.getSupportMods().shouldUseWorldMapChunks()) {
            return this.modMain.getSupportMods().worldmapSupport.getWorldMapIgnoreHeightmaps();
        }
        MinimapWorldRootContainer currentRootContainer = ((MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession()).getWorldManager().getAutoRootContainer();
        return currentRootContainer.getConfig().isIgnoreHeightmaps();
    }

    @Deprecated
    public void saveAllWaypoints(WaypointsManager waypointsManager) throws IOException {
        waypointsManager.getWorldManagerIO().saveAllWorlds(waypointsManager);
    }

    @Deprecated
    public void saveWaypoints(WaypointWorld wpw) throws IOException {
        saveWaypoints(wpw, true);
    }

    @Deprecated
    public void saveWaypoints(WaypointWorld wpw, boolean overwrite) throws IOException {
        wpw.getContainer().getSession().getWorldManagerIO().saveWorld(wpw, overwrite);
    }

    public void writeSettings(PrintWriter writer) {
    }

    public void saveSettings() throws IOException {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(this.modMain.getConfigFile().toFile()));
            writeSettings(writer);
            Object[] keys = legacyServerSlimeSeeds.keySet().toArray();
            Object[] values = legacyServerSlimeSeeds.values().toArray();
            for (int i = 0; i < keys.length; i++) {
                writer.println("seed:" + String.valueOf(keys[i]) + ":" + String.valueOf(values[i]));
            }
            if (this.loadedWaypointLines != null && !this.loadedWaypointLines.isEmpty()) {
                writer.print(this.loadedWaypointLines);
            }
            this.modMain.getHudIO().save(writer);
            if (writer != null) {
                writer.close();
            }
            this.needsLegacySlimeSeedResave = false;
        } catch (Throwable th) {
            if (writer != null) {
                writer.close();
            }
            throw th;
        }
    }

    public void readSetting(String[] args) throws NumberFormatException {
        String valueString = args.length < 2 ? "" : args[1];
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        if (args[0].equalsIgnoreCase("ignoreUpdate")) {
            configManager.getPrimaryConfigManager().getConfig().set(MinimapPrimaryClientConfigOptions.IGNORED_UPDATE, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("updateNotification")) {
            configManager.getPrimaryConfigManager().getConfig().set(MinimapPrimaryClientConfigOptions.UPDATE_NOTIFICATIONS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("minimapItemId")) {
            String minimapItemId = valueString + ":" + args[2];
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.MINIMAP_ITEM, minimapItemId);
            return;
        }
        if (args[0].equalsIgnoreCase("allowWrongWorldTeleportation")) {
            configManager.getPrimaryConfigManager().getConfig().set(MinimapPrimaryClientConfigOptions.WRONG_WORLD_TELEPORT, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("differentiateByServerAddress")) {
            configManager.getPrimaryConfigManager().getConfig().set(MinimapPrimaryClientConfigOptions.DIFFERENTIATE_BY_SERVER_ADDRESS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("debugEntityIcons")) {
            configManager.getPrimaryConfigManager().getConfig().set(MinimapPrimaryClientConfigOptions.DEBUG_ENTITY_ICONS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("debugEntityVariantIds")) {
            configManager.getPrimaryConfigManager().getConfig().set(MinimapPrimaryClientConfigOptions.DEBUG_ENTITY_VARIANT_IDS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("radarHideInvisibleEntities")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.RADAR_HIDE_INVISIBLE, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("allowInternetAccess")) {
            boolean savedAllowInternetAccess = valueString.equals("true");
            if (!savedAllowInternetAccess) {
                XaeroLib.INSTANCE.getLibConfigChannel().getPrimaryCommonConfigManager().getConfig().set(LibPrimaryCommonConfigOptions.ALLOW_INTERNET, false);
                XaeroLib.INSTANCE.getLibConfigChannel().getPrimaryCommonConfigManagerIO().save();
                return;
            }
            return;
        }
        if (args[0].equalsIgnoreCase(SupportXaeroWorldmap.MINIMAP_MW)) {
            BuiltInHudModules.MINIMAP.setActive(configManager, valueString.equals("true"));
            return;
        }
        if (args[0].equalsIgnoreCase("caveMaps")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.AUTO_CAVE_MODE, Integer.valueOf(valueString.equals("true") ? 1 : valueString.equals("false") ? 0 : Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("caveZoom")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.CAVE_ZOOM, Integer.valueOf(valueString.equals("true") ? 3 : valueString.equals("false") ? 1 : Integer.parseInt(valueString) + 1));
            return;
        }
        if (args[0].equalsIgnoreCase("showWaypoints")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINTS_ON_MINIMAP, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("deathpoints")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.DEATHPOINTS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("oldDeathpoints")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.OLD_DEATHPOINTS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("showIngameWaypoints")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINTS_IN_WORLD, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("displayRedstone")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.DISPLAY_REDSTONE, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("distance")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_DISTANCE_IN_WORLD, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("showCoords")) {
            this.showCoordsLegacy = Boolean.valueOf(valueString.equals("true"));
            return;
        }
        if (args[0].equalsIgnoreCase("lockNorth")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.NORTH_LOCKED, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("zoom")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.ZOOM, Integer.valueOf(Integer.parseInt(valueString) + 1));
            return;
        }
        if (args[0].equalsIgnoreCase("mapSize")) {
            int oldSize = Integer.parseInt(valueString);
            if (oldSize == -1) {
                configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.SIZE, 0);
                return;
            } else {
                configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.SIZE, Integer.valueOf(OLD_MINIMAP_SIZES[oldSize]));
                return;
            }
        }
        if (args[0].equalsIgnoreCase("minimapSize")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.SIZE, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("chunkGrid")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.CHUNK_GRID, Integer.valueOf(valueString.equals("true") ? 0 : valueString.equals("false") ? -1 : Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("slimeChunks")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.SLIME_CHUNKS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("mapSafeMode")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.SAFE_MODE, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("minimapOpacity")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.OPACITY, Integer.valueOf(Double.valueOf(valueString).intValue()));
            return;
        }
        if (args[0].equalsIgnoreCase("waypointsIngameIconScale")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_ICON_SCALE_IN_WORLD, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("waypointsIngameDistanceScale")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_DISTANCE_SCALE_IN_WORLD, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("waypointsIngameNameScale")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_NAME_SCALE_IN_WORLD, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("waypointsIngameCloseScale")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_CLOSE_SCALE_IN_WORLD, Double.valueOf(valueString));
            return;
        }
        if (args[0].equalsIgnoreCase("antiAliasing")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.ANTI_ALIASING, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("blockColours")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.BLOCK_COLORS, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("lighting")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.LIGHTING, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("dotsStyle")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.RADAR_DOTS_STYLE, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("dotNameScale")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.RADAR_NAME_SCALE, Double.valueOf(valueString));
            return;
        }
        if (args[0].equalsIgnoreCase("compassOverEverything")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.COMPASS_OVER_EVERYTHING, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("showBiome")) {
            this.showBiomeLegacy = Boolean.valueOf(valueString.equals("true"));
            return;
        }
        if (args[0].equalsIgnoreCase("showFlowers")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.DISPLAY_FLOWERS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("keepWaypointNames")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_NAME_IN_WORLD, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("waypointsDistance")) {
            double oldValue = Double.valueOf(valueString).doubleValue();
            int exp = oldValue <= 0.0d ? 0 : ((int) Math.max(3.0d, Math.ceil(Math.log(oldValue) / Math.log(2.0d)))) - 2;
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_MAX_DISTANCE, Integer.valueOf(getMaxWaypointsDistance(exp)));
            return;
        }
        if (args[0].equalsIgnoreCase("waypointsDistanceExp")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_MAX_DISTANCE, Integer.valueOf(getMaxWaypointsDistance(Integer.parseInt(valueString))));
            return;
        }
        if (args[0].equalsIgnoreCase("waypointsDistanceMin")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_MIN_DISTANCE_IN_WORLD, Double.valueOf(valueString));
            return;
        }
        if (args[0].equalsIgnoreCase("waypointTp")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_DEFAULT_TELEPORT_FORMAT, "/" + valueString + " {x} {y} {z}");
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_DEFAULT_TELEPORT_ROTATION_FORMAT, "/" + valueString + " {x} {y} {z} {yaw} ~");
            return;
        }
        if (args[0].equalsIgnoreCase("waypointTPCommand")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_DEFAULT_TELEPORT_FORMAT, valueString.replace("^col^", ":") + " {x} {y} {z}");
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_DEFAULT_TELEPORT_ROTATION_FORMAT, valueString.replace("^col^", ":") + " {x} {y} {z} {yaw} ~");
            return;
        }
        if (args[0].equalsIgnoreCase("defaultWaypointTPCommandFormat")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_DEFAULT_TELEPORT_FORMAT, valueString.replace("^col^", ":"));
            return;
        }
        if (args[0].equalsIgnoreCase("defaultWaypointTPCommandRotationFormat")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_DEFAULT_TELEPORT_ROTATION_FORMAT, valueString.replace("^col^", ":"));
            return;
        }
        if (args[0].equalsIgnoreCase("arrowScale")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.ARROW_SCALE, Double.valueOf(valueString));
            return;
        }
        if (args[0].equalsIgnoreCase("arrowColour")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.ARROW_COLOR, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("seed")) {
            legacyServerSlimeSeeds.put(new XaeroPathReader().read(valueString), Long.valueOf(Long.parseLong(args[2])));
            return;
        }
        if (args[0].equalsIgnoreCase("smoothDots")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.RADAR_SMOOTH_DOTS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("worldMap")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.DISPLAY_WORLD_MAP_CHUNKS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("terrainDepth")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.TERRAIN_DEPTH, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("terrainSlopes")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.TERRAIN_SLOPES, Integer.valueOf(valueString.equals("true") ? 2 : valueString.equals("false") ? 0 : Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("alwaysArrow") && valueString.equals("true")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.RADAR_MAIN_ENTITY, 2);
            return;
        }
        if (args[0].equalsIgnoreCase("mainEntityAs")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.RADAR_MAIN_ENTITY, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("blockTransparency")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.BLOCK_TRANSPARENCY, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("waypointOpacityIngame")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_OPACITY_IN_WORLD, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("waypointOpacityMap")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_OPACITY_ON_MINIMAP, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("hideWorldNames")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.HIDE_WORLD_NAMES, Integer.valueOf(valueString.equals("true") ? 2 : valueString.equals("false") ? 1 : Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("openSlimeSettings")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.OPEN_SLIME_CHUNKS_SCREEN, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("alwaysShowDistance")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_SHORT_DISTANCE_IN_WORLD, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("showLightLevel")) {
            this.showLightLevelLegacy = Integer.valueOf(valueString.equals("true") ? 1 : valueString.equals("false") ? 0 : Integer.parseInt(valueString));
            return;
        }
        if (args[0].equalsIgnoreCase("renderLayerIndex")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.RENDER_LAYER, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("crossDimensionalTp")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_TELEPORT_CROSS_DIMENSION, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("showTime")) {
            this.showTimeLegacy = Integer.valueOf(Integer.parseInt(valueString));
            return;
        }
        if (args[0].equalsIgnoreCase("biomeColorsVanillaMode")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.BIOMES_IN_VANILLA_COLORS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("lookingAtAngle")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_HORIZONTAL_POINTING_ANGLE, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("lookingAtAngleVertical")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_VERTICAL_POINTING_ANGLE, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("centeredEnlarged")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.CENTERED_ENLARGED, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("zoomedOutEnlarged")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.ZOOM_ENLARGED, Integer.valueOf(valueString.equals("true") ? 1 : 0));
            return;
        }
        if (args[0].equalsIgnoreCase("zoomOnEnlarged")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.ZOOM_ENLARGED, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("minimapTextAlign")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.INFO_DISPLAY_ALIGNMENT, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("showAngles")) {
            this.showAnglesLegacy = Boolean.valueOf(valueString.equals("true"));
            return;
        }
        if (args[0].equalsIgnoreCase("waypointsMutualEdit")) {
            configManager.getPrimaryConfigManager().getConfig().set(MinimapPrimaryClientConfigOptions.WAYPOINT_MUTUAL_EDIT, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("compass")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.COMPASS_LOCATION, Integer.valueOf(valueString.equals("true") ? 1 : 0));
            return;
        }
        if (args[0].equalsIgnoreCase("compassLocation")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.COMPASS_LOCATION, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("compassDirectionScale")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.COMPASS_SCALE, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("caveMapsDepth")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.CAVE_MODE_DEPTH, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("hideWaypointCoordinates")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.HIDE_WAYPOINT_COORDINATES, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("renderAllSets")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINTS_ALL_SETS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("playerArrowOpacity")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.ARROW_OPACITY, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("waypointsBottom")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.NEW_WAYPOINTS_TO_BOTTOM, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("minimapShape")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.SHAPE, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("lightOverlayType")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.LIGHT_OVERLAY_TYPE, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("lightOverlayMaxLight")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.LIGHT_OVERLAY_MAX_LIGHT, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("lightOverlayMinLight")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.LIGHT_OVERLAY_MIN_LIGHT, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("lightOverlayColor")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.LIGHT_OVERLAY_COLOR, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("uiScale")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.UI_SCALE, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("bossHealthPushBox")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.BOSS_HEALTH_PUSH_BOX, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("potionEffectPushBox")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.POTION_EFFECT_PUSH_BOX, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("minimapFrame")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.FRAME, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("minimapFrameColor")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.FRAME_COLOR, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("compassColor")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.COMPASS_SHADOW_COLOR, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("northCompassColor")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.COMPASS_NORTH_SHADOW_COLOR, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("showDimensionName")) {
            this.showDimensionNameLegacy = Boolean.valueOf(valueString.equals("true"));
            return;
        }
        if (args[0].equalsIgnoreCase("displayMultipleWaypointInfo")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.MULTIPLE_WAYPOINTS_INFO, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("entityRadar")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.DISPLAY_RADAR, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (this.entityRadarBackwardsCompatibilityConfig.readSetting(args)) {
            this.foundOldRadarSettings = true;
            return;
        }
        if (args[0].equalsIgnoreCase("adjustHeightForCarpetLikeBlocks")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.ADJUST_HEIGHT_FOR_SHORT_BLOCKS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("autoConvertWaypointDistanceToKmThreshold")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_CONVERT_DISTANCE_TO_KM_AT, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("waypointDistancePrecision")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_DISTANCE_PRECISION, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("mainDotSize")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.RADAR_MAIN_DOT_SIZE, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("partialYTeleportation")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_PARTIAL_Y_TELEPORT, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("deleteReachedDeathpoints")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.DELETE_REACHED_DEATHPOINTS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("hideMinimapUnderScreen")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.HIDE_UNDER_SCREEN, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("manualCaveModeStart")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.MANUAL_CAVE_MODE_START, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("manualCaveModeStartAuto")) {
            boolean savedManualCaveModeStartAuto = valueString.equals("true");
            if (savedManualCaveModeStartAuto) {
                configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.MANUAL_CAVE_MODE_START, 0);
                return;
            }
            return;
        }
        if (args[0].equalsIgnoreCase("chunkGridLineWidth")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.CHUNK_GRID_LINE_WIDTH, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("hideMinimapUnderF3")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.HIDE_UNDER_F3, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("temporaryWaypointsGlobal")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.TEMPORARY_WAYPOINTS_GLOBAL, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("keepUnlockedWhenEnlarged")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.KEEP_ENLARGED_UNLOCKED, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("enlargedMinimapAToggle")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.TOGGLED_ENLARGED, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("displayStainedGlass")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.DISPLAY_STAINED_GLASS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("waypointOnMapScale")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_ICON_SCALE_ON_MINIMAP, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("switchToAutoOnDeath")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.AUTO_WAYPOINTS_ON_DEATH, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("displayWeatherInfo")) {
            this.displayWeatherInfoLegacy = Boolean.valueOf(valueString.equals("true"));
            return;
        }
        if (args[0].equalsIgnoreCase("infoDisplayBackgroundOpacity")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.INFO_DISPLAY_BG_OPACITY, Integer.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("caveModeToggleTimer")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.CAVE_MODE_TOGGLE_TIMER, Double.valueOf(Integer.parseInt(valueString)));
            return;
        }
        if (args[0].equalsIgnoreCase("legibleCaveMaps")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.LEGIBLE_CAVE_MAPS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("biomeBlending")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.BIOME_BLENDING, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("displayPacPlayers") || args[0].equalsIgnoreCase("displayTrackedPlayers")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.TRACKED_PLAYERS_ON_MINIMAP, Boolean.valueOf(valueString.equals("true")));
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.TRACKED_PLAYERS_IN_WORLD, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("displayTrackedPlayersOnMap")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.TRACKED_PLAYERS_ON_MINIMAP, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("displayTrackedPlayersInWorld")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.TRACKED_PLAYERS_IN_WORLD, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("dimensionScaledMaxWaypointDistance")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.WAYPOINT_MAX_DISTANCE_DIMENSION_SCALE, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("trackedPlayerWorldIconScale")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.TRACKED_PLAYER_WORLD_ICON_SCALE, Integer.valueOf(Integer.parseInt(args[1])));
            return;
        }
        if (args[0].equalsIgnoreCase("trackedPlayerWorldNameScale")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.TRACKED_PLAYER_WORLD_NAME_SCALE, Integer.valueOf(Integer.parseInt(args[1])));
            return;
        }
        if (args[0].equalsIgnoreCase("trackedPlayerMinimapIconScale")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.TRACKED_PLAYER_MINIMAP_ICON_SCALE, Integer.valueOf(Integer.parseInt(args[1])));
            return;
        }
        if (args[0].equalsIgnoreCase("displayClaims")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.OPAC_CLAIMS, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("displayCurrentClaim")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.OPAC_CURRENT_CLAIM, Boolean.valueOf(valueString.equals("true")));
            return;
        }
        if (args[0].equalsIgnoreCase("claimsOpacity")) {
            int borderOpacity = Integer.parseInt(valueString);
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.OPAC_CLAIMS_BORDER_OPACITY, Integer.valueOf(borderOpacity));
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.OPAC_CLAIMS_FILL_OPACITY, Integer.valueOf((borderOpacity * 58) / 100));
        } else if (args[0].equalsIgnoreCase("claimsBorderOpacity")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.OPAC_CLAIMS_BORDER_OPACITY, Integer.valueOf(Integer.parseInt(valueString)));
        } else if (args[0].equalsIgnoreCase("claimsFillOpacity")) {
            configManager.getCurrentProfile().set(MinimapProfiledConfigOptions.OPAC_CLAIMS_FILL_OPACITY, Integer.valueOf(Integer.parseInt(valueString)));
        }
    }

    public void loadDefaultSettings(boolean shouldLoadLegacySettings) throws IOException {
        Path mainConfigFile = this.modMain.getConfigFile();
        Path legacyDefaultConfigFile = mainConfigFile.getParent().resolveSibling("defaultconfigs").resolve(HudMod.INSTANCE.getOldConfigFileName());
        if (Files.exists(legacyDefaultConfigFile, new LinkOption[0])) {
            loadSettingsFile(legacyDefaultConfigFile.toFile(), shouldLoadLegacySettings);
        }
        File defaultConfigFile = mainConfigFile.getParent().resolveSibling("defaultconfigs").resolve(mainConfigFile.getFileName()).toFile();
        if (defaultConfigFile.exists()) {
            loadSettingsFile(defaultConfigFile, shouldLoadLegacySettings);
        }
    }

    public void loadSettings(boolean shouldLoadLegacySettings) throws IOException {
        loadDefaultSettings(shouldLoadLegacySettings);
        Path mainConfigFile = this.modMain.getConfigFile();
        Path configFolderPath = mainConfigFile.getParent();
        if (!Files.exists(configFolderPath, new LinkOption[0])) {
            Files.createDirectories(configFolderPath, new FileAttribute[0]);
        }
        if (Files.exists(mainConfigFile, new LinkOption[0])) {
            loadSettingsFile(mainConfigFile.toFile(), shouldLoadLegacySettings);
        }
        saveSettings();
    }

    /* JADX WARN: Removed duplicated region for block: B:44:0x0152  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void loadSettingsFile(java.io.File r7, boolean r8) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 346
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: xaero.common.settings.ModSettings.loadSettingsFile(java.io.File, boolean):void");
    }

    public static String getTranslation(boolean o) {
        return I18n.get("gui.xaero_" + (o ? "on" : "off"), new Object[0]);
    }

    @Deprecated
    private void refreshScreen() {
        GuiUtils.refreshScreenBase();
    }

    public boolean minimapDisabled() {
        return (serverSettings & 1) != 1;
    }

    public boolean caveMapsDisabled() {
        if (HudMod.INSTANCE.getHudConfigs().getClientConfigManager().getServerSynced().isChannelPresentOnServer()) {
            return false;
        }
        return (serverSettings & 16384) != 16384 || (Minecraft.getInstance().level != null && (!(MinimapClientWorldDataHelper.getCurrentWorldData().getSyncedRules().allowCaveModeOnServer || Minecraft.getInstance().level.dimension() == Level.NETHER) || (!MinimapClientWorldDataHelper.getCurrentWorldData().getSyncedRules().allowNetherCaveModeOnServer && Minecraft.getInstance().level.dimension() == Level.NETHER)));
    }

    public boolean showWaypointsDisabled() {
        return (serverSettings & 65536) != 65536;
    }

    public boolean deathpointsDisabled() {
        return (serverSettings & 2097152) == 0;
    }

    public void resetServerSettings() {
        serverSettings = defaultSettings;
    }

    public static void setServerSettings() {
    }

    public static boolean canEditIngameSettings() {
        MinimapSession minimapSession = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        return (minimapSession == null || minimapSession.getWorldState().getAutoWorldPath() == null) ? false : true;
    }

    public EntityRadarBackwardsCompatibilityConfig getEntityRadarBackwardsCompatibilityConfig() {
        return this.entityRadarBackwardsCompatibilityConfig;
    }

    public void resetEntityRadarBackwardsCompatibilityConfig() {
        this.entityRadarBackwardsCompatibilityConfig = new EntityRadarBackwardsCompatibilityConfig();
        this.foundOldRadarSettings = false;
    }

    public boolean foundOldRadarSettings() {
        return this.foundOldRadarSettings;
    }

    public String getLoadedWaypointLines() {
        return this.loadedWaypointLines;
    }

    public void removeLoadedWaypointLines() {
        this.loadedWaypointLines = null;
    }
}
