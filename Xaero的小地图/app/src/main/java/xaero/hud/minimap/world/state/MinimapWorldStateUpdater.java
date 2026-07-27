package xaero.hud.minimap.world.state;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import xaero.common.HudMod;
import xaero.common.minimap.mcworld.MinimapClientWorldData;
import xaero.common.minimap.mcworld.MinimapClientWorldDataHelper;
import xaero.common.mods.SupportXaeroWorldmap;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.config.primary.option.MinimapPrimaryClientConfigOptions;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.minimap.world.container.MinimapWorldContainerUtil;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.minimap.world.container.config.RootConfig;
import xaero.hud.path.XaeroPath;
import xaero.lib.client.config.ClientConfigManager;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.single.SingleConfigManager;
import xaero.lib.common.util.IOUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/state/MinimapWorldStateUpdater.class */
public class MinimapWorldStateUpdater {
    public static final int ROOT_CONTAINER_FORMAT = 4;
    private final HudMod modMain;
    private final MinimapSession session;
    private final ClientPacketListener connection;
    private BlockPos currentWorldSpawn;

    public MinimapWorldStateUpdater(HudMod modMain, MinimapSession session, ClientPacketListener connection) {
        this.modMain = modMain;
        this.session = session;
        this.connection = connection;
    }

    public void init() {
        this.session.getWorldState().setAutoRootContainerPath(getAutoRootContainerPath(4));
        for (int i = 0; i < 4; i++) {
            this.session.getWorldState().setOutdatedAutoRootContainerPath(i, getAutoRootContainerPath(i));
        }
    }

    @Deprecated
    public void update(MinimapSession session) {
        update();
    }

    public void update() {
        MinimapWorldState state = this.session.getWorldState();
        XaeroPath oldAutoWorldPath = state.getAutoWorldPath();
        XaeroPath potentialAutoContainerPath = getPotentialContainerPath();
        state.setAutoContainerPathIgnoreCaseCache(potentialAutoContainerPath);
        boolean worldmap = this.modMain.getSupportMods().worldmap();
        String potentialAutoWorldNode = getPotentialWorldNode(this.session.getMc().level.dimension(), worldmap);
        if (potentialAutoWorldNode == null) {
            return;
        }
        XaeroPath autoWorldPath = potentialAutoContainerPath.resolve(potentialAutoWorldNode);
        state.setAutoWorldPath(autoWorldPath);
        if (oldAutoWorldPath != null && potentialAutoContainerPath.equals(oldAutoWorldPath.getParent())) {
            return;
        }
        MinimapWorldRootContainer autoRootContainer = this.session.getWorldManager().getAutoRootContainer();
        autoRootContainer.renameOldContainer(potentialAutoContainerPath);
        autoRootContainer.updateDimensionType(this.session.getMc().level);
        if (oldAutoWorldPath == null) {
            return;
        }
        MinimapWorldContainer oldContainer = this.session.getWorldManager().getWorldContainer(oldAutoWorldPath.getParent());
        oldContainer.getServerWaypointManager().clear();
    }

    @Deprecated
    public XaeroPath getPotentialContainerPath(MinimapSession session) {
        return getPotentialContainerPath();
    }

    public XaeroPath getPotentialContainerPath() {
        String dimensionNode = this.session.getDimensionHelper().getDimensionDirectoryName(this.session.getMc().level.dimension());
        XaeroPath potentialContainerPath = this.session.getWorldState().getAutoRootContainerPath().resolve(dimensionNode);
        return ignoreContainerCase(potentialContainerPath, this.session.getWorldState().getAutoContainerPathIgnoreCaseCache());
    }

    @Deprecated
    public XaeroPath ignoreContainerCase(XaeroPath potentialContainerPath, XaeroPath currentPath, MinimapSession session) {
        return ignoreContainerCase(potentialContainerPath, currentPath);
    }

    public XaeroPath ignoreContainerCase(XaeroPath potentialContainerPath, XaeroPath currentPath) {
        if (potentialContainerPath.equals(currentPath)) {
            return currentPath;
        }
        for (MinimapWorldRootContainer rootContainer : this.session.getWorldManager().getRootContainers()) {
            XaeroPath containerSearch = rootContainer.fixPathCharacterCases(potentialContainerPath);
            if (containerSearch != null) {
                return containerSearch;
            }
        }
        return potentialContainerPath;
    }

    @Deprecated
    public XaeroPath getAutoRootContainerPath(int version, ClientPacketListener connection, MinimapSession session) {
        return getAutoRootContainerPath(version);
    }

