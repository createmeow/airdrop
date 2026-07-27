package xaero.common.graphics;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import xaero.hud.render.TextureLocations;
import xaero.lib.client.graphics.XaeroRenderType;
import xaero.lib.client.graphics.shader.LibShaders;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/graphics/CustomRenderTypes.class */
public class CustomRenderTypes extends RenderType {
    public static final RenderType GUI_BILINEAR;
    public static final RenderType GUI_BILINEAR_PREMULTIPLIED;
    public static final RenderType GUI_BILINEAR_NO_DEPTH;
    public static final RenderType GUI_NEAREST;
    public static final RenderType COLORED_WAYPOINTS_BGS;
    public static final RenderType MAP_CHUNK_OVERLAY;
    public static final RenderType MAP_LINES;
    public static final RenderType RADAR_NAME_BGS;
    public static final RenderType DEPTH_CLEAR;

    public static RenderType entityIconRenderType(ResourceLocation texture, EntityIconLayerPhases layerPhases) {
        ImmutableList<RenderStateShard> rendertype$state = new XaeroRenderType.MultiPhaseBuilder().texture(layerPhases.texture).transparency(layerPhases.transparency).shader(layerPhases.shader).depthTest(layerPhases.depthTest).writeMaskState(layerPhases.writeMask).cull(layerPhases.cull).lightmap(LIGHTMAP).overlay(OVERLAY).target(XaeroRenderType.KEEP_TARGET).build();
        return new XaeroRenderType.MultiPhaseRenderType("xaero_entity_icon", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$state);
    }

    static {
        ImmutableList<RenderStateShard> multiPhaseParameters = new XaeroRenderType.MultiPhaseBuilder().texture(new RenderStateShard.TextureStateShard(TextureLocations.GUI_TEXTURES, false, false)).transparency(XaeroRenderType.DEFAULT_TRANSLUCENT_TRANSPARENCY).shader(new RenderStateShard.ShaderStateShard(() -> {
            return LibShaders.POSITION_COLOR_TEX;
        })).cull(NO_CULL).target(XaeroRenderType.KEEP_TARGET).build();
        GUI_NEAREST = new XaeroRenderType.MultiPhaseRenderType("xaero_gui_nearest", XaeroRenderType.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, multiPhaseParameters);
        ImmutableList<RenderStateShard> multiPhaseParameters2 = new XaeroRenderType.MultiPhaseBuilder().texture(new RenderStateShard.TextureStateShard(TextureLocations.GUI_TEXTURES, true, false)).transparency(XaeroRenderType.DEFAULT_TRANSLUCENT_TRANSPARENCY).shader(new RenderStateShard.ShaderStateShard(() -> {
            return LibShaders.POSITION_COLOR_TEX;
        })).cull(NO_CULL).target(XaeroRenderType.KEEP_TARGET).build();
        GUI_BILINEAR = new XaeroRenderType.MultiPhaseRenderType("xaero_gui_bilinear", XaeroRenderType.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, multiPhaseParameters2);
        ImmutableList<RenderStateShard> multiPhaseParameters3 = new XaeroRenderType.MultiPhaseBuilder().texture(new RenderStateShard.TextureStateShard(TextureLocations.GUI_TEXTURES, true, false)).transparency(XaeroRenderType.DEFAULT_TRANSLUCENT_TRANSPARENCY).shader(new RenderStateShard.ShaderStateShard(() -> {
            return LibShaders.POSITION_COLOR_TEX;
        })).cull(NO_CULL).target(XaeroRenderType.KEEP_TARGET).depthTest(RenderStateShard.NO_DEPTH_TEST).build();
        GUI_BILINEAR_NO_DEPTH = new XaeroRenderType.MultiPhaseRenderType("xaero_gui_bilinear_no_depth", XaeroRenderType.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, multiPhaseParameters3);
        ImmutableList<RenderStateShard> multiPhaseParameters4 = new XaeroRenderType.MultiPhaseBuilder().texture(new RenderStateShard.TextureStateShard(TextureLocations.GUI_TEXTURES, true, false)).transparency(XaeroRenderType.PREMULTIPLIED_TRANSPARENCY).shader(new RenderStateShard.ShaderStateShard(() -> {
            return LibShaders.POSITION_COLOR_TEX_PRE;
        })).cull(NO_CULL).target(XaeroRenderType.KEEP_TARGET).build();
        GUI_BILINEAR_PREMULTIPLIED = new XaeroRenderType.MultiPhaseRenderType("xaero_gui_bilinear_pre", XaeroRenderType.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, false, multiPhaseParameters4);
        ImmutableList<RenderStateShard> multiPhaseParameters5 = new XaeroRenderType.MultiPhaseBuilder().transparency(XaeroRenderType.DEFAULT_TRANSLUCENT_TRANSPARENCY).shader(new RenderStateShard.ShaderStateShard(() -> {
            return LibShaders.POSITION_COLOR;
        })).target(XaeroRenderType.KEEP_TARGET).layering(XaeroRenderType.DEFAULT_POLYGON_OFFSET_LAYERING).build();
        COLORED_WAYPOINTS_BGS = new XaeroRenderType.MultiPhaseRenderType("xaero_colored_waypoints", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, multiPhaseParameters5);
        ImmutableList<RenderStateShard> multiPhaseParameters6 = new XaeroRenderType.MultiPhaseBuilder().transparency(XaeroRenderType.DEFAULT_TRANSLUCENT_TRANSPARENCY).shader(new RenderStateShard.ShaderStateShard(() -> {
            return LibShaders.POSITION_COLOR;
        })).target(XaeroRenderType.KEEP_TARGET).layering(XaeroRenderType.DEFAULT_POLYGON_OFFSET_LAYERING).build();
        RADAR_NAME_BGS = new XaeroRenderType.MultiPhaseRenderType("xaero_radar_name_bg", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, multiPhaseParameters6);
        ImmutableList<RenderStateShard> multiPhaseParameters7 = new XaeroRenderType.MultiPhaseBuilder().transparency(XaeroRenderType.DEFAULT_TRANSLUCENT_TRANSPARENCY).shader(new RenderStateShard.ShaderStateShard(() -> {
            return LibShaders.POSITION_COLOR;
        })).target(XaeroRenderType.KEEP_TARGET).build();
        MAP_CHUNK_OVERLAY = new XaeroRenderType.MultiPhaseRenderType("xaero_chunk_overlay", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, multiPhaseParameters7);
        ImmutableList<RenderStateShard> multiPhaseParameters8 = new XaeroRenderType.MultiPhaseBuilder().transparency(XaeroRenderType.LINES_TRANSPARENCY).shader(new RenderStateShard.ShaderStateShard(() -> {
            return LibShaders.FRAMEBUFFER_LINES;
        })).target(XaeroRenderType.KEEP_TARGET).build();
        MAP_LINES = new XaeroRenderType.MultiPhaseRenderType("xaero_lines", DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES, 256, false, false, multiPhaseParameters8);
        ImmutableList<RenderStateShard> multiPhaseParameters9 = new XaeroRenderType.MultiPhaseBuilder().transparency(XaeroRenderType.DEFAULT_TRANSLUCENT_TRANSPARENCY).shader(new RenderStateShard.ShaderStateShard(() -> {
            return LibShaders.POSITION_COLOR_NO_ALPHA_TEST;
        })).target(XaeroRenderType.KEEP_TARGET).depthTest(GREATER_DEPTH_TEST).writeMaskState(DEPTH_WRITE).build();
        DEPTH_CLEAR = new XaeroRenderType.MultiPhaseRenderType("xaero_depth_clear", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, multiPhaseParameters9);
    }

