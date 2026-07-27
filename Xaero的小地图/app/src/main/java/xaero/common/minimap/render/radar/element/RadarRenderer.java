package xaero.common.minimap.render.radar.element;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRenderer;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.radar.MinimapRadar;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.Minimap;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.radar.category.EntityRadarCategory;
import xaero.hud.minimap.radar.icon.RadarIconManager;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/render/radar/element/RadarRenderer.class */
public final class RadarRenderer extends xaero.hud.minimap.radar.render.element.RadarRenderer {
    private MinimapElementRenderInfo compatibleRenderInfo;

    public RadarRenderer(RadarIconManager radarIconManager, Minimap minimap, xaero.hud.minimap.radar.render.element.RadarElementReader elementReader, xaero.hud.minimap.radar.render.element.RadarRenderProvider provider, xaero.hud.minimap.radar.render.element.RadarRenderContext context) {
        super(radarIconManager, minimap, elementReader, provider, context);
    }

    @Override // xaero.hud.minimap.radar.render.element.RadarRenderer, xaero.common.minimap.element.render.MinimapElementRenderer, xaero.hud.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public void preRender(MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        super.preRender(renderInfo, vanillaBufferSource, multiTextureRenderTypeRenderers);
    }

    @Override // xaero.hud.minimap.radar.render.element.RadarRenderer, xaero.common.minimap.element.render.MinimapElementRenderer, xaero.hud.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public void postRender(MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        super.postRender(renderInfo, vanillaBufferSource, multiTextureRenderTypeRenderers);
    }

    @Override // xaero.hud.minimap.radar.render.element.RadarRenderer, xaero.common.minimap.element.render.MinimapElementRenderer, xaero.hud.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public boolean renderElement(Entity e, boolean highlighted, boolean outOfBounds, double optionalDepth, float optionalScale, double partialX, double partialY, MinimapElementRenderInfo renderInfo, GuiGraphics guiGraphics, MultiBufferSource.BufferSource vanillaBufferSource) {
        return super.renderElement(e, highlighted, outOfBounds, optionalDepth, optionalScale, partialX, partialY, renderInfo, guiGraphics, vanillaBufferSource);
    }

    @Deprecated
    public void renderEntityDotToFBO(int location, boolean highlit, GuiGraphics guiGraphics, MinimapProcessor minimap, Player p, Entity renderEntity, Entity e, float partial, boolean name, boolean icon, MinimapRadar minimapRadar, int style, boolean smooth, boolean debug, boolean debugEntityVariantIds, boolean cave, double dotNameScale, MultiBufferSource.BufferSource textRenderTypeBuffer, RenderType dotsRenderType, VertexConsumer dotsBufferBuilder, MultiTextureRenderTypeRenderer iconsRenderer, VertexConsumer nameBgBuilder, int dotIndex, boolean displayNameWhenIconFails, int heightLimit, boolean heightBasedFade, int startFadingAt, double iconScale, int dotSize, int colorIndex, int displayY, EntityRadarCategory category, MinimapRendererHelper helper, Font font, RenderTarget framebuffer, float minimapScale) {
        renderEntityDotToFBO(MinimapElementRenderLocation.fromIndex(location), highlit, guiGraphics, minimap, p, renderEntity, e, partial, name, icon, minimapRadar, style, smooth, debug, debugEntityVariantIds, cave, dotNameScale, textRenderTypeBuffer, dotsRenderType, dotsBufferBuilder, iconsRenderer, nameBgBuilder, dotIndex, displayNameWhenIconFails, heightLimit, heightBasedFade, startFadingAt, iconScale, dotSize, colorIndex, displayY, category, helper, font, framebuffer, minimapScale);
    }

    @Deprecated
    public void renderEntityDotToFBO(MinimapElementRenderLocation location, boolean highlit, GuiGraphics guiGraphics, MinimapProcessor minimap, Player p, Entity renderEntity, Entity e, float partial, boolean name, boolean icon, MinimapRadar minimapRadar, int style, boolean smooth, boolean debug, boolean debugEntityVariantIds, boolean cave, double dotNameScale, MultiBufferSource.BufferSource textRenderTypeBuffer, RenderType dotsRenderType, VertexConsumer dotsBufferBuilder, MultiTextureRenderTypeRenderer iconsRenderer, VertexConsumer nameBgBuilder, int dotIndex, boolean displayNameWhenIconFails, int heightLimit, boolean heightBasedFade, int startFadingAt, double iconScale, int dotSize, int colorIndex, int displayY, EntityRadarCategory category, MinimapRendererHelper helper, Font font, RenderTarget framebuffer, float minimapScale) {
        super.renderSingleEntity(e, cave, highlit, location == MinimapElementRenderLocation.OVER_MINIMAP ? 2.0f : 1.0f, icon, name || displayY > 0, location, framebuffer, guiGraphics);
    }

    @Override // xaero.hud.minimap.radar.render.element.RadarRenderer, xaero.common.minimap.element.render.MinimapElementRenderer, xaero.hud.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public boolean shouldRender(MinimapElementRenderLocation location) {
        return super.shouldRender(location);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public boolean renderElement(int location, boolean highlit, boolean outOfBounds, GuiGraphics guiGraphics, MultiBufferSource.BufferSource renderTypeBuffers, Font font, RenderTarget framebuffer, MinimapRendererHelper helper, Entity renderEntity, Player player, double renderX, double renderY, double renderZ, int elementIndex, double optionalDepth, float optionalScale, Entity element, double partialX, double partialY, boolean cave, float partialTicks) {
        if (this.compatibleRenderInfo == null) {
            MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
            this.compatibleRenderInfo = new MinimapElementRenderInfo(MinimapElementRenderLocation.fromIndex(location), renderEntity, player, new Vec3(renderX, renderY, renderZ), cave, partialTicks, framebuffer, session.getProcessor().getLastMapDimensionScale(), session.getProcessor().getLastMapDimension());
        }
        return renderElement(element, highlit, outOfBounds, optionalDepth, optionalScale, partialX, partialY, this.compatibleRenderInfo, guiGraphics, renderTypeBuffers);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public void preRender(int location, Entity renderEntity, Player player, double renderX, double renderY, double renderZ, IXaeroMinimap modMain, MultiBufferSource.BufferSource renderTypeBuffers, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        preRender(getPreInfo(location, renderEntity, player, renderX, renderY, renderZ), renderTypeBuffers, multiTextureRenderTypeRenderers);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public void postRender(int location, Entity renderEntity, Player player, double renderX, double renderY, double renderZ, IXaeroMinimap modMain, MultiBufferSource.BufferSource renderTypeBuffers, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        if (this.compatibleRenderInfo == null) {
            this.compatibleRenderInfo = getPreInfo(location, renderEntity, player, renderX, renderY, renderZ);
        }
        postRender(this.compatibleRenderInfo, renderTypeBuffers, multiTextureRenderTypeRenderers);
        this.compatibleRenderInfo = null;
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public boolean shouldRender(int location) {
        return shouldRender(MinimapElementRenderLocation.fromIndex(location));
    }
}
