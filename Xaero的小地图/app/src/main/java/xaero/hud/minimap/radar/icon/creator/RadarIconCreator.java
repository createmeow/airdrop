package xaero.hud.minimap.radar.icon.creator;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import xaero.common.exception.OpenGLException;
import xaero.common.graphics.ImprovedFramebuffer;
import xaero.common.icon.XaeroIcon;
import xaero.common.icon.XaeroIconAtlas;
import xaero.common.icon.XaeroIconAtlasManager;
import xaero.common.minimap.render.MinimapRendererHelper;
import xaero.hud.compat.mods.ImmediatelyFastHelper;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.radar.icon.RadarIconManager;
import xaero.hud.minimap.radar.icon.creator.render.form.IRadarIconFormPrerenderer;
import xaero.hud.minimap.radar.icon.creator.render.trace.EntityRenderTracer;
import xaero.hud.minimap.radar.icon.creator.render.trace.ModelRenderTrace;
import xaero.hud.minimap.radar.icon.definition.form.RadarIconForm;
import xaero.hud.minimap.radar.icon.definition.form.model.config.RadarIconModelConfig;
import xaero.lib.client.graphics.util.ImmediateRenderUtil;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/RadarIconCreator.class */
public class RadarIconCreator {
    private static final int PREFERRED_ATLAS_WIDTH = 1024;
    public static final int ICON_WIDTH = 64;
    public static final int FAR_PLANE = 500;
    private ImprovedFramebuffer formRenderFramebuffer;
    private ImprovedFramebuffer iconRenderFramebuffer;
    private ImprovedFramebuffer atlasRenderFramebuffer;
    private final EntityRenderTracer renderTracer = new EntityRenderTracer();
    private final XaeroIconAtlasManager iconAtlasManager;
    private Matrix4f projectionMatrixBackup;
    private VertexSorting vertexSortingBackup;

    public RadarIconCreator() throws OpenGLException {
        int maxTextureSize = GlStateManager._getInteger(3379);
        int atlasTextureSize = (Math.min(maxTextureSize, PREFERRED_ATLAS_WIDTH) / 64) * 64;
        this.iconAtlasManager = new XaeroIconAtlasManager(64, atlasTextureSize, new ArrayList());
        initFramebuffers(atlasTextureSize);
    }

