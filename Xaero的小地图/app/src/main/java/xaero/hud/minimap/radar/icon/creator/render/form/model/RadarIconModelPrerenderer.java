package xaero.hud.minimap.radar.icon.creator.render.form.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.apache.logging.log4j.Logger;
import xaero.common.graphics.CustomRenderTypes;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.radar.icon.creator.render.form.model.part.ModelPartUtil;
import xaero.hud.minimap.radar.icon.creator.render.form.model.part.RadarIconModelPartPrerenderer;
import xaero.hud.minimap.radar.icon.creator.render.form.model.part.ResolvedFieldModelPartRenderer;
import xaero.hud.minimap.radar.icon.creator.render.form.model.resolver.RadarIconModelFieldResolver;
import xaero.hud.minimap.radar.icon.creator.render.form.model.resolver.ResolvedFieldModelRootPathListener;
import xaero.hud.minimap.radar.icon.creator.render.trace.ModelRenderTrace;
import xaero.hud.minimap.radar.icon.definition.BuiltInRadarIconDefinitions;
import xaero.hud.minimap.radar.icon.definition.form.model.config.RadarIconModelConfig;
import xaero.lib.common.reflection.util.ReflectionUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/RadarIconModelPrerenderer.class */
public class RadarIconModelPrerenderer {
    private static final Object[] ONE_RENDERER_ARRAY = new Object[1];
    private static final Object[] ONE_OBJECT_ARRAY = new Object[1];
    private final RadarIconModelPartPrerenderer partPrerenderer = new RadarIconModelPartPrerenderer();
    private final ResolvedFieldModelPartRenderer resolvedFieldRenderer = new ResolvedFieldModelPartRenderer();
    private final ResolvedFieldModelRootPathListener modelRootPathListener = new ResolvedFieldModelRootPathListener();
    private ModelPart mainPart;

    public ModelPart renderModel(PoseStack matrixStack, MultiBufferSource.BufferSource bufferSource, EntityModel<?> model, Entity entity, ModelPart mainPart, Parameters parameters) {
        ModelPart fixedModelRoot;
        this.mainPart = mainPart;
        boolean forceFieldCheck = parameters.forceFieldCheck;
        boolean fullModelIcon = parameters.fullModelIcon;
        RadarIconModelConfig config = parameters.config;
        Object modelRoot = null;
        if (config.modelRootPath != null) {
            modelRoot = resolveModelRoot(model, config.modelRootPath, entity);
        }
        if (modelRoot == null) {
            modelRoot = BuiltInRadarIconDefinitions.getModelRoot(model);
        }
        boolean treatAsHierarchicalRoot = false;
        if (config == parameters.defaultConfig && (modelRoot instanceof AgeableListModel) && !(modelRoot instanceof HumanoidModel) && (fixedModelRoot = fixHeadRoot(modelRoot)) != null) {
            modelRoot = fixedModelRoot;
            treatAsHierarchicalRoot = true;
            fullModelIcon = true;
        }
        VertexConsumer vertexConsumer = getLayerModelVertexConsumer(bufferSource, parameters.texture, parameters.textureAtlasSprite, parameters.mrt);
        if (config.modelMainPartFieldAliases != null && !config.modelMainPartFieldAliases.isEmpty()) {
            searchAndRenderFields(matrixStack, vertexConsumer, modelRoot, config.modelMainPartFieldAliases, true, parameters);
        }
        if (!forceFieldCheck && (modelRoot instanceof AgeableListModel)) {
            renderAgeableListModel((AgeableListModel) modelRoot, matrixStack, vertexConsumer, parameters);
            bufferSource.endBatch();
            return this.mainPart;
        }
        if (!forceFieldCheck && ((treatAsHierarchicalRoot || (modelRoot instanceof HierarchicalModel)) && renderHierarchicalModel(modelRoot, treatAsHierarchicalRoot, matrixStack, vertexConsumer, parameters))) {
            bufferSource.endBatch();
            return this.mainPart;
        }
        if (!forceFieldCheck && (modelRoot instanceof ListModel) && fullModelIcon) {
            this.mainPart = this.partPrerenderer.renderDeclaredMethod(matrixStack, vertexConsumer, this.partPrerenderer.listModelPartsMethod, (ListModel) modelRoot, mainPart, parameters);
            bufferSource.endBatch();
            return this.mainPart;
        }
        if (!forceFieldCheck && (modelRoot instanceof HeadedModel)) {
            ModelPart headPart = ((HeadedModel) modelRoot).getHead();
            renderPart(matrixStack, vertexConsumer, headPart, parameters);
        }
        List<String> hardcodedMainPartAliases = parameters.hardcodedMainPartAliases;
        List<String> hardcodedModelPartsFields = parameters.hardcodedModelPartsFields;
        if (config.modelPartsFields == null) {
            searchAndRenderFields(matrixStack, vertexConsumer, modelRoot, hardcodedMainPartAliases, true, parameters);
        }
        List<String> headPartsFields = hardcodedModelPartsFields;
        if (fullModelIcon) {
            headPartsFields = null;
        } else if (config.modelPartsFields != null) {
            headPartsFields = config.modelPartsFields;
        }
        searchAndRenderFields(matrixStack, vertexConsumer, modelRoot, headPartsFields, false, parameters);
        bufferSource.endBatch();
        return this.mainPart;
    }

