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

    public static final ModConfigSpec.IntValue MIN_SPAWN_RANGE = BUILDER
            .comment("Minimum distance from world spawn (0, 0) for airdrop spawning")
            .defineInRange("minSpawnRange", 500, 0, 10000);

    public static final ModConfigSpec.IntValue MAX_SPAWN_RANGE = BUILDER
            .comment("Maximum distance from world spawn (0, 0) for airdrop spawning")
            .defineInRange("maxSpawnRange", 3000, 100, 30000);

    static final ModConfigSpec SPEC = BUILDER.build();
}