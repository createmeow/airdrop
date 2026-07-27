package xaero.common.minimap.highlight;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.hud.minimap.info.render.compile.InfoDisplayCompiler;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/highlight/AbstractHighlighter.class */
public abstract class AbstractHighlighter {
    protected final boolean coveringOutsideDiscovered;
    protected final int[] resultStore = new int[256];

    public abstract boolean regionHasHighlights(ResourceKey<Level> resourceKey, int i, int i2);

    public abstract boolean chunkIsHighlit(ResourceKey<Level> resourceKey, int i, int i2);

    public abstract int[] getChunkHighlitColor(ResourceKey<Level> resourceKey, int i, int i2);

    public abstract void addBlockHighlightTooltips(InfoDisplayCompiler infoDisplayCompiler, ResourceKey<Level> resourceKey, int i, int i2, int i3);

    protected AbstractHighlighter(boolean coveringOutsideDiscovered) {
        this.coveringOutsideDiscovered = coveringOutsideDiscovered;
    }

    protected void setResult(int x, int z, int color) {
        this.resultStore[(z << 4) | x] = color;
    }

    protected int getBlend(int color1, int color2) {
        if (color1 == color2) {
            return color1;
        }
        int red1 = (color1 >> 8) & 255;
        int green1 = (color1 >> 16) & 255;
        int blue1 = (color1 >> 24) & 255;
        int alpha1 = color1 & 255;
        int red2 = (color2 >> 8) & 255;
        int green2 = (color2 >> 16) & 255;
        int blue2 = (color2 >> 24) & 255;
        int alpha2 = color2 & 255;
        int red = (red1 + red2) >> 1;
        int green = (green1 + green2) >> 1;
        int blue = (blue1 + blue2) >> 1;
        int alpha = (alpha1 + alpha2) >> 1;
        return (blue << 24) | (green << 16) | (red << 8) | alpha;
    }

    public boolean isCoveringOutsideDiscovered() {
        return this.coveringOutsideDiscovered;
    }
}