    private void renderAgeableListModel(AgeableListModel<?> modelRoot, PoseStack matrixStack, VertexConsumer vertexConsumer, Parameters parameters) {
        if (modelRoot instanceof HumanoidModel) {
            ModelPart headRenderer = ((HumanoidModel) modelRoot).head;
            ModelPart headWearRenderer = ((HumanoidModel) modelRoot).hat;
            renderPart(matrixStack, vertexConsumer, headRenderer, parameters);
            renderPart(matrixStack, vertexConsumer, headWearRenderer, parameters);
        }
        this.mainPart = this.partPrerenderer.renderDeclaredMethod(matrixStack, vertexConsumer, this.partPrerenderer.ageableModelHeadPartsMethod, modelRoot, this.mainPart, parameters);
        if (parameters.fullModelIcon) {
            this.mainPart = this.partPrerenderer.renderDeclaredMethod(matrixStack, vertexConsumer, this.partPrerenderer.ageableModelBodyPartsMethod, modelRoot, this.mainPart, parameters);
        }
    }

    private boolean renderHierarchicalModel(Object modelRoot, boolean treatAsHierarchicalRoot, PoseStack matrixStack, VertexConsumer vertexConsumer, Parameters parameters) {
        ModelPart rootPart;
        ModelPart headPart;
        boolean success = false;
        if (treatAsHierarchicalRoot) {
            rootPart = (ModelPart) modelRoot;
        } else {
            HierarchicalModel<?> singlePartModel = (HierarchicalModel) modelRoot;
            rootPart = singlePartModel.root();
        }
        if (rootPart == null) {
            return false;
        }
        try {
            headPart = rootPart.getChild("head");
        } catch (NoSuchElementException e) {
            headPart = null;
        }
        if (headPart != null) {
            renderPart(matrixStack, vertexConsumer, headPart, parameters);
            success = true;
        }
        if (!parameters.fullModelIcon) {
            return success;
        }
        Map<String, ModelPart> rootChildren = ModelPartUtil.getChildren(rootPart);
        this.mainPart = this.partPrerenderer.renderPartsIterable(rootChildren.values(), matrixStack, vertexConsumer, this.mainPart, parameters);
        return true;
    }

    private void renderPart(PoseStack matrixStack, VertexConsumer vertexConsumer, ModelPart part, Parameters parameters) {
        if (this.mainPart == null) {
            this.mainPart = part;
        }
        this.partPrerenderer.renderPart(matrixStack, vertexConsumer, part, this.mainPart, parameters);
    }

