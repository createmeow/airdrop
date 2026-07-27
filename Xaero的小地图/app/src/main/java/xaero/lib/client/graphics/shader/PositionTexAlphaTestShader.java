package xaero.lib.client.graphics.shader;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/graphics/shader/PositionTexAlphaTestShader.class */
public class PositionTexAlphaTestShader extends ShaderInstance {

    @Nullable
    private Uniform discardAlpha;

    public PositionTexAlphaTestShader(ResourceProvider factory, String path) throws IOException {
        super(factory, path, DefaultVertexFormat.POSITION_TEX);
        this.discardAlpha = getUniform("DiscardAlpha");
    }

    public void setDiscardAlpha(float alpha) {
        if (this.discardAlpha.getFloatBuffer().get(0) != alpha) {
            this.discardAlpha.set(alpha);
        }
    }
}
