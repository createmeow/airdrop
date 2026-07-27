package xaero.common.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.core.XaeroMinimapCore;

@Mixin({LocalPlayer.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinClientPlayerEntity.class */
public class MixinClientPlayerEntity {
    @Inject(at = {@At("HEAD")}, method = {"respawn"})
    public void onTickStart(CallbackInfo info) {
        XaeroMinimapCore.beforeRespawn((Player) this);
    }
}
