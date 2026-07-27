package xaero.common.minimap.waypoints;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/waypoints/WaypointUtil.class */
public class WaypointUtil {
    public static int getAddedMinimapIconFrame(int initialsWidth) {
        return getAddedMinimapIconFrame(0, initialsWidth);
    }

    public static int getAddedMinimapIconFrame(int addedFrame, int initialsWidth) {
        int totalToAdd;
        int frameToAdd;
        if (initialsWidth > 8 && (frameToAdd = (totalToAdd = initialsWidth - 8) - (totalToAdd / 2)) > addedFrame) {
            addedFrame = frameToAdd;
        }
        return addedFrame;
    }
}
