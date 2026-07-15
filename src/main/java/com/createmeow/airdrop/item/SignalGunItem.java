package com.createmeow.airdrop.item;

import com.createmeow.airdrop.airdrop.AirdropScheduler;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;

import java.util.List;

public class SignalGunItem extends Item {
    private final boolean isAdvanced;

    public SignalGunItem(Properties properties, boolean isAdvanced) {
        super(properties);
        this.isAdvanced = isAdvanced;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        if (AirdropScheduler.isSignalGunOnCooldown(player.getUUID())) {
            long remaining = AirdropScheduler.getSignalGunCooldownRemaining(player.getUUID());
            long seconds = remaining / 1000;
            player.sendSystemMessage(Component.translatable("airdrop.message.signal_gun.cooldown", seconds));
            return InteractionResultHolder.fail(stack);
        }

        ServerLevel serverLevel = (ServerLevel) level;

        // 创建并发射爆炸性烟花火箭（红色大球 + 黄色渐变 + 拖尾闪烁）
        FireworkExplosion explosion = new FireworkExplosion(
                FireworkExplosion.Shape.LARGE_BALL,
                IntArrayList.of(0xFF0000),
                IntArrayList.of(0xFFFF00),
                true,
                true
        );
        Fireworks fireworks = new Fireworks(3, List.of(explosion));
        ItemStack fireworkStack = new ItemStack(Items.FIREWORK_ROCKET);
        fireworkStack.set(DataComponents.FIREWORKS, fireworks);

        FireworkRocketEntity rocket = new FireworkRocketEntity(
                level,
                player.getX(),
                player.getY() + player.getEyeHeight(),
                player.getZ(),
                fireworkStack
        );
        rocket.setDeltaMovement(0, 2.0, 0);
        level.addFreshEntity(rocket);

        // 生成空投
        var tier = AirdropScheduler.rollSignalGunTier(isAdvanced);
        AirdropScheduler.spawnAirdropNear(serverLevel, player.blockPosition(), 1000, tier, false, serverLevel.getServer());
        AirdropScheduler.setSignalGunCooldown(player.getUUID());

        stack.shrink(1);

        return InteractionResultHolder.consume(stack);
    }
}