    private CustomRenderTypes(String name, VertexFormat vertexFormat, VertexFormat.Mode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static EntityIconLayerPhases getBasicEntityIconLayerPhases(ResourceLocation texture) {
        return new EntityIconLayerPhases(new RenderStateShard.TextureStateShard(texture, false, false), XaeroRenderType.DEFAULT_TRANSLUCENT_TRANSPARENCY, LEQUAL_DEPTH_TEST, COLOR_DEPTH_WRITE, NO_CULL, RENDERTYPE_ENTITY_TRANSLUCENT_SHADER);
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/graphics/CustomRenderTypes$EntityIconLayerPhases.class */
    public static class EntityIconLayerPhases {
        public RenderStateShard.TextureStateShard texture;
        public RenderStateShard.TransparencyStateShard transparency;
        public RenderStateShard.DepthTestStateShard depthTest;
        public RenderStateShard.WriteMaskStateShard writeMask;
        public RenderStateShard.CullStateShard cull;
        public RenderStateShard.ShaderStateShard shader;

        public EntityIconLayerPhases(Object texture, Object transparency, Object depthTest, Object writeMask, Object cull, Object shader) {
            this.texture = (RenderStateShard.TextureStateShard) texture;
            this.transparency = (RenderStateShard.TransparencyStateShard) transparency;
            this.depthTest = (RenderStateShard.DepthTestStateShard) depthTest;
            this.writeMask = (RenderStateShard.WriteMaskStateShard) writeMask;
            this.cull = (RenderStateShard.CullStateShard) cull;
            this.shader = (RenderStateShard.ShaderStateShard) shader;
        }
    }
}
