package xaero.common.minimap.waypoints.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import xaero.common.HudMod;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.minimap.MinimapProcessor;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.settings.ModSettings;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.config.util.MinimapConfigClientUtils;
import xaero.hud.minimap.element.render.MinimapElementReader;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.render.world.WaypointWorldRenderContext;
import xaero.hud.minimap.waypoint.render.world.WaypointWorldRenderProvider;
import xaero.hud.minimap.waypoint.render.world.WaypointWorldRenderer;
import xaero.lib.client.config.ClientConfigManager;

@Deprecated
/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/minimap/waypoints/render/WaypointsIngameRenderer.class */
public class WaypointsIngameRenderer extends WaypointWorldRenderer {
    private GuiGraphics guiGraphics;
    private Vector4f origin4f;

    public WaypointsIngameRenderer(MinimapElementReader<Waypoint, WaypointWorldRenderContext> elementReader, WaypointWorldRenderProvider provider, WaypointWorldRenderContext context, Vector4f origin4f) {
        super(elementReader, provider, context);
        this.origin4f = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Deprecated
    public void render(MinimapSession session, float partial, MinimapProcessor minimap, Matrix4f waypointsProjection, Matrix4f worldModelView) {
        Vec3 renderPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        if (this.guiGraphics == null) {
            this.guiGraphics = new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());
        }
        HudMod.INSTANCE.getMinimap().getWorldRendererHandler().prepareRender(waypointsProjection, worldModelView);
        Minecraft mc = Minecraft.getInstance();
        HudMod.INSTANCE.getMinimap().getWorldRendererHandler().render(this.guiGraphics, renderPos, partial, null, mc.level.dimensionType().coordinateScale(), mc.level.dimension());
    }

    @Deprecated
    public void drawAsOverlay(PoseStack matrixStack, PoseStack matrixStackOverlay, MinimapRendererHelper helper, Waypoint w, ModSettings settings, Tesselator tessellator, Font fontrenderer, String name, String distance, float textSize, boolean showDistance, MultiBufferSource.BufferSource renderTypeBuffer, VertexConsumer waypointBackgroundConsumer, Matrix4f waypointsProjection, int screenWidth, int screenHeight, double depthClamp, double depth, boolean isTheMain, String subworldName) {
        this.origin4f.mul(matrixStack.last().pose());
        this.origin4f.mul(waypointsProjection);
        int overlayPosX = (int) (((1.0f + (this.origin4f.x() / this.origin4f.w())) / 2.0f) * screenWidth);
        int overlayPosY = (int) (((1.0f - (this.origin4f.y() / this.origin4f.w())) / 2.0f) * screenHeight);
        this.origin4f.set(0.0f, 0.0f, 0.0f, 1.0f);
        matrixStackOverlay.translate(overlayPosX, overlayPosY, 0.0f);
        if (depth < depthClamp) {
            float scale = (float) (depthClamp / depth);
            matrixStackOverlay.scale(scale, scale, scale);
        }
        drawIconInWorld(matrixStackOverlay, helper, w, settings, tessellator, fontrenderer, name, distance, textSize, showDistance, renderTypeBuffer, waypointBackgroundConsumer, isTheMain, subworldName);
    }

    @Deprecated
    public void drawIconInWorld(PoseStack matrixStack, MinimapRendererHelper helper, Waypoint w, ModSettings settings, Tesselator tessellator, Font fontRenderer, String name, String distanceText, float textSize, boolean displayingDistance, MultiBufferSource.BufferSource bufferSource, VertexConsumer waypointBackgroundConsumer, boolean isTheMain, String subWorldName) {
        if (!displayingDistance) {
            distanceText = null;
        }
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        this.texturedIconConsumer = bufferSource.getBuffer(CustomRenderTypes.GUI_NEAREST);
        this.waypointBackgroundConsumer = waypointBackgroundConsumer;
        this.opacity = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_OPACITY_IN_WORLD)).intValue();
        float iconScale = MinimapConfigClientUtils.getUIScale(configManager, MinimapProfiledConfigOptions.WAYPOINT_ICON_SCALE_IN_WORLD);
        int distanceTextScale = (int) Math.ceil(MinimapConfigClientUtils.getUIScale(configManager, MinimapProfiledConfigOptions.WAYPOINT_DISTANCE_SCALE_IN_WORLD));
        int nameScale = (int) MinimapConfigClientUtils.getUIScale(configManager, MinimapProfiledConfigOptions.WAYPOINT_NAME_SCALE_IN_WORLD, 0.5d);
        if (Minecraft.getInstance().isEnforceUnicode()) {
            iconScale = (float) (Math.ceil(iconScale / 2.0f) * 2.0d);
            distanceTextScale = ((distanceTextScale + 1) / 2) * 2;
            nameScale = ((nameScale + 1) / 2) * 2;
        }
        int halfIconPixel = ((int) iconScale) / 2;
        matrixStack.translate(halfIconPixel, 0.0f, 0.0f);
        renderIconWithLabels(w, isTheMain, name, distanceText, subWorldName, iconScale, nameScale, distanceTextScale, fontRenderer, halfIconPixel, matrixStack, bufferSource);
    }
}
