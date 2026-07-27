package xaero.common.mixin;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xaero.common.core.IBufferSource;
import xaero.common.core.XaeroMinimapCore;

@Mixin({MultiBufferSource.BufferSource.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinBufferSource.class */
public class MixinBufferSource implements IBufferSource {
    private RenderType xaero_lastRenderType;

    @Override // xaero.common.core.IBufferSource
    public RenderType getXaero_lastRenderType() {
        return this.xaero_lastRenderType;
    }

    @Override // xaero.common.core.IBufferSource
    public void setXaero_lastRenderType(RenderType lastRenderType) {
        this.xaero_lastRenderType = lastRenderType;
    }

    @ModifyVariable(method = {"getBuffer"}, index = 1, at = @At("HEAD"))
    public RenderType onGetBuffer(RenderType argument) {
        XaeroMinimapCore.onBufferSourceGetBuffer(this, argument);
        return argument;
    }
}
