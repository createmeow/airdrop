package xaero.hud.minimap.element.render.world;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import xaero.common.HudMod;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.element.render.MinimapElementReader;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.element.render.MinimapElementRenderer;
import xaero.hud.minimap.element.render.MinimapElementRendererHandler;
import xaero.hud.minimap.module.MinimapSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/element/render/world/MinimapElementWorldRendererHandler.class */
public class MinimapElementWorldRendererHandler extends MinimapElementRendererHandler {
    private static final float DEFAULT_SCALE = 0.8f;
    private static final float MINECRAFT_SCALE = 0.02666667f;
    private static final double ELEMENT_WORLD_SCALE = 0.02133333496749401d;
    private final PoseStack matrixStackWorld;
    private final Vector4f origin4f;
    private Matrix4f waypointsProjection;
    private Matrix4f worldModelView;
    private int screenWidth;
    private int screenHeight;
    private Object workingClosestHoveredElement;
    private float workingClosestHoveredElementDistance;
    private MinimapElementRenderer<?, ?> workingClosestHoveredElementRenderer;
    private Object previousClosestHoveredElement;
    private MinimapElementRenderer<?, ?> previousClosestHoveredElementRenderer;
    private boolean previousClosestHoveredElementPresent;
    private boolean renderingMainHighlightedElement;

    protected MinimapElementWorldRendererHandler(HudMod modMain, List<MinimapElementRenderer<?, ?>> renderers, PoseStack matrixStackWorld, Vector4f origin4f) {
        super(modMain, renderers, MinimapElementRenderLocation.IN_WORLD, 19499);
        this.matrixStackWorld = matrixStackWorld;
        this.origin4f = origin4f;
    }

