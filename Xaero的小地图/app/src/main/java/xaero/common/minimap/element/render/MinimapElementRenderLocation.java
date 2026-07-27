package xaero.common.minimap.element.render;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/element/render/MinimapElementRenderLocation.class */
public class MinimapElementRenderLocation {
    public static final int UNKNOWN = -1;
    public static final int IN_MINIMAP = 0;
    public static final int OVER_MINIMAP = 1;
    public static final int IN_GAME = 2;
    public static final int WORLD_MAP = 3;
    public static final int WORLD_MAP_MENU = 4;

    public static int fromWorldMap(int location) {
        if (location > 4 || location < -1) {
            return -1;
        }
        return location;
    }
}
