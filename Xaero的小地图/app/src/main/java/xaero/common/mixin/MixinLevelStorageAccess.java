package xaero.common.mixin;

import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.core.XaeroMinimapCore;

@Mixin({LevelStorageSource.LevelStorageAccess.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinLevelStorageAccess.class */
public class MixinLevelStorageAccess {
    @Inject(at = {@At("RETURN")}, method = {"deleteLevel"}, cancellable = false)
    public void onDeleteLevel(CallbackInfo info) throws InterruptedException {
        XaeroMinimapCore.onDeleteWorld((LevelStorageSource.LevelStorageAccess) this);
    }
}
