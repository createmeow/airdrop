package xaero.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xaero.common.config.LegacyCommonConfigIO;
import xaero.common.config.LegacyCommonConfigInit;
import xaero.common.core.XaeroMinimapCore;
import xaero.common.events.ClientEvents;
import xaero.common.events.ClientEventsListener;
import xaero.common.events.CommonEvents;
import xaero.common.events.ModClientEvents;
import xaero.common.events.ModCommonEvents;
import xaero.common.file.SimpleBackup;
import xaero.common.gui.GuiHelper;
import xaero.common.interfaces.InterfaceManager;
import xaero.common.interfaces.render.InterfaceRenderer;
import xaero.common.minimap.MinimapInterface;
import xaero.common.minimap.radar.tracker.system.PlayerTrackerSystemManager;
import xaero.common.misc.Internet;
import xaero.common.mods.SupportMods;
import xaero.common.mods.SupportXaeroWorldmap;
import xaero.common.platform.Services;
import xaero.common.server.XaeroMinimapServer;
import xaero.common.server.core.XaeroMinimapServerCore;
import xaero.common.server.mods.SupportServerMods;
import xaero.common.server.player.ServerPlayerTickHandler;
import xaero.common.settings.ModSettings;
import xaero.common.validator.FieldValidatorHolder;
import xaero.common.validator.NumericFieldValidator;
import xaero.common.validator.WaypointCoordinateFieldValidator;
import xaero.hud.Hud;
import xaero.hud.common.config.channel.register.HudModConfigCommonRegistryHandler;
import xaero.hud.controls.ControlsRegister;
import xaero.hud.controls.key.KeyMappingControllerManager;
import xaero.hud.io.HudIO;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.common.radar.category.EntityRadarCategorySerializers;
import xaero.hud.minimap.config.primary.option.MinimapPrimaryClientConfigOptions;
import xaero.hud.minimap.info.InfoDisplayIO;
import xaero.hud.minimap.player.tracker.PlayerTrackerMinimapElementRenderer;
import xaero.hud.minimap.player.tracker.synced.SyncedRenderedPlayerTracker;
import xaero.hud.minimap.player.tracker.system.RenderedPlayerTrackerManager;
import xaero.hud.minimap.radar.category.EntityRadarCategoryManager;
import xaero.hud.render.HudRenderer;
import xaero.lib.client.config.channel.register.handler.IConfigChannelClientRegistryHandler;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.channel.register.ConfigChannelRegistry;
import xaero.lib.common.packet.IPacketHandler;
import xaero.lib.common.packet.PacketHandlerRegistry;
import xaero.lib.patreon.Patreon;
import xaero.lib.patreon.PatreonMod;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/HudMod.class */
public abstract class HudMod implements IXaeroMinimap {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final boolean FAIRPLAY = false;
    public static HudMod INSTANCE;
    protected PlatformContext platformContext;
    protected boolean loadedClient;
    protected boolean loadedServer;
    protected boolean firstStageLoaded;
    protected static final String versionID_minecraft = "1.21.1";
    private String versionID;
    private int newestUpdateID;
    private boolean isOutdated;
    private String latestVersion;
    private String latestVersionMD5;
    private ConfigChannel hudConfigs;
    private ModSettings settings;
    private ControlsRegister controlsRegister;
    protected final ClientEvents events;
    protected final ModClientEvents modClientEvents;
    protected InterfaceRenderer interfaceRenderer;
    protected InterfaceManager interfaces;
    private GuiHelper guiHelper;
    private FieldValidatorHolder fieldValidators;
    private SupportMods supportMods;
    private SupportServerMods supportServerMods;
    private EntityRadarCategorySerializers entityRadarCategorySerializers;
    private EntityRadarCategoryManager entityRadarCategoryManager;
    private ServerPlayerTickHandler serverPlayerTickHandler;
    private RenderedPlayerTrackerManager renderedPlayerTrackerManager;
    private PlayerTrackerMinimapElementRenderer trackedPlayerRenderer;
    private IPacketHandler messageHandler;
    protected final CommonEvents commonEvents;
    protected final ModCommonEvents modCommonEvents;
    private LegacyCommonConfigIO commonConfigIO;
    private ClientEventsListener clientEventsListener;
    protected Hud hud;
    protected HudRenderer hudRenderer;
    protected HudIO hudIO;
    private HudCommonBase hudCommon;
    private HudClientOnlyBase clientOnlyBase;
    private MinimapInterface minimapInterface;
    public boolean shouldLoadLegacySettings;
    private final InfoDisplayIO infoDisplaysIO;
    private Path configFile;
    public Path waypointsFile;
    public Path minimapFolder;
    private XaeroMinimapServer minimapServer;
    private String message = "";
    private File modJAR = null;

