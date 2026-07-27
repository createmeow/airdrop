package xaero.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import xaero.common.config.LegacyCommonConfigIO;
import xaero.common.events.ClientEvents;
import xaero.common.events.ClientEventsListener;
import xaero.common.events.CommonEvents;
import xaero.common.events.ModClientEvents;
import xaero.common.events.ModCommonEvents;
import xaero.common.gui.GuiHelper;
import xaero.common.interfaces.InterfaceManager;
import xaero.common.interfaces.render.InterfaceRenderer;
import xaero.common.minimap.radar.tracker.system.PlayerTrackerSystemManager;
import xaero.common.mods.SupportMods;
import xaero.common.platform.Services;
import xaero.common.server.mods.SupportServerMods;
import xaero.common.server.player.ServerPlayerTickHandler;
import xaero.common.settings.ModSettings;
import xaero.common.validator.FieldValidatorHolder;
import xaero.hud.Hud;
import xaero.hud.controls.ControlsRegister;
import xaero.hud.io.HudIO;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.player.tracker.PlayerTrackerMinimapElementRenderer;
import xaero.hud.minimap.player.tracker.system.RenderedPlayerTrackerManager;
import xaero.hud.minimap.radar.category.EntityRadarCategoryManager;
import xaero.hud.render.HudRenderer;
import xaero.lib.common.packet.IPacketHandler;
import xaero.lib.patreon.PatreonMod;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/IXaeroMinimap.class */
public interface IXaeroMinimap {
    public static final Path old_waypointsFile = Services.PLATFORM.getGameDir().resolve("xaerowaypoints.txt");
    public static final File wrongWaypointsFile = Services.PLATFORM.getGameDir().resolve("config").resolve("xaerowaypoints.txt").toFile();
    public static final File wrongWaypointsFolder = Services.PLATFORM.getGameDir().resolve("mods").resolve("XaeroWaypoints").toFile();

    String getVersionID();

    String getFileLayoutID();

    Path getConfigFile();

    File getModJAR();

    ModSettings getSettings();

    void setSettings(ModSettings modSettings);

    boolean isOutdated();

    void setOutdated(boolean z);

    String getMessage();

    void setMessage(String str);

    String getLatestVersion();

    String getLatestVersionMD5();

    void setLatestVersion(String str);

    void setLatestVersionMD5(String str);

    int getNewestUpdateID();

    void setNewestUpdateID(int i);

    PatreonMod getPatreon();

    String getVersionsURL();

    void resetSettings() throws IOException;

    String getUpdateLink();

    Object getSettingsKey();

    Object getServerSettingsKey();

    Path getWaypointsFile();

    Path getWaypointsFolder();

    XaeroMinimapSession createSession();

    SupportMods getSupportMods();

    SupportServerMods getSupportServerMods();

    GuiHelper getGuiHelper();

    FieldValidatorHolder getFieldValidators();

    ControlsRegister getControlsRegister();

    ClientEvents getEvents();

    void tryLoadLater();

    void tryLoadLaterServer();

    ModClientEvents getModEvents();

    InterfaceManager getInterfaces();

    InterfaceRenderer getInterfaceRenderer();

    boolean isStandalone();

    EntityRadarCategoryManager getEntityRadarCategoryManager();

    boolean isFairPlay();

    PlayerTrackerMinimapElementRenderer getTrackedPlayerRenderer();

    @Deprecated
    PlayerTrackerSystemManager getPlayerTrackerSystemManager();

    RenderedPlayerTrackerManager getPlayerTracker();

    ServerPlayerTickHandler getServerPlayerTickHandler();

    void setServerPlayerTickHandler(ServerPlayerTickHandler serverPlayerTickHandler);

    IPacketHandler getMessageHandler();

    CommonEvents getCommonEvents();

    void setCommonConfigIO(LegacyCommonConfigIO legacyCommonConfigIO);

    LegacyCommonConfigIO getCommonConfigIO();

    ClientEventsListener getClientEventsListener();

    PlatformContext getPlatformContext();

    void ensureControlsRegister();

    ModClientEvents getModClientEvents();

    ModCommonEvents getModCommonEvents();

    String getModId();

    boolean isLoadedClient();

    boolean isLoadedServer();

    Hud getHud();

    HudRenderer getHudRenderer();

    HudIO getHudIO();

    Minimap getMinimap();

    boolean isFirstStageLoaded();
}
