package xaero.hud.minimap.element.render.over;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import xaero.common.HudMod;
import xaero.common.minimap.element.render.MinimapElementRendererHandler;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.element.render.MinimapElementRenderer;
import xaero.hud.minimap.element.render.MinimapElementRendererHandler;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/element/render/over/MinimapElementOverMapRendererHandler.class */
public class MinimapElementOverMapRendererHandler extends MinimapElementRendererHandler {
    private double ps;
    private double pc;
    private double zoom;
    private int halfViewW;
    private int halfViewH;
    private int specW;
    private int specH;
    private boolean circle;
    private float optionalScale;
    private final double[] partialTranslate;

    protected MinimapElementOverMapRendererHandler(HudMod modMain, List<MinimapElementRenderer<?, ?>> renderers, double[] partialTranslate) {
        super(modMain, renderers, MinimapElementRenderLocation.OVER_MINIMAP, 9800);
        this.partialTranslate = partialTranslate;
    }

    public void prepareRender(double ps, double pc, double zoom, int specW, int specH, int halfViewW, int halfViewH, boolean circle, float minimapScale) {
        this.ps = ps;
        this.pc = pc;
        this.zoom = zoom;
        this.specW = specW;
        this.specH = specH;
        this.halfViewW = halfViewW;
        this.halfViewH = halfViewH;
        this.circle = circle;
        this.optionalScale = minimapScale;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler
    protected <E, RRC, RR extends MinimapElementRenderer<E, RRC>> boolean transformAndRenderForRenderer(E element, double elementX, double elementY, double elementZ, RR renderer, RRC context, int elementIndex, double optionalDepth, MinimapElementRenderInfo renderInfo, GuiGraphics guiGraphics, MultiBufferSource.BufferSource vanillaBufferSource) {
        PoseStack matrixStack = guiGraphics.pose();
        Vec3 renderPos = renderInfo.renderPos;
        double offx = elementX - renderPos.x;
        double offy = elementZ - renderPos.z;
        matrixStack.pushPose();
        boolean outOfBounds = translatePosition(matrixStack, this.specW, this.specH, this.halfViewW, this.halfViewH, this.ps, this.pc, offx, offy, this.zoom, this.circle, this.partialTranslate);
        boolean result = renderer.renderElement(element, false, outOfBounds, optionalDepth, this.optionalScale, this.partialTranslate[0], this.partialTranslate[1], renderInfo, guiGraphics, vanillaBufferSource);
        matrixStack.popPose();
        return result;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler
    protected void beforeRender(GuiGraphics guiGraphics, MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource) {
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler
    protected void afterRender(GuiGraphics guiGraphics, MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource) {
    }

    public static boolean translatePosition(PoseStack matrixStack, int specW, int specH, int halfViewW, int halfViewH, double ps, double pc, double offx, double offy, double zoom, boolean circle, double[] partialTranslate) {
        boolean outOfBounds = false;
        double Y = ((pc * offx) + (ps * offy)) * zoom;
        double X = ((ps * offx) - (pc * offy)) * zoom;
        double borderedX = X;
        double borderedY = Y;
        if (!circle) {
            if (borderedX > specW) {
                borderedX = specW;
                borderedY = (Y * specW) / X;
                outOfBounds = true;
            } else if (borderedX < (-specW)) {
                borderedX = -specW;
                borderedY = ((-Y) * specW) / X;
                outOfBounds = true;
            }
            if (borderedY > specH) {
                borderedY = specH;
                borderedX = (X * specH) / Y;
                outOfBounds = true;
            } else if (borderedY < (-specH)) {
                borderedY = -specH;
                borderedX = ((-X) * specH) / Y;
                outOfBounds = true;
            }
            if (!outOfBounds && (borderedX > halfViewW || borderedX < (-halfViewW) || borderedY > halfViewH || borderedY < (-halfViewH))) {
                outOfBounds = true;
            }
        } else {
            double distSquared = (borderedX * borderedX) + (borderedY * borderedY);
            double maxDistSquared = specW * specW;
            if (distSquared > maxDistSquared) {
                double scaleDown = Math.sqrt(maxDistSquared / distSquared);
                borderedX *= scaleDown;
                borderedY *= scaleDown;
                outOfBounds = true;
            }
            if (!outOfBounds && distSquared > halfViewW * halfViewW) {
                outOfBounds = true;
            }
        }
        long roundedX = Math.round(borderedX);
        long roundedY = Math.round(borderedY);
        partialTranslate[0] = borderedX - roundedX;
        partialTranslate[1] = borderedY - roundedY;
        matrixStack.translate(roundedX, roundedY, 0.0f);
        return outOfBounds;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/element/render/over/MinimapElementOverMapRendererHandler$Builder.class */
    public static final class Builder extends MinimapElementRendererHandler.Builder {
        @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler.Builder
        protected /* bridge */ /* synthetic */ xaero.hud.minimap.element.render.MinimapElementRendererHandler buildInternally(List list) {
            return buildInternally((List<MinimapElementRenderer<?, ?>>) list);
        }

        @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler.Builder
        public xaero.common.minimap.element.render.over.MinimapElementOverMapRendererHandler build() {
            return (xaero.common.minimap.element.render.over.MinimapElementOverMapRendererHandler) super.build();
        }

        @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler.Builder
        protected xaero.common.minimap.element.render.over.MinimapElementOverMapRendererHandler buildInternally(List<MinimapElementRenderer<?, ?>> renderers) {
            return new xaero.common.minimap.element.render.over.MinimapElementOverMapRendererHandler(HudMod.INSTANCE, renderers, new double[2]);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler.Builder
        public Builder setDefault() {
            super.setDefault();
            return this;
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
