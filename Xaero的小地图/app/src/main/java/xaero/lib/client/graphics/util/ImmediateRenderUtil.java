package xaero.lib.client.graphics.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import xaero.lib.client.graphics.shader.LibShaders;
import xaero.lib.client.graphics.shader.PositionTexAlphaTestShader;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/graphics/util/ImmediateRenderUtil.class */
public class ImmediateRenderUtil {
    public static void coloredRectangle(PoseStack matrices, float x1, float y1, float x2, float y2, int color) {
        coloredRectangle(matrices.last().pose(), x1, y1, x2, y2, color);
    }

    public static void coloredRectangle(Matrix4f matrix, float x1, float y1, float x2, float y2, int color) {
        float a = ((color >> 24) & 255) / 255.0f;
        float r = ((color >> 16) & 255) / 255.0f;
        float g = ((color >> 8) & 255) / 255.0f;
        float b = (color & 255) / 255.0f;
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder vertexBuffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        vertexBuffer.addVertex(matrix, x1, y2, 0.0f).setColor(r, g, b, a);
        vertexBuffer.addVertex(matrix, x2, y2, 0.0f).setColor(r, g, b, a);
        vertexBuffer.addVertex(matrix, x2, y1, 0.0f).setColor(r, g, b, a);
        vertexBuffer.addVertex(matrix, x1, y1, 0.0f).setColor(r, g, b, a);
        BufferUploader.drawWithShader(vertexBuffer.build());
    }

    public static void texturedRect(PoseStack matrixStack, float x, float y, int textureX, int textureY, float width, float height, float theight, float factor) {
        texturedRect(matrixStack, x, y, textureX, textureY, width, height, theight, factor, 0.0f, true);
    }

    public static void texturedRect(PoseStack matrixStack, float x, float y, int textureX, int textureY, float width, float height, float theight, float factor, float discardAlpha, boolean blend) {
        if (discardAlpha < 0.0f) {
            RenderSystem.setShader(blend ? () -> {
                return LibShaders.POSITION_TEX_NO_ALPHA_TEST;
            } : () -> {
                return LibShaders.POSITION_TEX_NO_ALPHA_TEST_NO_BLEND;
            });
        } else {
            RenderSystem.setShader(blend ? () -> {
                return LibShaders.POSITION_TEX_ALPHA_TEST;
            } : () -> {
                return LibShaders.POSITION_TEX_ALPHA_TEST_NO_BLEND;
            });
            ((PositionTexAlphaTestShader) RenderSystem.getShader()).setDiscardAlpha(discardAlpha);
        }
        texturedRectInternal(matrixStack, x, y, textureX, textureY, width, height, theight, factor);
    }

    private static void texturedRectInternal(PoseStack matrixStack, float x, float y, int textureX, int textureY, float width, float height, float textureH, float factor) {
        float f = 1.0f / factor;
        Matrix4f matrix = matrixStack.last().pose();
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder vertexBuffer = tessellator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        vertexBuffer.addVertex(matrix, x + 0.0f, y + height, 0.0f).setUv((textureX + 0) * f, (textureY + 0) * f);
        vertexBuffer.addVertex(matrix, x + width, y + height, 0.0f).setUv((textureX + width) * f, (textureY + 0) * f);
        vertexBuffer.addVertex(matrix, x + width, y + 0.0f, 0.0f).setUv((textureX + width) * f, (textureY + textureH) * f);
        vertexBuffer.addVertex(matrix, x + 0.0f, y + 0.0f, 0.0f).setUv((textureX + 0) * f, (textureY + textureH) * f);
        BufferUploader.drawWithShader(vertexBuffer.build());
    }

    public static void drawOutlineLayer(PoseStack matrixStack, float x, float y, int textureX, int textureY, float width, float height, float theight, float factor, float discardAlpha) {
        LibShaders.POSITION_TEX_ICON_OUTLINE.setDiscardAlpha(discardAlpha);
        RenderSystem.setShader(() -> {
            return LibShaders.POSITION_TEX_ICON_OUTLINE;
        });
        texturedRectInternal(matrixStack, x, y, textureX, textureY, width, height, theight, factor);
    }
}
