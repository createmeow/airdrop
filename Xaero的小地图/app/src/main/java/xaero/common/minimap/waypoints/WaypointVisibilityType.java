package xaero.common.minimap.waypoints;

import net.minecraft.network.chat.Component;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/waypoints/WaypointVisibilityType.class */
public enum WaypointVisibilityType {
    LOCAL(Component.translatable("gui.xaero_waypoints_local"), false),
    GLOBAL(Component.translatable("gui.xaero_waypoints_global"), true),
    WORLD_MAP_LOCAL(Component.translatable("gui.xaero_waypoint_visibility_type_world_map_local"), false),
    WORLD_MAP_GLOBAL(Component.translatable("gui.xaero_waypoint_visibility_type_world_map_global"), true);

    private final Component translation;
    private final boolean global;

    WaypointVisibilityType(Component translation, boolean global) {
        this.translation = translation;
        this.global = global;
    }

    public Component getTranslation() {
        return this.translation;
    }

    public boolean isGlobal() {
        return this.global;
    }
}
