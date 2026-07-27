package xaero.hud.minimap.radar.icon.creator.render.trace;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SpriteCoordinateExpander;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.lwjgl.opengl.GL11;
import xaero.common.core.IBufferSource;
import xaero.common.exception.OpenGLException;
import xaero.common.graphics.CustomRenderTypes;
import xaero.hud.minimap.MinimapLogs;
import xaero.lib.client.graphics.XaeroRenderType;
import xaero.lib.common.reflection.util.ReflectionUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/trace/EntityRenderTracer.class */
public class EntityRenderTracer {
    public static boolean TRACING_MODEL_RENDERS;
    private Entity tracedEntity;
    private EntityRenderer tracedEntityRenderer;
    private Class<?> tracedEntityModelClass;
    private List<ModelRenderTrace> traceResultList;
    private ModelRenderTrace lastModelRenderDetected;
    private MultiBufferSource.BufferSource modelRenderDetectionRenderTypeBuffer;
    private Class<?> compositeRenderTypeClass;
    private Field enderDragonModelField;
    private Field stateField;
    private Class<?> textureStateShardClass;
    private Field compositeStateTextureStateField;
    private Field textureStateShardTextureField;
    private Field compositeStateDepthTestStateField;
    private Field compositeStateWriteMaskStateField;
    private Field compositeStateCullStateField;
    private Field compositeStateTransparencyStateField;
    private Field compositeStateShaderStateField;
    private Field renderBuffersBufferSourceField;
    private Field spriteCoordinateExpanderSpriteField;
    private Class<?> irisRenderLayerWrapperClass;
    private Method irisRenderLayerWrapperUnwrapMethod;

    public EntityRenderTracer() {
        initReflection();
        this.traceResultList = new ArrayList();
    }

    public <T extends Entity> List<ModelRenderTrace> trace(PoseStack matrixStack, T entity, EntityRenderer<? super T> entityRenderer) {
        TRACING_MODEL_RENDERS = true;
        this.tracedEntity = entity;
        this.tracedEntityRenderer = entityRenderer;
        this.tracedEntityModelClass = null;
        this.traceResultList.clear();
        this.lastModelRenderDetected = null;
        PoseStack.Pose matrixEntryToRestore = matrixStack.last();
        matrixStack.pushPose();
        try {
            IBufferSource iBufferSource = (MultiBufferSource.BufferSource) ReflectionUtils.getReflectFieldValue(Minecraft.getInstance().renderBuffers(), this.renderBuffersBufferSourceField);
            this.modelRenderDetectionRenderTypeBuffer = iBufferSource;
            iBufferSource.setXaero_lastRenderType(null);
            Minecraft mc = Minecraft.getInstance();
            EntityRenderDispatcher renderDispatcher = mc.getEntityRenderDispatcher();
            renderDispatcher.render(entity, 0.0d, 0.0d, 0.0d, 0.0f, 1.0f, matrixStack, iBufferSource, 0);
            iBufferSource.endBatch();
            OpenGLException.checkGLError();
        } catch (Throwable e) {
            this.traceResultList.clear();
            MinimapLogs.LOGGER.error("Exception when calling the full entity renderer for {} before prerendering the icon.", entity.getScoreboardName(), e);
        }
        TRACING_MODEL_RENDERS = false;
        this.tracedEntity = null;
        this.tracedEntityRenderer = null;
        while (matrixStack.last() != matrixEntryToRestore) {
            matrixStack.popPose();
        }
        while (GL11.glGetError() != 0) {
        }
        return this.traceResultList;
    }

