package xaero.common.cache;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import xaero.common.IXaeroMinimap;
import xaero.common.cache.placeholder.PlaceholderBlockGetter;
import xaero.hud.minimap.MinimapLogs;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/cache/BlockStateShortShapeCache.class */
public class BlockStateShortShapeCache {
    private IXaeroMinimap modMain;
    private BlockState lastShortChecked = null;
    private boolean lastShortCheckedResult = false;
    private Map<BlockState, Boolean> shortBlockStates = new HashMap();
    private PlaceholderBlockGetter placeholderBlockGetter = new PlaceholderBlockGetter();

    public BlockStateShortShapeCache(IXaeroMinimap modMain) {
        this.modMain = modMain;
    }

    public boolean isShort(BlockState state) {
        if (state == null || (state.getBlock() instanceof AirBlock) || (state.getBlock() instanceof LiquidBlock)) {
            return false;
        }
        synchronized (this.shortBlockStates) {
            if (state == this.lastShortChecked) {
                return this.lastShortCheckedResult;
            }
            Boolean cached = this.shortBlockStates.get(state);
            if (cached == null) {
                if (!Minecraft.getInstance().isSameThread()) {
                    return ((Boolean) Minecraft.getInstance().submit(() -> {
                        return Boolean.valueOf(isShort(state));
                    }).join()).booleanValue();
                }
                try {
                    this.placeholderBlockGetter.setPlaceholderState(state);
                    VoxelShape shape = state.getShape(this.placeholderBlockGetter, BlockPos.ZERO);
                    cached = Boolean.valueOf(shape.max(Direction.Axis.Y) < 0.25d);
                } catch (Throwable th) {
                    MinimapLogs.LOGGER.info("Defaulting world-dependent block state shape to not short: " + String.valueOf(state));
                    cached = false;
                }
                synchronized (this.shortBlockStates) {
                    this.shortBlockStates.put(state, cached);
                    this.lastShortChecked = state;
                    this.lastShortCheckedResult = cached.booleanValue();
                }
            }
            return cached.booleanValue();
        }
    }

    public void reset() {
        synchronized (this.shortBlockStates) {
            this.shortBlockStates.clear();
            this.lastShortChecked = null;
            this.lastShortCheckedResult = false;
        }
    }
}
