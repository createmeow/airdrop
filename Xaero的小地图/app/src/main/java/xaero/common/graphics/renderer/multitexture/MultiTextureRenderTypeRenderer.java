package xaero.common.graphics.renderer.multitexture;

import com.mojang.blaze3d.shaders.ProgramManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;
import net.minecraft.client.renderer.RenderType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/graphics/renderer/multitexture/MultiTextureRenderTypeRenderer.class */
public class MultiTextureRenderTypeRenderer {
    private boolean used;
    private BufferBuilder currentBufferBuilder;
    private IntConsumer textureBinderShader;
    private IntConsumer textureBinder;
    private Runnable textureFinalizer;
    private int prevTextureId;
    private RenderType renderType;
    private ByteBufferBuilder sharedBuffer = new ByteBufferBuilder(256);
    private List<MeshData> buffersForDrawCalls = new ArrayList();
    private IntArrayList texturesForDrawCalls = new IntArrayList();

    MultiTextureRenderTypeRenderer() {
    }

    void init(IntConsumer textureBinderShader, IntConsumer textureBinder, Runnable textureFinalizer, RenderType renderType) {
        if (this.used) {
            throw new IllegalStateException("Multi-texture renderer already in use!");
        }
        this.used = true;
        this.textureBinderShader = textureBinderShader;
        this.textureBinder = textureBinder;
        this.textureFinalizer = textureFinalizer;
        this.prevTextureId = -1;
        this.renderType = renderType;
    }

    void draw() {
        if (!this.used) {
            throw new IllegalStateException("Multi-texture renderer is not in use!");
        }
        if (!this.texturesForDrawCalls.isEmpty()) {
            IntConsumer textureBinder = this.textureBinder;
            IntConsumer textureBinderShader = this.textureBinderShader;
            Runnable textureFinalizer = this.textureFinalizer;
            boolean hasTextureFinalizer = textureFinalizer != null;
            this.renderType.setupRenderState();
            endBuffer(this.currentBufferBuilder);
            boolean first = true;
            int shaderProgram = RenderSystem.getShader().getId();
            for (int i = 0; i < this.texturesForDrawCalls.size(); i++) {
                int texture = this.texturesForDrawCalls.getInt(i);
                MeshData buffer = this.buffersForDrawCalls.get(i);
                if (texture == -1) {
                    texture = 0;
                }
                if (first) {
                    textureBinderShader.accept(texture);
                    BufferUploader.drawWithShader(buffer);
                    ProgramManager.glUseProgram(shaderProgram);
                } else {
                    textureBinder.accept(texture);
                    BufferUploader.draw(buffer);
                }
                if (hasTextureFinalizer) {
                    if (first) {
                        textureBinder.accept(texture);
                    }
                    textureFinalizer.run();
                }
                first = false;
            }
            textureBinder.accept(0);
            this.renderType.clearRenderState();
        }
        ProgramManager.glUseProgram(0);
        this.texturesForDrawCalls.clear();
        this.buffersForDrawCalls.clear();
        this.used = false;
        this.renderType = null;
    }

    private void endBuffer(BufferBuilder builder) {
        this.buffersForDrawCalls.add(builder.build());
    }

    public BufferBuilder begin(int textureId) {
        if (!this.used) {
            throw new IllegalStateException("Multi-texture renderer is not in use!");
        }
        if (textureId == -1) {
            throw new IllegalStateException("Attempted to use the multi-texture renderer with texture id -1!");
        }
        if (textureId != this.prevTextureId) {
            if (this.prevTextureId != -1) {
                endBuffer(this.currentBufferBuilder);
            }
            this.currentBufferBuilder = new BufferBuilder(this.sharedBuffer, this.renderType.mode(), this.renderType.format());
            this.prevTextureId = textureId;
            this.texturesForDrawCalls.add(textureId);
        }
        return this.currentBufferBuilder;
    }
}
