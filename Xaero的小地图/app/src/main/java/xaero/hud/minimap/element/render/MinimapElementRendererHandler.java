package xaero.hud.minimap.element.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import xaero.common.HudMod;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.misc.OptimizedMath;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.module.MinimapSession;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/element/render/MinimapElementRendererHandler.class */
public abstract class MinimapElementRendererHandler {
    private final HudMod modMain;
    private final List<MinimapElementRenderer<?, ?>> renderers;
    protected final MinimapElementRenderLocation location;
    private final int indexLimit;

    protected abstract <E, RRC, RR extends MinimapElementRenderer<E, RRC>> boolean transformAndRenderForRenderer(E e, double d, double d2, double d3, RR rr, RRC rrc, int i, double d4, MinimapElementRenderInfo minimapElementRenderInfo, GuiGraphics guiGraphics, MultiBufferSource.BufferSource bufferSource);

    protected abstract void beforeRender(GuiGraphics guiGraphics, MinimapElementRenderInfo minimapElementRenderInfo, MultiBufferSource.BufferSource bufferSource);

    protected abstract void afterRender(GuiGraphics guiGraphics, MinimapElementRenderInfo minimapElementRenderInfo, MultiBufferSource.BufferSource bufferSource);

    protected MinimapElementRendererHandler(HudMod modMain, List<MinimapElementRenderer<?, ?>> renderers, MinimapElementRenderLocation location, int indexLimit) {
        this.modMain = modMain;
        this.renderers = renderers;
        this.location = location;
        this.indexLimit = indexLimit;
    }

    public void add(MinimapElementRenderer<?, ?> renderer) {
        this.renderers.add(renderer);
        Collections.sort(this.renderers);
    }

    public void render(GuiGraphics guiGraphics, Vec3 renderPos, float partialTicks, RenderTarget framebuffer, double backgroundCoordinateScale, ResourceKey<Level> mapDimension) {
        Minecraft mc = Minecraft.getInstance();
        Entity renderEntity = mc.getCameraEntity();
        LocalPlayer localPlayer = mc.player;
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers = session.getMultiTextureRenderTypeRenderers();
        MultiBufferSource.BufferSource vanillaBufferSource = guiGraphics.bufferSource();
        boolean cave = session.getProcessor().isCaveModeDisplayed();
        MinimapElementRenderInfo renderInfo = new MinimapElementRenderInfo(this.location, renderEntity, localPlayer, renderPos, cave, partialTicks, framebuffer, backgroundCoordinateScale, mapDimension);
        PoseStack matrixStack = guiGraphics.pose();
        beforeRender(guiGraphics, renderInfo, vanillaBufferSource);
        int indexLimit = getIndexLimit();
        for (int i = 0; i < this.renderers.size(); i++) {
            MinimapElementRenderer<?, ?> renderer = this.renderers.get(i);
            int elementIndex = renderForRenderer(renderer, guiGraphics, 0, multiTextureRenderTypeRenderers, indexLimit, renderInfo);
            matrixStack.translate(0.0d, 0.0d, getElementIndexDepth(elementIndex, indexLimit));
            indexLimit -= elementIndex;
            if (indexLimit < 0) {
                indexLimit = 0;
            }
        }
        afterRender(guiGraphics, renderInfo, vanillaBufferSource);
    }

    protected <E, RRC, RR extends MinimapElementRenderer<E, RRC>> int renderForRenderer(RR renderer, GuiGraphics guiGraphics, int elementIndex, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers, int indexLimit, MinimapElementRenderInfo renderInfo) {
        MinimapElementRenderLocation location = this.location;
        if (!renderer.shouldRender(location)) {
            return elementIndex;
        }
        MultiBufferSource.BufferSource vanillaBufferSource = guiGraphics.bufferSource();
        MinimapElementReader<E, RC> minimapElementReader = renderer.elementReader;
        MinimapElementRenderProvider<E, RC> minimapElementRenderProvider = renderer.provider;
        RC rc = renderer.context;
        renderer.preRender(renderInfo, vanillaBufferSource, multiTextureRenderTypeRenderers);
        minimapElementRenderProvider.begin(location, rc);
        while (minimapElementRenderProvider.hasNext(location, rc)) {
            E e = minimapElementRenderProvider.setupContextAndGetNext(location, rc);
            if (e != null && !minimapElementReader.isHidden(e, rc)) {
                double optionalDepth = getElementIndexDepth(elementIndex, indexLimit);
                if (transformAndRenderForRenderer(e, renderer, rc, elementIndex, optionalDepth, renderInfo, guiGraphics, vanillaBufferSource)) {
                    elementIndex++;
                }
            }
        }
        minimapElementRenderProvider.end(location, rc);
        renderer.postRender(renderInfo, vanillaBufferSource, multiTextureRenderTypeRenderers);
        return elementIndex;
    }

    protected <E, RRC, RR extends MinimapElementRenderer<E, RRC>> boolean transformAndRenderForRenderer(E element, RR renderer, RRC context, int elementIndex, double optionalDepth, MinimapElementRenderInfo renderInfo, GuiGraphics guiGraphics, MultiBufferSource.BufferSource vanillaBufferSource) {
        double elementX;
        double elementZ;
        MinimapElementReader<E, RC> minimapElementReader = renderer.elementReader;
        double elementX2 = minimapElementReader.getRenderX(element, context, renderInfo.partialTicks);
        double elementY = minimapElementReader.getRenderY(element, context, renderInfo.partialTicks);
        double elementZ2 = minimapElementReader.getRenderZ(element, context, renderInfo.partialTicks);
        double elementCoordinateScale = minimapElementReader.getCoordinateScale(element, context, renderInfo);
        double coordinateMultiplier = elementCoordinateScale / renderInfo.backgroundCoordinateScale;
        if (coordinateMultiplier == 1.0d) {
            return transformAndRenderForRenderer(element, elementX2, elementY, elementZ2, renderer, context, elementIndex, optionalDepth, renderInfo, guiGraphics, vanillaBufferSource);
        }
        if (minimapElementReader.shouldScalePartialCoordinates(element, context, renderInfo)) {
            elementX = elementX2 * coordinateMultiplier;
            elementZ = elementZ2 * coordinateMultiplier;
        } else {
            int flooredRenderX = OptimizedMath.myFloor(elementX2);
            int flooredRenderZ = OptimizedMath.myFloor(elementZ2);
            elementX = OptimizedMath.myFloor(flooredRenderX * coordinateMultiplier) + (elementX2 - flooredRenderX);
            elementZ = OptimizedMath.myFloor(flooredRenderZ * coordinateMultiplier) + (elementZ2 - flooredRenderZ);
        }
        return transformAndRenderForRenderer(element, elementX, elementY, elementZ, renderer, context, elementIndex, optionalDepth, renderInfo, guiGraphics, vanillaBufferSource);
    }

    protected double getElementIndexDepth(int elementIndex, int indexLimit) {
        return (elementIndex >= indexLimit ? indexLimit : elementIndex) * 0.1d;
    }

    protected int getIndexLimit() {
        return this.indexLimit;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/element/render/MinimapElementRendererHandler$Builder.class */
    public static abstract class Builder {
        protected abstract MinimapElementRendererHandler buildInternally(List<MinimapElementRenderer<?, ?>> list);

        protected Builder() {
        }

        protected Builder setDefault() {
            return this;
        }

        public MinimapElementRendererHandler build() {
            return buildInternally(new ArrayList());
        }
    }
}