    protected abstract PlatformContext createPlatformContext();

    protected abstract String getCommonConfigFileName();

    protected abstract ModSettings createModSettings();

    protected abstract ControlsRegister createControlsRegister();

    protected abstract GuiHelper createGuiHelper();

    public abstract String getOldConfigFileName();

    protected abstract HudClientOnlyBase createClientOnly();

    protected abstract String getModName();

    protected abstract Logger getLogger();

    protected abstract ClientEventsListener createForgeEventHandlerListener();

    protected abstract HudModConfigCommonRegistryHandler createConfigCommonRegistryHandler();

    protected abstract Supplier<IConfigChannelClientRegistryHandler> createConfigClientRegistryHandlerSupplier();

    public abstract Path getConfigSubFolder();

    public abstract Path getDefaultConfigsSubFolder();

    @Override // xaero.common.IXaeroMinimap
    public boolean isLoadedClient() {
        return this.loadedClient;
    }

    @Override // xaero.common.IXaeroMinimap
    public boolean isLoadedServer() {
        return this.loadedServer;
    }

    protected HudMod() {
        INSTANCE = this;
        this.platformContext = createPlatformContext();
        this.renderedPlayerTrackerManager = RenderedPlayerTrackerManager.Builder.begin().build();
        this.commonEvents = this.platformContext.createCommonEvents(this);
        this.modCommonEvents = this.platformContext.createModCommonEvents(this);
        this.entityRadarCategorySerializers = EntityRadarCategorySerializers.Builder.begin().build();
        Path configSubFolder = getConfigSubFolder();
        Path defaultConfigsSubFolder = getDefaultConfigsSubFolder();
        this.shouldLoadLegacySettings = !Files.exists(configSubFolder, new LinkOption[0]);
        if (!Services.PLATFORM.isDedicatedServer()) {
            this.events = this.platformContext.createClientEvents(this);
            this.clientEventsListener = createForgeEventHandlerListener();
            this.modClientEvents = this.platformContext.createModClientEvents(this);
            this.entityRadarCategoryManager = EntityRadarCategoryManager.Builder.begin().setDefaultClientFilePath(configSubFolder.resolve("default_radar_categories_client.json")).setDefaultServerFilePath(configSubFolder.resolve("default_radar_categories_server.json")).setSecondaryDefaultFolderPath(defaultConfigsSubFolder).setSerializers(this.entityRadarCategorySerializers).build();
        } else {
            this.events = null;
            this.modClientEvents = null;
        }
        this.infoDisplaysIO = new InfoDisplayIO();
        this.hudConfigs = ConfigChannel.Builder.begin().setId(ResourceLocation.fromNamespaceAndPath(getModId(), "main")).setCommonRegistryHandler(createConfigCommonRegistryHandler()).setClientRegistryHandlerSupplier(createConfigClientRegistryHandlerSupplier()).setLogger(LOGGER).setConfigPath(configSubFolder).setDefaultConfigsPath(defaultConfigsSubFolder).setDefaultEnforcedServerProfileNodePath("xaero." + configSubFolder.getFileName().toString() + ".enforced_server_profile").build();
        ConfigChannelRegistry.INSTANCE.register(this.hudConfigs);
        new LegacyCommonConfigInit().init(this, getCommonConfigFileName());
    }

