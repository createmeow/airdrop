package xaero.common.server.player;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import xaero.common.server.MinecraftServerData;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/server/player/ServerPlayerTickHandler.class */
public class ServerPlayerTickHandler {
    public void tick(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        MinecraftServerData serverData = MinecraftServerData.get(server);
        ServerPlayerData playerData = ServerPlayerData.get(player);
        serverData.getSyncedPlayerTracker().onTick(server, player, serverData, playerData);
    }
}
