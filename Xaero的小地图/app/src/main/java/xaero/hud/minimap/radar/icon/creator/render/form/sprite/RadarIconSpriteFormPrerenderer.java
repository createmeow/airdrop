package xaero.hud.minimap.radar.icon.creator.render.form.sprite;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.lwjgl.opengl.GL11;
import xaero.hud.minimap.radar.icon.creator.RadarIconCreator;
import xaero.hud.minimap.radar.icon.creator.render.form.IRadarIconFormPrerenderer;
import xaero.hud.minimap.radar.icon.creator.render.trace.ModelRenderTrace;
import xaero.hud.minimap.radar.icon.definition.form.sprite.RadarIconSpriteForm;
import xaero.lib.client.graphics.util.ImmediateRenderUtil;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/hud/minimap/radar/icon/creator/render/form/sprite/RadarIconSpriteFormPrerenderer.class */
public class RadarIconSpriteFormPrerenderer implements IRadarIconFormPrerenderer {
    private final boolean flipped;
    private final boolean outlined;

    public RadarIconSpriteFormPrerenderer(boolean flipped, boolean outlined) {
        this.flipped = flipped;
        this.outlined = outlined;
    }

    @Override // xaero.hud.minimap.radar.icon.creator.render.form.IRadarIconFormPrerenderer
    public boolean requiresEntityModel() {
        return false;
    }

    @Override // xaero.hud.minimap.radar.icon.creator.render.form.IRadarIconFormPrerenderer
    public boolean isFlipped() {
        return this.flipped;
    }

    @Override // xaero.hud.minimap.radar.icon.creator.render.form.IRadarIconFormPrerenderer
    public boolean isOutlined() {
        return this.outlined;
    }

    @Override // xaero.hud.minimap.radar.icon.creator.render.form.IRadarIconFormPrerenderer
    public <T extends Entity> boolean prerender(GuiGraphics guiGraphics, EntityRenderer<? super T> entityRenderer, @Nullable EntityModel<T> entityModel, T entity, @Nullable List<ModelRenderTrace> traceResult, RadarIconCreator.Parameters parameters) {
        PoseStack matrixStack = guiGraphics.pose();
        RadarIconSpriteForm spriteForm = (RadarIconSpriteForm) parameters.form;
        ResourceLocation sprite = spriteForm.getSpriteLocation();
        GlStateManager._disableBlend();
        GlStateManager._enableBlend();
        GlStateManager._disableCull();
        Lighting.setupForFlatItems();
        Minecraft.getInstance().getTextureManager().bindForSetup(sprite);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9728);
        GL11.glTexParameteri(3553, 10242, 33071);
        GL11.glTexParameteri(3553, 10243, 33071);
        matrixStack.translate(32, 32, 1.0f);
        float scale = parameters.scale;
        if (scale < 1.0f) {
            matrixStack.scale(scale, scale, 1.0f);
        }
        RenderSystem.setShaderTexture(0, sprite);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        ImmediateRenderUtil.texturedRect(matrixStack, -32, -32, 0, 64, 64.0f, 64.0f, -64.0f, 64.0f);
        GlStateManager._enableCull();
        return true;
    }
}
