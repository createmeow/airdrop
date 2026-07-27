package xaero.common.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.server.core.XaeroMinimapServerCore;

@Mixin({PlayerList.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinPlayerList.class */
public class MixinPlayerList {
    @Inject(at = {@At("HEAD")}, method = {"sendLevelInfo"})
    public void onSendWorldInfo(ServerPlayer player, ServerLevel world, CallbackInfo info) {
        XaeroMinimapServerCore.onServerWorldInfo(player);
    }
}
