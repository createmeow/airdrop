package com.createmeow.airdrop.block;

import com.createmeow.airdrop.airdrop.AirdropData;
import com.createmeow.airdrop.particle.AirdropParticleOptions;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class AirDropBlock extends BaseEntityBlock {
    public static final EnumProperty<AirdropData.Tier> TIER = EnumProperty.create("tier", AirdropData.Tier.class);

    private static final VoxelShape SHAPE = Shapes.box(0.0625, 0.0, 0.0625, 0.9375, 0.875, 0.9375);

    public AirDropBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(TIER, AirdropData.Tier.COMMON));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(AirDropBlock::new);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TIER);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AirDropBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof AirDropBlockEntity be) {
            player.openMenu(be, pos);
            return ItemInteractionResult.CONSUME;
        }
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        float age = 1.0f;
        if (level.getBlockEntity(pos) instanceof AirDropBlockEntity be) {
            age = be.getAgeRatio();
        }

        Vector3f redColor = new Vector3f(1.0f, 0.1f, 0.1f);
        Vector3f grayColor = new Vector3f(0.6f, 0.6f, 0.6f);

        // age=0 -> red, age=1 -> gray
        float r = redColor.x() + (grayColor.x() - redColor.x()) * age;
        float g = redColor.y() + (grayColor.y() - redColor.y()) * age;
        float b = redColor.z() + (grayColor.z() - redColor.z()) * age;

        double centerX = pos.getX() + 0.5;
        double centerY = pos.getY() + 1.0;
        double centerZ = pos.getZ() + 0.5;

        // Spawn many particles each time animateTick is called (client tick ~20Hz)
        for (int i = 0; i < 15; i++) {
            double offsetX = (random.nextDouble() - 0.5) * 0.8;
            double offsetZ = (random.nextDouble() - 0.5) * 0.8;
            level.addParticle(
                    new AirdropParticleOptions(r, g, b, age),
                    centerX + offsetX,
                    centerY + random.nextDouble() * 0.3,
                    centerZ + offsetZ,
                    0.0, 0.15 + random.nextDouble() * 0.1, 0.0
            );
        }
    }
}