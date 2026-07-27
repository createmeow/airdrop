package xaero.lib.client.gui.widget.online;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/online/ScalableWidgetBuilder.class */
public abstract class ScalableWidgetBuilder extends WidgetBuilder {
    protected double scale = 1.0d;
    protected int scaledOffsetX;
    protected int scaledOffsetY;
    protected boolean noGuiScale;

    public void setScale(double scale) {
        this.scale = scale;
    }

    public void setScaledOffsetX(int scaledOffsetX) {
        this.scaledOffsetX = scaledOffsetX;
    }

    public void setScaledOffsetY(int scaledOffsetY) {
        this.scaledOffsetY = scaledOffsetY;
    }

    public void setNoGuiScale(boolean noGuiScale) {
        this.noGuiScale = noGuiScale;
    }

    @Override // xaero.lib.client.gui.widget.online.WidgetBuilder
    public boolean validate() {
        return super.validate() && this.scale != 0.0d;
    }
}
