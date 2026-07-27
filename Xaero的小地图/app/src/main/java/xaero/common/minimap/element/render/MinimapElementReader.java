package xaero.common.minimap.element.render;

import net.minecraft.client.Minecraft;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/element/render/MinimapElementReader.class */
public abstract class MinimapElementReader<E, RC> extends xaero.hud.minimap.element.render.MinimapElementReader<E, RC> {
    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract boolean isHidden(E e, RC rc);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract double getRenderX(E e, RC rc, float f);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract double getRenderY(E e, RC rc, float f);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract double getRenderZ(E e, RC rc, float f);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract int getInteractionBoxLeft(E e, RC rc, float f);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract int getInteractionBoxRight(E e, RC rc, float f);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract int getInteractionBoxTop(E e, RC rc, float f);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract int getInteractionBoxBottom(E e, RC rc, float f);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract int getRenderBoxLeft(E e, RC rc, float f);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract int getRenderBoxRight(E e, RC rc, float f);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract int getRenderBoxTop(E e, RC rc, float f);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract int getRenderBoxBottom(E e, RC rc, float f);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract int getLeftSideLength(E e, Minecraft minecraft);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract String getMenuName(E e);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract String getFilterName(E e);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract int getMenuTextFillLeftPadding(E e);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract int getRightClickTitleBackgroundColor(E e);

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public abstract boolean shouldScaleBoxWithOptionalScale();

    @Deprecated
    public boolean isInteractable(int location, E element) {
        return super.isInteractable(xaero.hud.minimap.element.render.MinimapElementRenderLocation.fromIndex(location), (xaero.hud.minimap.element.render.MinimapElementRenderLocation) element);
    }

    @Deprecated
    public float getBoxScale(int location, E element, RC context) {
        return super.getBoxScale(xaero.hud.minimap.element.render.MinimapElementRenderLocation.fromIndex(location), (xaero.hud.minimap.element.render.MinimapElementRenderLocation) element, (E) context);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public boolean isInteractable(xaero.hud.minimap.element.render.MinimapElementRenderLocation location, E element) {
        return isInteractable(location.getIndex(), (int) element);
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementReader
    public float getBoxScale(xaero.hud.minimap.element.render.MinimapElementRenderLocation location, E element, RC context) {
        return getBoxScale(location.getIndex(), (int) element, (E) context);
    }
}
