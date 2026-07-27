package xaero.common.icon;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/icon/XaeroIcon.class */
public class XaeroIcon {
    private final XaeroIconAtlas textureAtlas;
    private final int offsetX;
    private final int offsetY;

    public XaeroIcon(XaeroIconAtlas textureAtlas, int offsetX, int offsetY) {
        this.textureAtlas = textureAtlas;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public XaeroIconAtlas getTextureAtlas() {
        return this.textureAtlas;
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public int getOffsetY() {
        return this.offsetY;
    }
}
