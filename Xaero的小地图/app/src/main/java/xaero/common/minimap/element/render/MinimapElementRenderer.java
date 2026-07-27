package xaero.common.minimap.element.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/element/render/MinimapElementRenderer.class */
public abstract class MinimapElementRenderer<E, RC> extends xaero.hud.minimap.element.render.MinimapElementRenderer<E, RC> {

    @Deprecated
    protected final MinimapElementReader<E, RC> elementReader;

    @Deprecated
    protected final MinimapElementRenderProvider<E, RC> provider;

    @Deprecated
    public abstract boolean renderElement(int i, boolean z, boolean z2, GuiGraphics guiGraphics, MultiBufferSource.BufferSource bufferSource, Font font, RenderTarget renderTarget, MinimapRendererHelper minimapRendererHelper, Entity entity, Player player, double d, double d2, double d3, int i2, double d4, float f, E e, double d5, double d6, boolean z3, float f2);

    @Deprecated
    public abstract void preRender(int i, Entity entity, Player player, double d, double d2, double d3, IXaeroMinimap iXaeroMinimap, MultiBufferSource.BufferSource bufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRendererProvider);

    @Deprecated
    public abstract void postRender(int i, Entity entity, Player player, double d, double d2, double d3, IXaeroMinimap iXaeroMinimap, MultiBufferSource.BufferSource bufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRendererProvider);

    @Deprecated
    public abstract boolean shouldRender(int i);

    @Deprecated
    public MinimapElementRenderer(MinimapElementReader<E, RC> elementReader, MinimapElementRenderProvider<E, RC> provider, RC context) {
        super(elementReader, provider, context);
        this.elementReader = getElementReader();
        this.provider = getProvider();
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public MinimapElementRenderProvider<E, RC> getProvider() {
        return (MinimapElementRenderProvider) super.getProvider();
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    @Deprecated
    public MinimapElementReader<E, RC> getElementReader() {
        return (MinimapElementReader) super.getElementReader();
    }

    @Deprecated
    protected MinimapElementRenderInfo getPreInfo(int location, Entity renderEntity, Player player, double renderX, double renderY, double renderZ) {
        return new MinimapElementRenderInfo(xaero.hud.minimap.element.render.MinimapElementRenderLocation.fromIndex(location), renderEntity, player, new Vec3(renderX, renderY, renderZ), false, 1.0f, null, Minecraft.getInstance().level.dimensionType().coordinateScale(), Minecraft.getInstance().level.dimension());
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public boolean renderElement(E element, boolean highlighted, boolean outOfBounds, double optionalDepth, float optionalScale, double partialX, double partialY, MinimapElementRenderInfo renderInfo, GuiGraphics guiGraphics, MultiBufferSource.BufferSource vanillaBufferSource) {
        return renderElement(renderInfo.location.getIndex(), highlighted, outOfBounds, guiGraphics, vanillaBufferSource, Minecraft.getInstance().font, renderInfo.framebuffer, HudMod.INSTANCE.getMinimap().getMinimapFBORenderer().getHelper(), renderInfo.renderEntity, renderInfo.player, renderInfo.renderPos.x, renderInfo.renderPos.y, renderInfo.renderPos.z, 0, optionalDepth, optionalScale, element, partialX, partialY, renderInfo.cave, renderInfo.partialTicks);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public void preRender(MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        preRender(renderInfo.location.getIndex(), renderInfo.renderEntity, renderInfo.player, renderInfo.renderPos.x, renderInfo.renderPos.y, renderInfo.renderPos.z, HudMod.INSTANCE, vanillaBufferSource, multiTextureRenderTypeRenderers);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public void postRender(MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        postRender(renderInfo.location.getIndex(), renderInfo.renderEntity, renderInfo.player, renderInfo.renderPos.x, renderInfo.renderPos.y, renderInfo.renderPos.z, HudMod.INSTANCE, vanillaBufferSource, multiTextureRenderTypeRenderers);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public boolean shouldRender(xaero.hud.minimap.element.render.MinimapElementRenderLocation location) {
        return shouldRender(location.getIndex());
    }
}