    public void onModelRender(EntityModel<?> model, VertexConsumer vertexConsumer, int color) throws IllegalAccessException, IllegalArgumentException {
        Object renderState;
        this.lastModelRenderDetected = null;
        if (this.tracedEntityModelClass == null) {
            EntityModel<?> currentMainModel = getEntityRendererModel(this.tracedEntityRenderer);
            this.tracedEntityModelClass = currentMainModel == null ? null : currentMainModel.getClass();
            if (this.tracedEntityModelClass == null) {
                return;
            }
        }
        if (!model.getClass().isAssignableFrom(this.tracedEntityModelClass) && !this.tracedEntityModelClass.isAssignableFrom(model.getClass())) {
            return;
        }
        RenderType lastRenderType = getLastRenderType(this.modelRenderDetectionRenderTypeBuffer);
        if (lastRenderType == null && this.traceResultList.isEmpty()) {
            ResourceLocation textureLocation = null;
            try {
                ResourceLocation textureLocationUnchecked = this.tracedEntityRenderer.getTextureLocation(this.tracedEntity);
                textureLocation = textureLocationUnchecked;
            } catch (Throwable t) {
                MinimapLogs.LOGGER.error("Couldn't fetch main entity texture when trying to use an alternative render type for an icon!", t);
            }
            if (textureLocation != null) {
                lastRenderType = model.renderType(textureLocation);
            }
        }
        if (lastRenderType == null || (renderState = getRenderState(lastRenderType)) == null) {
            return;
        }
        Object renderTextureState = ReflectionUtils.getReflectFieldValue(renderState, this.compositeStateTextureStateField);
        ResourceLocation texture = null;
        if (this.textureStateShardClass.isAssignableFrom(renderTextureState.getClass())) {
            texture = getRenderStateTextureStateTexture(renderTextureState);
        }
        Object renderTransparencyState = ReflectionUtils.getReflectFieldValue(renderState, this.compositeStateTransparencyStateField);
        Object renderTransparencyState2 = fixAdditiveTransparency(renderTransparencyState);
        Object renderDepthTestState = ReflectionUtils.getReflectFieldValue(renderState, this.compositeStateDepthTestStateField);
        Object renderWriteMaskState = ReflectionUtils.getReflectFieldValue(renderState, this.compositeStateWriteMaskStateField);
        Object renderCullState = ReflectionUtils.getReflectFieldValue(renderState, this.compositeStateCullStateField);
        Object renderShaderState = ReflectionUtils.getReflectFieldValue(renderState, this.compositeStateShaderStateField);
        CustomRenderTypes.EntityIconLayerPhases layerPhases = new CustomRenderTypes.EntityIconLayerPhases(renderTextureState, renderTransparencyState2, renderDepthTestState, renderWriteMaskState, renderCullState, renderShaderState);
        TextureAtlasSprite renderAtlasSprite = null;
        if (vertexConsumer instanceof SpriteCoordinateExpander) {
            renderAtlasSprite = (TextureAtlasSprite) ReflectionUtils.getReflectFieldValue(vertexConsumer, this.spriteCoordinateExpanderSpriteField);
        }
        this.lastModelRenderDetected = new ModelRenderTrace(model, texture, renderAtlasSprite, layerPhases, color);
        this.traceResultList.add(this.lastModelRenderDetected);
    }

    private Object fixAdditiveTransparency(Object renderTransparencyState) {
        int blendSrcFactor;
        Object result = renderTransparencyState;
        ((RenderStateShard) renderTransparencyState).setupRenderState();
        int blendDestFactor = GL11.glGetInteger(32968);
        if (blendDestFactor == 1 && (blendSrcFactor = GL11.glGetInteger(32969)) != 0) {
            result = XaeroRenderType.getTransparencyPhase(blendSrcFactor, blendDestFactor, 0, 1);
        }
        ((RenderStateShard) renderTransparencyState).clearRenderState();
        return result;
    }

    public void onModelPartRender(ModelPart modelRenderer, int color) {
        if (this.lastModelRenderDetected != null) {
            this.lastModelRenderDetected.addVisibleModelPart(modelRenderer, color);
        }
    }

    private RenderType getLastRenderType(MultiBufferSource renderTypeBuffer) {
        if (renderTypeBuffer instanceof IBufferSource) {
            IBufferSource xaeroBufferSource = (IBufferSource) renderTypeBuffer;
            return xaeroBufferSource.getXaero_lastRenderType();
        }
        return null;
    }

    private Object getRenderState(RenderType renderType) {
        while (renderType.getClass() == this.irisRenderLayerWrapperClass && this.irisRenderLayerWrapperUnwrapMethod != null) {
            renderType = (RenderType) ReflectionUtils.getReflectMethodValue(renderType, this.irisRenderLayerWrapperUnwrapMethod, new Object[0]);
        }
        if (renderType.getClass() == this.compositeRenderTypeClass) {
            return ReflectionUtils.getReflectFieldValue(renderType, this.stateField);
        }
        return null;
    }

    private ResourceLocation getRenderStateTextureStateTexture(Object renderTextureState) throws IllegalAccessException, IllegalArgumentException {
        Object renderStateTextureObject = ReflectionUtils.getReflectFieldValue(renderTextureState, this.textureStateShardTextureField);
        if (!(renderStateTextureObject instanceof Optional)) {
            return (ResourceLocation) renderStateTextureObject;
        }
        Optional optional = (Optional) renderStateTextureObject;
        return (ResourceLocation) optional.orElse(null);
    }

