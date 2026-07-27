package xaero.common.graphics;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL15;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/graphics/PixelBuffers.class */
public class PixelBuffers {
    private static int buffersType = 0;

    public static int glGenBuffers() {
        switch (buffersType) {
            case 0:
                return GL15.glGenBuffers();
            default:
                return 0;
        }
    }

    public static void glBindBuffer(int target, int buffer) {
        switch (buffersType) {
            case 0:
                GL15.glBindBuffer(target, buffer);
                break;
        }
    }

    public static void glBufferData(int target, long size, int usage) {
        switch (buffersType) {
            case 0:
                GL15.glBufferData(target, size, usage);
                break;
        }
    }

    public static ByteBuffer glMapBuffer(int target, int access, long length, ByteBuffer old_buffer) {
        switch (buffersType) {
            case 0:
                return GL15.glMapBuffer(target, access, length, old_buffer);
            default:
                return null;
        }
    }

    public static boolean glUnmapBuffer(int target) {
        switch (buffersType) {
            case 0:
                return GL15.glUnmapBuffer(target);
            default:
                return false;
        }
    }

    public static void glDeleteBuffers(int buffer) {
        switch (buffersType) {
            case 0:
                GL15.glDeleteBuffers(buffer);
                break;
        }
    }

    public static void glDeleteBuffers(IntBuffer buffers) {
        switch (buffersType) {
            case 0:
                GL15.glDeleteBuffers(buffers);
                break;
        }
    }

    public static ByteBuffer glMapBuffer(int target, int access) {
        switch (buffersType) {
            case 0:
                return GL15.glMapBuffer(target, access);
            default:
                return null;
        }
    }
}
