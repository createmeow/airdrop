package xaero.hud.render.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/render/util/RenderBufferUtil.class */
public class RenderBufferUtil {
    public static void addColoredRect(Matrix4f matrix, VertexConsumer vertexBuffer, float x, float y, int w, int h, int color) {
        float a = ((color >> 24) & 255) / 255.0f;
        float r = ((color >> 16) & 255) / 255.0f;
        float g = ((color >> 8) & 255) / 255.0f;
        float b = (color & 255) / 255.0f;
        addColoredRect(matrix, vertexBuffer, x, y, w, h, r, g, b, a);
    }

    public static void addColoredRect(Matrix4f matrix, VertexConsumer vertexBuffer, float x, float y, int w, int h, float r, float g, float b, float a) {
        vertexBuffer.addVertex(matrix, x, y + h, 0.0f).setColor(r, g, b, a);
        vertexBuffer.addVertex(matrix, x + w, y + h, 0.0f).setColor(r, g, b, a);
        vertexBuffer.addVertex(matrix, x + w, y, 0.0f).setColor(r, g, b, a);
        vertexBuffer.addVertex(matrix, x, y, 0.0f).setColor(r, g, b, a);
    }

    public static void addTexturedColoredRect(Matrix4f matrix, VertexConsumer vertexBuffer, float x, float y, int u, int v, int w, int h, float r, float g, float b, float a, float factor) {
        addTexturedColoredRect(matrix, vertexBuffer, x, y, u, v, w, h, w, h, r, g, b, a, factor);
    }

    public static void addTexturedColoredRect(Matrix4f matrix, VertexConsumer vertexBuffer, float x, float y, int u, int v, int w, int h, int tw, int th, float r, float g, float b, float a, float factor) {
        float f = 1.0f / factor;
        float normalizedU1 = u * f;
        float normalizedV1 = v * f;
        float normalizedU2 = (u + tw) * f;
        float normalizedV2 = (v + th) * f;
        vertexBuffer.addVertex(matrix, x, y + h, 0.0f).setColor(r, g, b, a).setUv(normalizedU1, normalizedV1);
        vertexBuffer.addVertex(matrix, x + w, y + h, 0.0f).setColor(r, g, b, a).setUv(normalizedU2, normalizedV1);
        vertexBuffer.addVertex(matrix, x + w, y, 0.0f).setColor(r, g, b, a).setUv(normalizedU2, normalizedV2);
        vertexBuffer.addVertex(matrix, x, y, 0.0f).setColor(r, g, b, a).setUv(normalizedU1, normalizedV2);
    }
}
