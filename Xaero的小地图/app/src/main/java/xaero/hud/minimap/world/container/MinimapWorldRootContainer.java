package xaero.hud.minimap.world.container;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import xaero.common.HudMod;
import xaero.common.file.SimpleBackup;
import xaero.common.minimap.waypoints.WaypointWorldContainer;
import xaero.common.minimap.waypoints.WaypointWorldRootContainer;
import xaero.common.minimap.waypoints.WaypointsSort;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.WaypointSession;
import xaero.hud.minimap.world.connection.MinimapWorldConnectionManager;
import xaero.hud.minimap.world.container.config.RootConfig;
import xaero.hud.path.XaeroPath;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/container/MinimapWorldRootContainer.class */
public class MinimapWorldRootContainer extends WaypointWorldContainer {
    private final RootConfig config;
    private final Map<ResourceKey<Level>, ResourceLocation> dimensionTypeIds;
    private final Map<ResourceKey<Level>, DimensionType> dimensionTypes;

    protected MinimapWorldRootContainer(HudMod modMain, MinimapSession session, XaeroPath path) {
        super(modMain, session, path, (MinimapWorldRootContainer) null);
        this.config = new RootConfig(MinimapWorldContainerUtil.isMultiplayer(path));
        this.dimensionTypeIds = new HashMap();
        this.dimensionTypes = new HashMap();
    }

    public void updateConnectionsField(WaypointSession session) {
        this.config.resetSubWorldConnections(MinimapWorldContainerUtil.isMultiplayer(this.path));
    }

    public MinimapWorldConnectionManager getSubWorldConnections() {
        return this.config.getSubWorldConnections();
    }