    protected void loadClient() throws IOException {
        Path wrongWaypointsFolder2;
        Logger LOGGER2 = getLogger();
        LOGGER2.info("Loading {} - Stage 1/2", getModName());
        String modId = getModId();
        Path modFile = Services.PLATFORM.getModFile(modId);
        HudClientOnlyBase hudClientOnlyBaseCreateClientOnly = createClientOnly();
        this.clientOnlyBase = hudClientOnlyBaseCreateClientOnly;
        hudClientOnlyBaseCreateClientOnly.preInit(modId, this);
        String fileName = modFile.getFileName().toString();
        if (fileName.endsWith(".jar")) {
            this.modJAR = modFile.toFile();
        }
        Path gameDir = Services.PLATFORM.getGameDir();
        Path config = Services.PLATFORM.getConfigDir();
        this.waypointsFile = config.resolve("xaerowaypoints.txt");
        Path wrongWaypointsFolder3 = config.resolve("XaeroWaypoints");
        if (this.modJAR != null) {
            wrongWaypointsFolder2 = this.modJAR.toPath().getParent().resolve("XaeroWaypoints");
        } else {
            wrongWaypointsFolder2 = config.getParent().resolve("mods").resolve("XaeroWaypoints");
        }
        Path wrongWaypointsFolder4 = new File(config.toFile().getCanonicalPath()).toPath().getParent().resolve("XaeroWaypoints");
        Path wrongWaypointsFolder5 = config.getParent().resolve("XaeroWaypoints");
        Path preMinimapWorldsFolder = gameDir.resolve("XaeroWaypoints");
        Path xaeroFolder = gameDir.resolve("xaero");
        if (!Files.exists(xaeroFolder, new LinkOption[0])) {
            Files.createDirectories(xaeroFolder, new FileAttribute[0]);
        }
        this.minimapFolder = xaeroFolder.resolve(SupportXaeroWorldmap.MINIMAP_MW);
        if (Files.exists(preMinimapWorldsFolder, new LinkOption[0]) && !Files.exists(this.minimapFolder, new LinkOption[0])) {
            Files.move(preMinimapWorldsFolder, this.minimapFolder, new CopyOption[0]);
        }
        if (wrongWaypointsFile.exists() && !Files.exists(this.waypointsFile, new LinkOption[0])) {
            Files.move(wrongWaypointsFile.toPath(), this.waypointsFile, new CopyOption[0]);
        }
        if (wrongWaypointsFolder.exists() && !Files.exists(this.minimapFolder, new LinkOption[0])) {
            Files.move(wrongWaypointsFolder.toPath(), this.minimapFolder, new CopyOption[0]);
        } else if (wrongWaypointsFolder2.toFile().exists() && !Files.exists(this.minimapFolder, new LinkOption[0])) {
            Files.move(wrongWaypointsFolder2, this.minimapFolder, new CopyOption[0]);
        } else if (wrongWaypointsFolder3.toFile().exists() && !Files.exists(this.minimapFolder, new LinkOption[0])) {
            Files.move(wrongWaypointsFolder3, this.minimapFolder, new CopyOption[0]);
        } else if (wrongWaypointsFolder4.toFile().exists() && !Files.exists(this.minimapFolder, new LinkOption[0])) {
            Files.move(wrongWaypointsFolder4, this.minimapFolder, new CopyOption[0]);
        } else if (wrongWaypointsFolder5.toFile().exists() && !Files.exists(this.minimapFolder, new LinkOption[0])) {
            Files.move(wrongWaypointsFolder5, this.minimapFolder, new CopyOption[0]);
        }
        Path waypointsFolderBackup = gameDir.resolve("XaeroWaypoints_BACKUP240807");
        if (!Files.exists(waypointsFolderBackup, new LinkOption[0]) && Files.exists(this.minimapFolder, new LinkOption[0])) {
            LOGGER2.info("Backing up Xaero's minimap data...");
            SimpleBackup.copyDirectoryWithContents(this.minimapFolder, waypointsFolderBackup, 32, new CopyOption[0]);
            LOGGER2.info("Done backing up Xaero's minimap data!");
        }
        Path legacyConfigFile = config.resolve(getOldConfigFileName());
        this.configFile = config.resolve("xaerohud.txt");
        if (Files.exists(legacyConfigFile, new LinkOption[0]) && !Files.exists(this.configFile, new LinkOption[0])) {
            Files.move(legacyConfigFile, this.configFile, new CopyOption[0]);
        }
        Path evenOlderConfigFile = gameDir.resolve("config").resolve(getOldConfigFileName());
        if (Files.exists(evenOlderConfigFile, new LinkOption[0]) && !Files.exists(this.configFile, new LinkOption[0])) {
            Files.move(evenOlderConfigFile, this.configFile, new CopyOption[0]);
        }
        this.settings = createModSettings();
        ensureControlsRegister();
        this.guiHelper = createGuiHelper();
        this.fieldValidators = new FieldValidatorHolder(new NumericFieldValidator(), new WaypointCoordinateFieldValidator());
        this.interfaceRenderer = new InterfaceRenderer(this);
        this.interfaces = new InterfaceManager(this);
        this.minimapInterface = new MinimapInterface(this);
        Path old_optionsFile = gameDir.resolve(getOldConfigFileName());
        if (Files.exists(old_optionsFile, new LinkOption[0]) && !Files.exists(this.configFile, new LinkOption[0])) {
            Path configFileParent = this.configFile.getParent();
            if (!Files.exists(configFileParent, new LinkOption[0])) {
                Files.createDirectories(configFileParent, new FileAttribute[0]);
            }
            Files.move(old_optionsFile, this.configFile, new CopyOption[0]);
        }
        if (Files.exists(old_waypointsFile, new LinkOption[0]) && !Files.exists(this.waypointsFile, new LinkOption[0])) {
            Path waypointFileParent = this.waypointsFile.getParent();
            if (!Files.exists(waypointFileParent, new LinkOption[0])) {
                Files.createDirectories(waypointFileParent, new FileAttribute[0]);
            }
            Files.move(old_waypointsFile, this.waypointsFile, new CopyOption[0]);
        }
        this.trackedPlayerRenderer = PlayerTrackerMinimapElementRenderer.Builder.begin(this).build();
        XaeroMinimapCore.modMain = this;
        this.firstStageLoaded = true;
    }

