package xaero.common.minimap.waypoints;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/waypoints/WaypointsSort.class */
public enum WaypointsSort {
    NONE("gui.xaero_sort_unsorted"),
    NAME("gui.xaero_sort_name"),
    SYMBOL("gui.xaero_sort_symbol"),
    COLOR("gui.xaero_sort_color"),
    DISTANCE("gui.xaero_sort_distance"),
    ANGLE("gui.xaero_sort_angle");

    public final String optionName;

    WaypointsSort(String optionName) {
        this.optionName = optionName;
    }
}
