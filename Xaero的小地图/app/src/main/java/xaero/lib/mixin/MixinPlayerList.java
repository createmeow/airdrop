package xaero.lib.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.lib.XaeroLib;
import xaero.lib.common.packet.ClientboundDimensionHandshakePacket;

@Mixin({PlayerList.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/mixin/MixinPlayerList.class */
public class MixinPlayerList {
    @Inject(at = {@At("HEAD")}, method = {"sendLevelInfo"})
    public void onSendLevelInfo(ServerPlayer player, ServerLevel world, CallbackInfo info) {
        if (!XaeroLib.isLoaded()) {
            return;
        }
        XaeroLib.INSTANCE.getPacketHandler().sendToPlayer(player, (ServerPlayer) new ClientboundDimensionHandshakePacket());
    }
}