    public <T extends Entity> EntityModel<T> getEntityRendererModel(EntityRenderer<? super T> entityRenderer) {
        if (entityRenderer instanceof LivingEntityRenderer) {
            return ((LivingEntityRenderer) entityRenderer).getModel();
        }
        if (entityRenderer instanceof EnderDragonRenderer) {
            return (EntityModel) ReflectionUtils.getReflectFieldValue(entityRenderer, this.enderDragonModelField);
        }
        return null;
    }

    private void initReflection() {
        this.enderDragonModelField = ReflectionUtils.getFieldReflection(EnderDragonRenderer.class, "model", "field_21008", "Lnet/minecraft/class_895$class_625;", "f_114183_");
        try {
            this.compositeRenderTypeClass = ReflectionUtils.getClassForName("net.minecraft.class_1921$class_4687", "net.minecraft.client.renderer.RenderType$CompositeRenderType");
            this.stateField = ReflectionUtils.getFieldReflection(this.compositeRenderTypeClass, "state", "field_21403", "Lnet/minecraft/class_1921$class_4688;", "f_110511_");
            Class<?> compositeStateClass = ReflectionUtils.getClassForName("net.minecraft.class_1921$class_4688", "net.minecraft.client.renderer.RenderType$CompositeState");
            this.textureStateShardClass = ReflectionUtils.getClassForName("net.minecraft.class_4668$class_4683", "net.minecraft.client.renderer.RenderStateShard$TextureStateShard");
            this.compositeStateTextureStateField = ReflectionUtils.getFieldReflection(compositeStateClass, "textureState", "field_21406", "Lnet/minecraft/class_4668$class_5939;", "f_110576_");
            this.textureStateShardTextureField = ReflectionUtils.getFieldReflection(this.textureStateShardClass, "texture", "field_21397", "Ljava/util/Optional;", "f_110328_");
            this.compositeStateTransparencyStateField = ReflectionUtils.getFieldReflection(compositeStateClass, "transparencyState", "field_21407", "Lnet/minecraft/class_4668$class_4685;", "f_110577_");
            this.compositeStateDepthTestStateField = ReflectionUtils.getFieldReflection(compositeStateClass, "depthTestState", "field_21411", "Lnet/minecraft/class_4668$class_4672;", "f_110581_");
            this.compositeStateWriteMaskStateField = ReflectionUtils.getFieldReflection(compositeStateClass, "writeMaskState", "field_21419", "Lnet/minecraft/class_4668$class_4686;", "f_110589_");
            this.compositeStateCullStateField = ReflectionUtils.getFieldReflection(compositeStateClass, "cullState", "field_21412", "Lnet/minecraft/class_4668$class_4671;", "f_110582_");
            this.compositeStateShaderStateField = ReflectionUtils.getFieldReflection(compositeStateClass, "shaderState", "field_29461", "Lnet/minecraft/class_4668$class_5942;", "f_173274_");
            this.renderBuffersBufferSourceField = ReflectionUtils.getFieldReflection(RenderBuffers.class, "bufferSource", "field_46901", "Lnet/minecraft/class_4597$class_4598;", "f_110094_");
            this.spriteCoordinateExpanderSpriteField = ReflectionUtils.getFieldReflection(SpriteCoordinateExpander.class, "sprite", "field_21731", "Lnet/minecraft/class_1058;", "f_110796_");
            try {
                try {
                    this.irisRenderLayerWrapperClass = Class.forName("net.coderbot.iris.layer.IrisRenderTypeWrapper");
                } catch (ClassNotFoundException e) {
                    this.irisRenderLayerWrapperClass = Class.forName("net.coderbot.iris.layer.IrisRenderLayerWrapper");
                }
                this.irisRenderLayerWrapperUnwrapMethod = ReflectionUtils.getMethodReflection(this.irisRenderLayerWrapperClass, "unwrap", "unwrap", "()Lnet/minecraft/class_1921;", "unwrap", new Class[0]);
                MinimapLogs.LOGGER.info("Old Iris detected and supported!");
            } catch (Exception e2) {
            }
        } catch (ClassNotFoundException e22) {
            throw new RuntimeException(e22);
        }
    }
}
