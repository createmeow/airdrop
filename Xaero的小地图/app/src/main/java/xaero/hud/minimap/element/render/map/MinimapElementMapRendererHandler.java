package xaero.hud.minimap.element.render.map;

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

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/element/render/map/MinimapElementMapRendererHandler.class */
public class MinimapElementMapRendererHandler extends MinimapElementRendererHandler {
    private double ps;
    private double pc;
    private double zoom;
    private float halfWView;

    protected MinimapElementMapRendererHandler(HudMod modMain, List<MinimapElementRenderer<?, ?>> renderers) {
        super(modMain, renderers, MinimapElementRenderLocation.IN_MINIMAP, 19490);
    }

    public void prepareRender(double ps, double pc, double zoom, float halfWView) {
        this.ps = ps;
        this.pc = pc;
        this.zoom = zoom;
        this.halfWView = halfWView;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler
    protected <E, RRC, RR extends MinimapElementRenderer<E, RRC>> boolean transformAndRenderForRenderer(E element, double elementX, double elementY, double elementZ, RR renderer, RRC context, int elementIndex, double optionalDepth, MinimapElementRenderInfo renderInfo, GuiGraphics guiGraphics, MultiBufferSource.BufferSource vanillaBufferSource) {
        PoseStack matrixStack = guiGraphics.pose();
        Vec3 renderPos = renderInfo.renderPos;
        double offx = elementX - renderPos.x;
        double offz = elementZ - renderPos.z;
        matrixStack.pushPose();
        double zoomedOffX = offx * this.zoom;
        double zoomedOffZ = offz * this.zoom;
        double translateX = (this.ps * zoomedOffX) - (this.pc * zoomedOffZ);
        double translateY = (this.pc * zoomedOffX) + (this.ps * zoomedOffZ);
        int roundedX = (int) Math.round(translateX);
        int roundedY = (int) Math.round(translateY);
        boolean outOfBounds = ((float) Math.abs(roundedX)) > this.halfWView || ((float) Math.abs(roundedY)) > this.halfWView;
        double partialX = translateX - roundedX;
        double partialY = translateY - roundedY;
        matrixStack.translate(roundedX, roundedY, 0.0f);
        boolean result = renderer.renderElement(element, false, outOfBounds, optionalDepth, 1.0f, partialX, partialY, renderInfo, guiGraphics, vanillaBufferSource);
        matrixStack.popPose();
        return result;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler
    protected void beforeRender(GuiGraphics guiGraphics, MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource) {
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler
    protected void afterRender(GuiGraphics guiGraphics, MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource) {
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/element/render/map/MinimapElementMapRendererHandler$Builder.class */
    public static final class Builder extends MinimapElementRendererHandler.Builder {
        @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler.Builder
        protected /* bridge */ /* synthetic */ xaero.hud.minimap.element.render.MinimapElementRendererHandler buildInternally(List list) {
            return buildInternally((List<MinimapElementRenderer<?, ?>>) list);
        }

        @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler.Builder
        public MinimapElementMapRendererHandler build() {
            return (MinimapElementMapRendererHandler) super.build();
        }

        @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler.Builder
        protected MinimapElementMapRendererHandler buildInternally(List<MinimapElementRenderer<?, ?>> renderers) {
            return new xaero.common.minimap.element.render.map.MinimapElementMapRendererHandler(HudMod.INSTANCE, renderers);
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
