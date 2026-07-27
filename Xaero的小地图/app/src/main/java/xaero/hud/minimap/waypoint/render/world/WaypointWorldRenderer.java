package xaero.hud.minimap.waypoint.render.world;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import xaero.common.HudMod;
import xaero.common.effect.Effects;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.gui.GuiMisc;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointUtil;
import xaero.common.minimap.waypoints.render.WaypointsIngameRenderer;
import xaero.common.misc.Misc;
import xaero.common.misc.OptimizedMath;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.config.util.MinimapConfigClientUtils;
import xaero.hud.minimap.element.render.MinimapElementReader;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.element.render.MinimapElementRenderer;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.WaypointPurpose;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.minimap.world.MinimapWorldManager;
import xaero.hud.render.util.RenderBufferUtil;
import xaero.lib.client.config.ClientConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/render/world/WaypointWorldRenderer.class */
public abstract class WaypointWorldRenderer extends MinimapElementRenderer<Waypoint, WaypointWorldRenderContext> {
    private Vector3f lookVector;
    private boolean temporaryWaypointsGlobal;
    private double waypointsDistance;
    private double waypointsDistanceMin;
    private int distanceSetting;
    private boolean displayShortDistances;
    private boolean dimensionScaleDistance;
    private double clampDepth;
    private int lookingAtAngle;
    private int lookingAtAngleVertical;
    private boolean keepWaypointNames;
    private int autoConvertWaypointDistanceToKmThreshold;
    private int waypointDistancePrecision;
    private float iconScale;
    private int distanceTextScale;
    private int nameScale;
    protected int opacity;
    private float cameraAngleYaw;
    private float cameraAnglePitch;
    private String subWorldName;
    private MinimapRendererHelper helper;
    private Font fontRenderer;
    private MultiBufferSource.BufferSource minimapBufferSource;
    protected VertexConsumer texturedIconConsumer;
    protected VertexConsumer waypointBackgroundConsumer;

