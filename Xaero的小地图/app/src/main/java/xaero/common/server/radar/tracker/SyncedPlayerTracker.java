package xaero.common.server.radar.tracker;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.server.MinecraftServerData;
import xaero.common.server.player.ServerPlayerData;
import xaero.hud.packet.tracker.ClientboundTrackedPlayerPacket;
import xaero.lib.XaeroLib;
import xaero.lib.common.config.Config;
import xaero.lib.common.config.primary.option.LibPrimaryCommonConfigOptions;
import xaero.lib.common.config.single.SingleConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/server/radar/tracker/SyncedPlayerTracker.class */
public class SyncedPlayerTracker {
    private final IXaeroMinimap modMain;

    public SyncedPlayerTracker(IXaeroMinimap modMain) {
        this.modMain = modMain;
    }

    public void onTick(MinecraftServer server, ServerPlayer player, MinecraftServerData serverData, ServerPlayerData playerData) {
        Set<UUID> otherPlayerSyncedPlayers;
        long currentTime = System.currentTimeMillis();
        if (currentTime - playerData.getLastTrackedPlayerSync() < 250) {
            return;
        }
        playerData.setLastTrackedPlayerSync(currentTime);
        boolean playerHasMod = playerData.hasMod();
        Iterable<ISyncedPlayerTrackerSystem> playerTrackerSystems = serverData.getSyncedPlayerTrackerSystemManager().getSystems();
        Set<UUID> syncedPlayers = playerData.ensureCurrentlySyncedPlayers();
        Set<UUID> leftoverPlayers = new HashSet<>(syncedPlayers);
        SyncedTrackedPlayer toSync = playerData.getLastSyncedData();
        boolean shouldSyncToOthers = toSync == null || !toSync.matchesEnough(player, 0.0d);
        if (shouldSyncToOthers) {
            toSync = playerData.ensureLastSyncedData();
            toSync.update(player);
        }
        HudMod.INSTANCE.getHudConfigs().getPrimaryCommonConfigManager();
        SingleConfigManager<Config> libPrimaryCommonConfig = XaeroLib.INSTANCE.getLibConfigChannel().getPrimaryCommonConfigManager();
        boolean everyoneIsTracked = ((Boolean) libPrimaryCommonConfig.getEffective(LibPrimaryCommonConfigOptions.EVERYONE_TRACKS_EVERYONE)).booleanValue();
        boolean opacReceiveParty = playerHasMod && this.modMain.getSupportServerMods().hasOpac() && this.modMain.getSupportServerMods().getOpac().getReceiveLocationsFromPartyConfigValue(player);
        boolean opacReceiveMutualAllies = playerHasMod && this.modMain.getSupportServerMods().hasOpac() && this.modMain.getSupportServerMods().getOpac().getReceiveLocationsFromMutualAlliesConfigValue(player);
        if (this.modMain.getSupportServerMods().hasOpac()) {
            this.modMain.getSupportServerMods().getOpac().updateShareLocationConfigValues(player, playerData);
        }
        for (Player player2 : server.getPlayerList().getPlayers()) {
            if (player2 != player) {
                leftoverPlayers.remove(player2.getUUID());
                ServerPlayerData otherPlayerData = ServerPlayerData.get(player2);
                if (shouldSyncToOthers && (otherPlayerSyncedPlayers = otherPlayerData.getCurrentlySyncedPlayers()) != null && otherPlayerSyncedPlayers.contains(player.getUUID())) {
                    sendTrackedPlayerPacket(player2, toSync);
                }
                if (playerHasMod) {
                    boolean tracked = everyoneIsTracked;
                    if (!tracked) {
                        boolean opacConfigsAllowPartySync = !this.modMain.getSupportServerMods().hasOpac() || this.modMain.getSupportServerMods().getOpac().isPositionSyncAllowed(2, otherPlayerData, opacReceiveParty);
                        boolean opacConfigsAllowAllySync = !this.modMain.getSupportServerMods().hasOpac() || this.modMain.getSupportServerMods().getOpac().isPositionSyncAllowed(1, otherPlayerData, opacReceiveMutualAllies);
                        for (ISyncedPlayerTrackerSystem system : playerTrackerSystems) {
                            int trackingLevel = system.getTrackingLevel(player, player2);
                            if (trackingLevel > 0 && (!system.isPartySystem() || ((trackingLevel == 1 && opacConfigsAllowAllySync) || (trackingLevel > 1 && opacConfigsAllowPartySync)))) {
                                tracked = true;
                                break;
                            }
                        }
                    }
                    boolean alreadySynced = syncedPlayers.contains(player2.getUUID());
                    if (!tracked) {
                        if (alreadySynced) {
                            syncedPlayers.remove(player2.getUUID());
                            sendRemovePacket(player, player2.getUUID());
                        }
                    } else if (!alreadySynced && otherPlayerData.getLastSyncedData() != null) {
                        syncedPlayers.add(player2.getUUID());
                        sendTrackedPlayerPacket(player, otherPlayerData.getLastSyncedData());
                    }
                }
            }
        }
        for (UUID offlineId : leftoverPlayers) {
            syncedPlayers.remove(offlineId);
            sendRemovePacket(player, offlineId);
        }
    }

    private void sendRemovePacket(ServerPlayer player, UUID toRemove) {
        this.modMain.getMessageHandler().sendToPlayer(player, (ServerPlayer) new ClientboundTrackedPlayerPacket(true, toRemove, 0.0d, 0.0d, 0.0d, null));
    }

    private void sendTrackedPlayerPacket(ServerPlayer player, SyncedTrackedPlayer tracked) {
        this.modMain.getMessageHandler().sendToPlayer(player, (ServerPlayer) new ClientboundTrackedPlayerPacket(false, tracked.getId(), tracked.getX(), tracked.getY(), tracked.getZ(), tracked.getDimensionKey().location()));
    }
}
