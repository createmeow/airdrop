package xaero.lib.client.gui.widget.online;

import net.minecraft.client.gui.screens.Screen;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/widget/online/ImageWidget.class */
public class ImageWidget extends ScalableWidget {
    private String imageId;
    private int imageW;
    private int imageH;
    private int glTexture;

    public ImageWidget(Class<? extends Screen> location, float horizontalAnchor, float verticalAnchor, ClickAction onClick, HoverAction onHover, int x, int y, int scaledOffsetX, int scaledOffsetY, String url, String tooltip, double scale, String imageId, int imageW, int imageH, int glTexture, boolean noGuiScale, int minWidgetLevel, int maxWidgetLevel) {
        super(WidgetType.IMAGE, location, horizontalAnchor, verticalAnchor, onClick, onHover, x, y, scaledOffsetX, scaledOffsetY, url, tooltip, noGuiScale, scale, minWidgetLevel, maxWidgetLevel);
        this.imageId = imageId;
        this.imageW = imageW;
        this.imageH = imageH;
        this.glTexture = glTexture;
    }

    public String getImageId() {
        return this.imageId;
    }

    public int getImageW() {
        return this.imageW;
    }

    public int getImageH() {
        return this.imageH;
    }

    public int getGlTexture() {
        return this.glTexture;
    }

    @Override // xaero.lib.client.gui.widget.online.Widget
    public int getW() {
        return this.imageW;
    }

    @Override // xaero.lib.client.gui.widget.online.Widget
    public int getH() {
        return this.imageH;
    }
}