    public <T extends Entity> XaeroIcon create(GuiGraphics guiGraphics, EntityRenderer<? super T> entityRenderer, T entity, RenderTarget defaultFramebuffer, Parameters parameters) throws IllegalAccessException, OpenGLException, IllegalArgumentException {
        IRadarIconFormPrerenderer formPrerenderer = parameters.form.getPrerenderer();
        if (formPrerenderer == null) {
            MinimapLogs.LOGGER.error("Tried prerendering radar icon for {} variant {} but the icon form used doesn't have a prerenderer!", EntityType.getKey(entity.getType()), parameters.variant);
            return RadarIconManager.FAILED;
        }
        OpenGLException.checkGLError();
        PoseStack matrixStack = guiGraphics.pose();
        guiGraphics.flush();
        ImmediatelyFastHelper.triggerBatchingBuffersFlush(guiGraphics);
        this.formRenderFramebuffer.bindAsMainTarget(true);
        setupMatrices(matrixStack, 64, FAR_PLANE);
        OpenGLException.checkGLError();
        List<ModelRenderTrace> traceResult = null;
        EntityModel<T> entityModel = null;
        if (formPrerenderer.requiresEntityModel()) {
            GlStateManager._enableCull();
            if (Minecraft.getInstance().getEntityRenderDispatcher().camera != null) {
                traceResult = this.renderTracer.trace(matrixStack, entity, entityRenderer);
                this.formRenderFramebuffer.bindAsMainTarget(true);
            } else {
                MinimapLogs.LOGGER.info("Render info was null for entity " + entity.getScoreboardName());
            }
            entityModel = this.renderTracer.getEntityRendererModel(entityRenderer);
            if (entityModel == null) {
                endFormRendering();
                bindDefaultFramebuffer(defaultFramebuffer);
                restoreMatrices(matrixStack);
                return RadarIconManager.FAILED;
            }
        }
        boolean formRenderResult = false;
        PoseStack.Pose matrixEntryToRestore = matrixStack.last();
        matrixStack.pushPose();
        try {
            try {
                GlStateManager._clearColor(0.0f, 0.0f, 0.0f, 0.0f);
                GlStateManager._clear(16640, Minecraft.ON_OSX);
                formRenderResult = formPrerenderer.prerender(guiGraphics, entityRenderer, entityModel, entity, traceResult, parameters);
                guiGraphics.flush();
                while (matrixStack.last() != matrixEntryToRestore) {
                    matrixStack.popPose();
                }
            } catch (Throwable t) {
                MinimapLogs.LOGGER.error("Exception using the radar icon form prerenderer for entity {} variant {}!", EntityType.getKey(entity.getType()), parameters.variant, t);
                guiGraphics.flush();
                while (matrixStack.last() != matrixEntryToRestore) {
                    matrixStack.popPose();
                }
            }
            endFormRendering();
            XaeroIcon icon = parameters.form.getFailureResult();
            if (formRenderResult) {
                icon = getFinalIcon(matrixStack, entity, formPrerenderer, parameters);
            }
            restoreMatrices(matrixStack);
            this.atlasRenderFramebuffer.unbindWrite();
            bindDefaultFramebuffer(defaultFramebuffer);
            return icon;
        } catch (Throwable th) {
            guiGraphics.flush();
            while (matrixStack.last() != matrixEntryToRestore) {
                matrixStack.popPose();
            }
            throw th;
        }
    }

    private XaeroIcon getFinalIcon(PoseStack matrixStack, Entity entity, IRadarIconFormPrerenderer formPrerenderer, Parameters parameters) throws IllegalAccessException, IllegalArgumentException {
        this.iconRenderFramebuffer.bindAsMainTarget(true);
        GlStateManager._clearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GlStateManager._clear(16384, Minecraft.ON_OSX);
        GlStateManager._disableBlend();
        if (parameters.debug) {
            matrixStack.pushPose();
            matrixStack.translate(18.0f, 10.0f, -10.0f);
            matrixStack.scale(1.0f, 1.0f, 1.0f);
            ImmediateRenderUtil.coloredRectangle(matrixStack, 0.0f, 0.0f, 9.0f, 9.0f, -16776961);
            matrixStack.popPose();
            RenderSystem.blendFuncSeparate(770, 771, 1, 771);
        }
        this.formRenderFramebuffer.bindRead();
        boolean outlined = formPrerenderer.isOutlined();
        boolean flipped = formPrerenderer.isFlipped();
        if (outlined) {
            renderOutline(matrixStack);
        }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager._disableBlend();
        ImmediateRenderUtil.texturedRect(matrixStack, 0.0f, 0.0f, 0, 0, 64.0f, 64.0f, 64.0f, 64.0f, 0.05f, false);
        RenderSystem.blendFuncSeparate(770, 771, 1, 771);
        if (parameters.debug) {
            matrixStack.pushPose();
            matrixStack.translate(27.0f, 10.0f, -10.0f);
            matrixStack.scale(1.0f, 1.0f, 1.0f);
            ImmediateRenderUtil.coloredRectangle(matrixStack, 0.0f, 0.0f, 9.0f, 9.0f, -16711681);
            matrixStack.popPose();
            RenderSystem.blendFuncSeparate(770, 771, 1, 771);
        }
        GlStateManager._enableBlend();
        this.iconRenderFramebuffer.unbindWrite();
        this.iconRenderFramebuffer.bindRead();
        this.iconRenderFramebuffer.generateMipmaps();
        GlStateManager._bindTexture(0);
        XaeroIcon icon = null;
        try {
            XaeroIconAtlas atlas = getCurrentAtlas();
            icon = atlas.createIcon();
            this.atlasRenderFramebuffer.bindAsMainTarget(false);
            GlStateManager._viewport(icon.getOffsetX(), icon.getOffsetY(), 64, 64);
            this.atlasRenderFramebuffer.setFramebufferTexture(atlas.getTextureId());
            this.atlasRenderFramebuffer.checkStatus();
            this.iconRenderFramebuffer.bindRead();
            GlStateManager._disableBlend();
            if (flipped) {
                ImmediateRenderUtil.texturedRect(matrixStack, 0.0f, 0.0f, 0, 64, 64.0f, 64.0f, -64.0f, 64.0f, -1.0f, false);
            } else {
                ImmediateRenderUtil.texturedRect(matrixStack, 0.0f, 0.0f, 0, 0, 64.0f, 64.0f, 64.0f, 64.0f, -1.0f, false);
            }
            if (parameters.debug) {
                matrixStack.pushPose();
                matrixStack.translate(36.0f, 10.0f, -10.0f);
                matrixStack.scale(1.0f, 1.0f, 1.0f);
                ImmediateRenderUtil.coloredRectangle(matrixStack, 0.0f, 0.0f, 9.0f, 9.0f, -256);
                matrixStack.popPose();
                RenderSystem.blendFuncSeparate(770, 771, 1, 771);
            }
        } catch (Throwable t) {
            MinimapLogs.LOGGER.error("Exception rendering to a entity icon atlas for {} {}!", EntityType.getKey(entity.getType()), parameters.variant, t);
        }
        GlStateManager._enableBlend();
        GlStateManager._bindTexture(0);
        MinimapRendererHelper.restoreDefaultShaderBlendState();
        return icon;
    }

