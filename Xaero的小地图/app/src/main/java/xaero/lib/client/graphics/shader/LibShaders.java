package xaero.lib.client.graphics.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import xaero.lib.XaeroLib;
import xaero.lib.client.graphics.XaeroRenderType;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:META-INF/jarjar/xaerolib-neoforge-1.21.1-1.1.0.jar:xaero/lib/client/graphics/shader/LibShaders.class */
public class LibShaders {
    public static FramebufferLinesShader FRAMEBUFFER_LINES = null;
    public static ShaderInstance POSITION_COLOR_TEX = null;
    public static ShaderInstance POSITION_COLOR_TEX_PRE = null;
    public static ShaderInstance POSITION_COLOR = null;
    public static ShaderInstance POSITION_COLOR_NO_ALPHA_TEST = null;
    public static ShaderInstance POSITION_TEX_NO_ALPHA_TEST = null;
    public static ShaderInstance POSITION_TEX_NO_ALPHA_TEST_NO_BLEND = null;
    public static PositionTexAlphaTestShader POSITION_TEX_ALPHA_TEST = null;
    public static PositionTexAlphaTestShader POSITION_TEX_ALPHA_TEST_NO_BLEND = null;
    public static PositionTexAlphaTestShader POSITION_TEX_ICON_OUTLINE = null;
    public static WorldMapShader WORLD_MAP = null;
    public static ShaderInstance WORLD_MAP_BRANCH = null;
    private static boolean firstTime = true;

    public static void onResourceReload(ResourceManager resourceManager) {
        try {
            FRAMEBUFFER_LINES = (FramebufferLinesShader) reloadShader(FRAMEBUFFER_LINES, new FramebufferLinesShader(resourceManager));
            POSITION_COLOR_TEX = reloadShader(POSITION_COLOR_TEX, new ShaderInstance(resourceManager, "xaerolib/position_color_tex", XaeroRenderType.POSITION_COLOR_TEX));
            POSITION_COLOR_TEX_PRE = reloadShader(POSITION_COLOR_TEX_PRE, new ShaderInstance(resourceManager, "xaerolib/position_color_tex_pre", XaeroRenderType.POSITION_COLOR_TEX));
            POSITION_COLOR = reloadShader(POSITION_COLOR, new ShaderInstance(resourceManager, "xaerolib/position_color", XaeroRenderType.POSITION_COLOR_TEX));
            POSITION_COLOR_NO_ALPHA_TEST = reloadShader(POSITION_COLOR_NO_ALPHA_TEST, new ShaderInstance(resourceManager, "xaerolib/position_color_no_alpha_test", XaeroRenderType.POSITION_COLOR_TEX));
            POSITION_TEX_NO_ALPHA_TEST = reloadShader(POSITION_TEX_NO_ALPHA_TEST, new ShaderInstance(resourceManager, "xaerolib/pos_tex_no_alpha_test", DefaultVertexFormat.POSITION_TEX));
            POSITION_TEX_NO_ALPHA_TEST_NO_BLEND = reloadShader(POSITION_TEX_NO_ALPHA_TEST_NO_BLEND, new ShaderInstance(resourceManager, "xaerolib/pos_tex_no_alpha_test_no_blend", DefaultVertexFormat.POSITION_TEX));
            POSITION_TEX_ALPHA_TEST = (PositionTexAlphaTestShader) reloadShader(POSITION_TEX_ALPHA_TEST, new PositionTexAlphaTestShader(resourceManager, "xaerolib/pos_tex_alpha_test"));
            POSITION_TEX_ALPHA_TEST_NO_BLEND = (PositionTexAlphaTestShader) reloadShader(POSITION_TEX_ALPHA_TEST_NO_BLEND, new PositionTexAlphaTestShader(resourceManager, "xaerolib/pos_tex_alpha_test_no_blend"));
            POSITION_TEX_ICON_OUTLINE = (PositionTexAlphaTestShader) reloadShader(POSITION_TEX_ICON_OUTLINE, new PositionTexAlphaTestShader(resourceManager, "xaerolib/pos_tex_icon_outline"));
            WORLD_MAP = (WorldMapShader) reloadShader(WORLD_MAP, new WorldMapShader(resourceManager));
            WORLD_MAP_BRANCH = reloadShader(WORLD_MAP_BRANCH, new ShaderInstance(resourceManager, "xaerolib/map_branch", DefaultVertexFormat.POSITION_TEX));
            XaeroLib.LOGGER.info("Successfully reloaded the XaeroLib shaders!");
        } catch (IOException e) {
            if (firstTime) {
                throw new RuntimeException("Couldn't reload the XaeroLib shaders!", e);
            }
            XaeroLib.LOGGER.error("Couldn't reload the XaeroLib shaders!", e);
        }
        firstTime = false;
    }

    private static <S extends ShaderInstance> S reloadShader(S current, S newOne) throws IOException {
        if (current != null) {
            current.close();
        }
        return newOne;
    }

    public static void ensureShaders() {
        if (FRAMEBUFFER_LINES == null && firstTime) {
            onResourceReload(Minecraft.getInstance().getResourceManager());
        }
    }
}
