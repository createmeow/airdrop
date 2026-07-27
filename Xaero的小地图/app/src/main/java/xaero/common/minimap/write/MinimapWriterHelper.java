package xaero.common.minimap.write;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/write/MinimapWriterHelper.class */
public class MinimapWriterHelper {
    void getGlowingColour(int r, int g, int b, int[] result) {
        int total = r + g + b;
        float brightener = Math.max(1.0f, 407.0f / total);
        result[0] = (int) (r * brightener);
        result[1] = (int) (g * brightener);
        result[2] = (int) (b * brightener);
    }
}