    public void loadLater() {
        PatreonMod patreonEntry;
        Logger LOGGER2 = getLogger();
        LOGGER2.info("Loading {} - Stage 2/2", getModName());
        try {
            this.minimapInterface.getInfoDisplays().getManager().applyLocalConfig();
            this.hudCommon.preLoadLater(this);
            this.clientOnlyBase.preLoadLater(this);
            this.controlsRegister.onStage2();
            this.settings.loadSettings(this.shouldLoadLegacySettings);
            if (this.shouldLoadLegacySettings) {
                this.hudConfigs.getClientConfigProfileIO().save(this.hudConfigs.getClientConfigManager().getCurrentProfile());
                this.hudConfigs.getPrimaryClientConfigManagerIO().save();
            }
            this.entityRadarCategoryManager.init();
            Patreon.checkPatreon();
            Internet.checkModVersion(this);
            if (this.isOutdated && (patreonEntry = getPatreon()) != null) {
                patreonEntry.modJar = this.modJAR;
                patreonEntry.currentVersion = getVersionID();
                patreonEntry.latestVersion = this.latestVersion;
                patreonEntry.md5 = this.latestVersionMD5;
                patreonEntry.onVersionIgnore = () -> {
                    getHudConfigs().getPrimaryClientConfigManager().getConfig().set(MinimapPrimaryClientConfigOptions.IGNORED_UPDATE, Integer.valueOf(this.newestUpdateID));
                    getHudConfigs().getPrimaryClientConfigManagerIO().save();
                };
                Patreon.addOutdatedMod(patreonEntry);
            }
            this.renderedPlayerTrackerManager.register("minimap_synced", new SyncedRenderedPlayerTracker());
            this.supportMods = this.platformContext.createSupportMods(this);
            getMinimap().getOverMapRendererHandler().add(this.trackedPlayerRenderer);
            getMinimap().getWorldRendererHandler().add(this.trackedPlayerRenderer);
        } catch (Throwable e) {
            LOGGER2.error("error", e);
            this.interfaces.getMinimapInterface().setCrashedWith(e);
        }
        this.loadedClient = true;
    }

    public void loadServer() {
        Logger LOGGER2 = getLogger();
        LOGGER2.info("Loading {} - Stage 1/2 (Server)", getModName());
        this.minimapServer = new XaeroMinimapServer(this);
        this.minimapServer.load();
        this.firstStageLoaded = true;
    }

    protected void loadCommon() {
        this.versionID = "1.21.1_" + this.platformContext.getModInfoVersion();
        XaeroMinimapServerCore.modMain = this;
        this.messageHandler = PacketHandlerRegistry.INSTANCE.register(ResourceLocation.fromNamespaceAndPath(getModId(), "main"), 1000000, "1.0");
        this.supportServerMods = new SupportServerMods();
        this.hudCommon = new HudCommonBase();
        this.hudCommon.setup(this);
    }