    public XaeroPath getAutoRootContainerPath(int version) {
        String potentialContainerID;
        int portDivider;
        ServerData serverData = this.connection.getServerData();
        Minecraft mc = Minecraft.getInstance();
        if (mc.getSingleplayerServer() != null) {
            potentialContainerID = MinimapWorldContainerUtil.convertWorldFolderToContainerNode(mc.getSingleplayerServer().getWorldPath(LevelResource.ROOT).getParent().getFileName().toString(), version);
        } else if (serverData != null && serverData.isRealm() && this.modMain.getEvents().latestRealm != null) {
            potentialContainerID = "Realms_" + String.valueOf(this.modMain.getEvents().latestRealm.ownerUUID) + "." + this.modMain.getEvents().latestRealm.id;
        } else if (serverData != null) {
            ClientConfigManager configManager = this.modMain.getHudConfigs().getClientConfigManager();
            SingleConfigManager<Config> primaryConfigManager = configManager.getPrimaryConfigManager();
            boolean differentiateByServerAddress = ((Boolean) primaryConfigManager.getEffective(MinimapPrimaryClientConfigOptions.DIFFERENTIATE_BY_SERVER_ADDRESS)).booleanValue();
            String serverIP = differentiateByServerAddress ? serverData.ip : "Any Address";
            if (version >= 1 && serverIP.indexOf(":") != serverIP.lastIndexOf(":")) {
                portDivider = serverIP.lastIndexOf("]:") + 1;
            } else {
                portDivider = serverIP.indexOf(":");
            }
            if (portDivider > 0) {
                serverIP = serverIP.substring(0, portDivider);
            }
            while (serverIP.endsWith(".")) {
                serverIP = serverIP.substring(0, serverIP.length() - 1);
            }
            if (version >= 2) {
                serverIP = serverIP.replace("[", "").replace("]", "");
            }
            String serverIP2 = serverIP.replace(":", version < 3 ? "§" : ".").replace("_", "%us%").replace("/", "%fs%").replace("\\", "%bs%");
            if (version >= 4) {
                serverIP2 = IOUtils.replaceTrailingDots(serverIP2.trim(), ',');
            }
            if (serverIP2.isEmpty()) {
                serverIP2 = "Empty Address";
            }
            potentialContainerID = "Multiplayer_" + serverIP2;
        } else {
            potentialContainerID = "Unknown";
        }
        XaeroPath potentialContainerPath = XaeroPath.root(potentialContainerID);
        return ignoreContainerCase(potentialContainerPath, null);
    }

    @Deprecated
    public String getPotentialWorldNode(ResourceKey<Level> dimId, boolean useWorldmap, MinimapSession session) {
        return getPotentialWorldNode(dimId, useWorldmap);
    }

    public String getPotentialWorldNode(ResourceKey<Level> dimId, boolean useWorldmap) {
        String actualWorldNode;
        if (this.session.getMc().getSingleplayerServer() != null) {
            return "waypoints";
        }
        MinimapWorldState state = this.session.getWorldState();
        MinimapWorldRootContainer rootContainer = this.session.getWorldManager().getRootWorldContainer(state.getAutoRootContainerPath());
        Object autoNodeBase = getAutoWorldNodeBase(rootContainer);
        if (autoNodeBase == null) {
            return null;
        }
        String worldmapWorldNode = useWorldmap ? this.modMain.getSupportMods().worldmapSupport.tryToGetMultiworldId(dimId) : null;
        if (useWorldmap && worldmapWorldNode == null) {
            return null;
        }
        if (autoNodeBase instanceof BlockPos) {
            BlockPos pos = (BlockPos) autoNodeBase;
            actualWorldNode = "mw" + (pos.getX() >> 6) + "," + (pos.getY() >> 6) + "," + (pos.getZ() >> 6);
            RootConfig config = rootContainer.getConfig();
            if (!config.isUsingMultiworldDetection()) {
                String defaultMultiworldId = config.getDefaultMultiworldId();
                if (defaultMultiworldId == null) {
                    config.setDefaultMultiworldId(actualWorldNode);
                    this.session.getWorldManagerIO().getRootConfigIO().save(rootContainer);
                } else {
                    actualWorldNode = defaultMultiworldId;
                }
            }
        } else {
            actualWorldNode = "mw$" + String.valueOf(autoNodeBase);
        }
        if (useWorldmap && worldmapWorldNode != SupportXaeroWorldmap.MINIMAP_MW) {
            actualWorldNode = worldmapWorldNode;
        }
        return actualWorldNode;
    }

    public boolean hasServerLevelId(MinimapWorldRootContainer rootContainer) {
        MinimapClientWorldData worldData = MinimapClientWorldDataHelper.getCurrentWorldData();
        return (worldData.serverLevelId == null || rootContainer.getConfig().isIgnoreServerLevelId()) ? false : true;
    }

    public Object getAutoWorldNodeBase(MinimapWorldRootContainer rootContainer) {
        MinimapClientWorldData worldData = MinimapClientWorldDataHelper.getCurrentWorldData();
        return hasServerLevelId(rootContainer) ? worldData.serverLevelId : this.currentWorldSpawn;
    }

    public void onServerLevelId(int id) {
        MinimapClientWorldData worldData = MinimapClientWorldDataHelper.getCurrentWorldData();
        worldData.serverLevelId = Integer.valueOf(id);
        MinimapLogs.LOGGER.info("Minimap updated server level id: " + id + " for world " + String.valueOf(Minecraft.getInstance().level.dimension()));
    }

    public void setCurrentWorldSpawn(BlockPos currentWorldSpawn) {
        this.currentWorldSpawn = currentWorldSpawn;
    }
}
