package xaero.common.cache.placeholder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/cache/placeholder/PlaceholderBlockGetter.class */
public class PlaceholderBlockGetter implements BlockGetter {
    private BlockState placeholderState;

    public void setPlaceholderState(BlockState placeholderState) {
        this.placeholderState = placeholderState;
    }

    public BlockEntity getBlockEntity(BlockPos blockPos) {
        return null;
    }

    public BlockState getBlockState(BlockPos blockPos) {
        return this.placeholderState;
    }

    public FluidState getFluidState(BlockPos blockPos) {
        if (this.placeholderState == null) {
            return null;
        }
        return this.placeholderState.getFluidState();
    }

    public int getHeight() {
        return 16;
    }

    public int getMinBuildHeight() {
        return 0;
    }
}
