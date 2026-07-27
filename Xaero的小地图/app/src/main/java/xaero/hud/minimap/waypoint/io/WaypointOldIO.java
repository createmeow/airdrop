package xaero.hud.minimap.waypoint.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.common.HudMod;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.WaypointColor;
import xaero.hud.minimap.waypoint.set.WaypointSet;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.path.XaeroPath;
import xaero.lib.common.util.IOUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/io/WaypointOldIO.class */
public class WaypointOldIO {
    private final Path oldWaypointsFile;

    public WaypointOldIO(Path oldWaypointsFile) {
        this.oldWaypointsFile = oldWaypointsFile;
    }

    public boolean load(MinimapSession session) throws IOException {
        boolean shouldResave = loadFromConfigLines(session);
        return loadOldWaypoints(session) || shouldResave;
    }

    public boolean loadOldWaypoints(MinimapSession session) throws IOException {
        if (!Files.exists(this.oldWaypointsFile, new LinkOption[0])) {
            return false;
        }
        boolean result = loadFromFile(session, this.oldWaypointsFile);
        IOUtils.quickFileBackupMove(this.oldWaypointsFile);
        return result;
    }

    public boolean checkLine(String[] args, MinimapSession session) {
        if (args.length == 0) {
            return false;
        }
        if (!args[0].equalsIgnoreCase("world") && !args[0].equalsIgnoreCase("waypoint")) {
            return false;
        }
        if (!args[1].contains("_")) {
            args[1] = args[1] + "_null";
        }
        MinimapWorldContainer container = session.getWorldManager().addWorldContainer(convertToNewContainerID(args[1], session));
        MinimapWorld world = container.addWorld("waypoints");
        if (args[0].equalsIgnoreCase("world")) {
            world.setCurrentWaypointSetId(args[2]);
            for (int i = 2; i < args.length; i++) {
                if (world.getWaypointSet(args[i]) == null) {
                    world.addWaypointSet(WaypointSet.Builder.begin().setName(args[i]).build());
                }
            }
            return true;
        }
        if (args[0].equalsIgnoreCase("waypoint")) {
            String setName = "gui.xaero_default";
            if (args.length > 10) {
                setName = args[10];
            }
            WaypointSet waypoints = world.getWaypointSet(setName);
            if (waypoints == null) {
                WaypointSet waypointSetBuild = WaypointSet.Builder.begin().setName(setName).build();
                waypoints = waypointSetBuild;
                world.addWaypointSet(waypointSetBuild);
            }
            Waypoint loadedWaypoint = new Waypoint(Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]), args[2].replace("§§", ":"), args[3].replace("§§", ":"), WaypointColor.fromIndex(Integer.parseInt(args[7])));
            if (args.length > 8) {
                loadedWaypoint.setDisabled(args[8].equals("true"));
            }
            if (args.length > 9) {
                loadedWaypoint.setType(Integer.parseInt(args[9]));
            }
            if (args.length > 11) {
                loadedWaypoint.setRotation(args[11].equals("true"));
            }
            if (args.length > 12) {
                loadedWaypoint.setYaw(Integer.parseInt(args[12]));
            }
            waypoints.add(loadedWaypoint);
            return true;
        }
        return false;
    }

    public boolean loadFromFile(MinimapSession session, Path filePath) throws IOException {
        if (!Files.exists(filePath, new LinkOption[0])) {
            return false;
        }
        BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()));
        while (true) {
            try {
                String s = reader.readLine();
                if (s != null) {
                    String[] args = s.split(":");
                    try {
                        checkLine(args, session);
                    } catch (Exception e) {
                        MinimapLogs.LOGGER.info("Skipping old waypoint line:" + args[0]);
                    }
                } else {
                    reader.close();
                    return true;
                }
            } catch (Throwable th) {
                try {
                    reader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
    }

    public XaeroPath convertToNewContainerID(String oldID, MinimapSession session) throws NumberFormatException {
        int separatorIndex = oldID.lastIndexOf("_");
        String parentContainer = oldID.substring(0, separatorIndex);
        String dimension = oldID.substring(separatorIndex + 1);
        if (dimension.equals("null")) {
            dimension = "Overworld";
        } else if (dimension.startsWith("DIM")) {
            int dimensionId = Integer.parseInt(dimension.substring(3));
            dimension = "dim%" + dimensionId;
            ResourceKey<Level> dimRegistryKey = session.getDimensionHelper().getDimensionKeyForDirectoryName(dimension);
            if (dimRegistryKey != null) {
                dimension = session.getDimensionHelper().getDimensionDirectoryName(dimRegistryKey);
            }
        }
        return XaeroPath.root(parentContainer).resolve(fixOldDimensionName(dimension));
    }

    public String fixOldDimensionName(String savedDimName) {
        if (savedDimName.equals("Overworld")) {
            return "dim%0";
        }
        if (savedDimName.equals("Nether")) {
            return "dim%-1";
        }
        if (savedDimName.equals("The End")) {
            return "dim%1";
        }
        return savedDimName;
    }

    private boolean loadFromConfigLines(MinimapSession session) throws IOException {
        String configWaypointLines = HudMod.INSTANCE.getSettings().getLoadedWaypointLines();
        if (configWaypointLines == null || configWaypointLines.isEmpty()) {
            return false;
        }
        configWaypointLines.lines().forEach(line -> {
            String[] args = line.split(":");
            try {
                checkLine(args, session);
            } catch (Exception e) {
                MinimapLogs.LOGGER.info("Skipping waypoint line in old config:" + args[0]);
            }
        });
        HudMod.INSTANCE.getSettings().removeLoadedWaypointLines();
        HudMod.INSTANCE.getSettings().saveSettings();
        return true;
    }
}
