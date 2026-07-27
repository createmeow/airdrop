package xaero.lib.common.event;

import java.util.Iterator;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import xaero.lib.common.config.channel.ConfigChannel;
import xaero.lib.common.config.channel.register.ConfigChannelRegistry;
import xaero.lib.common.player.IServerPlayer;
import xaero.lib.common.player.ServerPlayerData;
import xaero.lib.common.player.ServerPlayerLoginHandler;
import xaero.lib.common.player.ServerPlayerTickHandler;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/event/CommonEvents.class */
public class CommonEvents {
    private final ServerPlayerTickHandler playerTickHandler = new ServerPlayerTickHandler();

    public void clonePlayer(Player from, Player to) {
        if (!(to instanceof IServerPlayer)) {
            return;
        }
        ((IServerPlayer) to).xaerolib_setData(((IServerPlayer) from).xaerolib_getData());
        ((IServerPlayer) to).xaerolib_getData().setPlayer((ServerPlayer) to);
    }

    public void playerLogIn(Player player) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayer serverPlayer = (ServerPlayer) player;
        new ServerPlayerLoginHandler().handle(serverPlayer);
    }

    public void onPlayerTick(boolean isTickStart, boolean isServerSide, Player player) {
        if (!isTickStart || !isServerSide || !(player instanceof ServerPlayer)) {
            return;
        }
        this.playerTickHandler.handle((ServerPlayer) player);
    }

    public void serverStarting(MinecraftServer server) {
        Iterator<ConfigChannel> it = ConfigChannelRegistry.INSTANCE.iterator();
        while (it.hasNext()) {
            ConfigChannel configChannel = it.next();
            configChannel.getServerConfigManager().setServer(server);
        }
    }

    public void onPlayerPermissionChange(Player player) {
        if (!(player instanceof ServerPlayer)) {
            return;
        }
        ServerPlayerData.get((ServerPlayer) player).setShouldUpdateConfigPermissions(true);
    }
}
