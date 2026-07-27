package xaero.hud.minimap.radar.icon.creator.render.form.model;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import xaero.common.exception.OpenGLException;
import xaero.common.graphics.CustomRenderTypes;
import xaero.common.misc.OptimizedMath;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.radar.icon.creator.RadarIconCreator;
import xaero.hud.minimap.radar.icon.creator.entity.LivingEntityPoseResetter;
import xaero.hud.minimap.radar.icon.creator.render.form.IRadarIconFormPrerenderer;
import xaero.hud.minimap.radar.icon.creator.render.form.model.RadarIconModelPrerenderer;
import xaero.hud.minimap.radar.icon.creator.render.form.model.custom.RadarIconCustomPrerenderer;
import xaero.hud.minimap.radar.icon.creator.render.form.model.part.RadarIconModelPartPrerenderer;
import xaero.hud.minimap.radar.icon.creator.render.trace.ModelRenderTrace;
import xaero.hud.minimap.radar.icon.definition.BuiltInRadarIconDefinitions;
import xaero.hud.minimap.radar.icon.definition.form.model.RadarIconModelForm;
import xaero.hud.minimap.radar.icon.definition.form.model.config.RadarIconModelConfig;
import xaero.lib.client.graphics.util.ImmediateRenderUtil;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/RadarIconModelFormPrerenderer.class */
public class RadarIconModelFormPrerenderer implements IRadarIconFormPrerenderer {
    private ModelRenderTrace mainModelTrace;
    private ModelPart mainPart;
    private List<String> hardcodedMainPartAliases;
    private List<String> hardcodedModelPartsFields;
    private boolean forceFieldCheck;
    private boolean fullModelIcon;
    private MultiBufferSource.BufferSource entityIconRenderTypeBuffer = MultiBufferSource.immediate(new ByteBufferBuilder(256));
    private final LivingEntityPoseResetter livingEntityPoseResetter = new LivingEntityPoseResetter();
    private final ArrayList<ModelPart> mainRenderedModels = new ArrayList<>();
    private final RadarIconModelPrerenderer modelPrerenderer = new RadarIconModelPrerenderer();

    @Override // xaero.hud.minimap.radar.icon.creator.render.form.IRadarIconFormPrerenderer
    public boolean requiresEntityModel() {
        return true;
    }

    @Override // xaero.hud.minimap.radar.icon.creator.render.form.IRadarIconFormPrerenderer
    public boolean isFlipped() {
        return false;
    }

    @Override // xaero.hud.minimap.radar.icon.creator.render.form.IRadarIconFormPrerenderer
    public boolean isOutlined() {
        return true;
    }

    @Override // xaero.hud.minimap.radar.icon.creator.render.form.IRadarIconFormPrerenderer
    public <T extends Entity> boolean prerender(GuiGraphics guiGraphics, EntityRenderer<? super T> entityRenderer, @Nullable EntityModel<T> entityModel, T entity, @Nullable List<ModelRenderTrace> traceList, RadarIconCreator.Parameters parameters) {
        PoseStack matrixStack = guiGraphics.pose();
        RadarIconModelForm modelForm = (RadarIconModelForm) parameters.form;
        GlStateManager._enableCull();
        MultiBufferSource.BufferSource renderTypeBuffer = this.entityIconRenderTypeBuffer;
        GlStateManager._disableBlend();
        GlStateManager._enableBlend();
        GlStateManager._disableDepthTest();
        GlStateManager._enableCull();
        GlStateManager._depthFunc(515);
        Lighting.setupForFlatItems();
        if (parameters.debug) {
            matrixStack.pushPose();
            matrixStack.translate(0.0f, 10.0f, -10.0f);
            matrixStack.scale(1.0f, 1.0f, 1.0f);
            ImmediateRenderUtil.coloredRectangle(matrixStack, 0.0f, 0.0f, 9.0f, 9.0f, -65536);
            matrixStack.popPose();
            GlStateManager._enableBlend();
            RenderSystem.blendFuncSeparate(770, 771, 1, 771);
        }
        RadarIconModelConfig config = parameters.defaultModelConfig;
        RadarIconModelConfig variantModelConfig = modelForm.getConfig();
        if (variantModelConfig != null) {
            config = variantModelConfig;
        }
        matrixStack.pushPose();
        matrixStack.translate(32.0f, 32.0f, -450.0f);
        matrixStack.translate(config.offsetX, config.offsetY, 0.0f);
        matrixStack.scale(32, 32, -32);
        float scale = parameters.scale;
        if (scale < 1.0f) {
            matrixStack.scale(scale, scale, scale);
        }
        matrixStack.scale(config.baseScale, config.baseScale, config.baseScale);
        OptimizedMath.rotatePose(matrixStack, config.rotationY, OptimizedMath.YP);
        OptimizedMath.rotatePose(matrixStack, config.rotationX, OptimizedMath.XP);
        OptimizedMath.rotatePose(matrixStack, config.rotationZ, OptimizedMath.ZP);
        BuiltInRadarIconDefinitions.defaultTransformation(matrixStack, entityModel, entity);
        LivingEntity livingEntity = entity instanceof LivingEntity ? (LivingEntity) entity : null;
        if (livingEntity != null) {
            this.livingEntityPoseResetter.storeAndReset(livingEntity);
        }
        boolean isChildBU = entityModel.young;
        entityModel.young = false;
        boolean result = renderLayers(matrixStack, renderTypeBuffer, entityRenderer, entityModel, traceList, entity, config, parameters.defaultModelConfig);
        entityModel.young = isChildBU;
        if (livingEntity != null) {
            this.livingEntityPoseResetter.restore(livingEntity);
        }
        BuiltInRadarIconDefinitions.defaultPostIconModelRender(matrixStack, entityModel, entity);
        matrixStack.popPose();
        if (parameters.debug) {
            matrixStack.pushPose();
            matrixStack.translate(9.0f, 10.0f, -10.0f);
            matrixStack.scale(1.0f, 1.0f, 1.0f);
            ImmediateRenderUtil.coloredRectangle(matrixStack, 0.0f, 0.0f, 9.0f, 9.0f, -16711936);
            matrixStack.popPose();
            RenderSystem.blendFuncSeparate(770, 771, 1, 771);
        }
        return result;
    }

