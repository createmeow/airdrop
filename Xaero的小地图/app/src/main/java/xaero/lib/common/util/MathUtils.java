package xaero.lib.common.util;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/common/util/MathUtils.class */
public class MathUtils {
    public static int getDecimalCountAfterDot(double number, int maximum) {
        int result = 0;
        while (result <= maximum) {
            double multipliedStep = number * Math.pow(10.0d, result);
            if (Math.floor(multipliedStep) == multipliedStep) {
                break;
            }
            result++;
        }
        return result;
    }

    public static double floor(double number, int afterDot) {
        double shifter = Math.pow(10.0d, afterDot);
        return Math.floor(number * shifter) / shifter;
    }

    public static double round(double a, int afterDot) {
        double x = Math.pow(10.0d, afterDot);
        return Math.round(a * x) / x;
    }
}
