package xaero.common.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import xaero.common.server.player.IServerPlayer;
import xaero.common.server.player.ServerPlayerData;

@Mixin({ServerPlayer.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinServerPlayer.class */
public class MixinServerPlayer implements IServerPlayer {
    private ServerPlayerData xaeroMinimapPlayerData;

    @Override // xaero.common.server.player.IServerPlayer
    public ServerPlayerData getXaeroMinimapPlayerData() {
        return this.xaeroMinimapPlayerData;
    }

    @Override // xaero.common.server.player.IServerPlayer
    public void setXaeroMinimapPlayerData(ServerPlayerData data) {
        this.xaeroMinimapPlayerData = data;
    }
}
