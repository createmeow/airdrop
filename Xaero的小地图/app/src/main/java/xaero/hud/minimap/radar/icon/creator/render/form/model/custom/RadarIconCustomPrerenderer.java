package xaero.hud.minimap.radar.icon.creator.render.form.model.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import xaero.hud.minimap.radar.icon.creator.render.form.model.part.RadarIconModelPartPrerenderer;
import xaero.hud.minimap.radar.icon.creator.render.trace.ModelRenderTrace;
import xaero.hud.minimap.radar.icon.definition.form.model.config.RadarIconModelConfig;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/custom/RadarIconCustomPrerenderer.class */
public abstract class RadarIconCustomPrerenderer {
    public abstract <T extends Entity> ModelPart render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, EntityRenderer<? super T> entityRenderer, T t, EntityModel<T> entityModel, RadarIconModelPartPrerenderer radarIconModelPartPrerenderer, List<ModelPart> list, ModelPart modelPart, RadarIconModelConfig radarIconModelConfig, ModelRenderTrace modelRenderTrace);
}
