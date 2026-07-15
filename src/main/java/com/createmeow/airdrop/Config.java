package com.createmeow.airdrop;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue BASE_MIN_DISTANCE = BUILDER
            .comment("Minimum distance from a base for airdrop spawning")
            .defineInRange("baseMinDistance", 100, 0, 1000);

    public static final ModConfigSpec.IntValue AIRDROP_LIFETIME_TICKS = BUILDER
            .comment("Lifetime of an airdrop in ticks (default 4 in-game days = 96000 ticks)")
            .defineInRange("airdropLifetimeTicks", 96000, 12000, 480000);

    static final ModConfigSpec SPEC = BUILDER.build();
}