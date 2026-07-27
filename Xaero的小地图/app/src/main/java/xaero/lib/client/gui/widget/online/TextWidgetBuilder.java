package xaero.lib.client.gui.widget.online;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/online/TextWidgetBuilder.class */
public class TextWidgetBuilder extends ScalableWidgetBuilder {
    private String text;
    private Alignment alignment = Alignment.LEFT;

    public void setText(String text) {
        this.text = text;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    @Override // xaero.lib.client.gui.widget.online.ScalableWidgetBuilder, xaero.lib.client.gui.widget.online.WidgetBuilder
    public boolean validate() {
        return (!super.validate() || this.text == null || this.alignment == null) ? false : true;
    }

    @Override // xaero.lib.client.gui.widget.online.WidgetBuilder
    public Widget build() {
        return new TextWidget(this.location, this.horizontalAnchor, this.verticalAnchor, this.onClick, this.onHover, this.x, this.y, this.scaledOffsetX, this.scaledOffsetY, this.url, this.tooltip, this.text, this.alignment, this.noGuiScale, this.scale, this.minWidgetLevel, this.maxWidgetLevel);
    }
}
