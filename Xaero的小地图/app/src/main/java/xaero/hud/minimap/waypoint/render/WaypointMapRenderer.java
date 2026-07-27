package xaero.hud.minimap.waypoint.render;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import xaero.common.HudMod;
import xaero.common.IXaeroMinimap;
import xaero.common.effect.Effects;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.graphics.renderer.multitexture.MultiTextureRenderTypeRendererProvider;
import xaero.common.minimap.element.render.MinimapElementRenderer;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.common.minimap.waypoints.Waypoint;
import xaero.common.minimap.waypoints.WaypointUtil;
import xaero.common.minimap.waypoints.WaypointsManager;
import xaero.common.minimap.waypoints.render.WaypointGuiRenderContext;
import xaero.common.misc.Misc;
import xaero.hud.minimap.BuiltInHudModules;
import xaero.hud.minimap.common.config.option.MinimapProfiledConfigOptions;
import xaero.hud.minimap.config.util.MinimapConfigClientUtils;
import xaero.hud.minimap.element.render.MinimapElementRenderInfo;
import xaero.hud.minimap.element.render.MinimapElementRenderLocation;
import xaero.hud.minimap.module.MinimapSession;
import xaero.hud.minimap.waypoint.WaypointPurpose;
import xaero.hud.minimap.waypoint.WaypointSession;
import xaero.hud.minimap.world.MinimapWorld;
import xaero.hud.render.util.RenderBufferUtil;
import xaero.lib.client.config.ClientConfigManager;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/render/WaypointMapRenderer.class */
public abstract class WaypointMapRenderer extends MinimapElementRenderer<Waypoint, WaypointMapRenderContext> {
    private MinimapRendererHelper helper;
    private int scale;
    private boolean temporaryWaypointsGlobal;
    private double waypointsDistance;
    private boolean dimensionScaleDistance;
    private int opacity;
    private MultiBufferSource.BufferSource minimapBufferSource;
    private VertexConsumer texturedIconConsumer;
    private VertexConsumer waypointBackgroundConsumer;

