package xaero.lib.client.gui.widget.online;

import net.minecraft.client.gui.screens.Screen;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/online/ScalableWidget.class */
public class ScalableWidget extends Widget {
    private double scale;
    private int scaledOffsetX;
    private int scaledOffsetY;
    private boolean noGuiScale;

    public ScalableWidget(WidgetType type, Class<? extends Screen> location, float horizontalAnchor, float verticalAnchor, ClickAction onClick, HoverAction onHover, int x, int y, int scaledOffsetX, int scaledOffsetY, String url, String tooltip, boolean noGuiScale, double scale, int minWidgetLevel, int maxWidgetLevel) {
        super(type, location, horizontalAnchor, verticalAnchor, onClick, onHover, x, y, url, tooltip, minWidgetLevel, maxWidgetLevel);
        this.scale = scale;
        this.scaledOffsetX = scaledOffsetX;
        this.scaledOffsetY = scaledOffsetY;
        this.noGuiScale = noGuiScale;
    }

    public double getScale() {
        return this.scale;
    }

    public int getScaledOffsetX() {
        return this.scaledOffsetX;
    }

    public int getScaledOffsetY() {
        return this.scaledOffsetY;
    }

    public boolean isNoGuiScale() {
        return this.noGuiScale;
    }

    @Override // xaero.lib.client.gui.widget.online.Widget
    public int getBoxX(int width, double guiScale) {
        int originX = getX(width);
        double combinedScale = getScale() / (isNoGuiScale() ? guiScale : 1.0d);
        return (int) (originX + (getScaledOffsetX() * combinedScale));
    }

    @Override // xaero.lib.client.gui.widget.online.Widget
    public int getBoxY(int height, double guiScale) {
        int originY = getY(height);
        double combinedScale = getScale() / (isNoGuiScale() ? guiScale : 1.0d);
        return (int) (originY + (getScaledOffsetY() * combinedScale));
    }

    @Override // xaero.lib.client.gui.widget.online.Widget
    public int getBoxW(double guiScale) {
        double combinedScale = getScale() / (isNoGuiScale() ? guiScale : 1.0d);
        return (int) (getW() * combinedScale);
    }

    @Override // xaero.lib.client.gui.widget.online.Widget
    public int getBoxH(double guiScale) {
        double combinedScale = getScale() / (isNoGuiScale() ? guiScale : 1.0d);
        return (int) (getH() * combinedScale);
    }
}
