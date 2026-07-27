package xaero.common.minimap.highlight;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.hud.minimap.info.render.compile.InfoDisplayCompiler;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/highlight/ChunkHighlighter.class */
public abstract class ChunkHighlighter extends AbstractHighlighter {
    protected abstract int[] getColors(ResourceKey<Level> resourceKey, int i, int i2);

    public abstract void addChunkHighlightTooltips(InfoDisplayCompiler infoDisplayCompiler, ResourceKey<Level> resourceKey, int i, int i2, int i3);

    protected ChunkHighlighter(boolean coveringOutsideDiscovered) {
        super(coveringOutsideDiscovered);
    }

    @Override // xaero.common.minimap.highlight.AbstractHighlighter
    public int[] getChunkHighlitColor(ResourceKey<Level> dimension, int chunkX, int chunkZ) {
        int[] colors = getColors(dimension, chunkX, chunkZ);
        if (colors == null) {
            return null;
        }
        int centerColor = colors[0];
        int topColor = colors[1];
        int rightColor = colors[2];
        int bottomColor = colors[3];
        int leftColor = colors[4];
        int topLeftColor = getSideBlend(topColor, leftColor, centerColor);
        int topRightColor = getSideBlend(topColor, rightColor, centerColor);
        int bottomRightColor = getSideBlend(bottomColor, rightColor, centerColor);
        int bottomLeftColor = getSideBlend(bottomColor, leftColor, centerColor);
        setResult(0, 0, topLeftColor);
        setResult(15, 0, topRightColor);
        setResult(15, 15, bottomRightColor);
        setResult(0, 15, bottomLeftColor);
        for (int i = 1; i < 15; i++) {
            setResult(i, 0, topColor);
            setResult(15, i, rightColor);
            setResult(i, 15, bottomColor);
            setResult(0, i, leftColor);
            for (int j = 1; j < 15; j++) {
                setResult(i, j, centerColor);
            }
        }
        return this.resultStore;
    }

    private int getSideBlend(int color1, int color2, int centerColor) {
        return getBlend(color1 == centerColor ? color2 : color1, color2 == centerColor ? color1 : color2);
    }

    @Override // xaero.common.minimap.highlight.AbstractHighlighter
    public void addBlockHighlightTooltips(InfoDisplayCompiler compiler, ResourceKey<Level> dimension, int blockX, int blockZ, int width) {
        if (!chunkIsHighlit(dimension, blockX >> 4, blockZ >> 4)) {
            return;
        }
        addChunkHighlightTooltips(compiler, dimension, blockX >> 4, blockZ >> 4, width);
    }
}
