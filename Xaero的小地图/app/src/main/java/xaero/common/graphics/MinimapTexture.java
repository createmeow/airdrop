package xaero.common.graphics;

import com.mojang.blaze3d.platform.TextureUtil;
import java.io.IOException;
import java.nio.ByteBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.lwjgl.BufferUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/graphics/MinimapTexture.class */
public class MinimapTexture extends SimpleTexture {
    public ByteBuffer buffer;
    boolean loaded;

    public void loadIfNeeded() throws IOException {
        if (!this.loaded) {
            load(Minecraft.getInstance().getResourceManager());
            this.loaded = true;
        }
    }

    public MinimapTexture(ResourceLocation location) throws IOException {
        super(location);
        this.buffer = BufferUtils.createByteBuffer(786432);
        this.loaded = false;
    }

    public void load(ResourceManager resourceManager_1) throws IOException {
        TextureUtil.prepareImage(getId(), 0, 512, 512);
    }
}
