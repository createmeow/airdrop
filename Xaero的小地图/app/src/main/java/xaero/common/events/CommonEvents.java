package xaero.common.events;

import java.nio.file.Path;
import java.util.Set;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import xaero.common.HudMod;
import xaero.common.server.MinecraftServerData;
import xaero.common.server.MineraftServerDataInitializer;
import xaero.common.server.level.LevelMapProperties;
import xaero.common.server.level.LevelMapPropertiesIO;
import xaero.common.server.player.IServerPlayer;
import xaero.common.server.player.ServerPlayerData;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.packet.basic.ClientboundRulesPacket;
import xaero.hud.packet.basic.HandshakePacket;
import xaero.hud.packet.tracker.ClientboundPlayerTrackerResetPacket;
import xaero.lib.common.config.profile.ConfigProfile;
import xaero.lib.common.config.server.ServerConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/events/CommonEvents.class */
public abstract class CommonEvents {
    private final HudMod modMain;

    public CommonEvents(HudMod modMain) {
        this.modMain = modMain;
    }

    void onPlayerClone(Player oldPlayer, Player newPlayer, boolean alive) {
        if (oldPlayer instanceof ServerPlayer) {
            ((IServerPlayer) newPlayer).setXaeroMinimapPlayerData(ServerPlayerData.get((ServerPlayer) oldPlayer));
        }
    }

    public void onServerStarting(MinecraftServer server) {
        if (!this.modMain.isFirstStageLoaded()) {
            return;
        }
        new MineraftServerDataInitializer().init(server, this.modMain);
    }

    public void onServerStopped(MinecraftServer server) {
    }

    public void onPlayerLogIn(Player player) {
        if (this.modMain.isFirstStageLoaded() && (player instanceof ServerPlayer)) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            this.modMain.getMessageHandler().sendToPlayer(serverPlayer, (ServerPlayer) new ClientboundPlayerTrackerResetPacket());
        }
    }

    public void onPlayerWorldJoin(ServerPlayer player) {
        if (!this.modMain.isFirstStageLoaded()) {
            return;
        }
        this.modMain.getMessageHandler().sendToPlayer(player, (ServerPlayer) new HandshakePacket());
        ServerConfigManager configManager = this.modMain.getHudConfigs().getServerConfigManager();
        ConfigProfile defaultEnforcedProfile = configManager.getDefaultEnforcedProfile();
        boolean caveModeConfig = !Boolean.FALSE.equals(defaultEnforcedProfile.get(MinimapProfiledConfigOptions.CAVE_MODE_ALLOWED));
        Set<ResourceLocation> caveModeDimensionsConfig = (Set) defaultEnforcedProfile.get(MinimapProfiledConfigOptions.CAVE_MODE_ALLOWED_DIMENSIONS);
        boolean allowOverworldCaveModeOnServer = caveModeConfig && (caveModeDimensionsConfig == null || caveModeDimensionsConfig.isEmpty() || caveModeDimensionsConfig.contains(Level.OVERWORLD.location()));
        boolean allowNetherCaveModeOnServer = caveModeConfig && (caveModeDimensionsConfig == null || caveModeDimensionsConfig.isEmpty() || caveModeDimensionsConfig.contains(Level.NETHER.location()));
        boolean radarConfig = !Boolean.FALSE.equals(defaultEnforcedProfile.get(MinimapProfiledConfigOptions.DISPLAY_RADAR));
        this.modMain.getMessageHandler().sendToPlayer(player, (ServerPlayer) new ClientboundRulesPacket(allowOverworldCaveModeOnServer, allowNetherCaveModeOnServer, radarConfig));
        Path propertiesPath = player.level().getServer().getWorldPath(LevelResource.LEVEL_DATA_FILE).getParent().resolve(LevelMapPropertiesIO.FILE_NAME);
        try {
            MinecraftServerData serverData = MinecraftServerData.get(player.getServer());
            LevelMapProperties properties = serverData.getLevelProperties(propertiesPath);
            this.modMain.getMessageHandler().sendToPlayer(player, (ServerPlayer) properties);
        } catch (Throwable t) {
            MinimapLogs.LOGGER.error("suppressed exception", t);
            player.connection.disconnect(Component.translatable("gui.xaero_error_loading_properties"));
        }
    }

    public void handlePlayerTickStart(Player player) {
        if (!this.modMain.isFirstStageLoaded()) {
            return;
        }
        if (player instanceof ServerPlayer) {
            this.modMain.getServerPlayerTickHandler().tick((ServerPlayer) player);
        } else if (HudMod.INSTANCE.getEvents() != null) {
            HudMod.INSTANCE.getEvents().handlePlayerTickStart(player);
        }
    }
}
