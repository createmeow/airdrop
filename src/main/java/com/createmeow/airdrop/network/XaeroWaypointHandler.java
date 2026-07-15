package com.createmeow.airdrop.network;

import com.createmeow.airdrop.airDrop;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class XaeroWaypointHandler {
    private static final Map<UUID, Map<Integer, Object>> playerWaypoints = new HashMap<>();
    private static boolean xaeroAvailable = false;
    private static Field customWaypointsField = null;
    private static Method refreshMethod = null;
    private static Object waypointColorRed = null;
    private static Object waypointColorYellow = null;
    private static Object waypointPurposeNormal = null;

    static {
        try {
            Class.forName("xaero.common.minimap.waypoints.Waypoint");

            Class<?> waypointsManagerClass = Class.forName("xaero.common.minimap.waypoints.WaypointsManager");
            customWaypointsField = waypointsManagerClass.getField("customWaypoints");
            customWaypointsField.setAccessible(true);

            // Try to find the refresh method (different Xaero versions use different names)
            try {
                refreshMethod = waypointsManagerClass.getMethod("onCustomWaypointsUpdate");
            } catch (NoSuchMethodException e1) {
                try {
                    refreshMethod = waypointsManagerClass.getMethod("refreshWaypoints");
                } catch (NoSuchMethodException e2) {
                    try {
                        refreshMethod = waypointsManagerClass.getMethod("syncCustomWaypoints");
                    } catch (NoSuchMethodException e3) {
                        // Some versions auto-refresh, no refresh method needed
                        refreshMethod = null;
                    }
                }
            }

            Class<?> waypointColorClass = Class.forName("xaero.hud.minimap.waypoint.WaypointColor");
            waypointColorRed = waypointColorClass.getField("RED").get(null);
            waypointColorYellow = waypointColorClass.getField("YELLOW").get(null);

            Class<?> waypointPurposeClass = Class.forName("xaero.hud.minimap.waypoint.WaypointPurpose");
            waypointPurposeNormal = waypointPurposeClass.getField("NORMAL").get(null);

            xaeroAvailable = true;
            airDrop.LOGGER.info("Xaero's Minimap detected, waypoint integration enabled");
        } catch (Exception e) {
            xaeroAvailable = false;
            airDrop.LOGGER.info("Xaero's Minimap not detected, waypoint integration disabled");
        }
    }

    public static boolean isXaeroAvailable() {
        return xaeroAvailable;
    }

    /**
     * Get the dimension key string used by Xaero for the given player's level.
     * Xaero stores waypoints keyed by dimension ID (e.g. "minecraft:overworld").
     */
    private static String getDimensionKey(Player player) {
        if (player == null || player.level() == null) return "minecraft:overworld";
        return player.level().dimension().location().toString();
    }

    @SuppressWarnings("unchecked")
    private static Hashtable<Integer, Object> getCustomWaypoints(Player player) {
        try {
            Hashtable<String, Hashtable<Integer, Object>> allCustom =
                    (Hashtable<String, Hashtable<Integer, Object>>) customWaypointsField.get(null);
            String dimKey = getDimensionKey(player);
            Hashtable<Integer, Object> wps = allCustom.get(dimKey);
            if (wps == null) {
                wps = new Hashtable<>();
                allCustom.put(dimKey, wps);
            }
            return wps;
        } catch (Exception e) {
            airDrop.LOGGER.warn("Failed to access Xaero custom waypoints: {}", e.getMessage());
            return null;
        }
    }

    private static void refreshWaypoints() {
        if (refreshMethod != null) {
            try {
                refreshMethod.invoke(null);
            } catch (Exception ignored) {
            }
        }
    }

    public static void createWaypoint(Player player, BlockPos pos, String name, boolean isTimed) {
        if (!xaeroAvailable) return;

        try {
            Class<?> waypointColorClass = Class.forName("xaero.hud.minimap.waypoint.WaypointColor");
            Class<?> waypointPurposeClass = Class.forName("xaero.hud.minimap.waypoint.WaypointPurpose");
            Class<?> waypointClass = Class.forName("xaero.common.minimap.waypoints.Waypoint");

            Object color = isTimed ? waypointColorRed : waypointColorYellow;
            String initials = isTimed ? "A" : "V";

            Object waypoint = waypointClass.getConstructor(
                    int.class, int.class, int.class,
                    String.class, String.class,
                    waypointColorClass,
                    waypointPurposeClass,
                    boolean.class
            ).newInstance(pos.getX(), pos.getY(), pos.getZ(), name, initials, color, waypointPurposeNormal, true);

            Hashtable<Integer, Object> customWaypoints = getCustomWaypoints(player);
            if (customWaypoints != null) {
                int key = pos.hashCode();
                customWaypoints.put(key, waypoint);
                playerWaypoints.computeIfAbsent(player.getUUID(), k -> new HashMap<>()).put(key, waypoint);
                refreshWaypoints();
                airDrop.LOGGER.info("Xaero waypoint created at ({}, {}, {}) for dim '{}'",
                        pos.getX(), pos.getY(), pos.getZ(), getDimensionKey(player));
            }
        } catch (Exception e) {
            airDrop.LOGGER.warn("Failed to create Xaero waypoint: {}", e.getMessage());
        }
    }

    public static void removeWaypoint(Player player, BlockPos pos) {
        if (!xaeroAvailable) return;

        try {
            Hashtable<Integer, Object> customWaypoints = getCustomWaypoints(player);
            if (customWaypoints != null) {
                customWaypoints.remove(pos.hashCode());
            }

            Map<Integer, Object> waypoints = playerWaypoints.get(player.getUUID());
            if (waypoints != null) {
                waypoints.remove(pos.hashCode());
            }

            refreshWaypoints();
            airDrop.LOGGER.debug("Removed Xaero waypoint at ({}, {}, {})", pos.getX(), pos.getY(), pos.getZ());
        } catch (Exception e) {
            airDrop.LOGGER.warn("Failed to remove Xaero waypoint: {}", e.getMessage());
        }
    }
}