    protected void loadLaterServer() {
        Logger LOGGER2 = getLogger();
        LOGGER2.info("Loading {} - Stage 2/2 (Server)", getModName());
        this.hudCommon.preLoadLater(this);
        this.minimapServer.loadLater();
        this.loadedServer = true;
    }

    @Override // xaero.common.IXaeroMinimap
    public Path getConfigFile() {
        return this.configFile;
    }

    @Override // xaero.common.IXaeroMinimap
    public File getModJAR() {
        return this.modJAR;
    }

    @Override // xaero.common.IXaeroMinimap
    public ModSettings getSettings() {
        return this.settings;
    }

    @Override // xaero.common.IXaeroMinimap
    public void setSettings(ModSettings minimapSettings) {
        this.settings = minimapSettings;
    }

    @Override // xaero.common.IXaeroMinimap
    public void resetSettings() throws IOException {
    }

    @Override // xaero.common.IXaeroMinimap
    public boolean isOutdated() {
        return this.isOutdated;
    }

    @Override // xaero.common.IXaeroMinimap
    public void setOutdated(boolean value) {
        this.isOutdated = value;
    }

    @Override // xaero.common.IXaeroMinimap
    public String getMessage() {
        return this.message;
    }

    @Override // xaero.common.IXaeroMinimap
    public void setMessage(String string) {
        this.message = string;
    }

    @Override // xaero.common.IXaeroMinimap
    public String getLatestVersion() {
        return this.latestVersion;
    }

    @Override // xaero.common.IXaeroMinimap
    public void setLatestVersion(String string) {
        this.latestVersion = string;
    }

    @Override // xaero.common.IXaeroMinimap
    public int getNewestUpdateID() {
        return this.newestUpdateID;
    }

    @Override // xaero.common.IXaeroMinimap
    public void setNewestUpdateID(int parseInt) {
        this.newestUpdateID = parseInt;
    }

    @Override // xaero.common.IXaeroMinimap
    public PatreonMod getPatreon() {
        return (PatreonMod) Patreon.getMods().get(getFileLayoutID());
    }

    @Override // xaero.common.IXaeroMinimap
    public Path getWaypointsFile() {
        return this.waypointsFile;
    }

    public Path getMinimapFolder() {
        return this.minimapFolder;
    }

    @Override // xaero.common.IXaeroMinimap
    @Deprecated
    public Path getWaypointsFolder() {
        return getMinimapFolder();
    }

    @Override // xaero.common.IXaeroMinimap
    public SupportMods getSupportMods() {
        return this.supportMods;
    }

    @Override // xaero.common.IXaeroMinimap
    public ModClientEvents getModEvents() {
        return this.modClientEvents;
    }

    @Override // xaero.common.IXaeroMinimap
    public GuiHelper getGuiHelper() {
        return this.guiHelper;
    }

    @Override // xaero.common.IXaeroMinimap
    public FieldValidatorHolder getFieldValidators() {
        return this.fieldValidators;
    }

    @Override // xaero.common.IXaeroMinimap
    public ControlsRegister getControlsRegister() {
        return this.controlsRegister;
    }

    @Override // xaero.common.IXaeroMinimap
    public ClientEvents getEvents() {
        return this.events;
    }

    @Override // xaero.common.IXaeroMinimap
    public InterfaceManager getInterfaces() {
        return this.interfaces;
    }

    @Override // xaero.common.IXaeroMinimap
    public InterfaceRenderer getInterfaceRenderer() {
        return this.interfaceRenderer;
    }

    @Override // xaero.common.IXaeroMinimap
    public void setLatestVersionMD5(String string) {
        this.latestVersionMD5 = string;
    }

    @Override // xaero.common.IXaeroMinimap
    public String getLatestVersionMD5() {
        return this.latestVersionMD5;
    }

    @Override // xaero.common.IXaeroMinimap
    public boolean isStandalone() {
        return false;
    }

    @Override // xaero.common.IXaeroMinimap
    public EntityRadarCategoryManager getEntityRadarCategoryManager() {
        return this.entityRadarCategoryManager;
    }

