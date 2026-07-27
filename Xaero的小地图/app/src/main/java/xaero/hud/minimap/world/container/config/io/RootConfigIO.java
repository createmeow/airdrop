package xaero.hud.minimap.world.container.config.io;

import it.unimi.dsi.fastutil.ints.IntIterator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import xaero.common.HudMod;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointsSort;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.world.container.MinimapWorldContainer;
import xaero.hud.minimap.world.container.MinimapWorldRootContainer;
import xaero.hud.minimap.world.container.config.RootConfig;
import xaero.hud.path.XaeroPath;
import xaero.hud.path.XaeroPathReader;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/world/container/config/io/RootConfigIO.class */
public class RootConfigIO {
    private final HudMod modMain;

    public RootConfigIO(HudMod modMain) {
        this.modMain = modMain;
    }

    private File getFile(MinimapWorldRootContainer rootContainer) throws IOException {
        Path directoryPath = rootContainer.getDirectoryPath();
        try {
            if (!Files.exists(directoryPath, new LinkOption[0])) {
                Files.createDirectories(directoryPath, new FileAttribute[0]);
            }
        } catch (IOException e) {
            MinimapLogs.LOGGER.error("suppressed exception", e);
        }
        return directoryPath.resolve("config.txt").toFile();
    }

    public void save(MinimapWorldRootContainer rootContainer) {
        File configFile = getFile(rootContainer);
        PrintWriter writer = null;
        RootConfig config = rootContainer.getConfig();
        try {
            writer = new PrintWriter(new FileWriter(configFile));
            writer.println("//waypoints config options");
            writer.println("usingMultiworldDetection:" + config.isUsingMultiworldDetection());
            writer.println("ignoreServerLevelId:" + config.isIgnoreServerLevelId());
            if (config.getDefaultMultiworldId() != null) {
                writer.println("defaultMultiworldId:" + config.getDefaultMultiworldId());
            }
            writer.println("teleportationEnabled:" + config.isTeleportationEnabled());
            writer.println("usingDefaultTeleportCommand:" + config.isUsingDefaultTeleportCommand());
            if (config.getServerTeleportCommandFormat() != null) {
                writer.println("serverTeleportCommandFormat:" + config.getServerTeleportCommandFormat().replace(":", "^col^"));
            }
            if (config.getServerTeleportCommandRotationFormat() != null) {
                writer.println("serverTeleportCommandRotationFormat:" + config.getServerTeleportCommandRotationFormat().replace(":", "^col^"));
            }
            writer.println("sortType:" + config.getSortType().name());
            writer.println("sortReversed:" + config.isSortReversed());
            writer.println("");
            writer.println("//other config options");
            writer.println("ignoreHeightmaps:" + config.isIgnoreHeightmaps());
            rootContainer.getSubWorldConnections().save(writer);
            writer.println("");
            writer.println("//dimension types (DO NOT EDIT)");
            for (Map.Entry<ResourceKey<Level>, ResourceLocation> entry : rootContainer.getDimensionTypeIds()) {
                writer.println("dimensionType:" + entry.getKey().location().toString().replace(':', '$') + ":" + entry.getValue().toString().replace(':', '$'));
            }
            writer.println("//server waypoints");
            for (MinimapWorldContainer dimContainer : rootContainer.getSubContainers()) {
                IntIterator it = dimContainer.getServerWaypointManager().getIds().iterator();
                while (it.hasNext()) {
                    int serverWaypointId = ((Integer) it.next()).intValue();
                    Waypoint serverWaypoint = dimContainer.getServerWaypointManager().getById(serverWaypointId);
                    writer.print("server-waypoint");
                    writer.print(":");
                    writer.print(dimContainer.getLastNode());
                    writer.print(":");
                    writer.println(serverWaypointId);
                    writer.print(":");
                    writer.println(serverWaypoint.isDisabled());
                }
            }
        } catch (IOException e) {
            MinimapLogs.LOGGER.error("suppressed exception", e);
        }
        if (writer != null) {
            writer.close();
        }
    }