    protected WaypointWorldRenderer(MinimapElementReader<Waypoint, WaypointWorldRenderContext> elementReader, WaypointWorldRenderProvider provider, WaypointWorldRenderContext context) {
        super(elementReader, provider, context);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public boolean renderElement(Waypoint w, boolean highlighted, boolean outOfBounds, double optionalDepth, float optionalScale, double partialX, double partialY, MinimapElementRenderInfo renderInfo, GuiGraphics guiGraphics, MultiBufferSource.BufferSource vanillaBufferSource) {
        double waypointPosDivider = renderInfo.backgroundCoordinateScale / ((WaypointWorldRenderContext) this.context).dimCoordinateScale;
        double wX = w.getX(waypointPosDivider) + 0.5d;
        double wZ = w.getZ(waypointPosDivider) + 0.5d;
        double offX = wX - renderInfo.renderPos.x;
        double offY = (w.getY() + 1.0d) - renderInfo.renderPos.y;
        if (!w.isYIncluded()) {
            offY = (renderInfo.renderEntityPos.y + 1.0d) - renderInfo.renderPos.y;
        }
        double offZ = wZ - renderInfo.renderPos.z;
        double distance2D = Math.sqrt((offX * offX) + (offZ * offZ));
        if (this.waypointsDistanceMin != 0.0d && distance2D < this.waypointsDistanceMin) {
            return false;
        }
        double distanceScale = this.dimensionScaleDistance ? renderInfo.backgroundCoordinateScale : 1.0d;
        double scaledDistance2D = distance2D * distanceScale;
        if (!w.isDestination() && w.getPurpose() != WaypointPurpose.DEATH && !w.isGlobal() && ((!w.isTemporary() || !this.temporaryWaypointsGlobal) && this.waypointsDistance != 0.0d && scaledDistance2D > this.waypointsDistance)) {
            return false;
        }
        Vector3f lookVector = this.lookVector;
        double depth = (offX * lookVector.x()) + (offY * lookVector.y()) + (offZ * lookVector.z());
        double xFromEntity = wX - renderInfo.renderEntityPos.x;
        double yFromEntity = w.getY() - renderInfo.renderEntityPos.y;
        if (!w.isYIncluded()) {
            yFromEntity = 0.0d;
        }
        double zFromEntity = wZ - renderInfo.renderEntityPos.z;
        double distanceFromEntity = Math.sqrt((xFromEntity * xFromEntity) + (yFromEntity * yFromEntity) + (zFromEntity * zFromEntity));
        boolean usingNearbyDisplay = distanceFromEntity <= 20.0d && !this.displayShortDistances;
        boolean displayingDistance = !usingNearbyDisplay && highlighted;
        String distanceText = displayingDistance ? getDistanceText(distanceFromEntity) : null;
        String name = null;
        if (usingNearbyDisplay || ((displayingDistance && this.keepWaypointNames) || (!displayingDistance && w.getPurpose() == WaypointPurpose.DEATH))) {
            name = w.getLocalizedName();
        }
        Font fontRenderer = this.fontRenderer;
        MultiBufferSource.BufferSource bufferSource = this.minimapBufferSource;
        float iconScale = this.iconScale;
        int nameScale = this.nameScale;
        int halfIconPixel = ((int) iconScale) / 2;
        PoseStack matrixStack = guiGraphics.pose();
        if (renderInfo.location == MinimapElementRenderLocation.IN_WORLD && depth < this.clampDepth) {
            float scale = (float) (this.clampDepth / depth);
            matrixStack.scale(scale, scale, 1.0f);
        }
        matrixStack.translate(halfIconPixel, 0.0d, optionalDepth);
        renderIconWithLabels(w, highlighted, name, distanceText, this.subWorldName, iconScale, nameScale, this.distanceTextScale, fontRenderer, halfIconPixel, matrixStack, bufferSource);
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public void preRender(MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        int i;
        int i2;
        Minecraft mc = Minecraft.getInstance();
        Camera activeRender = mc.gameRenderer.getMainCamera();
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        MinimapWorldManager manager = session.getWorldManager();
        MinimapWorld currentWorld = manager.getCurrentWorld();
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        this.lookVector = activeRender.getLookVector().get(new Vector3f());
        this.cameraAngleYaw = activeRender.getYRot();
        this.cameraAnglePitch = activeRender.getXRot();
        double fov = ((Integer) mc.options.fov().get()).doubleValue();
        int screenWidth = mc.getWindow().getWidth();
        int screenHeight = mc.getWindow().getHeight();
        this.subWorldName = null;
        if (currentWorld != null && manager.getAutoWorld() != currentWorld) {
            this.subWorldName = "(" + currentWorld.getContainer().getSubName() + ")";
        }
        ((WaypointWorldRenderContext) this.context).dimCoordinateScale = session.getDimensionHelper().getDimCoordinateScale(manager.getCurrentWorld());
        ((WaypointWorldRenderContext) this.context).renderEntityPos = renderInfo.renderEntityPos;
        int displayMultipleWaypointInfo = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.MULTIPLE_WAYPOINTS_INFO)).intValue();
        ((WaypointWorldRenderContext) this.context).onlyMainInfo = displayMultipleWaypointInfo == 0 || (displayMultipleWaypointInfo == 1 && !renderInfo.renderEntity.isShiftKeyDown());
        this.temporaryWaypointsGlobal = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.TEMPORARY_WAYPOINTS_GLOBAL)).booleanValue();
        this.waypointsDistance = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_MAX_DISTANCE)).intValue();
        this.waypointsDistanceMin = ((Double) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_MIN_DISTANCE_IN_WORLD)).doubleValue();
        this.distanceSetting = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_DISTANCE_IN_WORLD)).intValue();
        this.displayShortDistances = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_SHORT_DISTANCE_IN_WORLD)).booleanValue();
        this.dimensionScaleDistance = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_MAX_DISTANCE_DIMENSION_SCALE)).booleanValue();
        this.clampDepth = MinimapConfigClientUtils.getWaypointsClampDepth(configManager, fov, screenHeight);
        int horizontalPointingAngleConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_HORIZONTAL_POINTING_ANGLE)).intValue();
        this.lookingAtAngle = Mth.clamp(horizontalPointingAngleConfig, 0, 180);
        int verticalPointingAngleConfig = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_VERTICAL_POINTING_ANGLE)).intValue();
        this.lookingAtAngleVertical = Mth.clamp(verticalPointingAngleConfig, 0, 180);
        this.keepWaypointNames = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_NAME_IN_WORLD)).booleanValue();
        this.autoConvertWaypointDistanceToKmThreshold = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_CONVERT_DISTANCE_TO_KM_AT)).intValue();
        this.waypointDistancePrecision = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_DISTANCE_PRECISION)).intValue();
        this.iconScale = MinimapConfigClientUtils.getUIScale(configManager, MinimapProfiledConfigOptions.WAYPOINT_ICON_SCALE_IN_WORLD);
        this.distanceTextScale = (int) Math.ceil(MinimapConfigClientUtils.getUIScale(configManager, MinimapProfiledConfigOptions.WAYPOINT_DISTANCE_SCALE_IN_WORLD));
        this.nameScale = (int) MinimapConfigClientUtils.getUIScale(configManager, MinimapProfiledConfigOptions.WAYPOINT_NAME_SCALE_IN_WORLD, 0.5d);
        this.opacity = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_OPACITY_IN_WORLD)).intValue();
        WaypointWorldRenderContext waypointWorldRenderContext = (WaypointWorldRenderContext) this.context;
        if (this.distanceSetting == 0 || this.lookingAtAngleVertical == 0) {
            i = 0;
        } else {
            i = (this.distanceSetting == 2 || this.lookingAtAngleVertical >= 90) ? -screenHeight : -OptimizedMath.myFloor(((screenHeight / 2) * Math.tan(Math.toRadians(this.lookingAtAngleVertical))) / Math.tan(Math.toRadians(fov / 2.0d)));
        }
        waypointWorldRenderContext.interactionBoxTop = i;
        double horizontalTan = (Math.tan(Math.toRadians(fov / 2.0d)) * screenWidth) / screenHeight;
        WaypointWorldRenderContext waypointWorldRenderContext2 = (WaypointWorldRenderContext) this.context;
        if (this.distanceSetting == 0 || this.lookingAtAngle == 0) {
            i2 = 0;
        } else {
            i2 = (this.distanceSetting == 2 || this.lookingAtAngle >= 90) ? -screenWidth : -OptimizedMath.myFloor(((screenWidth / 2) * Math.tan(Math.toRadians(this.lookingAtAngle))) / horizontalTan);
        }
        waypointWorldRenderContext2.interactionBoxLeft = i2;
        if (Minecraft.getInstance().isEnforceUnicode()) {
            this.iconScale = (float) (Math.ceil(this.iconScale / 2.0f) * 2.0d);
            this.distanceTextScale = ((this.distanceTextScale + 1) / 2) * 2;
            this.nameScale = ((this.nameScale + 1) / 2) * 2;
        }
        this.helper = HudMod.INSTANCE.getMinimap().getMinimapFBORenderer().getHelper();
        this.fontRenderer = mc.font;
        RenderSystem.disableDepthTest();
        vanillaBufferSource.endBatch();
        this.minimapBufferSource = HudMod.INSTANCE.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
        this.waypointBackgroundConsumer = this.minimapBufferSource.getBuffer(CustomRenderTypes.COLORED_WAYPOINTS_BGS);
        this.texturedIconConsumer = this.minimapBufferSource.getBuffer(CustomRenderTypes.GUI_NEAREST);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public void postRender(MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        this.minimapBufferSource.endBatch();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(515);
        ((WaypointWorldRenderContext) this.context).onlyMainInfo = false;
        ((WaypointWorldRenderContext) this.context).renderEntityPos = null;
        this.fontRenderer = null;
        this.minimapBufferSource = null;
        this.waypointBackgroundConsumer = null;
        this.texturedIconConsumer = null;
    }

    protected void renderIconWithLabels(Waypoint w, boolean highlit, String name, String distanceText, String subWorldName, float iconScale, int nameScale, int distanceTextScale, Font fontRenderer, int halfIconPixel, PoseStack matrixStack, MultiBufferSource.BufferSource bufferSource) {
        matrixStack.scale(iconScale, iconScale, 1.0f);
        renderIcon(w, highlit, matrixStack, fontRenderer, bufferSource);
        matrixStack.scale(1.0f / iconScale, 1.0f / iconScale, 1.0f);
        matrixStack.translate(-halfIconPixel, 0.0f, 0.0f);
        matrixStack.translate(0.0f, 2.0f, 0.0f);
        if ((distanceText != null || name != null) && subWorldName != null) {
            renderWaypointLabel(subWorldName, matrixStack, this.helper, fontRenderer, nameScale, 0.3529412f);
            matrixStack.translate(0.0f, 2.0f, 0.0f);
        }
        if (name != null) {
            renderWaypointLabel(name, matrixStack, this.helper, fontRenderer, nameScale, 0.3529412f);
        }
        matrixStack.translate(0.0f, 2.0f, 0.0f);
        if (distanceText != null) {
            renderWaypointLabel(distanceText, matrixStack, this.helper, fontRenderer, distanceTextScale, 0.3529412f);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void renderIcon(Waypoint w, boolean highlit, PoseStack matrixStack, Font fontRenderer, MultiBufferSource.BufferSource bufferSource) {
        int color = w.getWaypointColor().getHex();
        float red = ((color >> 16) & 255) / 255.0f;
        float green = ((color >> 8) & 255) / 255.0f;
        float blue = (color & 255) / 255.0f;
        float alpha = (0.52274513f * this.opacity) / 100.0f;
        if (highlit && ((WaypointWorldRenderContext) this.context).onlyMainInfo) {
            alpha = Math.min(1.0f, alpha * 1.5f);
        }
        int initialsWidth = w.getPurpose() == WaypointPurpose.DEATH ? 7 : fontRenderer.width(w.getInitials());
        int addedFrame = WaypointUtil.getAddedMinimapIconFrame(initialsWidth);
        renderColorBackground(matrixStack, addedFrame, red, green, blue, alpha, this.waypointBackgroundConsumer);
        if (w.getPurpose() == WaypointPurpose.DEATH) {
            renderTexturedIcon(matrixStack, addedFrame, 0, 78, 0.9882f, 0.9882f, 0.9882f, 1.0f, this.texturedIconConsumer);
        } else {
            Misc.drawNormalText(matrixStack, w.getInitials(), (-initialsWidth) / 2, -8.0f, -1, false, bufferSource);
        }
    }

    private void renderColorBackground(PoseStack matrixStack, int addedFrame, float r, float g, float b, float a, VertexConsumer waypointBackgroundConsumer) {
        Matrix4f matrix = matrixStack.last().pose();
        waypointBackgroundConsumer.addVertex(matrix, (-5) - addedFrame, -9.0f, 0.0f).setColor(r, g, b, a);
        waypointBackgroundConsumer.addVertex(matrix, (-5) - addedFrame, 0.0f, 0.0f).setColor(r, g, b, a);
        waypointBackgroundConsumer.addVertex(matrix, 4 + addedFrame, 0.0f, 0.0f).setColor(r, g, b, a);
        waypointBackgroundConsumer.addVertex(matrix, 4 + addedFrame, -9.0f, 0.0f).setColor(r, g, b, a);
    }

    private void renderTexturedIcon(PoseStack matrixStack, int addedFrame, int textureX, int textureY, float r, float g, float b, float a, VertexConsumer vertexBuffer) {
        Matrix4f matrix = matrixStack.last().pose();
        vertexBuffer.addVertex(matrix, (-5) - addedFrame, (-9) - addedFrame, 0.0f).setColor(r, g, b, a).setUv(textureX * 0.00390625f, textureY * 0.00390625f);
        vertexBuffer.addVertex(matrix, (-5) - addedFrame, addedFrame, 0.0f).setColor(r, g, b, a).setUv(textureX * 0.00390625f, (textureY + 9 + (addedFrame * 2)) * 0.00390625f);
        vertexBuffer.addVertex(matrix, 4 + addedFrame, addedFrame, 0.0f).setColor(r, g, b, a).setUv((textureX + 9 + (addedFrame * 2)) * 0.00390625f, (textureY + 9 + (addedFrame * 2)) * 0.00390625f);
        vertexBuffer.addVertex(matrix, 4 + addedFrame, (-9) - addedFrame, 0.0f).setColor(r, g, b, a).setUv((textureX + 9 + (addedFrame * 2)) * 0.00390625f, textureY * 0.00390625f);
    }

    protected void renderWaypointLabel(String label, PoseStack matrixStack, MinimapRendererHelper helper, Font fontRenderer, int labelScale, float bgAlpha) {
        int nameWidth = fontRenderer.width(label);
        int backgroundWidth = nameWidth + 3;
        int halfBackgroundWidth = backgroundWidth / 2;
        int halfPixel = 0;
        if ((backgroundWidth & 1) != 0) {
            halfPixel = labelScale - (labelScale / 2);
            matrixStack.translate(-halfPixel, 0.0f, 0.0f);
        }
        matrixStack.scale(labelScale, labelScale, 1.0f);
        RenderBufferUtil.addColoredRect(matrixStack.last().pose(), this.waypointBackgroundConsumer, -halfBackgroundWidth, 0.0f, backgroundWidth, 9, 0.0f, 0.0f, 0.0f, bgAlpha);
        Misc.drawNormalText(matrixStack, label, (-halfBackgroundWidth) + 2, 1.0f, -1, false, this.minimapBufferSource);
        matrixStack.translate(0.0f, 9.0f, 0.0f);
        matrixStack.scale(1.0f / labelScale, 1.0f / labelScale, 1.0f);
        if ((backgroundWidth & 1) != 0) {
            matrixStack.translate(halfPixel, 0.0f, 0.0f);
        }
    }

    private String getDistanceText(double distanceFromEntity) {
        if (this.autoConvertWaypointDistanceToKmThreshold != -1 && distanceFromEntity >= this.autoConvertWaypointDistanceToKmThreshold) {
            return GuiMisc.getFormat(this.waypointDistancePrecision).format(distanceFromEntity / 1000.0d) + "km";
        }
        return GuiMisc.getFormat(this.waypointDistancePrecision).format(distanceFromEntity) + "m";
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public boolean shouldRender(MinimapElementRenderLocation location) {
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        if (!((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINTS_IN_WORLD)).booleanValue()) {
            return false;
        }
        Minecraft mc = Minecraft.getInstance();
        return (mc.player == null || Misc.hasEffect(mc.player, Effects.NO_WAYPOINTS) || Misc.hasEffect(mc.player, Effects.NO_WAYPOINTS_HARMFUL)) ? false : true;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public int getOrder() {
        return 100;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/render/world/WaypointWorldRenderer$Builder.class */
    public static final class Builder {
        private Builder() {
        }

        private Builder setDefault() {
            return this;
        }

        public WaypointWorldRenderer build() {
            WaypointWorldRenderContext context = new WaypointWorldRenderContext();
            return new WaypointsIngameRenderer(new WaypointWorldRenderReader(context), new WaypointWorldRenderProvider(), context, new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
        }

        public static Builder begin() {
            return new Builder().setDefault();
        }
    }
}
