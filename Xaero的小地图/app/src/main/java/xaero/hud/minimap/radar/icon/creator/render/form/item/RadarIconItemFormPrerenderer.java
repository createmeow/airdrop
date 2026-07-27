package xaero.hud.minimap.radar.icon.creator.render.form.item;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xaero.hud.minimap.radar.icon.creator.RadarIconCreator;
import xaero.hud.minimap.radar.icon.creator.render.form.IRadarIconFormPrerenderer;
import xaero.hud.minimap.radar.icon.creator.render.trace.ModelRenderTrace;
import xaero.hud.minimap.radar.icon.definition.form.item.RadarIconItemForm;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/item/RadarIconItemFormPrerenderer.class */
public class RadarIconItemFormPrerenderer implements IRadarIconFormPrerenderer {
    @Override // xaero.hud.minimap.radar.icon.creator.render.form.IRadarIconFormPrerenderer
    public boolean requiresEntityModel() {
        return false;
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
    public <T extends Entity> boolean prerender(GuiGraphics guiGraphics, EntityRenderer<? super T> entityRenderer, @Nullable EntityModel<T> entityModel, T entity, @Nullable List<ModelRenderTrace> traceResult, RadarIconCreator.Parameters parameters) {
        RadarIconItemForm itemForm = (RadarIconItemForm) parameters.form;
        ItemStack itemStack = getItemToRender(entity, itemForm);
        if (itemStack == null || itemStack.isEmpty()) {
            return false;
        }
        PoseStack matrixStack = guiGraphics.pose();
        matrixStack.translate(32, 32, 1.0f);
        float scale = parameters.scale;
        if (scale < 1.0f) {
            matrixStack.scale(scale, scale, 1.0f);
        }
        matrixStack.translate(0.0f, 0.0f, -300.0f);
        guiGraphics.renderItem(itemStack, -8, -8);
        guiGraphics.flush();
        return true;
    }

    private ItemStack getItemToRender(Entity entity, RadarIconItemForm itemForm) {
        ResourceLocation itemKey = itemForm.getItemKey();
        if (itemKey != null) {
            Item item = (Item) BuiltInRegistries.ITEM.get(itemKey);
            if (item == null) {
                return null;
            }
            return new ItemStack(item);
        }
        ItemStack selfStack = getSelfItem(entity);
        if (selfStack == null) {
            return null;
        }
        return new ItemStack(selfStack.getItem());
    }

    private ItemStack getSelfItem(Entity entity) {
        if (entity instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity) entity;
            return itemEntity.getItem();
        }
        if (entity instanceof ItemFrame) {
            ItemFrame itemFrame = (ItemFrame) entity;
            return itemFrame.getItem();
        }
        return entity.getPickResult();
    }
}
