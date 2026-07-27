package xaero.lib.client.gui.widget.online;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/online/ImageWidgetBuilder.class */
public class ImageWidgetBuilder extends ScalableWidgetBuilder {
    private String imageId;
    private int imageW;
    private int imageH;
    private int glTexture;

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public void setImageW(int imageW) {
        this.imageW = imageW;
    }

    public void setImageH(int imageH) {
        this.imageH = imageH;
    }

    public void setGlTexture(int glTexture) {
        this.glTexture = glTexture;
    }

    @Override // xaero.lib.client.gui.widget.online.ScalableWidgetBuilder, xaero.lib.client.gui.widget.online.WidgetBuilder
    public boolean validate() {
        return super.validate() && this.imageId != null && this.imageW > 0 && this.imageH > 0 && this.glTexture > 0;
    }

    @Override // xaero.lib.client.gui.widget.online.WidgetBuilder
    public Widget build() {
        return new ImageWidget(this.location, this.horizontalAnchor, this.verticalAnchor, this.onClick, this.onHover, this.x, this.y, this.scaledOffsetX, this.scaledOffsetY, this.url, this.tooltip, this.scale, this.imageId, this.imageW, this.imageH, this.glTexture, this.noGuiScale, this.minWidgetLevel, this.maxWidgetLevel);
    }
}
