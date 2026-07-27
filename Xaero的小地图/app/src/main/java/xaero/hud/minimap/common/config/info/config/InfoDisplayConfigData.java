package xaero.hud.minimap.common.config.info.config;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/common/config/info/config/InfoDisplayConfigData.class */
public final class InfoDisplayConfigData {
    private final Integer backgroundColor;
    private final Integer textColor;
    private final String state;

    public InfoDisplayConfigData(Integer backgroundColor, Integer textColor, String state) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.state = state;
    }

    public Integer getBackgroundColor() {
        return this.backgroundColor;
    }

    public Integer getTextColor() {
        return this.textColor;
    }

    public String getState() {
        return this.state;
    }
}
