package xaero.common.mixin;

import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({LevelChunk.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinWorldChunk.class */
public class MixinWorldChunk {
    public boolean xaero_chunkClean = false;
}
