package xaero.lib.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundInitializeBorderPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.lib.XaeroLib;
import xaero.lib.client.level.ClientLevelData;

@Mixin({ClientPacketListener.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/mixin/MixinClientPacketListener.class */
public class MixinClientPacketListener {
    @Inject(at = {@At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V")}, method = {"handleInitializeBorder"})
    public void onHandleInitializeBorder(ClientboundInitializeBorderPacket clientboundInitializeBorderPacket, CallbackInfo info) {
        if (!XaeroLib.isLoaded()) {
            return;
        }
        ClientLevelData clientLevelData = ClientLevelData.get(Minecraft.getInstance().level);
        if (!clientLevelData.serverHasMod()) {
            XaeroLib xaeroLib = XaeroLib.INSTANCE;
            XaeroLib.LOGGER.warn("Server side doesn't have XaeroLib installed! Resetting.");
            XaeroLib.INSTANCE.getClient().getConfigSynchronizer().reset();
        }
    }
}
