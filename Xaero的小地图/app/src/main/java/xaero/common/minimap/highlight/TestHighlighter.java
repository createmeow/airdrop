package xaero.common.minimap.highlight;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import xaero.hud.minimap.info.render.compile.InfoDisplayCompiler;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/highlight/TestHighlighter.class */
public class TestHighlighter extends ChunkHighlighter {
    public TestHighlighter() {
        super(true);
    }

    @Override // xaero.common.minimap.highlight.AbstractHighlighter
    public boolean regionHasHighlights(ResourceKey<Level> dimension, int regionX, int regionZ) {
        return true;
    }

    @Override // xaero.common.minimap.highlight.ChunkHighlighter
    protected int[] getColors(ResourceKey<Level> dimension, int chunkX, int chunkZ) {
        int i;
        int i2;
        int i3;
        int i4;
        if (!chunkIsHighlit(dimension, chunkX, chunkZ)) {
            return null;
        }
        this.resultStore[0] = -11184777;
        int[] iArr = this.resultStore;
        if ((chunkZ & 3) != 0) {
            i = -11184777;
        } else {
            i = -11184692;
        }
        iArr[1] = i;
        int[] iArr2 = this.resultStore;
        if ((chunkX & 3) != 3) {
            i2 = -11184777;
        } else {
            i2 = -11184692;
        }
        iArr2[2] = i2;
        int[] iArr3 = this.resultStore;
        if ((chunkZ & 3) != 3) {
            i3 = -11184777;
        } else {
            i3 = -11184692;
        }
        iArr3[3] = i3;
        int[] iArr4 = this.resultStore;
        if ((chunkX & 3) != 0) {
            i4 = -11184777;
        } else {
            i4 = -11184692;
        }
        iArr4[4] = i4;
        return this.resultStore;
    }

    @Override // xaero.common.minimap.highlight.AbstractHighlighter
    public boolean chunkIsHighlit(ResourceKey<Level> dimension, int chunkX, int chunkZ) {
        return ((chunkX >> 2) & 1) == ((chunkZ >> 2) & 1);
    }

    @Override // xaero.common.minimap.highlight.ChunkHighlighter
    public void addChunkHighlightTooltips(InfoDisplayCompiler compiler, ResourceKey<Level> dimension, int chunkX, int chunkZ, int width) {
        compiler.addLine((Component) Component.literal("subtle!"));
    }
}
