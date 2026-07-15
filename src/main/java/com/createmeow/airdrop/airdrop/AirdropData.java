package com.createmeow.airdrop.airdrop;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public class AirdropData {
    public enum Tier implements StringRepresentable {
        COMMON(1, 0.50f),
        MEDIUM(2, 0.35f),
        ADVANCED(3, 0.15f);

        private final int level;
        private final float defaultChance;

        Tier(int level, float defaultChance) {
            this.level = level;
            this.defaultChance = defaultChance;
        }

        public int getLevel() {
            return level;
        }

        public float getDefaultChance() {
            return defaultChance;
        }

        public static Tier fromLevel(int level) {
            return switch (level) {
                case 1 -> COMMON;
                case 2 -> MEDIUM;
                case 3 -> ADVANCED;
                default -> COMMON;
            };
        }

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }

        public Component getDisplayName() {
            return Component.translatable("airdrop.tier." + getSerializedName());
        }
    }

    private final BlockPos pos;
    private final Tier tier;
    private final boolean isTimed;
    private final long createTime;

    public AirdropData(BlockPos pos, Tier tier, boolean isTimed, long createTime) {
        this.pos = pos;
        this.tier = tier;
        this.isTimed = isTimed;
        this.createTime = createTime;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Tier getTier() {
        return tier;
    }

    public boolean isTimed() {
        return isTimed;
    }

    public long getCreateTime() {
        return createTime;
    }
}