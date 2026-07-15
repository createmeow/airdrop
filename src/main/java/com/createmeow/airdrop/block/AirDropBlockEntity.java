package com.createmeow.airdrop.block;

import com.createmeow.airdrop.airDrop;
import com.createmeow.airdrop.airdrop.AirdropData;
import com.createmeow.airdrop.airdrop.AirdropLootManager;
import com.createmeow.airdrop.airdrop.AirdropScheduler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AirDropBlockEntity extends BlockEntity implements Container, MenuProvider {
    public static final int CONTAINER_SIZE = 27;
    public static final long LIFETIME_TICKS = 4L * 24000L;

    private NonNullList<ItemStack> items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
    private AirdropData.Tier tier = AirdropData.Tier.COMMON;
    private long spawnTime = 0;
    private boolean initialized = false;
    private boolean isTimedAirdrop = false;

    public AirDropBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.AIR_DROP_BE.get(), pos, state);
    }

    public static BlockEntityType<AirDropBlockEntity> createType() {
        return BlockEntityType.Builder.of(AirDropBlockEntity::new, airDrop.AIR_DROP_BLOCK.get()).build(null);
    }

    public void init(AirdropData.Tier tier, boolean isTimed) {
        if (!initialized) {
            this.tier = tier;
            this.spawnTime = level != null ? level.getGameTime() : 0;
            this.initialized = true;
            this.isTimedAirdrop = isTimed;
            if (level != null && !level.isClientSide) {
                level.setBlock(getBlockPos(), getBlockState().setValue(AirDropBlock.TIER, tier), 3);
                fillLoot();
            }
            setChanged();
        }
    }

    private void fillLoot() {
        items.clear();
        var loot = AirdropLootManager.generateLoot(tier, level);
        int slot = 0;
        for (ItemStack stack : loot) {
            if (slot < CONTAINER_SIZE) {
                items.set(slot, stack);
                slot++;
            }
        }
    }

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (!initialized) return;
        long elapsed = level.getGameTime() - spawnTime;
        if (elapsed >= LIFETIME_TICKS || isEmpty()) {
            AirdropScheduler.removeAirdrop(level, pos, this);
            level.removeBlock(pos, false);
        }
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    public float getAgeRatio() {
        if (spawnTime == 0) return 1.0f;
        long elapsed = level != null ? level.getGameTime() - spawnTime : 0;
        return Math.max(0.0f, Math.min(1.0f, (float) elapsed / LIFETIME_TICKS));
    }

    public AirdropData.Tier getTier() {
        return tier;
    }

    public boolean isTimedAirdrop() {
        return isTimedAirdrop;
    }

    @Override
    public int getContainerSize() {
        return CONTAINER_SIZE;
    }

    @Override
    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(items, slot, amount);
        if (!result.isEmpty()) setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        if (stack.getCount() > getMaxStackSize()) {
            stack.setCount(getMaxStackSize());
        }
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this) return false;
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.airdrop.air_drop");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ChestMenu(MenuType.GENERIC_9x3, containerId, playerInventory, this, 3);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putString("Tier", tier.name());
        tag.putLong("SpawnTime", spawnTime);
        tag.putBoolean("Initialized", initialized);
        tag.putBoolean("IsTimedAirdrop", isTimedAirdrop);
        ContainerHelper.saveAllItems(tag, items, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Tier")) {
            tier = AirdropData.Tier.valueOf(tag.getString("Tier"));
        }
        spawnTime = tag.getLong("SpawnTime");
        initialized = tag.getBoolean("Initialized");
        isTimedAirdrop = tag.getBoolean("IsTimedAirdrop");
        items = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, items, registries);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}