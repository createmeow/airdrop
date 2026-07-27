package xaero.lib.client.gui.widget.online;

import net.minecraft.client.gui.screens.Screen;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/online/ButtonWidget.class */
public class ButtonWidget extends Widget {
    private String buttonText;
    private int buttonW;
    private int buttonH;

    public ButtonWidget(Class<? extends Screen> location, float horizontalAnchor, float verticalAnchor, ClickAction onClick, HoverAction onHover, int x, int y, String url, String tooltip, String buttonText, int buttonW, int buttonH, int minWidgetLevel, int maxWidgetLevel) {
        super(WidgetType.BUTTON, location, horizontalAnchor, verticalAnchor, onClick, onHover, x, y, url, tooltip, minWidgetLevel, maxWidgetLevel);
        this.buttonText = buttonText;
        this.buttonW = buttonW;
        this.buttonH = buttonH;
    }

    public String getButtonText() {
        return this.buttonText;
    }

    @Override // xaero.lib.client.gui.widget.online.Widget
    public int getW() {
        return this.buttonW;
    }

    @Override // xaero.lib.client.gui.widget.online.Widget
    public int getH() {
        return this.buttonH;
    }
}
