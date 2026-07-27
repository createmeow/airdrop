package xaero.common.exception;

import org.lwjgl.opengl.GL11;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/exception/OpenGLException.class */
public class OpenGLException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public OpenGLException(int error) {
        super("OpenGL error: " + error);
    }

    public static void checkGLError() throws OpenGLException {
        int error = GL11.glGetError();
        if (error != 0) {
            throw new OpenGLException(error);
        }
    }
}