    public void prepareRender(Matrix4f waypointsProjection, Matrix4f worldModelView) {
        this.waypointsProjection = waypointsProjection;
        this.worldModelView = worldModelView;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler
    public void render(GuiGraphics guiGraphics, Vec3 renderPos, float partialTicks, RenderTarget framebuffer, double backgroundCoordinateScale, ResourceKey<Level> mapDimension) {
        if (HudMod.INSTANCE.getSupportMods().vivecraft) {
            return;
        }
        this.renderingMainHighlightedElement = false;
        super.render(guiGraphics, renderPos, partialTicks, framebuffer, backgroundCoordinateScale, mapDimension);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler
    protected <E, RRC, RR extends MinimapElementRenderer<E, RRC>> boolean transformAndRenderForRenderer(E element, double elementX, double elementY, double elementZ, RR renderer, RRC context, int elementIndex, double optionalDepth, MinimapElementRenderInfo renderInfo, GuiGraphics guiGraphics, MultiBufferSource.BufferSource vanillaBufferSource) {
        PoseStack matrixStackOverlay = guiGraphics.pose();
        float partialTicks = renderInfo.partialTicks;
        Vec3 renderPos = renderInfo.renderPos;
        MinimapElementReader<E, RRC> elementReader = renderer.getElementReader();
        double offX = elementX - renderPos.x;
        double offY = elementReader.getRenderY(element, context, partialTicks) - renderPos.y;
        double offZ = elementZ - renderPos.z;
        Vector3f lookVector = Minecraft.getInstance().gameRenderer.getMainCamera().getLookVector().get(new Vector3f());
        double depth = (offX * lookVector.x()) + (offY * lookVector.y()) + (offZ * lookVector.z());
        if (depth < 0.05d) {
            return false;
        }
        if (!this.renderingMainHighlightedElement && element == this.previousClosestHoveredElement) {
            this.previousClosestHoveredElementPresent = true;
            return false;
        }
        double distance = Math.sqrt((offX * offX) + (offY * offY) + (offZ * offZ));
        if (distance > 250000.0d) {
            double offScaler = 250000.0d / distance;
            offX *= offScaler;
            offY *= offScaler;
            offZ *= offScaler;
        }
        matrixStackOverlay.pushPose();
        this.matrixStackWorld.pushPose();
        this.matrixStackWorld.translate(offX, offY, offZ);
        this.origin4f.mul(this.matrixStackWorld.last().pose());
        this.matrixStackWorld.popPose();
        this.origin4f.mul(this.waypointsProjection);
        float translateX = ((1.0f + (this.origin4f.x() / this.origin4f.w())) / 2.0f) * this.screenWidth;
        float translateY = ((1.0f - (this.origin4f.y() / this.origin4f.w())) / 2.0f) * this.screenHeight;
        this.origin4f.set(0.0f, 0.0f, 0.0f, 1.0f);
        int roundedX = Math.round(translateX);
        int roundedY = Math.round(translateY);
        boolean outOfBounds = roundedX < 0 || roundedY < 0 || roundedX >= this.screenWidth || roundedY >= this.screenHeight;
        boolean renderingHoveredElement = isElementHovered(element, roundedX, roundedY, elementReader, context, renderInfo);
        double partialX = translateX - roundedX;
        double partialY = translateY - roundedY;
        matrixStackOverlay.translate(roundedX, roundedY, 0.0f);
        boolean highlighted = this.renderingMainHighlightedElement;
        boolean highlighted2 = highlighted || (renderingHoveredElement && elementReader.isAlwaysHighlightedWhenHovered(element, context));
        boolean result = renderer.renderElement(element, highlighted2, outOfBounds, optionalDepth, 1.0f, partialX, partialY, renderInfo, guiGraphics, vanillaBufferSource);
        matrixStackOverlay.popPose();
        if (result && renderingHoveredElement) {
            handleClosestHovered(element, renderer, roundedX, roundedY);
        }
        return result;
    }

    private <E, RRC> boolean isElementHovered(E element, int roundedX, int roundedY, MinimapElementReader<E, RRC> elementReader, RRC context, MinimapElementRenderInfo renderInfo) {
        if (!elementReader.isInteractable(this.location, element)) {
            return false;
        }
        float partialTicks = renderInfo.partialTicks;
        int interactionLeft = elementReader.getInteractionBoxLeft(element, context, partialTicks);
        int interactionRight = elementReader.getInteractionBoxRight(element, context, partialTicks);
        int interactionTop = elementReader.getInteractionBoxTop(element, context, partialTicks);
        int interactionBottom = elementReader.getInteractionBoxBottom(element, context, partialTicks);
        double boxScale = elementReader.getBoxScale(this.location, element, context);
        if (boxScale != 1.0d) {
            interactionLeft = (int) (interactionLeft * boxScale);
            interactionRight = (int) (interactionRight * boxScale);
            interactionTop = (int) (interactionTop * boxScale);
            interactionBottom = (int) (interactionBottom * boxScale);
        }
        int centerX = this.screenWidth / 2;
        if (centerX - roundedX < interactionLeft || centerX - roundedX >= interactionRight) {
            return false;
        }
        int centerY = this.screenHeight / 2;
        return centerY - roundedY >= interactionTop && centerY - roundedY < interactionBottom;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <E, RRC, RR extends MinimapElementRenderer<E, RRC>> void handleClosestHovered(E element, RR rr, int roundedX, int roundedY) {
        int centerX = this.screenWidth / 2;
        int centerY = this.screenHeight / 2;
        int screenOffX = roundedX - centerX;
        int screenOffY = roundedY - centerY;
        float squaredScreenDistance = (screenOffX * screenOffX) + (screenOffY * screenOffY);
        if (this.workingClosestHoveredElement == null || squaredScreenDistance < this.workingClosestHoveredElementDistance || (element == this.previousClosestHoveredElement && squaredScreenDistance <= this.workingClosestHoveredElementDistance)) {
            this.workingClosestHoveredElement = element;
            this.workingClosestHoveredElementDistance = squaredScreenDistance;
            this.workingClosestHoveredElementRenderer = rr;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <E, RR extends MinimapElementRenderer<E, RRC>, RRC> void renderMainHighlightedElement(MinimapElementRenderInfo renderInfo, GuiGraphics guiGraphics, MultiBufferSource.BufferSource vanillaBufferSource) {
        if (!this.previousClosestHoveredElementPresent) {
            return;
        }
        PoseStack matrixStack = guiGraphics.pose();
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers = session.getMultiTextureRenderTypeRenderers();
        Object obj = this.previousClosestHoveredElement;
        MinimapElementRenderer<?, ?> minimapElementRenderer = this.previousClosestHoveredElementRenderer;
        this.renderingMainHighlightedElement = true;
        minimapElementRenderer.preRender(renderInfo, vanillaBufferSource, multiTextureRenderTypeRenderers);
        boolean result = transformAndRenderForRenderer(obj, minimapElementRenderer, minimapElementRenderer.getContext(), 0, 0.0d, renderInfo, guiGraphics, vanillaBufferSource);
        minimapElementRenderer.postRender(renderInfo, vanillaBufferSource, multiTextureRenderTypeRenderers);
        this.renderingMainHighlightedElement = false;
        this.previousClosestHoveredElementPresent = false;
        if (!result) {
            return;
        }
        matrixStack.translate(0.0d, 0.0d, getElementIndexDepth(1, 1));
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler
    protected void beforeRender(GuiGraphics guiGraphics, MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource) {
        this.screenWidth = Minecraft.getInstance().getWindow().getWidth();
        this.screenHeight = Minecraft.getInstance().getWindow().getHeight();
        this.matrixStackWorld.pushPose();
        this.matrixStackWorld.last().pose().mul(this.worldModelView);
        PoseStack matrixStackOverlay = guiGraphics.pose();
        matrixStackOverlay.pushPose();
        matrixStackOverlay.translate(0.0f, 0.0f, -2980.0f);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRendererHandler
    protected void afterRender(GuiGraphics guiGraphics, MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource) {
        renderMainHighlightedElement(renderInfo, guiGraphics, vanillaBufferSource);
        this.previousClosestHoveredElement = this.workingClosestHoveredElement;
        this.previousClosestHoveredElementRenderer = this.workingClosestHoveredElementRenderer;
        this.workingClosestHoveredElement = null;
        this.workingClosestHoveredElementRenderer = null;
        PoseStack matrixStackOverlay = guiGraphics.pose();
        matrixStackOverlay.popPose();
        this.matrixStackWorld.popPose();
        CustomRenderTypes.DEPTH_CLEAR.setupRenderState();
        RenderSystem.clear(256, Minecraft.ON_OSX);
        CustomRenderTypes.DEPTH_CLEAR.clearRenderState();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/element/render/world/MinimapElementWorldRendererHandler$Builder.class */
    public static final class Builder {
        public MinimapElementWorldRendererHandler build() {
            List<MinimapElementRenderer<?, ?>> renderers = new ArrayList<>();
            return new MinimapElementWorldRendererHandler(HudMod.INSTANCE, renderers, new PoseStack(), new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
        }

        protected Builder setDefault() {
            return this;
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
