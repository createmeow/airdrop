package xaero.lib.client.gui.util.graphics;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/gui/util/graphics/GuiGraphicsUtils.class */
public class GuiGraphicsUtils {
    public static void blit(PoseStack pose, int x, int y, float u, float v, int w, int h) {
        blit(pose, x, x + w, y, y + h, 0, w, h, u, v, 256, 256);
    }

    public static void blit(PoseStack pose, int x, int y, int z, float u, float v, int w, int h, int textureW, int textureH) {
        blit(pose, x, x + w, y, y + h, z, w, h, u, v, textureW, textureH);
    }

    static void blit(PoseStack pose, int left, int right, int top, int bottom, int z, int uw, int vh, float u, float v, int textureW, int textureH) {
        innerBlit(pose, left, right, top, bottom, z, (u + 0.0f) / textureW, (u + uw) / textureW, (v + 0.0f) / textureH, (v + vh) / textureH);
    }

    static void innerBlit(PoseStack pose, int left, int right, int top, int bottom, int z, float uLeft, float uRight, float vTop, float vBottom) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = pose.last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.addVertex(matrix4f, left, top, z).setUv(uLeft, vTop);
        bufferBuilder.addVertex(matrix4f, left, bottom, z).setUv(uLeft, vBottom);
        bufferBuilder.addVertex(matrix4f, right, bottom, z).setUv(uRight, vBottom);
        bufferBuilder.addVertex(matrix4f, right, top, z).setUv(uRight, vTop);
        BufferUploader.drawWithShader(bufferBuilder.build());
    }
}
