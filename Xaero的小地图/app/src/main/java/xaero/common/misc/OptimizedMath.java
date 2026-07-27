package xaero.common.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/misc/OptimizedMath.class */
public class OptimizedMath {
    public static final Vector3f XP = new Vector3f(1.0f, 0.0f, 0.0f);
    public static final Vector3f YP = new Vector3f(0.0f, 1.0f, 0.0f);
    public static final Vector3f ZP = new Vector3f(0.0f, 0.0f, 1.0f);

    public static int myFloor(double d) {
        int asInt = (int) d;
        if (asInt != d && d < 0.0d) {
            asInt--;
        }
        return asInt;
    }

    public static void rotatePose(PoseStack poseStack, float degrees, Vector3fc vector) {
        PoseStack.Pose pose = poseStack.last();
        pose.pose().rotate(degrees * 0.017453292f, vector);
        pose.normal().rotate(degrees * 0.017453292f, vector);
    }

    public static void rotateMatrix(Matrix4f matrix, float degrees, Vector3fc vector) {
        matrix.rotate(degrees * 0.017453292f, vector);
    }
}
