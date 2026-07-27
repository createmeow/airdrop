package xaero.lib.client.gui.widget.online.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import xaero.lib.client.gui.widget.online.ScalableWidget;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/online/render/ScalableWidgetRenderer.class */
public abstract class ScalableWidgetRenderer<T extends ScalableWidget> implements WidgetRenderer<T> {
    protected abstract void renderScaled(GuiGraphics guiGraphics, int i, int i2, int i3, int i4, double d, T t);

    @Override // xaero.lib.client.gui.widget.online.render.WidgetRenderer
    public void render(GuiGraphics guiGraphics, int width, int height, int mouseX, int mouseY, double guiScale, T widget) {
        PoseStack matrixStack = guiGraphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(widget.getX(width), widget.getY(height), 0.0f);
        if (widget.isNoGuiScale()) {
            matrixStack.scale((float) (1.0d / guiScale), (float) (1.0d / guiScale), 1.0f);
        }
        matrixStack.scale((float) widget.getScale(), (float) widget.getScale(), 1.0f);
        matrixStack.translate(widget.getScaledOffsetX(), widget.getScaledOffsetY(), 0.0f);
        renderScaled(guiGraphics, width, height, mouseX, mouseY, guiScale, widget);
        matrixStack.popPose();
    }
}
