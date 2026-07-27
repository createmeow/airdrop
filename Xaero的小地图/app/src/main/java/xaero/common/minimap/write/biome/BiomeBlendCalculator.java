package xaero.common.minimap.write.biome;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/write/biome/BiomeBlendCalculator.class */
public class BiomeBlendCalculator implements BlockAndTintGetter {
    private Level original;
    private int startI;
    private int endI;
    private int startJ;
    private int endJ;

    public void prepare(Level original, boolean biomeBlending) {
        this.original = original;
        this.endJ = 0;
        this.startJ = 0;
        this.endI = 0;
        this.startI = 0;
        if (biomeBlending) {
            this.startI = -1;
            this.endI = 1;
            this.startJ = -1;
            this.endJ = 1;
        }
    }

    public BlockEntity getBlockEntity(BlockPos blockPos) {
        return this.original.getBlockEntity(blockPos);
    }

    public BlockState getBlockState(BlockPos blockPos) {
        return this.original.getBlockState(blockPos);
    }

    public FluidState getFluidState(BlockPos blockPos) {
        return this.original.getFluidState(blockPos);
    }

    public float getShade(Direction direction, boolean bl) {
        return this.original.getShade(direction, bl);
    }

    public LevelLightEngine getLightEngine() {
        return this.original.getLightEngine();
    }

    public int getHeight() {
        return this.original.getHeight();
    }

    public int getMinBuildHeight() {
        return this.original.getMinBuildHeight();
    }

    public int getBlockTint(BlockPos blockPos, ColorResolver colorResolver) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        int redAccumulator = 0;
        int greenAccumulator = 0;
        int blueAccumulator = 0;
        Level original = this.original;
        int total = 0;
        for (int i = this.startI; i <= this.endI; i++) {
            for (int j = this.startJ; j <= this.endJ; j++) {
                if (i == 0 || j == 0) {
                    mutableBlockPos.set(x + i, y, z + j);
                    Holder<Biome> biomeHolder = original.getBiome(mutableBlockPos);
                    Biome biome = biomeHolder == null ? null : (Biome) biomeHolder.value();
                    if (biome != null) {
                        int colorSample = colorResolver.getColor(biome, mutableBlockPos.getX(), mutableBlockPos.getZ());
                        redAccumulator += colorSample & 16711680;
                        greenAccumulator += colorSample & 65280;
                        blueAccumulator += colorSample & 255;
                        total++;
                    }
                }
            }
        }
        int red = redAccumulator / total;
        int green = greenAccumulator / total;
        int blue = blueAccumulator / total;
        return (-16777216) | (red & 16711680) | (green & 65280) | blue;
    }
}
