package xaero.hud.minimap.waypoint;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.entity.player.Player;
import xaero.common.HudMod;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.misc.OptimizedMath;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.path.XaeroPath;
import xaero.lib.client.config.ClientConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/DeathpointHandler.class */
public class DeathpointHandler {
    private final HudMod modMain;
    private final MinimapSession session;

    public DeathpointHandler(HudMod modMain, MinimapSession session) {
        this.modMain = modMain;
        this.session = session;
    }

    public void createDeathpoint(Player player) {
        List<String> allPotentialMWIds;
        String potentialAutoWorldNode;
        this.session.getWorldStateUpdater().update();
        ClientConfigManager configManager = this.modMain.getHudConfigs().getClientConfigManager();
        if (((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.AUTO_WAYPOINTS_ON_DEATH)).booleanValue()) {
            this.session.getWorldState().setCustomWorldPath(null);
        }
        boolean worldmap = this.modMain.getSupportMods().worldmap();
        MinimapWorld potentialAutoWorld = null;
        XaeroPath usedAutoWorldPath = this.session.getWorldState().getAutoWorldPath();
        XaeroPath usedAutoContainerPath = usedAutoWorldPath == null ? null : usedAutoWorldPath.getParent();
        XaeroPath potentialAutoContainerPath = this.session.getWorldStateUpdater().getPotentialContainerPath();
        if (!potentialAutoContainerPath.equals(usedAutoContainerPath) && (potentialAutoWorldNode = this.session.getWorldStateUpdater().getPotentialWorldNode(this.session.getMc().level.dimension(), worldmap)) != null) {
            XaeroPath potentialAutoWorldPath = potentialAutoContainerPath.resolve(potentialAutoWorldNode);
            potentialAutoWorld = this.session.getWorldManager().getWorld(potentialAutoWorldPath);
            createDeathpoint(player, potentialAutoWorld, false);
        }
        MinimapWorld autoWorld = this.session.getWorldManager().getAutoWorld();
        if (potentialAutoWorld == null && autoWorld != null) {
            createDeathpoint(player, autoWorld, false);
        }
        if (!worldmap || (allPotentialMWIds = this.modMain.getSupportMods().worldmapSupport.getPotentialMultiworldIds(player.level().dimension())) == null) {
            return;
        }
        for (String mwId : allPotentialMWIds) {
            MinimapWorld potentialWorld = this.session.getWorldManager().getWorld(potentialAutoContainerPath.resolve(mwId));
            if (potentialWorld != autoWorld && potentialWorld != potentialAutoWorld) {
                createDeathpoint(player, potentialWorld, false);
            }
        }
    }

    public void createDeathpoint(Player player, MinimapWorld world, boolean temporary) {
        WaypointSet currentSet = world.getCurrentWaypointSet();
        if (currentSet == null) {
            return;
        }
        ClientConfigManager configManager = this.modMain.getHudConfigs().getClientConfigManager();
        boolean disabled = false;
        boolean oldDeathpoints = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.OLD_DEATHPOINTS)).booleanValue();
        for (WaypointSet set : world.getIterableWaypointSets()) {
            Iterator<Waypoint> waypoints = set.getWaypoints().iterator();
            while (true) {
                if (waypoints.hasNext()) {
                    Waypoint w = waypoints.next();
                    if (w.getPurpose() == WaypointPurpose.DEATH) {
                        if (set == currentSet) {
                            disabled = w.isDisabled();
                        }
                        if (!oldDeathpoints) {
                            waypoints.remove();
                        } else {
                            w.setPurpose(WaypointPurpose.OLD_DEATH);
                            w.setName("gui.xaero_deathpoint_old");
                        }
                    }
                }
            }
        }
        double dimDiv = this.session.getDimensionHelper().getDimensionDivision(world);
        if (((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.DEATHPOINTS)).booleanValue()) {
            Waypoint deathpoint = new Waypoint(OptimizedMath.myFloor(OptimizedMath.myFloor(player.getX()) * dimDiv), OptimizedMath.myFloor(player.getY()), OptimizedMath.myFloor(OptimizedMath.myFloor(player.getZ()) * dimDiv), "gui.xaero_deathpoint", "D", WaypointColor.BLACK, WaypointPurpose.DEATH);
            deathpoint.setTemporary(temporary);
            deathpoint.setDisabled(disabled);
            currentSet.add(deathpoint, true);
        }
        try {
            this.session.getWorldManagerIO().saveWorld(world);
        } catch (IOException e) {
            MinimapLogs.LOGGER.error("suppressed exception", e);
        }
    }
}
