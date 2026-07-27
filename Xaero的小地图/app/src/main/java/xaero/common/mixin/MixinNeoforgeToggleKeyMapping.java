package xaero.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.ToggleKeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xaero.common.core.XaeroMinimapCore;

@Mixin({ToggleKeyMapping.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinNeoforgeToggleKeyMapping.class */
public class MixinNeoforgeToggleKeyMapping {
    @ModifyReturnValue(method = {"isDown"}, at = {@At("RETURN")})
    public boolean onIsDown(boolean original) throws Exception {
        if (XaeroMinimapCore.onToggleKeyIsDown((ToggleKeyMapping) this)) {
            return true;
        }
        return original;
    }
}
