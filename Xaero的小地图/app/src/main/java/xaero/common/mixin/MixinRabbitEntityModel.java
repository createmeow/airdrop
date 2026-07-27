package xaero.common.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.RabbitModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.core.XaeroMinimapCore;

@Mixin({RabbitModel.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinRabbitEntityModel.class */
public class MixinRabbitEntityModel {
    @Inject(at = {@At("HEAD")}, method = {"renderToBuffer"})
    public void onRender(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color, CallbackInfo info) {
        XaeroMinimapCore.onEntityIconsModelRenderDetection((EntityModel) this, vertices, color);
    }
}
