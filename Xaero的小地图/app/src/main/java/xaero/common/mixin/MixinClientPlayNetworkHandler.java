package xaero.common.mixin;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.common.XaeroMinimapSession;
import xaero.common.core.IXaeroMinimapClientPlayNetHandler;
import xaero.common.core.XaeroMinimapCore;

@Mixin({ClientPacketListener.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinClientPlayNetworkHandler.class */
public class MixinClientPlayNetworkHandler implements IXaeroMinimapClientPlayNetHandler {
    XaeroMinimapSession xaero_minimapSession;

    @Inject(at = {@At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V")}, method = {"handleChunkBlocksUpdate"})
    public void onOnChunkDeltaUpdate(ClientboundSectionBlocksUpdatePacket packet, CallbackInfo info) throws IllegalAccessException, IllegalArgumentException {
        XaeroMinimapCore.onMultiBlockChange(packet);
    }

    @Inject(at = {@At("HEAD")}, method = {"updateLevelChunk"})
    public void onOnChunkData(int x, int z, ClientboundLevelChunkPacketData packet, CallbackInfo info) throws IllegalAccessException, IllegalArgumentException {
        XaeroMinimapCore.onChunkData(x, z, packet);
    }

    @Inject(at = {@At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V")}, method = {"handleLevelChunkWithLight"})
    public void onHandleLevelChunkWithLight(ClientboundLevelChunkWithLightPacket packet, CallbackInfo info) throws IllegalAccessException, IllegalArgumentException {
        XaeroMinimapCore.onHandleLevelChunkWithLight(packet);
    }

    @Inject(at = {@At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V")}, method = {"handleLightUpdatePacket"})
    public void onHandleLightUpdatePacket(ClientboundLightUpdatePacket packet, CallbackInfo info) throws IllegalAccessException, IllegalArgumentException {
        XaeroMinimapCore.onHandleLightUpdatePacket(packet);
    }

    @Inject(at = {@At("HEAD")}, method = {"queueLightRemoval"})
    public void onQueueLightRemoval(ClientboundForgetLevelChunkPacket packet, CallbackInfo info) throws IllegalAccessException, IllegalArgumentException {
        XaeroMinimapCore.onQueueLightRemoval(packet);
    }

    @Inject(at = {@At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V")}, method = {"handleBlockUpdate"})
    public void onOnBlockUpdate(ClientboundBlockUpdatePacket packet, CallbackInfo info) throws IllegalAccessException, IllegalArgumentException {
        XaeroMinimapCore.onBlockChange(packet);
    }

    @Inject(at = {@At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V")}, method = {"handleSetSpawn"})
    public void onOnPlayerSpawnPosition(ClientboundSetDefaultSpawnPositionPacket packet, CallbackInfo info) {
        XaeroMinimapCore.onSpawn(packet);
    }

    @Override // xaero.common.core.IXaeroMinimapClientPlayNetHandler
    public XaeroMinimapSession getXaero_minimapSession() {
        return this.xaero_minimapSession;
    }

    @Override // xaero.common.core.IXaeroMinimapClientPlayNetHandler
    public void setXaero_minimapSession(XaeroMinimapSession session) {
        this.xaero_minimapSession = session;
    }

    @Inject(at = {@At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V")}, method = {"handleLogin"})
    public void onOnGameJoin(ClientboundLoginPacket packet, CallbackInfo info) {
        XaeroMinimapCore.onPlayNetHandler((ClientPacketListener) this, packet);
    }

    @Inject(at = {@At("HEAD")}, method = {"close"})
    public void onClose(CallbackInfo info) {
        XaeroMinimapCore.onPlayNetHandlerCleanup((ClientPacketListener) this);
    }

    @Inject(at = {@At("HEAD")}, method = {"sendCommand(Ljava/lang/String;)V"}, cancellable = true)
    public void onSendCommand(String string_1, CallbackInfo info) {
        if (XaeroMinimapCore.onLocalPlayerCommand(string_1)) {
            info.cancel();
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"sendUnsignedCommand(Ljava/lang/String;)Z"}, cancellable = true)
    public void onSendUnsignedCommand(String string_1, CallbackInfoReturnable<Boolean> info) {
        if (XaeroMinimapCore.onLocalPlayerCommand(string_1)) {
            info.setReturnValue(true);
        }
    }
}
