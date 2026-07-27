package com.createmeow.airdrop.network;

import com.createmeow.airdrop.airDrop;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class XaeroWaypointHandler {
    // 存储每个玩家的活跃空投路径点：UUID -> (路径点key -> 空投信息)
    private static final Map<UUID, Map<String, AirdropWaypointInfo>> activeAirdropWaypoints = new ConcurrentHashMap<>();

    // 延迟初始化状态：null=未初始化, true=可用, false=不可用
    private static volatile Boolean xaeroAvailable = null;

    // 缓存的反射对象
    private static Field customWaypointsField = null;
    private static Method refreshMethod = null;
    private static Object waypointColorRed = null;
    private static Object waypointColorYellow = null;
    private static Object waypointPurposeNormal = null;
    private static Constructor<?> waypointConstructor = null;

    /**
     * 延迟初始化Xaero集成。只在客户端第一次调用时执行。
     * 这是因为Xaero的类只在客户端存在，在服务端加载会导致ClassNotFoundException。
     */
    private static boolean ensureInitialized() {
        if (xaeroAvailable != null) {
            return xaeroAvailable;
        }

        synchronized (XaeroWaypointHandler.class) {
            if (xaeroAvailable != null) {
                return xaeroAvailable;
            }

            try {
                // 尝试加载Xaero的路径点类
                Class<?> waypointClass = Class.forName("xaero.common.minimap.waypoints.Waypoint");
                airDrop.LOGGER.info("[Airdrop] Found Xaero waypoint class");

                // 获取WaypointsManager的customWaypoints字段
                Class<?> waypointsManagerClass = Class.forName("xaero.common.minimap.waypoints.WaypointsManager");
                customWaypointsField = waypointsManagerClass.getDeclaredField("customWaypoints");
                customWaypointsField.setAccessible(true);

                // 查找刷新方法
                try {
                    refreshMethod = waypointsManagerClass.getMethod("onCustomWaypointsUpdate");
                } catch (NoSuchMethodException e1) {
                    try {
                        refreshMethod = waypointsManagerClass.getMethod("refreshWaypoints");
                    } catch (NoSuchMethodException e2) {
                        try {
                            refreshMethod = waypointsManagerClass.getMethod("syncCustomWaypoints");
                        } catch (NoSuchMethodException e3) {
                            refreshMethod = null;
                        }
                    }
                }

                // 获取路径点颜色
                Class<?> waypointColorClass = Class.forName("xaero.hud.minimap.waypoint.WaypointColor");
                waypointColorRed = waypointColorClass.getField("RED").get(null);
                waypointColorYellow = waypointColorClass.getField("YELLOW").get(null);

                // 获取路径点用途
                Class<?> waypointPurposeClass = Class.forName("xaero.hud.minimap.waypoint.WaypointPurpose");
                waypointPurposeNormal = waypointPurposeClass.getField("NORMAL").get(null);

                // 缓存构造函数
                waypointConstructor = waypointClass.getConstructor(
                        int.class, int.class, int.class,
                        String.class, String.class,
                        waypointColorClass,
                        waypointPurposeClass,
                        boolean.class
                );

                xaeroAvailable = true;
                airDrop.LOGGER.info("[Airdrop] Xaero's Minimap integration enabled (lazy init)");
            } catch (Exception e) {
                xaeroAvailable = false;
                airDrop.LOGGER.info("[Airdrop] Xaero's Minimap not available on this side: {}", e.getMessage());
            }
        }

        return xaeroAvailable;
    }

    public static boolean isXaeroAvailable() {
        return ensureInitialized();
    }

    /**
     * 生成唯一的路径点 key，包含维度信息。
     */
    private static String generateWaypointKey(BlockPos pos, String dimension) {
        return dimension + ":" + pos.getX() + "," + pos.getY() + "," + pos.getZ();
    }

    /**
     * 获取维度key字符串。
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
            airDrop.LOGGER.warn("[Airdrop] Failed to access Xaero custom waypoints: {}", e.getMessage());
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

    /**
     * 创建空投路径点。只在客户端调用。
     */
    public static void createWaypoint(Player player, BlockPos pos, String name, boolean isTimed) {
        if (!ensureInitialized()) {
            airDrop.LOGGER.debug("[Airdrop] Xaero not available, skipping waypoint creation");
            return;
        }

        try {
            Object color = isTimed ? waypointColorRed : waypointColorYellow;
            String initials = isTimed ? "A" : "V";

            Object waypoint = waypointConstructor.newInstance(
                    pos.getX(), pos.getY(), pos.getZ(), name, initials, color, waypointPurposeNormal, true
            );

            Hashtable<Integer, Object> customWaypoints = getCustomWaypoints(player);
            if (customWaypoints != null) {
                String dimKey = getDimensionKey(player);
                String waypointKey = generateWaypointKey(pos, dimKey);
                int hashKey = waypointKey.hashCode();

                customWaypoints.put(hashKey, waypoint);

                // 记录活跃的空投路径点
                activeAirdropWaypoints.computeIfAbsent(player.getUUID(), k -> new ConcurrentHashMap<>())
                        .put(waypointKey, new AirdropWaypointInfo(pos, dimKey, hashKey));

                refreshWaypoints();
                airDrop.LOGGER.info("[Airdrop] Xaero waypoint created at ({}, {}, {}) for dim '{}'",
                        pos.getX(), pos.getY(), pos.getZ(), dimKey);
            }
        } catch (Exception e) {
            airDrop.LOGGER.error("[Airdrop] Failed to create Xaero waypoint", e);
        }
    }

    /**
     * 移除空投路径点。只在客户端调用。
     */
    public static void removeWaypoint(Player player, BlockPos pos) {
        if (!ensureInitialized()) return;

        try {
            String dimKey = getDimensionKey(player);
            String waypointKey = generateWaypointKey(pos, dimKey);

            // 先从活跃路径点映射中获取hashKey
            Map<String, AirdropWaypointInfo> playerWaypoints = activeAirdropWaypoints.get(player.getUUID());
            int hashKey;
            if (playerWaypoints != null && playerWaypoints.containsKey(waypointKey)) {
                AirdropWaypointInfo info = playerWaypoints.remove(waypointKey);
                hashKey = info.hashKey;
            } else {
                // 如果缓存中没有，重新计算hashKey
                hashKey = waypointKey.hashCode();
            }

            Hashtable<Integer, Object> customWaypoints = getCustomWaypoints(player);
            if (customWaypoints != null) {
                customWaypoints.remove(hashKey);
            }

            refreshWaypoints();
            airDrop.LOGGER.debug("[Airdrop] Removed Xaero waypoint at ({}, {}, {})", pos.getX(), pos.getY(), pos.getZ());
        } catch (Exception e) {
            airDrop.LOGGER.error("[Airdrop] Failed to remove Xaero waypoint", e);
        }
    }

    /**
     * 当玩家加入时调用。
     */
    public static void onPlayerJoin(Player player) {
        // 路径点同步由服务端通过网络包处理，客户端不需要额外操作
    }

    /**
     * 当玩家退出时，清理该玩家的路径点缓存。
     */
    public static void onPlayerLeave(UUID playerId) {
        activeAirdropWaypoints.remove(playerId);
        airDrop.LOGGER.debug("[Airdrop] Cleaned up waypoint cache for player {}", playerId);
    }

    /**
     * 存储空投路径点信息。
     */
    private static class AirdropWaypointInfo {
        final BlockPos pos;
        final String dimension;
        final int hashKey;

        AirdropWaypointInfo(BlockPos pos, String dimension, int hashKey) {
            this.pos = pos;
            this.dimension = dimension;
            this.hashKey = hashKey;
        }
    }
}