    private ModelPart fixHeadRoot(Object modelRoot) {
        ModelPart headPartTest;
        Iterable<ModelPart> headPartsTest = (Iterable) ReflectionUtils.getReflectMethodValue(modelRoot, this.partPrerenderer.ageableModelHeadPartsMethod, new Object[0]);
        if (headPartsTest == null) {
            return null;
        }
        Iterator<ModelPart> iterator = headPartsTest.iterator();
        if (!iterator.hasNext() || (headPartTest = iterator.next()) == null || iterator.hasNext() || ModelPartUtil.hasDirectCubes(headPartTest)) {
            return null;
        }
        return headPartTest;
    }

    private void searchAndRenderFields(PoseStack matrixStack, VertexConsumer vertexBuilder, Object modelRoot, List<String> filter, boolean justOne, Parameters parameters) {
        this.resolvedFieldRenderer.prepare(matrixStack, vertexBuilder, justOne, this.mainPart, parameters, this.partPrerenderer);
        RadarIconModelFieldResolver.searchSuperclassFields(modelRoot, filter, this.resolvedFieldRenderer, ONE_RENDERER_ARRAY);
        this.mainPart = this.resolvedFieldRenderer.getMainPart();
    }

    public VertexConsumer getLayerModelVertexConsumer(MultiBufferSource.BufferSource renderTypeBuffer, ResourceLocation entityTexture, TextureAtlasSprite entityAtlasSprite, ModelRenderTrace mrt) {
        RenderType renderType = CustomRenderTypes.entityIconRenderType(entityTexture, mrt.layerPhases);
        VertexConsumer regularConsumer = renderTypeBuffer.getBuffer(renderType);
        if (entityAtlasSprite != null) {
            return entityAtlasSprite.wrap(regularConsumer);
        }
        return regularConsumer;
    }

    private Object resolveModelRoot(EntityModel<?> model, ArrayList<ArrayList<String>> rootPath, Entity entity) {
        Object currentChainNode = model;
        Iterator<ArrayList<String>> it = rootPath.iterator();
        while (it.hasNext()) {
            ArrayList<String> pathStep = it.next();
            this.modelRootPathListener.prepare();
            RadarIconModelFieldResolver.searchSuperclassFields(currentChainNode, pathStep, this.modelRootPathListener, ONE_OBJECT_ARRAY);
            currentChainNode = this.modelRootPathListener.getCurrentNode();
            if (currentChainNode == null || this.modelRootPathListener.failed()) {
                MinimapLogs.LOGGER.info(String.format("The following entity icon model root path step couldn't be resolved for %s:", EntityType.getKey(entity.getType())));
                Logger logger = MinimapLogs.LOGGER;
                Objects.requireNonNull(logger);
                pathStep.forEach(logger::info);
                return null;
            }
        }
        return currentChainNode;
    }

    public RadarIconModelPartPrerenderer getPartPrerenderer() {
        return this.partPrerenderer;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/RadarIconModelPrerenderer$Parameters.class */
    public static final class Parameters extends RadarIconModelPartPrerenderer.Parameters {
        public final RadarIconModelConfig defaultConfig;
        public final ResourceLocation texture;
        public final TextureAtlasSprite textureAtlasSprite;
        public final boolean forceFieldCheck;
        public final boolean fullModelIcon;
        public final List<String> hardcodedMainPartAliases;
        public final List<String> hardcodedModelPartsFields;

        public Parameters(RadarIconModelConfig config, RadarIconModelConfig defaultConfig, ResourceLocation texture, TextureAtlasSprite textureAtlasSprite, ModelRenderTrace mrt, boolean forceFieldCheck, boolean fullModelIcon, List<String> hardcodedMainPartAliases, List<String> hardcodedModelPartsFields, List<ModelPart> renderedDest) {
            super(config, mrt, renderedDest);
            this.defaultConfig = defaultConfig;
            this.texture = texture;
            this.textureAtlasSprite = textureAtlasSprite;
            this.forceFieldCheck = forceFieldCheck;
            this.fullModelIcon = fullModelIcon;
            this.hardcodedMainPartAliases = hardcodedMainPartAliases;
            this.hardcodedModelPartsFields = hardcodedModelPartsFields;
        }
    }
}
