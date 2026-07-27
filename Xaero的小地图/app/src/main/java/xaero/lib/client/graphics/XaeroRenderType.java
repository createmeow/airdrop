package xaero.lib.client.graphics;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/graphics/XaeroRenderType.class */
public class XaeroRenderType extends RenderType {
    public static final float DEFAULT_POLYGON_OFFSET = 10.0f;
    public static final VertexFormat POSITION_COLOR_TEX = VertexFormat.builder().add("Position", VertexFormatElement.POSITION).add("Color", VertexFormatElement.COLOR).add("UV0", VertexFormatElement.UV0).build();
    public static final RenderStateShard.OutputStateShard KEEP_TARGET = new RenderStateShard.OutputStateShard("xaero_keep_target", () -> {
    }, () -> {
    });
    public static final RenderStateShard.TransparencyStateShard DEFAULT_TRANSLUCENT_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("xaero_translucent_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });
    public static final RenderStateShard.TransparencyStateShard LINES_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("xaero_lines_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });
    public static final RenderStateShard.TransparencyStateShard PREMULTIPLIED_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("xaero_premultiplied_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });
    public static final RenderStateShard.TransparencyStateShard DEST_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("xaero_destination_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });
    public static final RenderStateShard.LayeringStateShard DEFAULT_POLYGON_OFFSET_LAYERING = new RenderStateShard.LayeringStateShard("xaero_polygon_offset_layering", () -> {
        RenderSystem.polygonOffset(0.0f, 10.0f);
        RenderSystem.enablePolygonOffset();
    }, () -> {
        RenderSystem.polygonOffset(0.0f, 0.0f);
        RenderSystem.disablePolygonOffset();
    });

    private XaeroRenderType(String name, VertexFormat vertexFormat, VertexFormat.Mode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/graphics/XaeroRenderType$MultiPhaseRenderType.class */
    public static class MultiPhaseRenderType extends RenderType {
        public MultiPhaseRenderType(String name, VertexFormat vertexFormat, VertexFormat.Mode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, ImmutableList<RenderStateShard> phases) {
            super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, () -> {
                phases.forEach((v0) -> {
                    v0.setupRenderState();
                });
            }, () -> {
                phases.forEach((v0) -> {
                    v0.clearRenderState();
                });
            });
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/graphics/XaeroRenderType$MultiPhaseBuilder.class */
    public static class MultiPhaseBuilder extends RenderStateShard {
        private RenderStateShard.EmptyTextureStateShard texture;
        private RenderStateShard.ShaderStateShard shader;
        private RenderStateShard.TransparencyStateShard transparency;
        private RenderStateShard.DepthTestStateShard depthTest;
        private RenderStateShard.CullStateShard cull;
        private RenderStateShard.LightmapStateShard lightmap;
        private RenderStateShard.OverlayStateShard overlay;
        private RenderStateShard.LayeringStateShard layering;
        private RenderStateShard.OutputStateShard target;
        private RenderStateShard.TexturingStateShard texturing;
        private RenderStateShard.WriteMaskStateShard writeMaskState;
        private RenderStateShard.LineStateShard lineWidth;

        public MultiPhaseBuilder() {
            super("weird access error workaround", (Runnable) null, (Runnable) null);
            this.texture = RenderStateShard.NO_TEXTURE;
            this.shader = RenderStateShard.NO_SHADER;
            this.transparency = RenderStateShard.NO_TRANSPARENCY;
            this.depthTest = RenderStateShard.LEQUAL_DEPTH_TEST;
            this.cull = RenderStateShard.CULL;
            this.lightmap = RenderStateShard.NO_LIGHTMAP;
            this.overlay = RenderStateShard.NO_OVERLAY;
            this.layering = RenderStateShard.NO_LAYERING;
            this.target = RenderStateShard.MAIN_TARGET;
            this.texturing = RenderStateShard.DEFAULT_TEXTURING;
            this.writeMaskState = RenderStateShard.COLOR_DEPTH_WRITE;
            this.lineWidth = RenderStateShard.DEFAULT_LINE;
        }

        public MultiPhaseBuilder texture(RenderStateShard.EmptyTextureStateShard texture) {
            this.texture = texture;
            return this;
        }

        public MultiPhaseBuilder shader(RenderStateShard.ShaderStateShard shader) {
            this.shader = shader;
            return this;
        }

        public MultiPhaseBuilder transparency(RenderStateShard.TransparencyStateShard transparency) {
            this.transparency = transparency;
            return this;
        }

        public MultiPhaseBuilder depthTest(RenderStateShard.DepthTestStateShard depthTest) {
            this.depthTest = depthTest;
            return this;
        }

        public MultiPhaseBuilder cull(RenderStateShard.CullStateShard cull) {
            this.cull = cull;
            return this;
        }

        public MultiPhaseBuilder lightmap(RenderStateShard.LightmapStateShard lightmap) {
            this.lightmap = lightmap;
            return this;
        }

        public MultiPhaseBuilder overlay(RenderStateShard.OverlayStateShard overlay) {
            this.overlay = overlay;
            return this;
        }

        public MultiPhaseBuilder layering(RenderStateShard.LayeringStateShard layering) {
            this.layering = layering;
            return this;
        }

        public MultiPhaseBuilder target(RenderStateShard.OutputStateShard target) {
            this.target = target;
            return this;
        }

        public MultiPhaseBuilder texturing(RenderStateShard.TexturingStateShard texturing) {
            this.texturing = texturing;
            return this;
        }

        public MultiPhaseBuilder writeMaskState(RenderStateShard.WriteMaskStateShard writeMaskState) {
            this.writeMaskState = writeMaskState;
            return this;
        }

        public MultiPhaseBuilder lineWidth(RenderStateShard.LineStateShard lineWidth) {
            this.lineWidth = lineWidth;
            return this;
        }

        public ImmutableList<RenderStateShard> build() {
            return ImmutableList.of(this.texture, this.shader, this.transparency, this.depthTest, this.cull, this.lightmap, this.overlay, this.layering, this.target, this.texturing, this.writeMaskState, this.lineWidth, new RenderStateShard[0]);
        }

        public static void resetDepthTest() {
            RenderStateShard.EQUAL_DEPTH_TEST.setupRenderState();
            RenderStateShard.EQUAL_DEPTH_TEST.clearRenderState();
        }

        public static void resetWriteMask() {
            RenderStateShard.COLOR_WRITE.setupRenderState();
            RenderStateShard.COLOR_WRITE.clearRenderState();
            RenderStateShard.DEPTH_WRITE.setupRenderState();
            RenderStateShard.DEPTH_WRITE.clearRenderState();
        }

        public static void resetTransparency() {
            RenderStateShard.ADDITIVE_TRANSPARENCY.setupRenderState();
            RenderStateShard.ADDITIVE_TRANSPARENCY.clearRenderState();
        }
    }

    public static void resetDepthTest() {
        MultiPhaseBuilder.resetDepthTest();
    }

    public static void resetWriteMask() {
        MultiPhaseBuilder.resetWriteMask();
    }

    public static void resetTransparency() {
        MultiPhaseBuilder.resetTransparency();
    }

    public static RenderStateShard.TransparencyStateShard getTransparencyPhase(int srcFactor, int dstFactor, int srcFactorAlpha, int dstFactorAlpha) {
        return new RenderStateShard.TransparencyStateShard("xaero_custom_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(srcFactor, dstFactor, srcFactorAlpha, dstFactorAlpha);
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });
    }
}
