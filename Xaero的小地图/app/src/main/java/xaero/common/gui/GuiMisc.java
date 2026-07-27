package xaero.common.gui;

import java.text.DecimalFormat;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/gui/GuiMisc.class */
public class GuiMisc {
    private static int wpDistanceFormatPrecision = 1;
    private static DecimalFormat wpDistanceFormat = new DecimalFormat("0.0");

    public static DecimalFormat getFormat(int precision) {
        if (precision != wpDistanceFormatPrecision) {
            StringBuilder formatStringBuilder = new StringBuilder();
            formatStringBuilder.append("0");
            if (precision > 0) {
                formatStringBuilder.append(".");
            }
            for (int i = 0; i < precision; i++) {
                formatStringBuilder.append("0");
            }
            wpDistanceFormat = new DecimalFormat(formatStringBuilder.toString());
            wpDistanceFormatPrecision = precision;
        }
        return wpDistanceFormat;
    }
}
