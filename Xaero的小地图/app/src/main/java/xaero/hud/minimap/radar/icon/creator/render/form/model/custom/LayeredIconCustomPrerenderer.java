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

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/custom/LayeredIconCustomPrerenderer.class */
public class LayeredIconCustomPrerenderer extends RadarIconCustomPrerenderer {
    private List<RadarIconCustomPrerenderer> layers;

    public LayeredIconCustomPrerenderer(List<RadarIconCustomPrerenderer> layers) {
        this.layers = layers;
    }

    @Override // xaero.hud.minimap.radar.icon.creator.render.form.model.custom.RadarIconCustomPrerenderer
    public <T extends Entity> ModelPart render(PoseStack matrixStack, MultiBufferSource.BufferSource bufferSource, EntityRenderer<? super T> entityRenderer, T e, EntityModel<T> defaultModel, RadarIconModelPartPrerenderer partPrerenderer, List<ModelPart> rendered, ModelPart mainPart, RadarIconModelConfig modelConfig, ModelRenderTrace mrt) {
        for (RadarIconCustomPrerenderer layer : this.layers) {
            mainPart = layer.render(matrixStack, bufferSource, entityRenderer, e, defaultModel, partPrerenderer, rendered, mainPart, modelConfig, mrt);
        }
        return mainPart;
    }
}
