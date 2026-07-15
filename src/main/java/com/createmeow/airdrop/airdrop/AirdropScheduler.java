package com.createmeow.airdrop.airdrop;

import com.createmeow.airdrop.airDrop;
import com.createmeow.airdrop.block.AirDropBlockEntity;
import com.createmeow.airdrop.integration.BaseCraftIntegration;
import com.createmeow.airdrop.network.AirdropNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AirdropScheduler {
    private static final int[] SCHEDULED_HOURS = {2, 5, 8, 11, 14, 17, 20, 23};
    private static final int SIGNAL_GUN_RADIUS = 1000;
    private static final int SIGNAL_GUN_COOLDOWN_TICKS = 20 * 60 * 5;
    private static final int BASE_MIN_DISTANCE = 100;

    private static final Map<UUID, Long> signalGunCooldowns = new ConcurrentHashMap<>();
    private static final Map<ServerLevel, List<ActiveAirdrop>> activeAirdrops = new ConcurrentHashMap<>();

    private static float mediumBonus = 0.0f;
    private static float advancedBonus = 0.0f;
    private static int lastScheduledHour = -1;

    private static boolean initialized = false;

    public static void init(MinecraftServer server) {
        if (initialized) return;
        initialized = true;
        AirdropLootManager.init();
        // Initialize to current hour so the first tick won't immediately trigger
        // a scheduled airdrop. Only natural hour transitions will trigger it.
        lastScheduledHour = LocalDateTime.now(ZoneId.systemDefault()).getHour();
        airDrop.LOGGER.info("AirDrop scheduler initialized, lastScheduledHour={}", lastScheduledHour);
    }

    public static void onServerStop() {
        initialized = false;
        activeAirdrops.clear();
        signalGunCooldowns.clear();
        lastScheduledHour = -1;
    }

    public static void tick(MinecraftServer server) {
        if (!initialized) return;

        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        if (overworld == null) return;

        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        int currentHour = now.getHour();

        if (currentHour != lastScheduledHour && isScheduledHour(currentHour)) {
            lastScheduledHour = currentHour;
            triggerScheduledAirdrop(server);
        }

        for (var entry : activeAirdrops.entrySet()) {
            ServerLevel level = entry.getKey();
            List<ActiveAirdrop> drops = new ArrayList<>(entry.getValue());
            for (ActiveAirdrop airdrop : drops) {
                if (level.isLoaded(airdrop.pos)) {
                    if (level.getBlockEntity(airdrop.pos) instanceof AirDropBlockEntity be) {
                        be.tick(level, airdrop.pos, level.getBlockState(airdrop.pos));
                    }
                }
            }
        }
    }

    private static boolean isScheduledHour(int hour) {
        for (int h : SCHEDULED_HOURS) {
            if (h == hour) return true;
        }
        return false;
    }

    private static void triggerScheduledAirdrop(MinecraftServer server) {
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        if (overworld == null) return;

        boolean hasPlayers = !server.getPlayerList().getPlayers().isEmpty();

        if (!hasPlayers) {
            mediumBonus = Math.min(mediumBonus + 0.05f, 0.20f);
            advancedBonus = Math.min(advancedBonus + 0.02f, 0.20f);
            airDrop.LOGGER.info("No players online, next airdrop bonus: medium +{}%, advanced +{}%",
                    (int)(mediumBonus * 100), (int)(advancedBonus * 100));
            return;
        }

        AirdropData.Tier tier = rollTier();

        mediumBonus = 0.0f;
        advancedBonus = 0.0f;

        BlockPos pos = findValidPosition(overworld);
        if (pos == null) {
            airDrop.LOGGER.warn("Could not find valid position for scheduled airdrop");
            return;
        }

        spawnAirdrop(overworld, pos, tier, true, server);
    }

    public static AirdropData.Tier rollTier() {
        float commonChance = AirdropData.Tier.COMMON.getDefaultChance();
        float mediumChance = AirdropData.Tier.MEDIUM.getDefaultChance() + mediumBonus;
        float advancedChance = AirdropData.Tier.ADVANCED.getDefaultChance() + advancedBonus;

        float total = commonChance + mediumChance + advancedChance;
        float roll = (float) Math.random() * total;

        if (roll < advancedChance) {
            return AirdropData.Tier.ADVANCED;
        } else if (roll < advancedChance + mediumChance) {
            return AirdropData.Tier.MEDIUM;
        } else {
            return AirdropData.Tier.COMMON;
        }
    }

    public static AirdropData.Tier rollSignalGunTier(boolean isAdvanced) {
        if (isAdvanced) {
            return Math.random() < 0.25f ? AirdropData.Tier.ADVANCED : AirdropData.Tier.MEDIUM;
        } else {
            float roll = (float) Math.random();
            if (roll < 0.15f) return AirdropData.Tier.ADVANCED;
            if (roll < 0.50f) return AirdropData.Tier.MEDIUM;
            return AirdropData.Tier.COMMON;
        }
    }

    public static boolean isSignalGunOnCooldown(UUID playerId) {
        Long lastUse = signalGunCooldowns.get(playerId);
        if (lastUse == null) return false;
        return System.currentTimeMillis() - lastUse < SIGNAL_GUN_COOLDOWN_TICKS * 50L;
    }

    public static long getSignalGunCooldownRemaining(UUID playerId) {
        Long lastUse = signalGunCooldowns.get(playerId);
        if (lastUse == null) return 0;
        long elapsed = System.currentTimeMillis() - lastUse;
        long remaining = (SIGNAL_GUN_COOLDOWN_TICKS * 50L) - elapsed;
        return Math.max(0, remaining);
    }

    public static void setSignalGunCooldown(UUID playerId) {
        signalGunCooldowns.put(playerId, System.currentTimeMillis());
    }

    public static BlockPos findValidPosition(ServerLevel level) {
        int maxAttempts = 50;
        for (int i = 0; i < maxAttempts; i++) {
            int x = level.random.nextInt(6000) - 3000;
            int z = level.random.nextInt(6000) - 3000;

            BlockPos pos = findSurfaceAbove(level, x, z);
            if (pos != null && isValidPosition(level, pos)) {
                return pos;
            }
        }
        return null;
    }

    public static BlockPos findValidPositionNear(ServerLevel level, BlockPos center, int radius) {
        int maxAttempts = 50;
        for (int i = 0; i < maxAttempts; i++) {
            int dx = level.random.nextInt(radius * 2) - radius;
            int dz = level.random.nextInt(radius * 2) - radius;
            int x = center.getX() + dx;
            int z = center.getZ() + dz;

            BlockPos pos = findSurfaceAbove(level, x, z);
            if (pos != null && isValidPosition(level, pos)) {
                return pos;
            }
        }
        return null;
    }

    /**
     * Scans from top of world downward to find the surface.
     * This is more reliable than getHeight() for ungenerated chunks,
     * because it forces full chunk generation and finds the actual solid surface.
     */
    private static BlockPos findSurfaceAbove(ServerLevel level, int x, int z) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(x, level.getMaxBuildHeight(), z);

        // Scan from top down to find the first solid block
        for (int y = level.getMaxBuildHeight(); y > level.getMinBuildHeight(); y--) {
            cursor.setY(y);
            if (level.getBlockState(cursor).isSolid()) {
                // Found the surface block, the airdrop goes one block above
                return cursor.above().immutable();
            }
        }
        return null;
    }

    private static boolean isValidPosition(ServerLevel level, BlockPos pos) {
        if (pos.getY() <= level.getMinBuildHeight() || pos.getY() >= level.getMaxBuildHeight()) {
            return false;
        }
        if (!level.getBlockState(pos.below()).isSolid()) {
            return false;
        }
        if (level.getBlockState(pos).isSolid() || level.getBlockState(pos.above()).isSolid()) {
            return false;
        }
        if (level.getBiome(pos).value().getBaseTemperature() < 0.15f) {
            return false;
        }

        if (BaseCraftIntegration.isNearBase(level, pos, BASE_MIN_DISTANCE)) {
            return false;
        }

        return true;
    }

    public static void spawnAirdrop(ServerLevel level, BlockPos pos, AirdropData.Tier tier, boolean isTimed, MinecraftServer server) {
        BlockState state = airDrop.AIR_DROP_BLOCK.get().defaultBlockState().setValue(
                com.createmeow.airdrop.block.AirDropBlock.TIER, tier);
        level.setBlock(pos, state, 3);

        if (level.getBlockEntity(pos) instanceof AirDropBlockEntity be) {
            be.init(tier, isTimed);
        }

        activeAirdrops.computeIfAbsent(level, k -> new ArrayList<>())
                .add(new ActiveAirdrop(pos, tier, System.currentTimeMillis()));

        level.playSound(null, pos, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.BLOCKS, 1.5f, 0.5f);

        Component message = Component.translatable("airdrop.message.announcement",
                tier.getDisplayName(),
                Component.translatable(isTimed ? "airdrop.type.timed" : "airdrop.type.manual"),
                pos.getX(), pos.getY(), pos.getZ());

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.sendSystemMessage(message);
        }

        airDrop.LOGGER.info("Airdrop spawned: {} tier at ({}, {}, {}), timed={}",
                tier, pos.getX(), pos.getY(), pos.getZ(), isTimed);

        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        String waypointName = String.format("%d/%d/%d %s空投",
                now.getMonthValue(), now.getDayOfMonth(), now.getHour(), isTimed ? "定时" : "手动");
        AirdropNetwork.sendWaypointCreateToAll(server, pos, waypointName, isTimed);
    }

    public static void spawnAirdropNear(ServerLevel level, BlockPos center, int radius, AirdropData.Tier tier, boolean isTimed, MinecraftServer server) {
        BlockPos pos = findValidPositionNear(level, center, radius);
        if (pos == null) {
            airDrop.LOGGER.warn("Could not find valid position near ({}, {}, {})", center.getX(), center.getY(), center.getZ());
            return;
        }
        spawnAirdrop(level, pos, tier, isTimed, server);
    }

    public static void removeAirdrop(Level level, BlockPos pos, AirDropBlockEntity be) {
        activeAirdrops.computeIfPresent((ServerLevel) level, (k, v) -> {
            v.removeIf(a -> a.pos.equals(pos));
            return v;
        });

        AirdropNetwork.sendWaypointRemoveToAll(level.getServer(), pos);
    }

    public static void spawnCommandAirdrop(MinecraftServer server, AirdropData.Tier tier, BlockPos pos) {
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        if (overworld == null) {
            airDrop.LOGGER.warn("Overworld not available");
            return;
        }
        spawnAirdrop(overworld, pos, tier, false, server);
    }

    public static class ActiveAirdrop {
        public final BlockPos pos;
        public final AirdropData.Tier tier;
        public final long spawnTime;

        public ActiveAirdrop(BlockPos pos, AirdropData.Tier tier, long spawnTime) {
            this.pos = pos;
            this.tier = tier;
            this.spawnTime = spawnTime;
        }
    }
}