    protected WaypointMapRenderer(WaypointReader elementReader, WaypointRenderProvider provider, WaypointMapRenderContext context) {
        super(elementReader, provider, context);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.common.minimap.element.render.MinimapElementRenderer, xaero.hud.minimap.element.render.MinimapElementRenderer
    public boolean renderElement(Waypoint w, boolean highlighted, boolean outOfBounds, double optionalDepth, float optionalScale, double partialX, double partialY, MinimapElementRenderInfo renderInfo, GuiGraphics guiGraphics, MultiBufferSource.BufferSource vanillaBufferSource) {
        double waypointPosDivider = renderInfo.backgroundCoordinateScale / ((WaypointMapRenderContext) this.context).dimCoordinateScale;
        double wX = w.getX(waypointPosDivider) + 0.5d;
        double wZ = w.getZ(waypointPosDivider) + 0.5d;
        double offX = wX - renderInfo.renderPos.x;
        double offZ = wZ - renderInfo.renderPos.z;
        double distance2D = Math.sqrt((offX * offX) + (offZ * offZ));
        double distanceScale = this.dimensionScaleDistance ? renderInfo.backgroundCoordinateScale : 1.0d;
        double scaledDistance2D = distance2D * distanceScale;
        if (!w.isDestination() && w.getPurpose() != WaypointPurpose.DEATH && !w.isGlobal() && ((!w.isTemporary() || !this.temporaryWaypointsGlobal) && this.waypointsDistance != 0.0d && scaledDistance2D > this.waypointsDistance)) {
            return false;
        }
        PoseStack matrixStack = guiGraphics.pose();
        MinimapElementRenderLocation location = renderInfo.location;
        matrixStack.translate(-1.0d, -1.0d, optionalDepth);
        if (this.scale <= 0 || location != MinimapElementRenderLocation.OVER_MINIMAP) {
            matrixStack.scale(optionalScale, optionalScale, 1.0f);
        } else {
            matrixStack.scale(this.scale, this.scale, 1.0f);
        }
        drawIconOnGUI(guiGraphics, this.helper, w, 0, 0, this.opacity, this.minimapBufferSource, this.waypointBackgroundConsumer, this.texturedIconConsumer);
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // xaero.common.minimap.element.render.MinimapElementRenderer, xaero.hud.minimap.element.render.MinimapElementRenderer
    public void preRender(MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        RenderSystem.disableDepthTest();
        vanillaBufferSource.endBatch();
        this.minimapBufferSource = HudMod.INSTANCE.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
        this.waypointBackgroundConsumer = this.minimapBufferSource.getBuffer(CustomRenderTypes.COLORED_WAYPOINTS_BGS);
        this.texturedIconConsumer = this.minimapBufferSource.getBuffer(CustomRenderTypes.GUI_NEAREST);
        this.helper = HudMod.INSTANCE.getMinimap().getMinimapFBORenderer().getHelper();
        MinimapSession session = (MinimapSession) BuiltInHudModules.MINIMAP.getCurrentSession();
        MinimapWorld currentWorld = session.getWorldManager().getCurrentWorld();
        ((WaypointMapRenderContext) this.context).dimCoordinateScale = session.getDimensionHelper().getDimCoordinateScale(currentWorld);
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        this.scale = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_ICON_SCALE_ON_MINIMAP)).intValue();
        if (this.scale > 0) {
            this.scale = (int) MinimapConfigClientUtils.getUIScale(configManager, MinimapProfiledConfigOptions.WAYPOINT_ICON_SCALE_ON_MINIMAP);
        }
        this.temporaryWaypointsGlobal = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.TEMPORARY_WAYPOINTS_GLOBAL)).booleanValue();
        this.waypointsDistance = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_MAX_DISTANCE)).intValue();
        this.dimensionScaleDistance = ((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_MAX_DISTANCE_DIMENSION_SCALE)).booleanValue();
        this.opacity = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_OPACITY_ON_MINIMAP)).intValue();
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderer, xaero.hud.minimap.element.render.MinimapElementRenderer
    public void postRender(MinimapElementRenderInfo renderInfo, MultiBufferSource.BufferSource vanillaBufferSource, MultiTextureRenderTypeRendererProvider multiTextureRenderTypeRenderers) {
        this.minimapBufferSource.endBatch();
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(515);
        this.waypointBackgroundConsumer = null;
    }

    @Deprecated
    public void updateWaypointCollection() {
    }

    @Deprecated
    public void drawIconOnGUI(GuiGraphics guiGraphics, MinimapRendererHelper rendererHelper, Waypoint w, int drawX, int drawY, MultiBufferSource.BufferSource renderTypeBuffer, VertexConsumer waypointBackgroundConsumer, VertexConsumer texturedIconConsumer) {
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        int opacity = ((Integer) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINT_OPACITY_ON_MINIMAP)).intValue();
        drawIconOnGUI(guiGraphics, rendererHelper, w, drawX, drawY, opacity, renderTypeBuffer, waypointBackgroundConsumer, texturedIconConsumer);
    }

    public void drawIconOnGUI(GuiGraphics guiGraphics, MinimapRendererHelper rendererHelper, Waypoint w, int drawX, int drawY, int opacity, MultiBufferSource.BufferSource renderTypeBuffer, VertexConsumer waypointBackgroundConsumer, VertexConsumer texturedIconConsumer) {
        PoseStack matrixStack = guiGraphics.pose();
        int color = w.getWaypointColor().getHex();
        int r = (color >> 16) & 255;
        int g = (color >> 8) & 255;
        int b = color & 255;
        float a = opacity / 100.0f;
        int initialsWidth = w.getPurpose() == WaypointPurpose.DEATH ? 7 : Minecraft.getInstance().font.width(w.getInitials());
        int addedFrame = WaypointUtil.getAddedMinimapIconFrame(initialsWidth);
        int rectX1 = (drawX - 4) - addedFrame;
        int rectY1 = drawY - 4;
        int rectX2 = drawX + 5 + addedFrame;
        int rectY2 = drawY + 5;
        RenderBufferUtil.addColoredRect(matrixStack.last().pose(), waypointBackgroundConsumer, rectX1, rectY1, rectX2 - rectX1, rectY2 - rectY1, r / 255.0f, g / 255.0f, b / 255.0f, a);
        if (w.getPurpose() == WaypointPurpose.DEATH) {
            RenderBufferUtil.addTexturedColoredRect(matrixStack.last().pose(), texturedIconConsumer, rectX1 + 1, rectY1 + 1, 0, 87, 9, 9, 9, -9, 0.2431f, 0.2431f, 0.2431f, 1.0f, 256.0f);
            RenderBufferUtil.addTexturedColoredRect(matrixStack.last().pose(), texturedIconConsumer, rectX1, rectY1, 0, 87, 9, 9, 9, -9, 0.9882f, 0.9882f, 0.9882f, 1.0f, 256.0f);
        } else {
            Misc.drawNormalText(matrixStack, w.getInitials(), (drawX + 1) - (initialsWidth / 2), drawY - 3, -1, true, renderTypeBuffer);
        }
    }

    @Deprecated
    public void drawSetChange(WaypointsManager waypointsManager, GuiGraphics guiGraphics, Window res) {
        drawSetChange((MinimapSession) waypointsManager, guiGraphics, res);
    }

    public void drawSetChange(MinimapSession session, GuiGraphics guiGraphics, Window res) {
        MinimapWorld minimapWorld = session.getWorldManager().getCurrentWorld();
        if (minimapWorld == null) {
            return;
        }
        WaypointSession waypointSession = session.getWaypointSession();
        if (waypointSession.getSetChangedTime() == 0) {
            return;
        }
        int passed = (int) (System.currentTimeMillis() - waypointSession.getSetChangedTime());
        if (passed >= 1500) {
            waypointSession.setSetChangedTime(0L);
            return;
        }
        boolean fading = passed > 1500 - 300;
        float fadeFactor = fading ? (1500 - passed) / 300 : 1.0f;
        int alpha = 3 + ((int) (252.0f * fadeFactor));
        int c = 16777215 | (alpha << 24);
        MultiBufferSource.BufferSource renderBuffers = HudMod.INSTANCE.getHudRenderer().getCustomVertexConsumers().getBetterPVPRenderTypeBuffers();
        Misc.drawCenteredPiercingText(guiGraphics.pose(), I18n.get(minimapWorld.getCurrentWaypointSet().getName(), new Object[0]), res.getGuiScaledWidth() / 2, (res.getGuiScaledHeight() / 2) + 50, c, true, renderBuffers);
        renderBuffers.endBatch();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 771);
    }

    @Override // xaero.common.minimap.element.render.MinimapElementRenderer, xaero.hud.minimap.element.render.MinimapElementRenderer
    public boolean shouldRender(MinimapElementRenderLocation location) {
        ClientConfigManager configManager = HudMod.INSTANCE.getHudConfigs().getClientConfigManager();
        if (((location == MinimapElementRenderLocation.OVER_MINIMAP || location == MinimapElementRenderLocation.IN_MINIMAP) && !((Boolean) configManager.getEffective(MinimapProfiledConfigOptions.WAYPOINTS_ON_MINIMAP)).booleanValue()) || Misc.hasEffect(Effects.NO_WAYPOINTS) || Misc.hasEffect(Effects.NO_WAYPOINTS_HARMFUL)) {
            return false;
        }
        return true;
    }

    @Override // xaero.hud.minimap.element.render.MinimapElementRenderer
    public int getOrder() {
        return 100;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/waypoint/render/WaypointMapRenderer$Builder.class */
    public static final class Builder {
        private WaypointDeleter waypointDeleter;
        private final IXaeroMinimap modMain;

        private Builder(IXaeroMinimap modMain) {
            this.modMain = modMain;
        }

        private Builder setDefault() {
            setWaypointDeleter(null);
            return this;
        }

        public Builder setWaypointDeleter(WaypointDeleter waypointDeleter) {
            this.waypointDeleter = waypointDeleter;
            return this;
        }

        public WaypointMapRenderer build() {
            if (this.waypointDeleter == null) {
                throw new IllegalStateException();
            }
            WaypointGuiRenderContext context = new WaypointGuiRenderContext();
            return new xaero.common.minimap.waypoints.render.WaypointsGuiRenderer(new xaero.common.minimap.waypoints.render.WaypointReader(), new xaero.common.minimap.waypoints.render.WaypointRenderProvider(), context);
        }

        public static Builder begin(IXaeroMinimap modMain) {
            return new Builder(modMain).setDefault();
        }
    }
}
