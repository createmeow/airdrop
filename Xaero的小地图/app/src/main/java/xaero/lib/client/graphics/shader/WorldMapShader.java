package xaero.lib.client.graphics.shader;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.io.IOException;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.jetbrains.annotations.Nullable;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/graphics/shader/WorldMapShader.class */
public class WorldMapShader extends ShaderInstance {

    @Nullable
    public final Uniform brightness;

    @Nullable
    public final Uniform withLight;

    public WorldMapShader(ResourceProvider factory) throws IOException {
        super(factory, "xaerolib/map", DefaultVertexFormat.POSITION_TEX);
        this.brightness = getUniform("Brightness");
        this.withLight = getUniform("WithLight");
    }

    public void setBrightness(float brightness) {
        if (this.brightness.getFloatBuffer().get(0) != brightness) {
            this.brightness.set(brightness);
        }
    }

    public void setWithLight(boolean withLight) {
        int withLightInt = withLight ? 1 : 0;
        if (this.withLight.getIntBuffer().get(0) != withLightInt) {
            this.withLight.set(withLightInt);
        }
    }
}
