package xaero.hud.minimap.element.render;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/element/render/MinimapElementRenderer.class */
public abstract class MinimapElementRenderer<E, RC> implements Comparable<MinimapElementRenderer<?, ?>> {
    protected final MinimapElementReader<E, RC> elementReader;
    protected final RC context;
    protected final MinimapElementRenderProvider<E, RC> provider;

    public abstract boolean renderElement(E e, boolean z, boolean z2, double d, float f, double d2, double d3, MinimapElementRenderInfo minimapElementRenderInfo, GuiGraphics guiGraphics, MultiBufferSource.BufferSource bufferSource);

    public abstract void preRender(MinimapElementRenderInfo minimapElementRenderInfo, MultiBufferSource.BufferSource bufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRendererProvider);

    public abstract void postRender(MinimapElementRenderInfo minimapElementRenderInfo, MultiBufferSource.BufferSource bufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRendererProvider);

    public abstract boolean shouldRender(MinimapElementRenderLocation minimapElementRenderLocation);

    public MinimapElementRenderer(MinimapElementReader<E, RC> elementReader, MinimapElementRenderProvider<E, RC> provider, RC context) {
        this.elementReader = elementReader;
        this.context = context;
        this.provider = provider;
    }

    public int getOrder() {
        return 0;
    }

    @Override // java.lang.Comparable
    public int compareTo(MinimapElementRenderer<?, ?> o) {
        return Integer.compare(getOrder(), o.getOrder());
    }

    public RC getContext() {
        return this.context;
    }

    public MinimapElementRenderProvider<E, RC> getProvider() {
        return this.provider;
    }

    public MinimapElementReader<E, RC> getElementReader() {
        return this.elementReader;
    }
}
