package xaero.lib.client.gui.widget.online;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/online/TextWidget.class */
public class TextWidget extends ScalableWidget {
    private String text;
    private Alignment alignment;

    public TextWidget(Class<? extends Screen> location, float horizontalAnchor, float verticalAnchor, ClickAction onClick, HoverAction onHover, int x, int y, int scaledOffsetX, int scaledOffsetY, String url, String tooltip, String text, Alignment alignment, boolean noGuiScale, double scale, int minWidgetLevel, int maxWidgetLevel) {
        super(WidgetType.TEXT, location, horizontalAnchor, verticalAnchor, onClick, onHover, x, y, scaledOffsetX, scaledOffsetY, url, tooltip, noGuiScale, scale, minWidgetLevel, maxWidgetLevel);
        this.text = text;
        this.alignment = alignment;
    }

    public String getText() {
        return this.text;
    }

    public Alignment getAlignment() {
        return this.alignment;
    }

    @Override // xaero.lib.client.gui.widget.online.Widget
    public int getW() {
        return Minecraft.getInstance().font.width(this.text);
    }

    @Override // xaero.lib.client.gui.widget.online.Widget
    public int getH() {
        return 10;
    }

    @Override // xaero.lib.client.gui.widget.online.ScalableWidget
    public int getScaledOffsetX() {
        int pos = super.getScaledOffsetX();
        if (this.alignment == Alignment.RIGHT) {
            pos -= Minecraft.getInstance().font.width(this.text);
        } else if (this.alignment == Alignment.CENTER) {
            pos -= Minecraft.getInstance().font.width(this.text) / 2;
        }
        return pos;
    }
}
