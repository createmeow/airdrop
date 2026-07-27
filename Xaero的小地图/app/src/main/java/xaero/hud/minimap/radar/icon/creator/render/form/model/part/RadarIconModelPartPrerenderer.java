package xaero.hud.minimap.radar.icon.creator.render.form.model.part;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Entity;
import org.lwjgl.opengl.GL11;
import xaero.common.graphics.CustomRenderTypes;
import xaero.hud.minimap.MinimapLogs;
import xaero.hud.minimap.radar.icon.creator.render.trace.ModelPartRenderTrace;
import xaero.hud.minimap.radar.icon.creator.render.trace.ModelRenderTrace;
import xaero.hud.minimap.radar.icon.definition.form.model.config.RadarIconModelConfig;
import xaero.lib.common.reflection.util.ReflectionUtils;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/part/RadarIconModelPartPrerenderer.class */
public class RadarIconModelPartPrerenderer {
    private boolean testedRenderEngineWrapperCompatibility;
    private boolean renderEngineIsWrapperCompatible;
    public final Method ageableModelHeadPartsMethod = ReflectionUtils.getMethodReflection(AgeableListModel.class, "headParts", "method_22946", "()Ljava/lang/Iterable;", "m_5607_", new Class[0]);
    public final Method ageableModelBodyPartsMethod = ReflectionUtils.getMethodReflection(AgeableListModel.class, "bodyParts", "method_22948", "()Ljava/lang/Iterable;", "m_5608_", new Class[0]);
    public final Method listModelPartsMethod = ReflectionUtils.getMethodReflection(ListModel.class, "parts", "method_22960", "()Ljava/lang/Iterable;", "m_6195_", new Class[0]);
    private final VertexConsumerWrapper vertexConsumerWrapper = new VertexConsumerWrapper();
    private MultiBufferSource.BufferSource testBufferSource = MultiBufferSource.immediate(new ByteBufferBuilder(256));

    public void renderPart(PoseStack matrixStack, VertexConsumer vertexConsumer, ModelPart part, ModelPart mainPart, Parameters parameters) {
        ModelPartRenderTrace renderInfo;
        if (part == null || parameters.renderedDest.contains(part) || (renderInfo = parameters.mrt.getModelPartRenderInfo(part)) == null || !ModelPartUtil.hasCubes(part)) {
            return;
        }
        boolean showModelBU = part.visible;
        boolean skipDrawBU = part.skipDraw;
        if (!this.testedRenderEngineWrapperCompatibility) {
            testRenderEngineWrapperCompatibility(part, parameters.mrt, renderInfo);
        }
        float centerPointX = mainPart.x;
        float centerPointY = mainPart.y;
        float centerPointZ = mainPart.z;
        ModelPart.Cube biggestMainPartCuboid = ModelPartUtil.getBiggestCuboid(mainPart);
        if (biggestMainPartCuboid != null) {
            centerPointY += (biggestMainPartCuboid.maxY + biggestMainPartCuboid.minY) / 2.0f;
            centerPointZ += (biggestMainPartCuboid.maxZ + biggestMainPartCuboid.minZ) / 2.0f;
        }
        float xRotBU = 0.0f;
        float yRotBU = 0.0f;
        float zRotBU = 0.0f;
        if (parameters.config.modelPartsRotationReset) {
            xRotBU = part.xRot;
            yRotBU = part.yRot;
            zRotBU = part.zRot;
            PartPose initPose = part.getInitialPose();
            part.setRotation(initPose.xRot, initPose.yRot, initPose.zRot);
        }
        part.visible = true;
        part.skipDraw = false;
        float xBU = part.x;
        float yBU = part.y;
        float zBU = part.z;
        part.setPos(part.x - centerPointX, part.y - centerPointY, part.z - centerPointZ);
        try {
            if (this.renderEngineIsWrapperCompatible) {
                vertexConsumer = this.vertexConsumerWrapper.prepareDetection(vertexConsumer, 3.0d, 61.0d, 3.0d, 61.0d, -497.0d, -2.0d);
            }
            part.render(matrixStack, vertexConsumer, 15728880, OverlayTexture.NO_OVERLAY, renderInfo.color);
            if ((!this.renderEngineIsWrapperCompatible || this.vertexConsumerWrapper.hasDetectedVertex()) && FastColor.ARGB32.alpha(renderInfo.color) > 0) {
                parameters.renderedDest.add(part);
            }
        } catch (Throwable t) {
            MinimapLogs.LOGGER.info("Exception when rendering entity part. " + String.valueOf(part) + " " + t.getMessage());
        }
        part.setPos(xBU, yBU, zBU);
        while (GL11.glGetError() != 0) {
        }
        if (parameters.config.modelPartsRotationReset) {
            part.setRotation(xRotBU, yRotBU, zRotBU);
        }
        part.visible = showModelBU;
        part.skipDraw = skipDrawBU;
    }

