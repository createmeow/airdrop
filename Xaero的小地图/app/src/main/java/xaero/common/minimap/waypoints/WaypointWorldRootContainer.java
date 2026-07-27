package xaero.common.minimap.waypoints;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.XaeroMinimapSession;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.path.XaeroPath;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/waypoints/WaypointWorldRootContainer.class */
public class WaypointWorldRootContainer extends MinimapWorldRootContainer {
    @Deprecated
    public WaypointWorldRootContainer(IXaeroMinimap modMain, XaeroMinimapSession minimapSession, String key) {
        super((HudMod) modMain, minimapSession.getWaypointsManager(), XaeroPath.root(key));
    }

    @Deprecated
    public WaypointWorldRootContainer(HudMod modMain, MinimapSession session, XaeroPath path) {
        super(modMain, session, path);
    }

    @Deprecated
    public void updateConnectionsField(XaeroMinimapSession minimapSession) {
        super.updateConnectionsField(minimapSession.getWaypointsManager().getWaypointSession());
    }

    @Deprecated
    public void saveConfig() {
        super.getSession().getWorldManagerIO().getRootConfigIO().save(this);
    }

    @Deprecated
    public void loadConfig() {
        super.getSession().getWorldManagerIO().getRootConfigIO().load(this);
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public boolean isUsingMultiworldDetection() {
        return super.getConfig().isUsingMultiworldDetection();
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public void setUsingMultiworldDetection(boolean usingMultiworldDetection) {
        super.getConfig().setUsingMultiworldDetection(usingMultiworldDetection);
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public String getDefaultMultiworldId() {
        return super.getConfig().getDefaultMultiworldId();
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public void setDefaultMultiworldId(String defaultMultiworldId) {
        super.getConfig().setDefaultMultiworldId(defaultMultiworldId);
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public boolean isTeleportationEnabled() {
        return super.getConfig().isTeleportationEnabled();
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public void setTeleportationEnabled(boolean teleportation) {
        super.getConfig().setTeleportationEnabled(teleportation);
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public boolean isUsingDefaultTeleportCommand() {
        return super.getConfig().isUsingDefaultTeleportCommand();
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public void setUsingDefaultTeleportCommand(boolean usingDefaultTeleportCommand) {
        super.getConfig().setUsingDefaultTeleportCommand(usingDefaultTeleportCommand);
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public String getServerTeleportCommandFormat() {
        return super.getConfig().getServerTeleportCommandFormat();
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public String getServerTeleportCommandRotationFormat() {
        return super.getConfig().getServerTeleportCommandRotationFormat();
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public void setServerTeleportCommandFormat(String serverTeleportCommandFormat) {
        super.getConfig().setServerTeleportCommandFormat(serverTeleportCommandFormat);
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public void setServerTeleportCommandRotationFormat(String serverTeleportCommandRotationFormat) {
        super.getConfig().setServerTeleportCommandRotationFormat(serverTeleportCommandRotationFormat);
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public WaypointsSort getSortType() {
        return super.getConfig().getSortType();
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public void toggleSortType() {
        super.getConfig().toggleSortType();
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public boolean isSortReversed() {
        return super.getConfig().isSortReversed();
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public void toggleSortReversed() {
        super.getConfig().toggleSortReversed();
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public boolean isIgnoreServerLevelId() {
        return super.getConfig().isIgnoreServerLevelId();
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public WaypointWorldConnectionManager getSubWorldConnections() {
        return (WaypointWorldConnectionManager) super.getSubWorldConnections();
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public boolean isIgnoreHeightmaps() {
        return super.getConfig().isIgnoreHeightmaps();
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public void setIgnoreHeightmaps(boolean ignoreHeightmaps) {
        super.getConfig().setIgnoreHeightmaps(ignoreHeightmaps);
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public DimensionType getDimensionType(ResourceKey<Level> dimId) {
        return super.getDimensionType(dimId);
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public double getDimensionScale(ResourceKey<Level> dimId) {
        return super.getDimensionScale(dimId);
    }

    @Override // xaero.hud.minimap.world.container.MinimapWorldRootContainer
    @Deprecated
    public void updateDimensionType(ClientLevel level) {
        super.updateDimensionType(level);
    }

    @Override // xaero.common.minimap.waypoints.WaypointWorldContainer
    @Deprecated
    public WaypointWorldRootContainer getRootContainer() {
        return this;
    }
}