    private <T extends Entity> boolean renderLayers(PoseStack matrixStack, MultiBufferSource.BufferSource bufferSource, EntityRenderer<? super T> entityRenderer, EntityModel<T> mainEntityModel, List<ModelRenderTrace> traceList, T entity, RadarIconModelConfig config, RadarIconModelConfig defaultConfig) {
        boolean zBooleanValue;
        this.forceFieldCheck = (config.renderingFullModel == null || !config.renderingFullModel.booleanValue()) && (config.modelPartsFields != null || BuiltInRadarIconDefinitions.forceFieldCheck(mainEntityModel));
        if (config.renderingFullModel == null) {
            zBooleanValue = !this.forceFieldCheck && BuiltInRadarIconDefinitions.fullModelIcon(mainEntityModel);
        } else {
            zBooleanValue = config.renderingFullModel.booleanValue();
        }
        this.fullModelIcon = zBooleanValue;
        boolean renderedSomething = false;
        if (traceList.isEmpty()) {
            addDefaultLayer(traceList, entityRenderer, mainEntityModel, entity);
        }
        boolean allEmpty = true;
        Iterator<ModelRenderTrace> it = traceList.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            if (!it.next().isEmpty()) {
                allEmpty = false;
                break;
            }
        }
        if (allEmpty) {
            Iterator<ModelRenderTrace> it2 = traceList.iterator();
            while (it2.hasNext()) {
                it2.next().allVisible = true;
            }
        }
        this.mainPart = null;
        this.mainModelTrace = null;
        this.hardcodedMainPartAliases = BuiltInRadarIconDefinitions.getMainModelPartFields(entityRenderer, mainEntityModel, entity);
        this.hardcodedModelPartsFields = BuiltInRadarIconDefinitions.getSecondaryModelPartsFields(entityRenderer, mainEntityModel, entity);
        this.mainRenderedModels.clear();
        for (ModelRenderTrace mrt : traceList) {
            if (!mrt.isEmpty() && (!renderedSomething || config.layersAllowed)) {
                int result = renderLayer(matrixStack, bufferSource, mrt, mainEntityModel, entity, config, defaultConfig);
                if (result == -1) {
                    break;
                }
                if (result == 1) {
                    renderedSomething = true;
                }
            }
        }
        this.hardcodedMainPartAliases = null;
        this.hardcodedModelPartsFields = null;
        if (this.mainRenderedModels.isEmpty() || !config.layersAllowed) {
            return renderedSomething;
        }
        RadarIconCustomPrerenderer extraLayer = BuiltInRadarIconDefinitions.getCustomLayer(entityRenderer, entity);
        RadarIconModelPartPrerenderer partPrerenderer = this.modelPrerenderer.getPartPrerenderer();
        if (extraLayer != null) {
            this.mainPart = extraLayer.render(matrixStack, bufferSource, entityRenderer, entity, mainEntityModel, partPrerenderer, this.mainRenderedModels, this.mainPart, config, this.mainModelTrace);
        }
        return renderedSomething;
    }

    private <T extends Entity> void addDefaultLayer(List<ModelRenderTrace> traceList, EntityRenderer<? super T> entityRenderer, EntityModel<T> mainEntityModel, T entity) {
        ResourceLocation mainEntityTexture = null;
        try {
            mainEntityTexture = entityRenderer.getTextureLocation(entity);
        } catch (Throwable t) {
            MinimapLogs.LOGGER.error("Couldn't fetch main entity texture when prerendering an icon with nothing detected!", t);
        }
        if (mainEntityTexture == null) {
            return;
        }
        traceList.add(new ModelRenderTrace(mainEntityModel, mainEntityTexture, null, CustomRenderTypes.getBasicEntityIconLayerPhases(mainEntityTexture), -1));
    }

    private <T extends Entity> int renderLayer(PoseStack matrixStack, MultiBufferSource.BufferSource bufferSource, ModelRenderTrace mrt, EntityModel<T> mainEntityModel, T entity, RadarIconModelConfig config, RadarIconModelConfig defaultConfig) {
        EntityModel<?> traceModel = mrt.model;
        if ((entity instanceof Player) && (traceModel instanceof HumanoidArmorModel)) {
            return 0;
        }
        ResourceLocation traceTexture = mrt.renderTexture;
        TextureAtlasSprite traceAtlasSprite = mrt.renderAtlasSprite;
        boolean mainModel = traceModel == mainEntityModel;
        boolean mainPartsVisibility = mainModel && this.mainModelTrace != null && mrt.sameVisibility(this.mainModelTrace);
        if (mainModel && !mainPartsVisibility) {
            if (traceTexture == null) {
                return 0;
            }
            if (!resetModelRotations(entity, traceModel)) {
                return -1;
            }
            this.mainRenderedModels.clear();
            RadarIconModelPrerenderer.Parameters parameters = new RadarIconModelPrerenderer.Parameters(config, defaultConfig, traceTexture, traceAtlasSprite, mrt, this.forceFieldCheck, this.fullModelIcon, this.hardcodedMainPartAliases, this.hardcodedModelPartsFields, this.mainRenderedModels);
            this.mainPart = this.modelPrerenderer.renderModel(matrixStack, bufferSource, traceModel, entity, this.mainPart, parameters);
            this.mainModelTrace = mrt;
            if (!this.mainRenderedModels.isEmpty()) {
                return 1;
            }
            return 0;
        }
        if (!mainModel) {
            if (traceTexture == null) {
                return 0;
            }
            if (!resetModelRotations(entity, traceModel)) {
                return -1;
            }
            ArrayList<ModelPart> renderedModels = new ArrayList<>();
            RadarIconModelPrerenderer.Parameters parameters2 = new RadarIconModelPrerenderer.Parameters(config, defaultConfig, traceTexture, traceAtlasSprite, mrt, this.forceFieldCheck, this.fullModelIcon, this.hardcodedMainPartAliases, this.hardcodedModelPartsFields, renderedModels);
            this.mainPart = this.modelPrerenderer.renderModel(matrixStack, bufferSource, traceModel, entity, this.mainPart, parameters2);
            if (!renderedModels.isEmpty()) {
                return 1;
            }
            return 0;
        }
        if (this.mainRenderedModels.isEmpty()) {
            return 0;
        }
        VertexConsumer vertexConsumer = this.modelPrerenderer.getLayerModelVertexConsumer(bufferSource, traceTexture, traceAtlasSprite, mrt);
        RadarIconModelPartPrerenderer.Parameters parameters3 = new RadarIconModelPartPrerenderer.Parameters(config, mrt, new ArrayList());
        this.modelPrerenderer.getPartPrerenderer().renderPartsIterable(this.mainRenderedModels, matrixStack, vertexConsumer, this.mainPart, parameters3);
        bufferSource.endBatch();
        return 0;
    }

    private <T extends Entity> boolean resetModelRotations(Entity entity, EntityModel<T> model) {
        try {
            model.prepareMobModel(entity, 0.0f, 0.0f, 1.0f);
            model.setupAnim(entity, 0.0f, 0.0f, entity.tickCount, 0.0f, 0.0f);
            OpenGLException.checkGLError();
            return true;
        } catch (Throwable t) {
            MinimapLogs.LOGGER.error("suppressed exception", t);
            return false;
        }
    }
}