    private void testRenderEngineWrapperCompatibility(ModelPart part, ModelRenderTrace mrt, ModelPartRenderTrace renderInfo) {
        boolean normalWorks = false;
        try {
            try {
                PoseStack testMatrix = new PoseStack();
                testMatrix.translate(0.0f, 0.0f, -2500.0f);
                VertexConsumer actualVertexConsumer = this.testBufferSource.getBuffer(CustomRenderTypes.entityIconRenderType(mrt.renderTexture, mrt.layerPhases));
                part.render(testMatrix, actualVertexConsumer, 15728880, OverlayTexture.NO_OVERLAY, renderInfo.color);
                normalWorks = true;
                PoseStack testMatrix2 = new PoseStack();
                testMatrix2.translate(0.0f, 0.0f, -2500.0f);
                this.vertexConsumerWrapper.prepareDetection(actualVertexConsumer, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
                part.visible = true;
                part.skipDraw = false;
                part.render(testMatrix2, this.vertexConsumerWrapper, 15728880, OverlayTexture.NO_OVERLAY, renderInfo.color);
                this.renderEngineIsWrapperCompatible = this.vertexConsumerWrapper.hasDetectedVertex();
            } catch (Throwable t) {
                if (normalWorks) {
                    MinimapLogs.LOGGER.warn("Render engine used for entities is not fully compatible with the minimap entity icons. Using fallback. " + t.getMessage());
                }
                this.testBufferSource.endLastBatch();
            }
            if (!this.renderEngineIsWrapperCompatible) {
                throw new Exception("can't detect vertices");
            }
            this.testBufferSource.endLastBatch();
            if (normalWorks) {
                this.testedRenderEngineWrapperCompatibility = true;
            }
        } catch (Throwable th) {
            this.testBufferSource.endLastBatch();
            throw th;
        }
    }

    public <T extends Entity> ModelPart renderDeclaredMethod(PoseStack matrixStack, VertexConsumer vertexConsumer, Method method, EntityModel<T> model, ModelPart mainPart, Parameters parameters) {
        if (method == null) {
            return mainPart;
        }
        return renderPartsIterable((Iterable) ReflectionUtils.getReflectMethodValue(model, method, new Object[0]), matrixStack, vertexConsumer, mainPart, parameters);
    }

    public ModelPart renderPartsIterable(Iterable<ModelPart> parts, PoseStack matrixStack, VertexConsumer vertexConsumer, ModelPart mainPart, Parameters parameters) {
        if (parts == null) {
            return mainPart;
        }
        Iterator<ModelPart> partsIterator = parts.iterator();
        if (!partsIterator.hasNext()) {
            return mainPart;
        }
        if (mainPart == null) {
            mainPart = partsIterator.next();
            renderPart(matrixStack, vertexConsumer, mainPart, mainPart, parameters);
        }
        while (partsIterator.hasNext()) {
            renderPart(matrixStack, vertexConsumer, partsIterator.next(), mainPart, parameters);
        }
        return mainPart;
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/part/RadarIconModelPartPrerenderer$VertexConsumerWrapper.class */
    public static class VertexConsumerWrapper implements VertexConsumer {
        private VertexConsumer consumer;
        private boolean detectedVertex;
        private double detectionMinX;
        private double detectionMaxX;
        private double detectionMinY;
        private double detectionMaxY;
        private double detectionMinZ;
        private double detectionMaxZ;

        public VertexConsumerWrapper prepareDetection(VertexConsumer consumer, double detectionMinX, double detectionMaxX, double detectionMinY, double detectionMaxY, double detectionMinZ, double detectionMaxZ) {
            this.consumer = consumer;
            this.detectionMinX = detectionMinX;
            this.detectionMaxX = detectionMaxX;
            this.detectionMinY = detectionMinY;
            this.detectionMaxY = detectionMaxY;
            this.detectionMinZ = detectionMinZ;
            this.detectionMaxZ = detectionMaxZ;
            this.detectedVertex = false;
            return this;
        }

        public VertexConsumer addVertex(float d, float e, float f) {
            if (d >= this.detectionMinX && d <= this.detectionMaxX && e >= this.detectionMinY && e <= this.detectionMaxY && f >= this.detectionMinZ && f <= this.detectionMaxZ) {
                this.detectedVertex = true;
            }
            return this.consumer.addVertex(d, e, f);
        }

        public VertexConsumer setColor(int i, int j, int k, int l) {
            return this.consumer.setColor(i, j, k, l);
        }

        public VertexConsumer setUv(float f, float g) {
            return this.consumer.setUv(f, g);
        }

        public VertexConsumer setUv1(int var1, int var2) {
            return this.consumer.setUv1(var1, var2);
        }

        public VertexConsumer setOverlay(int i) {
            return this.consumer.setOverlay(i);
        }

        public VertexConsumer setUv2(int i, int j) {
            return this.consumer.setUv2(i, j);
        }

        public VertexConsumer setNormal(float f, float g, float h) {
            return this.consumer.setNormal(f, g, h);
        }

        public boolean hasDetectedVertex() {
            return this.detectedVertex;
        }
    }

    /* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/part/RadarIconModelPartPrerenderer$Parameters.class */
    public static class Parameters {
        public final RadarIconModelConfig config;
        public final ModelRenderTrace mrt;
        public final List<ModelPart> renderedDest;

        public Parameters(RadarIconModelConfig config, ModelRenderTrace mrt, List<ModelPart> renderedDest) {
            this.config = config;
            this.mrt = mrt;
            this.renderedDest = renderedDest;
        }
    }
}
