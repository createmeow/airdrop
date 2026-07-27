package xaero.lib.client.gui.widget.online.render;

import net.minecraft.client.gui.GuiGraphics;
import xaero.lib.client.gui.widget.online.Widget;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/online/render/WidgetRenderer.class */
public interface WidgetRenderer<T extends Widget> {
    void render(GuiGraphics guiGraphics, int i, int i2, int i3, int i4, double d, T t);
}
