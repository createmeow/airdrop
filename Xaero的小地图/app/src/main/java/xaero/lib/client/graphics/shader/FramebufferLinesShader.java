package xaero.lib.client.graphics.shader;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/graphics/shader/FramebufferLinesShader.class */
public class FramebufferLinesShader extends ShaderInstance {

    @Nullable
    private Uniform frameSize;

    public FramebufferLinesShader(ResourceProvider factory) throws IOException {
        super(factory, "xaerolib/framebuffer_lines", DefaultVertexFormat.POSITION_COLOR_NORMAL);
        this.frameSize = getUniform("FrameSize");
    }

    public void setFrameSize(float width, float height) {
        if (this.frameSize.getFloatBuffer().get(0) != width || this.frameSize.getFloatBuffer().get(1) != height) {
            this.frameSize.set(width, height);
        }
    }
}