    @Override // xaero.common.IXaeroMinimap
    public boolean isFairPlay() {
        XaeroMinimapSession session;
        return this.loadedClient && (session = XaeroMinimapSession.getCurrentSession()) != null && session.getMinimapProcessor().getForcedFairPlay();
    }

    @Override // xaero.common.IXaeroMinimap
    public PlayerTrackerMinimapElementRenderer getTrackedPlayerRenderer() {
        return this.trackedPlayerRenderer;
    }

    @Override // xaero.common.IXaeroMinimap
    @Deprecated
    public PlayerTrackerSystemManager getPlayerTrackerSystemManager() {
        return (PlayerTrackerSystemManager) this.renderedPlayerTrackerManager;
    }

    @Override // xaero.common.IXaeroMinimap
    public RenderedPlayerTrackerManager getPlayerTracker() {
        return this.renderedPlayerTrackerManager;
    }

    @Override // xaero.common.IXaeroMinimap
    public ServerPlayerTickHandler getServerPlayerTickHandler() {
        return this.serverPlayerTickHandler;
    }

    @Override // xaero.common.IXaeroMinimap
    public void setServerPlayerTickHandler(ServerPlayerTickHandler serverPlayerTickHandler) {
        this.serverPlayerTickHandler = serverPlayerTickHandler;
    }

    @Override // xaero.common.IXaeroMinimap
    public IPacketHandler getMessageHandler() {
        return this.messageHandler;
    }

    @Override // xaero.common.IXaeroMinimap
    public CommonEvents getCommonEvents() {
        return this.commonEvents;
    }

    @Override // xaero.common.IXaeroMinimap
    public SupportServerMods getSupportServerMods() {
        return this.supportServerMods;
    }

    @Override // xaero.common.IXaeroMinimap
    public void setCommonConfigIO(LegacyCommonConfigIO serverConfigIO) {
        this.commonConfigIO = serverConfigIO;
    }

    @Override // xaero.common.IXaeroMinimap
    public LegacyCommonConfigIO getCommonConfigIO() {
        return this.commonConfigIO;
    }

    @Override // xaero.common.IXaeroMinimap
    public ClientEventsListener getClientEventsListener() {
        return this.clientEventsListener;
    }

    @Override // xaero.common.IXaeroMinimap
    public PlatformContext getPlatformContext() {
        return this.platformContext;
    }

    @Override // xaero.common.IXaeroMinimap
    public void ensureControlsRegister() {
        if (this.controlsRegister == null) {
            this.controlsRegister = createControlsRegister();
        }
    }

    @Override // xaero.common.IXaeroMinimap
    public ModClientEvents getModClientEvents() {
        return this.modClientEvents;
    }

    @Override // xaero.common.IXaeroMinimap
    public ModCommonEvents getModCommonEvents() {
        return this.modCommonEvents;
    }

    @Override // xaero.common.IXaeroMinimap
    public void tryLoadLater() {
    }

    @Override // xaero.common.IXaeroMinimap
    public void tryLoadLaterServer() {
    }

    @Override // xaero.common.IXaeroMinimap
    public String getVersionID() {
        return this.versionID;
    }

    @Override // xaero.common.IXaeroMinimap
    public Hud getHud() {
        return this.hud;
    }

    @Override // xaero.common.IXaeroMinimap
    public HudRenderer getHudRenderer() {
        return this.hudRenderer;
    }

    @Override // xaero.common.IXaeroMinimap
    public HudIO getHudIO() {
        return this.hudIO;
    }

    @Override // xaero.common.IXaeroMinimap
    public Minimap getMinimap() {
        return this.minimapInterface;
    }

    @Override // xaero.common.IXaeroMinimap
    public boolean isFirstStageLoaded() {
        return this.firstStageLoaded;
    }

    public KeyMappingControllerManager getKeyMappingControllers() {
        return this.controlsRegister.getKeyMappingControllers();
    }

    public ConfigChannel getHudConfigs() {
        return this.hudConfigs;
    }

    public InfoDisplayIO getInfoDisplaysIO() {
        return this.infoDisplaysIO;
    }

    public EntityRadarCategorySerializers getEntityRadarCategorySerializers() {
        return this.entityRadarCategorySerializers;
    }
}
