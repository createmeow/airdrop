package xaero.hud.minimap.radar.icon.creator.render.form.model.custom;

import java.util.List;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import xaero.hud.minimap.radar.icon.creator.render.form.model.part.RadarIconModelPartPrerenderer;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/model/custom/CustomModelIconCustomPrenderer.class */
public abstract class CustomModelIconCustomPrenderer extends RenderTypeIconCustomPrerenderer {
    @Override // xaero.hud.minimap.radar.icon.creator.render.form.model.custom.RenderTypeIconCustomPrerenderer
    protected abstract <T extends Entity> Iterable<ModelPart> getModelParts(RadarIconModelPartPrerenderer radarIconModelPartPrerenderer, List<ModelPart> list, T t, EntityModel<T> entityModel);

    @Override // xaero.hud.minimap.radar.icon.creator.render.form.model.custom.RenderTypeIconCustomPrerenderer
    protected List<ModelPart> getRenderedDest(List<ModelPart> rendered) {
        return rendered;
    }
}
