package xaero.hud.minimap.element.render;

import net.minecraft.client.Minecraft;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/element/render/MinimapElementReader.class */
public abstract class MinimapElementReader<E, RC> {
    public abstract boolean isHidden(E e, RC rc);

    public abstract double getRenderX(E e, RC rc, float f);

    public abstract double getRenderY(E e, RC rc, float f);

    public abstract double getRenderZ(E e, RC rc, float f);

    public abstract int getInteractionBoxLeft(E e, RC rc, float f);

    public abstract int getInteractionBoxRight(E e, RC rc, float f);

    public abstract int getInteractionBoxTop(E e, RC rc, float f);

    public abstract int getInteractionBoxBottom(E e, RC rc, float f);

    public abstract int getRenderBoxLeft(E e, RC rc, float f);

    public abstract int getRenderBoxRight(E e, RC rc, float f);

    public abstract int getRenderBoxTop(E e, RC rc, float f);

    public abstract int getRenderBoxBottom(E e, RC rc, float f);

    public abstract int getLeftSideLength(E e, Minecraft minecraft);

    public abstract String getMenuName(E e);

    public abstract String getFilterName(E e);

    public abstract int getMenuTextFillLeftPadding(E e);

    public abstract int getRightClickTitleBackgroundColor(E e);

    public abstract boolean shouldScaleBoxWithOptionalScale();

    public double getCoordinateScale(E element, RC context, MinimapElementRenderInfo renderInfo) {
        return renderInfo.renderEntityDimensionScale;
    }

    public boolean shouldScalePartialCoordinates(E element, RC context, MinimapElementRenderInfo renderInfo) {
        return true;
    }

    public boolean isInteractable(MinimapElementRenderLocation location, E element) {
        return false;
    }

    public float getBoxScale(MinimapElementRenderLocation location, E element, RC context) {
        return 1.0f;
    }

    public boolean isAlwaysHighlightedWhenHovered(E element, RC context) {
        return false;
    }
}
