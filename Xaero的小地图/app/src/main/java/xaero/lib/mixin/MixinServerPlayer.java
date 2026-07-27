package xaero.lib.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import xaero.lib.common.player.IServerPlayer;
import xaero.lib.common.player.ServerPlayerData;

@Mixin({ServerPlayer.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/mixin/MixinServerPlayer.class */
public class MixinServerPlayer implements IServerPlayer {
    private ServerPlayerData xaerolib_data;

    @Override // xaero.lib.common.player.IServerPlayer
    public ServerPlayerData xaerolib_getData() {
        return this.xaerolib_data;
    }

    @Override // xaero.lib.common.player.IServerPlayer
    public void xaerolib_setData(ServerPlayerData data) {
        this.xaerolib_data = data;
    }
}