    private void renderOutline(PoseStack matrixStack) {
        RenderSystem.setShaderColor(0.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager._enableBlend();
        RenderSystem.blendFuncSeparate(770, 771, 1, 1);
        for (int shadowOffsetX = -1; shadowOffsetX < 2; shadowOffsetX++) {
            for (int shadowOffsetY = -1; shadowOffsetY < 2; shadowOffsetY++) {
                if (shadowOffsetX != 0 || shadowOffsetY != 0) {
                    ImmediateRenderUtil.drawOutlineLayer(matrixStack, shadowOffsetX, shadowOffsetY, 0, 0, 64.0f, 64.0f, 64.0f, 64.0f, 0.05f);
                }
            }
        }
    }

    private void setupMatrices(PoseStack matrixStack, int finalIconSize, int farPlane) {
        this.projectionMatrixBackup = RenderSystem.getProjectionMatrix();
        this.vertexSortingBackup = RenderSystem.getVertexSorting();
        matrixStack.pushPose();
        matrixStack.setIdentity();
        Matrix4f ortho = new Matrix4f().setOrtho(0.0f, finalIconSize, finalIconSize, 0.0f, -1.0f, farPlane);
        RenderSystem.setProjectionMatrix(ortho, VertexSorting.ORTHOGRAPHIC_Z);
        RenderSystem.getModelViewStack().pushMatrix();
        RenderSystem.getModelViewStack().identity();
        RenderSystem.applyModelViewMatrix();
    }

    private void restoreMatrices(PoseStack matrixStack) {
        matrixStack.popPose();
        RenderSystem.setProjectionMatrix(this.projectionMatrixBackup, this.vertexSortingBackup);
        RenderSystem.getModelViewStack().popMatrix();
        RenderSystem.applyModelViewMatrix();
    }

    private void endFormRendering() {
        this.formRenderFramebuffer.unbindWrite();
        GlStateManager._enableBlend();
        Lighting.setupFor3DItems();
        Lighting.setupForFlatItems();
    }

    private void bindDefaultFramebuffer(RenderTarget defaultFramebuffer) throws IllegalAccessException, IllegalArgumentException {
        if (defaultFramebuffer != null) {
            if (defaultFramebuffer instanceof ImprovedFramebuffer) {
                ((ImprovedFramebuffer) defaultFramebuffer).bindAsMainTarget(true);
                return;
            } else {
                ImprovedFramebuffer.restoreMainRenderTarget();
                defaultFramebuffer.bindWrite(true);
                return;
            }
        }
        this.atlasRenderFramebuffer.bindDefaultFramebuffer(Minecraft.getInstance());
        GlStateManager._viewport(0, 0, Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
    }

    public void clearAtlases() {
        this.iconAtlasManager.clearAtlases();
        this.atlasRenderFramebuffer.setFramebufferTexture(0);
    }

    public EntityRenderTracer getRenderTracer() {
        return this.renderTracer;
    }

    private XaeroIconAtlas getCurrentAtlas() {
        return this.iconAtlasManager.getCurrentAtlas();
    }

    private void initFramebuffers(int atlasTextureSize) throws OpenGLException {
        this.formRenderFramebuffer = new ImprovedFramebuffer(512, 512, true);
        OpenGLException.checkGLError();
        this.iconRenderFramebuffer = new ImprovedFramebuffer(512, 512, false);
        OpenGLException.checkGLError();
        this.formRenderFramebuffer.bindRead();
        OpenGLException.checkGLError();
        GL11.glTexParameteri(3553, 33085, 0);
        GL11.glTexParameterf(3553, 33082, 0.0f);
        GL11.glTexParameterf(3553, 33083, 0.0f);
        GL11.glTexParameterf(3553, 34049, 0.0f);
        GL11.glTexParameteri(3553, 10241, 9728);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10242, 33071);
        GL11.glTexParameteri(3553, 10243, 33071);
        GlStateManager._texImage2D(3553, 0, 32856, this.formRenderFramebuffer.viewWidth, this.formRenderFramebuffer.viewHeight, 0, 32993, 32821, (IntBuffer) null);
        OpenGLException.checkGLError();
        GlStateManager._bindTexture(0);
        this.iconRenderFramebuffer.bindRead();
        GL11.glTexParameteri(3553, 33085, 3);
        GL11.glTexParameterf(3553, 33082, 0.0f);
        GL11.glTexParameterf(3553, 33083, 3.0f);
        GL11.glTexParameterf(3553, 34049, 0.0f);
        GL11.glTexParameteri(3553, 10241, 9984);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10242, 33071);
        GL11.glTexParameteri(3553, 10243, 33071);
        GlStateManager._texImage2D(3553, 0, 32856, this.iconRenderFramebuffer.viewWidth, this.iconRenderFramebuffer.viewHeight, 0, 32993, 32821, (IntBuffer) null);
        OpenGLException.checkGLError();
        GlStateManager._bindTexture(0);
        this.atlasRenderFramebuffer = new ImprovedFramebuffer(atlasTextureSize, atlasTextureSize, false);
        OpenGLException.checkGLError();
        GlStateManager._deleteTexture(this.atlasRenderFramebuffer.getFramebufferTexture());
        this.atlasRenderFramebuffer.setFramebufferTexture(0);
        OpenGLException.checkGLError();
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/RadarIconCreator$Parameters.class */
    public static class Parameters {
        public final Object variant;
        public final float scale;
        public final RadarIconModelConfig defaultModelConfig;
        public final RadarIconForm form;
        public final boolean debug;

        public Parameters(Object variant, RadarIconModelConfig defaultModelConfig, RadarIconForm form, float scale, boolean debug) {
            this.variant = variant;
            this.scale = scale;
            this.defaultModelConfig = defaultModelConfig;
            this.form = form;
            this.debug = debug;
        }
    }
}
