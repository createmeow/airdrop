package xaero.common.minimap.highlight;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.common.minimap.write.MinimapWriter;
import xaero.hud.minimap.info.render.compile.InfoDisplayCompiler;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/highlight/DimensionHighlighterHandler.class */
public class DimensionHighlighterHandler {
    private final MinimapWriter writer;
    private final ResourceKey<Level> dimension;
    private final HighlighterRegistry registry;
    private int version;

    public DimensionHighlighterHandler(ResourceKey<Level> dimension, HighlighterRegistry registry, MinimapWriter writer) {
        this.dimension = dimension;
        this.registry = registry;
        this.writer = writer;
    }

    public boolean shouldApplyRegionHighlights(int regionX, int regionZ, boolean discovered) {
        ResourceKey<Level> dimension = this.dimension;
        for (AbstractHighlighter hl : this.registry.getHighlighters()) {
            if (discovered || hl.isCoveringOutsideDiscovered()) {
                if (hl.regionHasHighlights(dimension, regionX, regionZ)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean shouldApplyTileChunkHighlights(int regionX, int regionZ, int insideTileChunkX, int insideTileChunkZ, boolean discovered) {
        int startChunkX = (regionX << 5) | (insideTileChunkX << 2);
        int startChunkZ = (regionZ << 5) | (insideTileChunkZ << 2);
        for (AbstractHighlighter hl : this.registry.getHighlighters()) {
            if (shouldApplyTileChunkHighlightsHelp(hl, regionX, regionZ, startChunkX, startChunkZ, discovered)) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldApplyTileChunkHighlights(AbstractHighlighter hl, int regionX, int regionZ, int insideTileChunkX, int insideTileChunkZ, boolean discovered) {
        int startChunkX = (regionX << 5) | (insideTileChunkX << 2);
        int startChunkZ = (regionZ << 5) | (insideTileChunkZ << 2);
        return shouldApplyTileChunkHighlightsHelp(hl, regionX, regionZ, startChunkX, startChunkZ, discovered);
    }

    private boolean shouldApplyTileChunkHighlightsHelp(AbstractHighlighter hl, int regionX, int regionZ, int startChunkX, int startChunkZ, boolean discovered) {
        if (!discovered && !hl.isCoveringOutsideDiscovered()) {
            return false;
        }
        ResourceKey<Level> dimension = this.dimension;
        if (!hl.regionHasHighlights(dimension, regionX, regionZ)) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (hl.chunkIsHighlit(dimension, startChunkX | i, startChunkZ | j)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int[] applyChunkHighlightColors(int tileX, int tileZ) {
        int[] highlights = new int[256];
        ResourceKey<Level> dimension = this.dimension;
        for (AbstractHighlighter hl : this.registry.getHighlighters()) {
            int[] highlightColors = hl.getChunkHighlitColor(dimension, tileX, tileZ);
            if (highlightColors != null) {
                for (int i = 0; i < highlightColors.length; i++) {
                    int highlightColor = highlightColors[i];
                    int hlAlpha = highlightColor & 255;
                    float hlAlphaFloat = hlAlpha / 255.0f;
                    float oneMinusHlAlpha = 1.0f - hlAlphaFloat;
                    int hlRed = (highlightColor >> 8) & 255;
                    int hlGreen = (highlightColor >> 16) & 255;
                    int hlBlue = (highlightColor >> 24) & 255;
                    int destColor = highlights[i];
                    int red = (destColor >> 8) & 255;
                    int green = (destColor >> 16) & 255;
                    int red2 = (int) ((red * oneMinusHlAlpha) + (hlRed * hlAlphaFloat));
                    int green2 = (int) ((green * oneMinusHlAlpha) + (hlGreen * hlAlphaFloat));
                    int blue = (int) ((((destColor >> 24) & 255) * oneMinusHlAlpha) + (hlBlue * hlAlphaFloat));
                    int alpha = (int) (((destColor & 255) * oneMinusHlAlpha) + hlAlpha);
                    if (red2 > 255) {
                        red2 = 255;
                    }
                    if (green2 > 255) {
                        green2 = 255;
                    }
                    if (blue > 255) {
                        blue = 255;
                    }
                    if (alpha > 255) {
                        alpha = 255;
                    }
                    highlights[i] = (blue << 24) | (green2 << 16) | (red2 << 8) | alpha;
                }
            }
        }
        return highlights;
    }

    public void requestRefresh(int regionX, int regionZ) {
        int loadingCanvasLeft = this.writer.getLoadingMapChunkX();
        int loadingCanvasTop = this.writer.getLoadingMapChunkZ();
        int loadingCanvasRight = loadingCanvasLeft + this.writer.getLoadingSideInChunks();
        int loadingCanvasBottom = loadingCanvasTop + this.writer.getLoadingSideInChunks();
        int regionLeft = regionX << 3;
        int regionRight = (regionX + 1) << 3;
        int regionTop = regionZ << 3;
        int regionBottom = (regionZ + 1) << 3;
        if (regionRight > loadingCanvasLeft && regionLeft < loadingCanvasRight && regionBottom > loadingCanvasTop && regionTop < loadingCanvasBottom) {
            this.version++;
        }
    }

    public void requestRefresh() {
        this.version++;
    }

    public void addBlockHighlightTooltips(InfoDisplayCompiler compiler, int blockX, int blockZ, int width, boolean discovered) {
        ResourceKey<Level> dimension = this.dimension;
        int tileChunkX = blockX >> 6;
        int tileChunkZ = blockZ >> 6;
        int regionX = tileChunkX >> 3;
        int regionZ = tileChunkZ >> 3;
        if (!shouldApplyRegionHighlights(regionX, regionZ, discovered)) {
            return;
        }
        int localTileChunkX = tileChunkX & 7;
        int localTileChunkZ = tileChunkZ & 7;
        for (AbstractHighlighter hl : this.registry.getHighlighters()) {
            if (shouldApplyTileChunkHighlights(hl, regionX, regionZ, localTileChunkX, localTileChunkZ, discovered)) {
                hl.addBlockHighlightTooltips(compiler, dimension, blockX, blockZ, width);
            }
        }
    }

    public int getVersion() {
        return this.version;
    }
}
