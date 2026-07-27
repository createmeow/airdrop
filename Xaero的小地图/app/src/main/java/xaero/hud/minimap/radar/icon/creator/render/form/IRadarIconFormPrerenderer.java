package xaero.hud.minimap.radar.icon.creator.render.form;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import xaero.hud.minimap.radar.icon.creator.RadarIconCreator;
import xaero.hud.minimap.radar.icon.creator.render.trace.ModelRenderTrace;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/IRadarIconFormPrerenderer.class */
public interface IRadarIconFormPrerenderer {
    boolean requiresEntityModel();

    boolean isFlipped();

    boolean isOutlined();

    <T extends Entity> boolean prerender(GuiGraphics guiGraphics, EntityRenderer<? super T> entityRenderer, @Nullable EntityModel<T> entityModel, T t, @Nullable List<ModelRenderTrace> list, RadarIconCreator.Parameters parameters);
}