    public void load(MinimapWorldRootContainer rootContainer) {
        RootConfig config = rootContainer.getConfig();
        config.setLoaded(true);
        File configFile = getFile(rootContainer);
        if (!configFile.exists()) {
            save(rootContainer);
            return;
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(configFile));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String[] args = line.split(":");
                String valueString = args.length < 2 ? "" : args[1];
                if (args[0].equals("usingMultiworldDetection")) {
                    config.setUsingMultiworldDetection(valueString.equals("true"));
                } else if (args[0].equals("ignoreServerLevelId")) {
                    config.setIgnoreServerLevelId(valueString.equals("true"));
                } else if (args[0].equals("defaultMultiworldId")) {
                    if (valueString.matches("[a-zA-Z,$0-9-]+")) {
                        config.setDefaultMultiworldId(valueString);
                    } else {
                        MinimapLogs.LOGGER.warn("Ignoring invalid defaultMultiworldId in {}", configFile);
                    }
                } else if (args[0].equals("teleportationEnabled")) {
                    config.setTeleportationEnabled(valueString.equals("true"));
                } else if (args[0].equals("usingDefaultTeleportCommand")) {
                    config.setUsingDefaultTeleportCommand(valueString.equals("true"));
                } else if (args[0].equals("teleportCommand")) {
                    config.setServerTeleportCommandFormat("/" + valueString.replace("^col^", ":") + " {x} {y} {z}");
                    config.setServerTeleportCommandRotationFormat("/" + valueString.replace("^col^", ":") + " {x} {y} {z} {yaw} ~");
                } else if (args[0].equals("serverTeleportCommand")) {
                    config.setServerTeleportCommandFormat(valueString.replace("^col^", ":") + " {x} {y} {z}");
                    config.setServerTeleportCommandRotationFormat(valueString.replace("^col^", ":") + " {x} {y} {z} {yaw} ~");
                } else if (args[0].equals("serverTeleportCommandFormat")) {
                    config.setServerTeleportCommandFormat(valueString.replace("^col^", ":"));
                } else if (args[0].equals("serverTeleportCommandRotationFormat")) {
                    config.setServerTeleportCommandRotationFormat(valueString.replace("^col^", ":"));
                } else if (args[0].equals("sortType")) {
                    config.setSortType(WaypointsSort.valueOf(valueString));
                } else if (args[0].equals("sortReversed")) {
                    config.setSortReversed(valueString.equals("true"));
                } else if (args[0].equals("ignoreHeightmaps")) {
                    config.setIgnoreHeightmaps(valueString.equals("true"));
                } else if (args[0].equals("connection")) {
                    XaeroPath worldKey1 = new XaeroPathReader().read(valueString);
                    if (args.length > 2) {
                        XaeroPath worldKey2 = new XaeroPathReader().read(args[2]);
                        config.getSubWorldConnections().addConnection(worldKey1, worldKey2);
                    }
                } else if (args[0].equals("dimensionType")) {
                    try {
                        rootContainer.setDimensionTypeId(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(args[1].replace('$', ':'))), ResourceLocation.parse(args[2].replace('$', ':')));
                    } catch (Throwable th) {
                    }
                } else if (args[0].equals("server-waypoint")) {
                    String dimensionNode = args[1];
                    MinimapWorldContainer dimContainer = rootContainer.addSubContainer(rootContainer.getPath().resolve(dimensionNode));
                    boolean disabled = args[3].equals("true");
                    if (disabled) {
                        dimContainer.getServerWaypointManager().addDisabled(Integer.parseInt(args[2]));
                    }
                }
            }
        } catch (IOException e) {
            MinimapLogs.LOGGER.error("suppressed exception", e);
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e2) {
                MinimapLogs.LOGGER.error("suppressed exception", e2);
            }
        }
    }
}
