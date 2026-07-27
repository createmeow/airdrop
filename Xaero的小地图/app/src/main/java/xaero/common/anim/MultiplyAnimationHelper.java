package xaero.common.anim;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/anim/MultiplyAnimationHelper.class */
public class MultiplyAnimationHelper {
    public static long currentTick = System.currentTimeMillis();
    public static long lastTick = currentTick;
    public static final double STEP_TIME = 16.666666666666668d;

    public static void tick() {
        lastTick = currentTick;
        currentTick = System.currentTimeMillis();
    }

    public static double animate(double a, double factor) {
        double power = (currentTick - lastTick) / 16.666666666666668d;
        return a * Math.pow(factor, power);
    }
}
