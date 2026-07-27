package xaero.hud.minimap.radar.color;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/color/RadarColor.class */
public enum RadarColor {
    BLACK(Component.translatable("gui.xaero_black"), '0', -16777216),
    DARK_BLUE(Component.translatable("gui.xaero_dark_blue"), '1', -16777046),
    DARK_GREEN(Component.translatable("gui.xaero_dark_green"), '2', -16733696),
    DARK_AQUA(Component.translatable("gui.xaero_dark_aqua"), '3', -16733526),
    DARK_RED(Component.translatable("gui.xaero_dark_red"), '4', -5636096),
    DARK_PURPLE(Component.translatable("gui.xaero_dark_purple"), '5', -5635926),
    GOLD(Component.translatable("gui.xaero_gold"), '6', -22016),
    GRAY(Component.translatable("gui.xaero_gray"), '7', -5592406),
    DARK_GRAY(Component.translatable("gui.xaero_dark_gray"), '8', -11184811),
    BLUE(Component.translatable("gui.xaero_blue"), '9', -11184641),
    GREEN(Component.translatable("gui.xaero_green"), 'a', -11141291),
    AQUA(Component.translatable("gui.xaero_aqua"), 'b', -11141121),
    RED(Component.translatable("gui.xaero_red"), 'c', -65536),
    PURPLE(Component.translatable("gui.xaero_purple"), 'd', -43521),
    YELLOW(Component.translatable("gui.xaero_yellow"), 'e', -171),
    WHITE(Component.translatable("gui.xaero_white"), 'f', -1);

    private final Component name;
    private final char format;
    private final int hex;

    RadarColor(Component name, char format, int hex) {
        this.name = name;
        this.format = format;
        this.hex = hex;
    }

    public Component getName() {
        return this.name;
    }

    public char getFormat() {
        return this.format;
    }

    public int getHex() {
        return this.hex;
    }

    public static RadarColor fromIndex(int index) {
        if (index == -1) {
            return null;
        }
        return values()[Mth.clamp(index, 0, values().length - 1)];
    }

    public static RadarColor getRandom() {
        return fromIndex((int) (Math.random() * values().length));
    }
}