    public DimensionType getDimensionType(ResourceKey<Level> dimId) {
        ServerLevel serverLevel;
        DimensionType dimensionType = this.dimensionTypes.get(dimId);
        if (dimensionType != null) {
            return dimensionType;
        }
        ResourceLocation dimensionTypeId = this.dimensionTypeIds.get(dimId);
        if (dimensionTypeId == null) {
            if (dimId == Level.NETHER) {
                dimensionTypeId = BuiltinDimensionTypes.NETHER_EFFECTS;
            } else if (dimId == Level.OVERWORLD) {
                dimensionTypeId = BuiltinDimensionTypes.OVERWORLD_EFFECTS;
            } else if (dimId == Level.END) {
                dimensionTypeId = BuiltinDimensionTypes.END_EFFECTS;
            } else {
                IntegratedServer integratedServer = Minecraft.getInstance().getSingleplayerServer();
                if (integratedServer == null || (serverLevel = integratedServer.getLevel(dimId)) == null) {
                    return null;
                }
                this.dimensionTypes.put(dimId, serverLevel.dimensionType());
                return serverLevel.dimensionType();
            }
        }
        DimensionType dimensionType2 = (DimensionType) Minecraft.getInstance().level.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).get(dimensionTypeId);
        if (dimensionType2 != null) {
            this.dimensionTypes.put(dimId, dimensionType2);
        }
        return dimensionType2;
    }

    public double getDimensionScale(ResourceKey<Level> dimId) {
        DimensionType dimType = getDimensionType(dimId);
        if (dimType == null) {
            return 1.0d;
        }
        return dimType.coordinateScale();
    }

    public void updateDimensionType(ClientLevel level) {
        ResourceKey<Level> dimId = level.dimension();
        ResourceKey<DimensionType> dimTypeId = (ResourceKey) level.dimensionTypeRegistration().unwrapKey().get();
        DimensionType dimType = level.dimensionType();
        if (Objects.equals(this.dimensionTypeIds.get(dimId), dimTypeId.location())) {
            return;
        }
        this.dimensionTypes.put(dimId, dimType);
        this.dimensionTypeIds.put(dimId, dimTypeId.location());
        this.session.getWorldManagerIO().getRootConfigIO().save(this);
    }

    public void renameOldContainer(XaeroPath containerPath) {
        ResourceKey<Level> dimId;
        if (this.subContainers.isEmpty()) {
            return;
        }
        String dimensionPart = containerPath.getAtIndex(1).getLastNode();
        if (this.subContainers.containsKey(dimensionPart) || (dimId = this.session.getDimensionHelper().getDimensionKeyForDirectoryName(dimensionPart)) == null) {
            return;
        }
        ResourceLocation dimKey = dimId.location();
        String dimKeyOldValidation = dimKey.getPath().replaceAll("[^a-zA-Z0-9_]+", "");
        XaeroPath customWorldPath = this.session.getWorldState().getCustomWorldPath();
        MinimapWorldContainer currentCustomContainer = customWorldPath == null ? null : this.session.getWorldManager().getWorld(customWorldPath).getContainer();
        for (Map.Entry<String, MinimapWorldContainer> subContainerEntry : this.subContainers.entrySet()) {
            String subKey = subContainerEntry.getKey();
            if (subKey.equals(dimKeyOldValidation)) {
                MinimapWorldContainer dimContainer = subContainerEntry.getValue();
                boolean currentlySelected = currentCustomContainer != null && currentCustomContainer.getPath().isSubOf(dimContainer.getPath());
                this.subContainers.put(dimensionPart, dimContainer);
                this.subContainers.remove(subKey);
                SimpleBackup.moveToBackup(dimContainer.getDirectoryPath());
                dimContainer.setPath(this.path.resolve(dimensionPart));
                if (currentlySelected) {
                    this.session.getWorldState().setCustomWorldPath(dimContainer.getPath().resolve(customWorldPath.getSubPath(2)));
                }
                try {
                    this.session.getWorldManagerIO().saveWorlds(this);
                    MinimapWorldConnectionManager connections = getSubWorldConnections();
                    connections.renameDimension(subKey, dimensionPart);
                    this.session.getWorldManagerIO().getRootConfigIO().save(this);
                    return;
                } catch (IOException e) {
                    throw new RuntimeException("Failed to rename a dimension! Can't continue.", e);
                }
            }
        }
    }

    public Iterable<Map.Entry<ResourceKey<Level>, ResourceLocation>> getDimensionTypeIds() {
        return this.dimensionTypeIds.entrySet();
    }

    public void setDimensionTypeId(ResourceKey<Level> dim, ResourceLocation dimType) {
        this.dimensionTypes.remove(dim);
        this.dimensionTypeIds.put(dim, dimType);
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldContainer
    public MinimapWorldRootContainer getRoot() {
        return this;
    }

    public boolean isConfigLoaded() {
        return this.config.isLoaded();
    }

    public RootConfig getConfig() {
        return this.config;
    }

    @Deprecated
    public boolean isUsingMultiworldDetection() {
        return this.config.isUsingMultiworldDetection();
    }

    @Deprecated
    public void setUsingMultiworldDetection(boolean usingMultiworldDetection) {
        this.config.setUsingMultiworldDetection(usingMultiworldDetection);
    }

    @Deprecated
    public String getDefaultMultiworldId() {
        return this.config.getDefaultMultiworldId();
    }

    @Deprecated
    public void setDefaultMultiworldId(String defaultMultiworldId) {
        this.config.setDefaultMultiworldId(defaultMultiworldId);
    }

    @Deprecated
    public boolean isTeleportationEnabled() {
        return this.config.isTeleportationEnabled();
    }

    @Deprecated
    public void setTeleportationEnabled(boolean teleportation) {
        this.config.setTeleportationEnabled(teleportation);
    }

    @Deprecated
    public boolean isUsingDefaultTeleportCommand() {
        return this.config.isUsingDefaultTeleportCommand();
    }

    @Deprecated
    public void setUsingDefaultTeleportCommand(boolean usingDefaultTeleportCommand) {
        this.config.setUsingDefaultTeleportCommand(usingDefaultTeleportCommand);
    }

    @Deprecated
    public String getServerTeleportCommandFormat() {
        return this.config.getServerTeleportCommandFormat();
    }

    @Deprecated
    public String getServerTeleportCommandRotationFormat() {
        return this.config.getServerTeleportCommandRotationFormat();
    }

    @Deprecated
    public void setServerTeleportCommandFormat(String serverTeleportCommandFormat) {
        this.config.setServerTeleportCommandFormat(serverTeleportCommandFormat);
    }

    @Deprecated
    public void setServerTeleportCommandRotationFormat(String serverTeleportCommandRotationFormat) {
        this.config.setServerTeleportCommandRotationFormat(serverTeleportCommandRotationFormat);
    }

    @Deprecated
    public WaypointsSort getSortType() {
        return this.config.getSortType();
    }

    @Deprecated
    public void setSortType(WaypointsSort sortType) {
        this.config.setSortType(sortType);
    }

    @Deprecated
    public void toggleSortType() {
        this.config.toggleSortType();
    }

    @Deprecated
    public boolean isSortReversed() {
        return this.config.isSortReversed();
    }

    @Deprecated
    public void setSortReversed(boolean sortReversed) {
        this.config.setSortReversed(sortReversed);
    }

    @Deprecated
    public void toggleSortReversed() {
        this.config.toggleSortReversed();
    }

    @Deprecated
    public boolean isIgnoreServerLevelId() {
        return this.config.isIgnoreServerLevelId();
    }

    @Deprecated
    public void setIgnoreServerLevelId(boolean ignoreServerLevelId) {
        this.config.setIgnoreServerLevelId(ignoreServerLevelId);
    }

    @Deprecated
    public boolean isIgnoreHeightmaps() {
        return this.config.isIgnoreHeightmaps();
    }

    @Deprecated
    public void setIgnoreHeightmaps(boolean ignoreHeightmaps) {
        this.config.setIgnoreHeightmaps(ignoreHeightmaps);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/container/MinimapWorldRootContainer$Builder.class */
    public static final class Builder {
        private HudMod modMain;
        private MinimapSession session;
        private XaeroPath path;

        private Builder() {
        }

        public Builder setDefault() {
            setModMain(null);
            setSession(null);
            setPath(null);
            return this;
        }

        public Builder setModMain(HudMod modMain) {
            this.modMain = modMain;
            return this;
        }

        public Builder setSession(MinimapSession session) {
            this.session = session;
            return this;
        }

        public Builder setPath(XaeroPath path) {
            this.path = path;
            return this;
        }

        public MinimapWorldRootContainer build() {
            if (this.modMain == null || this.session == null || this.path == null) {
                throw new IllegalStateException();
            }
            return new WaypointWorldRootContainer(this.modMain, this.session, this.path);
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }

    @Override // xaero.common.minimap.waypoints.WaypointWorldContainer, xaero.hud.minimap.world.container.MinimapWorldContainer
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // xaero.common.minimap.waypoints.WaypointWorldContainer, xaero.hud.minimap.world.container.MinimapWorldContainer
    public String getName(String worldNode) {
        return super.getName(worldNode);
    }

    @Override // xaero.common.minimap.waypoints.WaypointWorldContainer, xaero.hud.minimap.world.container.MinimapWorldContainer
    public void removeName(String worldNode) {
        super.removeName(worldNode);
    }

    @Override // xaero.common.minimap.waypoints.WaypointWorldContainer, xaero.hud.minimap.world.container.MinimapWorldContainer
    public String getSubName() {
        return super.getSubName();
    }
}
