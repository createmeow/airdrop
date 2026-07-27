package xaero.common.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.common.core.XaeroMinimapCore;

@Mixin({ModelPart.class})
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/mixin/MixinModelPart.class */
public class MixinModelPart {
    @Inject(at = {@At("HEAD")}, method = {"compile(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"})
    public void onRender(PoseStack.Pose matrices, VertexConsumer vertexConsumer, int light, int overlay, int color, CallbackInfo info) {
        XaeroMinimapCore.onEntityIconsModelPartRenderDetection((ModelPart) this, color);
    }
}
